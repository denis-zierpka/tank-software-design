package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import org.junit.jupiter.api.Test;

import ru.mipt.bit.platformer.models.Tree;
import ru.mipt.bit.platformer.interfaces.Renderer;
import ru.mipt.bit.platformer.interfaces.RectangleFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class TreeTest {

    @Test
    void constructorCreatesRectangleCorrectly() {
        Texture texture = mock(Texture.class);
        GridPoint2 input = new GridPoint2(2, 3);
        Rectangle rect = new Rectangle();

        Renderer renderer = mock(Renderer.class);
        RectangleFactory rectangleFactory = mock(RectangleFactory.class);
        when(rectangleFactory.create(any(TextureRegion.class))).thenReturn(rect);

        Tree tree = new Tree(texture, input, renderer, rectangleFactory);
        assertSame(rect, tree.getRectangle());

        assertEquals(2, tree.getCoordinates().x);
        assertEquals(3, tree.getCoordinates().y);

        input.set(9, 9);
        assertEquals(2, tree.getCoordinates().x);
        assertEquals(3, tree.getCoordinates().y);

        assertSame(texture, tree.getTexture());
    }

    @Test
    void renderDrawsWithZeroRotation() {
        Texture texture = mock(Texture.class);
        Rectangle rect = new Rectangle();

        Renderer renderer = mock(Renderer.class);
        RectangleFactory rectangleFactory = mock(RectangleFactory.class);
        when(rectangleFactory.create(any(TextureRegion.class))).thenReturn(rect);

        Tree tree = new Tree(texture, new GridPoint2(1, 1), renderer, rectangleFactory);

        Batch batch = mock(Batch.class);
        tree.render(batch);

        verify(renderer, times(1))
                .draw(eq(batch), any(TextureRegion.class), eq(rect), eq(0f));
    }
}
