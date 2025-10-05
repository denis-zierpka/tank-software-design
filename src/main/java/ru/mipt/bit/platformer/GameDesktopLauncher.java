package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;

import ru.mipt.bit.platformer.models.Direction;
import ru.mipt.bit.platformer.models.Field;
import ru.mipt.bit.platformer.models.KeyInputHandler;
import ru.mipt.bit.platformer.models.Tank;
import ru.mipt.bit.platformer.models.Tree;

import static com.badlogic.gdx.Input.Keys.*;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class GameDesktopLauncher implements ApplicationListener {

    private static final float MOVEMENT_SPEED = 0.4f;

    private Batch batch;

    private TiledMap level;
    private MapRenderer levelRenderer;

    private Field field;

    private Texture blueTankTexture;
    private Tank player;

    private Texture greenTreeTexture;
    private Tree tree;

    private KeyInputHandler input;

    private static final java.util.Map<Integer, Direction> KEY_TO_DIRECTION = java.util.Map.of(
        UP, Direction.UP,
        LEFT, Direction.LEFT,
        DOWN, Direction.DOWN,
        RIGHT, Direction.RIGHT,
        W, Direction.UP,
        A, Direction.LEFT,
        S, Direction.DOWN,
        D, Direction.RIGHT
    );

    @Override
    public void create() {
        batch = new SpriteBatch();

        // load level tiles
        level = new TmxMapLoader().load("level.tmx");
        levelRenderer = createSingleLayerMapRenderer(level, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(level);

        field = new Field(levelRenderer, groundLayer);

        // Texture decodes an image file and loads it into GPU memory, it represents a native resource
        blueTankTexture = new Texture("images/tank_blue.png");
        player = new Tank(blueTankTexture, new GridPoint2(1, 1), field, Interpolation.smooth);

        greenTreeTexture = new Texture("images/greenTree.png");
        tree = new Tree(greenTreeTexture, new GridPoint2(1, 3));
        field.addTree(tree);

        input = new KeyInputHandler();
    }

    @Override
    public void render() {
        // clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        // get time passed since the last render
        float deltaTime = Gdx.graphics.getDeltaTime();

        input.handleMovement(player, KEY_TO_DIRECTION);
        input.handleActions(player);

        player.update(deltaTime, MOVEMENT_SPEED);

        // render each tile of the level
        field.render();

        // start recording all drawing commands
        batch.begin();

        // render player
        player.render(batch);

        // render tree obstacle
        field.renderTrees(batch);

        // submit all drawing requests
        batch.end();
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
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
