package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import ru.mipt.bit.platformer.models.Field;
import ru.mipt.bit.platformer.models.Tree;
import ru.mipt.bit.platformer.interfaces.LevelRenderer;
import ru.mipt.bit.platformer.interfaces.TileObjectPositioner;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class FieldTest {

    @Test
    void renderFieldVerify() {
        LevelRenderer levelRenderer = mock(LevelRenderer.class);
        TiledMapTileLayer layer = mock(TiledMapTileLayer.class);
        TileObjectPositioner positioner = mock(TileObjectPositioner.class);

        Field field = new Field(levelRenderer, layer, positioner);

        field.render();

        verify(levelRenderer, times(1)).render();
        verifyNoMoreInteractions(levelRenderer);
    }

    @Test
    void addTreeCallsMoveMethod() {
        LevelRenderer levelRenderer = mock(LevelRenderer.class);
        TiledMapTileLayer layer = mock(TiledMapTileLayer.class);
        TileObjectPositioner positioner = mock(TileObjectPositioner.class);

        when(layer.getWidth()).thenReturn(10);
        when(layer.getHeight()).thenReturn(8);

        Field field = new Field(levelRenderer, layer, positioner);

        Tree tree = mock(Tree.class);
        Rectangle rect = new Rectangle(); 
        GridPoint2 coords = new GridPoint2(2, 3);

        when(tree.getRectangle()).thenReturn(rect);
        when(tree.getCoordinates()).thenReturn(coords);

        field.addTree(tree);

        verify(positioner, times(1))
                .moveAtTileCenter(layer, rect, coords);

        assertFalse(field.playerCanMoveTo(new GridPoint2(coords)));
        assertTrue(field.playerCanMoveTo(new GridPoint2(5, 5)));
    }

    @Test
    void renderTreesDrawsEachTree() {
        LevelRenderer levelRenderer = mock(LevelRenderer.class);
        TiledMapTileLayer layer = mock(TiledMapTileLayer.class);
        TileObjectPositioner positioner = mock(TileObjectPositioner.class);
        Field field = new Field(levelRenderer, layer, positioner);

        Tree t1 = mock(Tree.class);
        Tree t2 = mock(Tree.class);
        when(t1.getRectangle()).thenReturn(new Rectangle());
        when(t1.getCoordinates()).thenReturn(new GridPoint2(1, 1));
        when(t2.getRectangle()).thenReturn(new Rectangle());
        when(t2.getCoordinates()).thenReturn(new GridPoint2(2, 2));

        field.addTree(t1);
        field.addTree(t2);

        Batch batch = mock(Batch.class);
        field.renderTrees(batch);

        verify(t1, times(1)).render(batch);
        verify(t2, times(1)).render(batch);
    }
}
