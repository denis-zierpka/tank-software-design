package ru.mipt.bit.platformer.interfaces;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public interface Renderer {
    void draw(Batch batch, TextureRegion region, Rectangle rect, float rotation);
}
