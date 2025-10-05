package ru.mipt.bit.platformer.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class Tree {

    private final Texture texture;
    private final TextureRegion treeObstacleGraphics;
    private final GridPoint2 coordinates;
    private final Rectangle treeObstacleRectangle;

    public Tree(Texture texture, GridPoint2 coordinates) {
        this.texture = texture;
        this.treeObstacleGraphics = new TextureRegion(texture);
        this.coordinates = new GridPoint2(coordinates);
        this.treeObstacleRectangle = createBoundingRectangle(treeObstacleGraphics);
    }

    public GridPoint2 getCoordinates() {
        return coordinates;
    }
    
    public Rectangle getRectangle() {
        return treeObstacleRectangle;
    }
    
    public Texture getTexture() { 
        return texture; 
    }

    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, treeObstacleGraphics, treeObstacleRectangle, 0f);
    }

}
