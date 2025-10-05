package ru.mipt.bit.platformer;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;

import ru.mipt.bit.platformer.models.TileMovement;
import ru.mipt.bit.platformer.util.GdxGameUtils;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public class TileMovementTest {

    @Test
    void moveRectangleCallsFuncTwice() {
        TiledMapTileLayer layer = mock(TiledMapTileLayer.class);
        Interpolation interpolation = mock(Interpolation.class);

        Rectangle rect = new Rectangle();

        GridPoint2 from = new GridPoint2(1, 1);
        GridPoint2 to = new GridPoint2(2, 3);

        try (MockedStatic<GdxGameUtils> utils = mockStatic(GdxGameUtils.class)) {
            TileMovement tileMovement = new TileMovement(layer, interpolation);

            utils.when(() -> moveRectangleAtTileCenter(layer, rect, from))
                 .thenAnswer(invocation -> {
                     rect.x = 10f;
                     rect.y = 20f;
                     return null;
                 });

            utils.when(() -> moveRectangleAtTileCenter(layer, rect, to))
                 .thenAnswer(invocation -> {
                     rect.x = 30f;
                     rect.y = 40f;
                     return null;
                 });

            when(interpolation.apply(10f, 30f, 0.5f)).thenReturn(20f);
            when(interpolation.apply(20f, 40f, 0.5f)).thenReturn(30f);

            Rectangle result = tileMovement.moveRectangleBetweenTileCenters(rect, from, to, 0.5f);

            utils.verify(() -> moveRectangleAtTileCenter(layer, rect, from), times(1));
            utils.verify(() -> moveRectangleAtTileCenter(layer, rect, to), times(1));

            verify(interpolation).apply(10f, 30f, 0.5f);
            verify(interpolation).apply(20f, 40f, 0.5f);

            assertSame(rect, result);

            assertEquals(20f, rect.x);
            assertEquals(30f, rect.y);
        }
    }

    @Test
    void moveRectangleBetweenTileCentersInterpolatesLinearly() {
        TiledMapTileLayer layer = mock(TiledMapTileLayer.class);
        Interpolation interpolation = Interpolation.linear;

        Rectangle rect = new Rectangle();

        GridPoint2 from = new GridPoint2(0, 0);
        GridPoint2 to = new GridPoint2(1, 0);

        try (MockedStatic<GdxGameUtils> utils = mockStatic(GdxGameUtils.class)) {
            utils.when(() -> moveRectangleAtTileCenter(layer, rect, from))
                 .thenAnswer(invocation -> {
                     rect.x = 0f;
                     rect.y = 0f;
                     return null;
                 });
            utils.when(() -> moveRectangleAtTileCenter(layer, rect, to))
                 .thenAnswer(invocation -> {
                     rect.x = 10f;
                     rect.y = 0f;
                     return null;
                 });

            TileMovement tm = new TileMovement(layer, interpolation);
            Rectangle result = tm.moveRectangleBetweenTileCenters(rect, from, to, 0.25f);

            assertEquals(2.5f, rect.x, 0.0001f);
            assertEquals(0f, rect.y, 0.0001f);
            assertSame(rect, result);
        }
    }
}
