package ru.mipt.bit.platformer.interfaces;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public interface RectangleFactory {
    Rectangle create(TextureRegion region);
}
