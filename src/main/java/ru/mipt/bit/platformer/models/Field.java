package ru.mipt.bit.platformer.models;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;

import ru.mipt.bit.platformer.interfaces.LevelRenderer;
import ru.mipt.bit.platformer.interfaces.TileObjectPositioner;

import java.util.ArrayList;
import java.util.List;

public class Field {

    private final LevelRenderer levelRenderer; 
    private final TiledMapTileLayer groundLayer;

    private final List<Tree> trees = new ArrayList<>();
    private final java.util.Set<GridPoint2> blocked = new java.util.HashSet<>();

    private final TileObjectPositioner tileObjectPositioner;

    public Field(LevelRenderer levelRenderer, TiledMapTileLayer groundLayer, TileObjectPositioner tileObjectPositioner) {
        this.levelRenderer = levelRenderer;
        this.groundLayer = groundLayer;
        this.tileObjectPositioner = tileObjectPositioner;
    }

    public TiledMapTileLayer ground() {
        return groundLayer;
    }

    public void render() {
        levelRenderer.render();
    }

    public void addTree(Tree tree) {
        trees.add(tree);
        tileObjectPositioner.moveAtTileCenter(groundLayer, tree.getRectangle(), tree.getCoordinates());
        blocked.add(new GridPoint2(tree.getCoordinates()));
    }

    private boolean isInsideBounds(GridPoint2 c) {
        int width = groundLayer.getWidth();
        int height = groundLayer.getHeight();
        return c.x >= 0 && c.y >= 0 && c.x < width && c.y < height;
    }

    public boolean playerCanMoveTo(GridPoint2 destination) {
        return isInsideBounds(destination) && !blocked.contains(destination);
    }

    public void renderTrees(Batch batch) {
        for (Tree tree : trees) {
            tree.render(batch);
        }
    }
}
