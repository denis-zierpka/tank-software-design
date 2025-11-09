package ru.mipt.bit.platformer;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;

import org.junit.jupiter.api.Test;

import ru.mipt.bit.platformer.interfaces.LevelRenderer;
import ru.mipt.bit.platformer.interfaces.TileObjectPositioner;
import ru.mipt.bit.platformer.models.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FieldReservationTest {

    private Field createField(int width, int height) {
        LevelRenderer levelRenderer = mock(LevelRenderer.class);
        TiledMapTileLayer layer = mock(TiledMapTileLayer.class);
        TileObjectPositioner positioner = mock(TileObjectPositioner.class);

        when(layer.getWidth()).thenReturn(width);
        when(layer.getHeight()).thenReturn(height);

        return new Field(levelRenderer, layer, positioner);
    }

    @Test
    void playerCanMoveToRespectsBounds() {
        Field field = createField(10, 8);

        assertTrue(field.playerCanMoveTo(new GridPoint2(0, 0)));
        assertTrue(field.playerCanMoveTo(new GridPoint2(9, 7)));
        assertTrue(field.playerCanMoveTo(new GridPoint2(5, 5)));

        assertFalse(field.playerCanMoveTo(new GridPoint2(-1, 0)));
        assertFalse(field.playerCanMoveTo(new GridPoint2(0, -1)));
        assertFalse(field.playerCanMoveTo(new GridPoint2(-1, -1)));

        assertFalse(field.playerCanMoveTo(new GridPoint2(10, 0)));
        assertFalse(field.playerCanMoveTo(new GridPoint2(0, 8)));
        assertFalse(field.playerCanMoveTo(new GridPoint2(10, 8)));
        assertFalse(field.playerCanMoveTo(new GridPoint2(15, 15)));
    }

    @Test
    void reserveMoveToAlreadyOccupiedTile() {
        Field field = createField(10, 8);

        GridPoint2 start = new GridPoint2(1, 1);
        GridPoint2 occupiedTile = new GridPoint2(2, 2);

        field.occupy(start);
        field.occupy(occupiedTile);

        assertFalse(field.reserveMove(start, occupiedTile));
        assertFalse(field.playerCanMoveTo(occupiedTile));
    }

    @Test
    void finishMoveWithoutReserving() {
        Field field = createField(10, 8);

        GridPoint2 start = new GridPoint2(1, 1);
        GridPoint2 dest = new GridPoint2(2, 1);

        field.occupy(start);
        field.finishMove(start, dest);

        assertTrue(field.playerCanMoveTo(start));
        assertFalse(field.playerCanMoveTo(dest));
    }

    @Test
    void reserveMoveKeepsStartBlocked() {
        Field field = createField(10, 8);

        GridPoint2 start = new GridPoint2(1, 1);
        GridPoint2 dest = new GridPoint2(2, 1);
        GridPoint2 otherDest = new GridPoint2(3, 1);

        field.occupy(start);
        assertFalse(field.playerCanMoveTo(start));

        assertTrue(field.reserveMove(start, dest));
        assertFalse(field.playerCanMoveTo(start));
        assertFalse(field.playerCanMoveTo(dest));

        assertFalse(field.reserveMove(new GridPoint2(4, 1), start));

        field.finishMove(start, dest);
        assertTrue(field.playerCanMoveTo(start));
        assertFalse(field.playerCanMoveTo(dest));

        field.occupy(otherDest);
        assertTrue(field.reserveMove(otherDest, start));
    }
}
