package ru.mipt.bit.platformer;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;

import ru.mipt.bit.platformer.models.TileMovement;
import ru.mipt.bit.platformer.interfaces.TileObjectPositioner;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TileMovementTest {

    @Test
    void moveRectangleCallsFuncTwice() {
        TiledMapTileLayer layer = mock(TiledMapTileLayer.class);
        Interpolation interpolation = mock(Interpolation.class);
        TileObjectPositioner positioner = mock(TileObjectPositioner.class);

        Rectangle rect = new Rectangle();

        GridPoint2 from = new GridPoint2(1, 1);
        GridPoint2 to = new GridPoint2(2, 3);

        doAnswer(invocation -> {
            rect.x = 10f;
            rect.y = 20f;
            return null;
        }).when(positioner).moveAtTileCenter(layer, rect, from);

        doAnswer(invocation -> {
            rect.x = 30f;
            rect.y = 40f;
            return null;
        }).when(positioner).moveAtTileCenter(layer, rect, to);

        when(interpolation.apply(10f, 30f, 0.5f)).thenReturn(20f);
        when(interpolation.apply(20f, 40f, 0.5f)).thenReturn(30f);

        TileMovement tileMovement = new TileMovement(layer, interpolation, positioner);
        Rectangle result = tileMovement.moveRectangleBetweenTileCenters(rect, from, to, 0.5f);

        verify(positioner, times(1)).moveAtTileCenter(layer, rect, from);
        verify(positioner, times(1)).moveAtTileCenter(layer, rect, to);

        verify(interpolation).apply(10f, 30f, 0.5f);
        verify(interpolation).apply(20f, 40f, 0.5f);

        assertSame(rect, result);

        assertEquals(20f, rect.x);
        assertEquals(30f, rect.y);
    }

    @Test
    void moveRectangleBetweenTileCentersInterpolatesLinearly() {
        TiledMapTileLayer layer = mock(TiledMapTileLayer.class);
        Interpolation interpolation = Interpolation.linear;
        TileObjectPositioner positioner = mock(TileObjectPositioner.class);

        Rectangle rect = new Rectangle();

        GridPoint2 from = new GridPoint2(0, 0);
        GridPoint2 to = new GridPoint2(1, 0);

        doAnswer(invocation -> {
            rect.x = 0f;
            rect.y = 0f;
            return null;
        }).when(positioner).moveAtTileCenter(layer, rect, from);

        doAnswer(invocation -> {
            rect.x = 10f;
            rect.y = 0f;
            return null;
        }).when(positioner).moveAtTileCenter(layer, rect, to);

        TileMovement tm = new TileMovement(layer, interpolation, positioner);
        Rectangle result = tm.moveRectangleBetweenTileCenters(rect, from, to, 0.25f);

        assertEquals(2.5f, rect.x, 0.0001f);
        assertEquals(0f, rect.y, 0.0001f);
        assertSame(rect, result);
    }
}
