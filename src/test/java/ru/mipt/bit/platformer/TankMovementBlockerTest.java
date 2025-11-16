package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import ru.mipt.bit.platformer.adapters.MoveCheckerAdapter;
import ru.mipt.bit.platformer.adapters.MovementBlockerAdapter;
import ru.mipt.bit.platformer.adapters.TileMoverAdapter;
import ru.mipt.bit.platformer.interfaces.LevelRenderer;
import ru.mipt.bit.platformer.interfaces.TileObjectPositioner;
import ru.mipt.bit.platformer.models.Direction;
import ru.mipt.bit.platformer.models.Field;
import ru.mipt.bit.platformer.models.Tank;
import ru.mipt.bit.platformer.models.TileMovement;
import ru.mipt.bit.platformer.util.GdxGameUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class TankMovementBlockerTest {
    @Test
    void tankReservesDestinationAndFreesPreviousOnComplete() {
        LevelRenderer levelRenderer = mock(LevelRenderer.class);
        TiledMapTileLayer layer = mock(TiledMapTileLayer.class);
        TileObjectPositioner positioner = mock(TileObjectPositioner.class);
        when(layer.getWidth()).thenReturn(10);
        when(layer.getHeight()).thenReturn(8);

        Field field = new Field(levelRenderer, layer, positioner);
        MoveCheckerAdapter moveChecker = new MoveCheckerAdapter(field);
        TileMovement tm = new TileMovement(layer, Interpolation.smooth, positioner);
        TileMoverAdapter mover = new TileMoverAdapter(tm);

        MovementBlockerAdapter blocker = new MovementBlockerAdapter(field);

        Texture texture = mock(Texture.class);
        Rectangle rect = new Rectangle();

        try (MockedStatic<GdxGameUtils> utils = mockStatic(GdxGameUtils.class)) {
            utils.when(() -> GdxGameUtils.createBoundingRectangle(any(TextureRegion.class))).thenReturn(rect);
            utils.when(() -> GdxGameUtils.continueProgress(anyFloat(), anyFloat(), anyFloat())).thenReturn(1f);

            GridPoint2 start = new GridPoint2(1, 1);
            Tank tank = new Tank(texture, start, moveChecker, mover, blocker);

            assertFalse(field.playerCanMoveTo(start));

            tank.tryMove(Direction.RIGHT);
            assertFalse(field.playerCanMoveTo(new GridPoint2(2, 1)));

            tank.tick(0.016f, 0.4f);
            assertTrue(field.playerCanMoveTo(new GridPoint2(1, 1)));
        }
    }
}
