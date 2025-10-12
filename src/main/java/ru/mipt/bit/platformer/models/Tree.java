package ru.mipt.bit.platformer.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import ru.mipt.bit.platformer.interfaces.RectangleFactory;
import ru.mipt.bit.platformer.interfaces.Renderer;

public class Tree {

    private final Texture texture;
    private final TextureRegion treeObstacleGraphics;
    private final GridPoint2 coordinates;
    private final Rectangle treeObstacleRectangle;
    private final Renderer renderer;

    public Tree(Texture texture, GridPoint2 coordinates, Renderer renderer, RectangleFactory rectangleFactory) {
        this.texture = texture;
        this.treeObstacleGraphics = new TextureRegion(texture);
        this.coordinates = new GridPoint2(coordinates);
        this.renderer = renderer;
        this.treeObstacleRectangle = rectangleFactory.create(treeObstacleGraphics);
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
        renderer.draw(batch, treeObstacleGraphics, treeObstacleRectangle, 0f);
    }
}
