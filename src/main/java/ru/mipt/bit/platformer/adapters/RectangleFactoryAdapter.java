package ru.mipt.bit.platformer.adapters;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import ru.mipt.bit.platformer.interfaces.RectangleFactory;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;

public class RectangleFactoryAdapter implements RectangleFactory {

    @Override
    public Rectangle create(TextureRegion region) {
        return createBoundingRectangle(region);
    }
}
