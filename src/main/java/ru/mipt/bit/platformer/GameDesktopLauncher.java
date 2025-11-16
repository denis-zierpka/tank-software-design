package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;

import ru.mipt.bit.platformer.adapters.BulletCreatorAdapter;
import ru.mipt.bit.platformer.adapters.BulletCreatorObserver;
import ru.mipt.bit.platformer.adapters.MoveCheckerAdapter;
import ru.mipt.bit.platformer.adapters.MovementBlockerAdapter;
import ru.mipt.bit.platformer.adapters.LevelRendererAdapter;
import ru.mipt.bit.platformer.adapters.RectangleFactoryAdapter;
import ru.mipt.bit.platformer.adapters.RendererAdapter;
import ru.mipt.bit.platformer.adapters.TileMoverAdapter;
import ru.mipt.bit.platformer.adapters.TileObjectPositionerAdapter;
import ru.mipt.bit.platformer.level.LevelLoader;
import ru.mipt.bit.platformer.level.LevelPopulation;
import ru.mipt.bit.platformer.models.Field;
import ru.mipt.bit.platformer.models.HealthBarToggleState;
import ru.mipt.bit.platformer.models.KeyInputHandler;
import ru.mipt.bit.platformer.models.Tank;
import ru.mipt.bit.platformer.models.TankWithHealthBar;
import ru.mipt.bit.platformer.models.TileMovement;
import ru.mipt.bit.platformer.models.Tree;
import ru.mipt.bit.platformer.ai.RandomAiController;
import ru.mipt.bit.platformer.graphics.GraphicsRenderer;
import ru.mipt.bit.platformer.interfaces.BulletCreator;
import ru.mipt.bit.platformer.interfaces.LevelRenderer;
import ru.mipt.bit.platformer.interfaces.MoveChecker;
import ru.mipt.bit.platformer.interfaces.MovementBlocker;
import ru.mipt.bit.platformer.interfaces.RectangleFactory;
import ru.mipt.bit.platformer.interfaces.Renderer;
import ru.mipt.bit.platformer.interfaces.TileMover;
import ru.mipt.bit.platformer.interfaces.TileObjectPositioner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class GameDesktopLauncher implements ApplicationListener {


    private Batch batch;
    private ShapeRenderer shapeRenderer;

    private TiledMap level;
    private MapRenderer libRenderer;
    private TiledMapTileLayer groundLayer;
    private LevelRenderer levelRenderer;
    private TileObjectPositioner positioner;

    private Field field;
    private GraphicsRenderer graphicsRenderer;

    private Texture blueTankTexture;
    private TileMovement tm;
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
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        random = new Random(System.currentTimeMillis());

        // load level tiles
        level = new TmxMapLoader().load("level.tmx");
        libRenderer = createSingleLayerMapRenderer(level, batch);
        groundLayer = getSingleLayer(level);

        levelRenderer = new LevelRendererAdapter(libRenderer);
        positioner = new TileObjectPositionerAdapter();

        field = new Field(levelRenderer, groundLayer, positioner);
        graphicsRenderer = new GraphicsRenderer(batch, field);
        bulletCreatorObserver = new BulletCreatorObserver(field, shapeRenderer);

        BulletCreator initialBulletCreator = new BulletCreatorAdapter(field, shapeRenderer);
        bulletCreatorObserver.setBulletCreator(initialBulletCreator);

        // Texture decodes an image file and loads it into GPU memory, it represents a native resource
        blueTankTexture = new Texture("images/tank_blue.png");
        tm = new TileMovement(field.ground(), Interpolation.smooth, positioner);
        moveChecker = new MoveCheckerAdapter(field);
        mover = new TileMoverAdapter(tm);
        movementBlocker = new MovementBlockerAdapter(field);

        population = LevelLoader.LOAD_FROM_TEXT
            ? LevelLoader.fromTextFile(groundLayer)
            : LevelLoader.random(groundLayer);
        
        int playerHealth = Tank.DEFAULT_MIN_HEALTH + random.nextInt(Tank.DEFAULT_MAX_HEALTH - Tank.DEFAULT_MIN_HEALTH + 1);
        Tank player = new TankWithHealthBar(blueTankTexture, population.playerStart, moveChecker, mover, movementBlocker, playerHealth, shapeRenderer);
        
        player.setBulletCreator(initialBulletCreator);
        field.addTank(player);
        field.setPlayer(player);

        greenTreeTexture = new Texture("images/greenTree.png");
        renderer = new RendererAdapter();
        rectangleFactory = new RectangleFactoryAdapter();

        for (GridPoint2 pos : population.trees) {
            Tree t = new Tree(greenTreeTexture, pos, renderer, rectangleFactory);
            field.addTree(t);
        }

        input = new KeyInputHandler();
        RandomAiController ai = new RandomAiController();
        field.setAiController(ai);
        field.setMovementSpeed(Tank.MOVEMENT_SPEED);
        healthBarToggleState = new HealthBarToggleState();

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

            player.tick(deltaTime, Tank.MOVEMENT_SPEED);
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
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
