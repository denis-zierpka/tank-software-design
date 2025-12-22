package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ru.mipt.bit.platformer.adapters.BulletCreatorObserver;
import ru.mipt.bit.platformer.config.GameConfig;
import ru.mipt.bit.platformer.level.LevelLoader;
import ru.mipt.bit.platformer.level.LevelPopulation;
import ru.mipt.bit.platformer.models.Field;
import ru.mipt.bit.platformer.models.HealthBarToggleState;
import ru.mipt.bit.platformer.models.KeyInputHandler;
import ru.mipt.bit.platformer.models.Tank;
import ru.mipt.bit.platformer.models.TankWithHealthBar;
import ru.mipt.bit.platformer.models.Tree;
import ru.mipt.bit.platformer.ai.RandomAiController;
import ru.mipt.bit.platformer.graphics.GraphicsRenderer;
import ru.mipt.bit.platformer.interfaces.BulletCreator;
import ru.mipt.bit.platformer.interfaces.MoveChecker;
import ru.mipt.bit.platformer.interfaces.MovementBlocker;
import ru.mipt.bit.platformer.interfaces.RectangleFactory;
import ru.mipt.bit.platformer.interfaces.TickContext;
import ru.mipt.bit.platformer.interfaces.Renderer;
import ru.mipt.bit.platformer.interfaces.TileMover;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameDesktopLauncher implements ApplicationListener {

    private AnnotationConfigApplicationContext applicationContext;

    private Batch batch;
    private ShapeRenderer shapeRenderer;

    private TiledMap level;
    private TiledMapTileLayer groundLayer;

    private Field field;
    private GraphicsRenderer graphicsRenderer;

    private Texture blueTankTexture;
    private MoveChecker moveChecker;
    private TileMover mover;
    private MovementBlocker movementBlocker;

    private Texture greenTreeTexture;
    private Renderer renderer;
    private RectangleFactory rectangleFactory;

    private KeyInputHandler input;
    private LevelPopulation population;
    private HealthBarToggleState healthBarToggleState;
    private Random random;
    
    private BulletCreatorObserver bulletCreatorObserver;

    private List<GridPoint2> randomFreeTiles(int count) {
        int width = groundLayer.getWidth();
        int height = groundLayer.getHeight();
        List<GridPoint2> freeTiles = new ArrayList<>();
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                GridPoint2 candidate = new GridPoint2(x, y);
                if (field.playerCanMoveTo(candidate)) {
                    freeTiles.add(candidate);
                }
            }
        }
        
        if (freeTiles.size() < count) {
            throw new IllegalStateException("Not enough free tiles available");
        }
        
        java.util.Collections.shuffle(freeTiles, new Random(System.currentTimeMillis()));
        return freeTiles.subList(0, count);
    }

    @Override
    public void create() {
        applicationContext = new AnnotationConfigApplicationContext(GameConfig.class);

        batch = applicationContext.getBean(Batch.class);
        shapeRenderer = applicationContext.getBean(ShapeRenderer.class);
        level = applicationContext.getBean(TiledMap.class);
        groundLayer = applicationContext.getBean(TiledMapTileLayer.class);
        field = applicationContext.getBean(Field.class);
        graphicsRenderer = applicationContext.getBean(GraphicsRenderer.class);
        bulletCreatorObserver = applicationContext.getBean(BulletCreatorObserver.class);
        moveChecker = applicationContext.getBean(MoveChecker.class);
        mover = applicationContext.getBean(TileMover.class);
        movementBlocker = applicationContext.getBean(MovementBlocker.class);
        renderer = applicationContext.getBean(Renderer.class);
        rectangleFactory = applicationContext.getBean(RectangleFactory.class);
        input = applicationContext.getBean(KeyInputHandler.class);
        healthBarToggleState = applicationContext.getBean(HealthBarToggleState.class);
        
        RandomAiController ai = applicationContext.getBean(RandomAiController.class);
        field.setAiController(ai);
        field.setMovementSpeed(Tank.MOVEMENT_SPEED);

        random = new Random(System.currentTimeMillis());
        BulletCreator initialBulletCreator = bulletCreatorObserver.getBulletCreator();

        // Texture decodes an image file and loads it into GPU memory, it represents a native resource
        blueTankTexture = new Texture("images/tank_blue.png");

        population = LevelLoader.LOAD_FROM_TEXT
            ? LevelLoader.fromTextFile(groundLayer)
            : LevelLoader.random(groundLayer);
        
        int playerHealth = Tank.DEFAULT_MIN_HEALTH + random.nextInt(Tank.DEFAULT_MAX_HEALTH - Tank.DEFAULT_MIN_HEALTH + 1);
        Tank player = new TankWithHealthBar(blueTankTexture, population.playerStart, moveChecker, mover, movementBlocker, playerHealth, shapeRenderer);
        
        player.setBulletCreator(initialBulletCreator);
        field.addTank(player);
        field.setPlayer(player);

        greenTreeTexture = new Texture("images/greenTree.png");

        for (GridPoint2 pos : population.trees) {
            Tree t = new Tree(greenTreeTexture, pos, renderer, rectangleFactory);
            field.addTree(t);
        }

        List<GridPoint2> enemyPositions = randomFreeTiles(RandomAiController.RANDOM_ENEMIES_COUNT);
        for (GridPoint2 pos : enemyPositions) {
            int enemyHealth = Tank.DEFAULT_MIN_HEALTH + random.nextInt(Tank.DEFAULT_MAX_HEALTH - Tank.DEFAULT_MIN_HEALTH + 1);
            Tank enemy = new TankWithHealthBar(blueTankTexture, pos, moveChecker, mover, movementBlocker, enemyHealth, shapeRenderer);
            enemy.setBulletCreator(bulletCreatorObserver.getBulletCreator());
            field.addTank(enemy);
        }
    }

    @Override
    public void render() {
        // get time passed since the last render
        float deltaTime = Gdx.graphics.getDeltaTime();

        Tank player = field.getPlayer();
        if (player != null && player.getCurrentHealth() > 0) {
            var cmd = input.getMoveCommand(player, KeyInputHandler.KEY_TO_DIRECTION);
            if (cmd != null) 
                cmd.execute();
            
            var toggleCmd = input.handleHealthBarToggle(healthBarToggleState);
            if (toggleCmd != null)
                toggleCmd.execute();
            
            var shootCmd = input.getShootCommand(player);
            if (shootCmd != null)
                shootCmd.execute();

            TickContext context = field.createTickContext(deltaTime);
            player.tick(context);
        }
        
        // update all objects on the field
        field.tickAll(deltaTime);

        // render all graphics
        graphicsRenderer.render(healthBarToggleState);
    }

    @Override
    public void resize(int width, int height) {
        // do not react to window resizing
    }

    @Override
    public void pause() {
        // game doesn't get paused
    }

    @Override
    public void resume() {
        // game doesn't get paused
    }

    @Override
    public void dispose() {
        // dispose of all the native resources (classes which implement com.badlogic.gdx.utils.Disposable)
        greenTreeTexture.dispose();
        blueTankTexture.dispose();
        level.dispose();
        batch.dispose();
        shapeRenderer.dispose();
        if (applicationContext != null) {
            applicationContext.close();
        }
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
