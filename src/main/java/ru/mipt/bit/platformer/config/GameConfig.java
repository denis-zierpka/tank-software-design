package ru.mipt.bit.platformer.config;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Interpolation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.mipt.bit.platformer.adapters.BulletCreatorAdapter;
import ru.mipt.bit.platformer.adapters.BulletCreatorObserver;
import ru.mipt.bit.platformer.adapters.LevelRendererAdapter;
import ru.mipt.bit.platformer.adapters.MoveCheckerAdapter;
import ru.mipt.bit.platformer.adapters.MovementBlockerAdapter;
import ru.mipt.bit.platformer.adapters.RectangleFactoryAdapter;
import ru.mipt.bit.platformer.adapters.RendererAdapter;
import ru.mipt.bit.platformer.adapters.TileMoverAdapter;
import ru.mipt.bit.platformer.adapters.TileObjectPositionerAdapter;
import ru.mipt.bit.platformer.ai.RandomAiController;
import ru.mipt.bit.platformer.graphics.GraphicsRenderer;
import ru.mipt.bit.platformer.interfaces.LevelRenderer;
import ru.mipt.bit.platformer.interfaces.MoveChecker;
import ru.mipt.bit.platformer.interfaces.MovementBlocker;
import ru.mipt.bit.platformer.interfaces.RectangleFactory;
import ru.mipt.bit.platformer.interfaces.Renderer;
import ru.mipt.bit.platformer.interfaces.TileMover;
import ru.mipt.bit.platformer.interfaces.TileObjectPositioner;
import ru.mipt.bit.platformer.models.Field;
import ru.mipt.bit.platformer.models.HealthBarToggleState;
import ru.mipt.bit.platformer.models.KeyInputHandler;
import ru.mipt.bit.platformer.models.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

@Configuration
public class GameConfig {

    @Bean
    public Batch batch() {
        return new SpriteBatch();
    }

    @Bean
    public ShapeRenderer shapeRenderer() {
        return new ShapeRenderer();
    }

    @Bean
    public TiledMap level() {
        return new TmxMapLoader().load("level.tmx");
    }

    @Bean
    public MapRenderer libRenderer(TiledMap level, Batch batch) {
        return createSingleLayerMapRenderer(level, batch);
    }

    @Bean
    public TiledMapTileLayer groundLayer(TiledMap level) {
        return getSingleLayer(level);
    }

    @Bean
    public LevelRenderer levelRenderer(MapRenderer libRenderer) {
        return new LevelRendererAdapter(libRenderer);
    }

    @Bean
    public TileObjectPositioner positioner() {
        return new TileObjectPositionerAdapter();
    }

    @Bean
    public Field field(LevelRenderer levelRenderer, TiledMapTileLayer groundLayer, TileObjectPositioner positioner) {
        return new Field(levelRenderer, groundLayer, positioner);
    }

    @Bean
    public GraphicsRenderer graphicsRenderer(Batch batch, Field field) {
        return new GraphicsRenderer(batch, field);
    }

    @Bean
    public TileMovement tileMovement(Field field, TileObjectPositioner positioner) {
        return new TileMovement(field.ground(), Interpolation.smooth, positioner);
    }

    @Bean
    public MoveChecker moveChecker(Field field) {
        return new MoveCheckerAdapter(field);
    }

    @Bean
    public TileMover mover(TileMovement tileMovement) {
        return new TileMoverAdapter(tileMovement);
    }

    @Bean
    public MovementBlocker movementBlocker(Field field) {
        return new MovementBlockerAdapter(field);
    }

    @Bean
    public Renderer renderer() {
        return new RendererAdapter();
    }

    @Bean
    public RectangleFactory rectangleFactory() {
        return new RectangleFactoryAdapter();
    }

    @Bean
    public KeyInputHandler keyInputHandler() {
        return new KeyInputHandler();
    }

    @Bean
    public RandomAiController randomAiController() {
        return new RandomAiController();
    }

    @Bean
    public HealthBarToggleState healthBarToggleState() {
        return new HealthBarToggleState();
    }

    @Bean
    public BulletCreatorAdapter bulletCreatorAdapter(Field field, ShapeRenderer shapeRenderer) {
        return new BulletCreatorAdapter(field, shapeRenderer);
    }

    @Bean
    public BulletCreatorObserver bulletCreatorObserver(Field field, ShapeRenderer shapeRenderer, BulletCreatorAdapter bulletCreatorAdapter) {
        BulletCreatorObserver observer = new BulletCreatorObserver(field, shapeRenderer);
        observer.setBulletCreator(bulletCreatorAdapter);
        return observer;
    }
}
