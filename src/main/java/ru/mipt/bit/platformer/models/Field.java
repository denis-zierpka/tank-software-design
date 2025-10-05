package ru.mipt.bit.platformer.models;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.List;

import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public class Field {
    
    private final MapRenderer levelRenderer;
    private final TiledMapTileLayer groundLayer;

    private final List<Tree> trees = new ArrayList<>();
    private final java.util.Set<GridPoint2> blocked = new java.util.HashSet<>();

    public Field(MapRenderer levelRenderer, TiledMapTileLayer groundLayer) {
        this.levelRenderer = levelRenderer;
        this.groundLayer = groundLayer;
    }

    public TiledMapTileLayer ground() {
        return groundLayer;
    }

    public void render() {
        levelRenderer.render();
    }

    public void addTree(Tree tree) {
        trees.add(tree);
        moveRectangleAtTileCenter(groundLayer, tree.getRectangle(), tree.getCoordinates());
        blocked.add(new GridPoint2(tree.getCoordinates()));
    }

    public boolean playerCanMoveTo(GridPoint2 destination) {
        return !blocked.contains(destination);
    }

    public void renderTrees(Batch batch) {
        for (Tree tree : trees) {
            tree.render(batch);
        }
    }
}
