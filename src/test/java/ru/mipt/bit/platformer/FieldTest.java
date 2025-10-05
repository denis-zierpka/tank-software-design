package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import ru.mipt.bit.platformer.models.Field;
import ru.mipt.bit.platformer.models.Tree;
import ru.mipt.bit.platformer.util.GdxGameUtils;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class FieldTest {

    @Test
    void renderFieldVerify() {
        MapRenderer mapRenderer = mock(MapRenderer.class);
        TiledMapTileLayer layer = mock(TiledMapTileLayer.class);

        Field field = new Field(mapRenderer, layer);

        field.render();

        verify(mapRenderer, times(1)).render();
        verifyNoMoreInteractions(mapRenderer);
    }

    @Test
    void addTreeCallsMoveMethod() {
        MapRenderer mapRenderer = mock(MapRenderer.class);
        TiledMapTileLayer layer = mock(TiledMapTileLayer.class);
        Field field = new Field(mapRenderer, layer);


        Tree tree = mock(Tree.class);
        Rectangle rect = new Rectangle(); 
        GridPoint2 coords = new GridPoint2(2, 3);

        when(tree.getRectangle()).thenReturn(rect);
        when(tree.getCoordinates()).thenReturn(coords);

        try (MockedStatic<GdxGameUtils> utils = Mockito.mockStatic(GdxGameUtils.class)) {
            field.addTree(tree);

            utils.verify(() ->
                    GdxGameUtils.moveRectangleAtTileCenter(layer, rect, coords),
                times(1)
            );
        }

        assertFalse(field.playerCanMoveTo(new GridPoint2(coords)));
        assertTrue(field.playerCanMoveTo(new GridPoint2(5, 5)));
    }

    @Test
    void renderTreesDrawsEachTree() {
        MapRenderer mapRenderer = mock(MapRenderer.class);
        TiledMapTileLayer layer = mock(TiledMapTileLayer.class);
        Field field = new Field(mapRenderer, layer);

        Tree t1 = mock(Tree.class);
        Tree t2 = mock(Tree.class);
        when(t1.getRectangle()).thenReturn(new Rectangle());
        when(t1.getCoordinates()).thenReturn(new GridPoint2(1, 1));
        when(t2.getRectangle()).thenReturn(new Rectangle());
        when(t2.getCoordinates()).thenReturn(new GridPoint2(2, 2));

        try (MockedStatic<GdxGameUtils> utils = Mockito.mockStatic(GdxGameUtils.class)) {
            field.addTree(t1);
            field.addTree(t2);

            Batch batch = mock(Batch.class);
            field.renderTrees(batch);

            verify(t1, times(1)).render(batch);
            verify(t2, times(1)).render(batch);
        }
    }
}
