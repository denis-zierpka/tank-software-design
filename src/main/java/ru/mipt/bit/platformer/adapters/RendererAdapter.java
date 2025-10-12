package ru.mipt.bit.platformer.adapters;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import ru.mipt.bit.platformer.interfaces.Renderer;

import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class RendererAdapter implements Renderer {

    @Override
    public void draw(Batch batch, TextureRegion region, Rectangle rect, float rotation) {
        drawTextureRegionUnscaled(batch, region, rect, rotation);
    }
}
