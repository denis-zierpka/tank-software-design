package ru.mipt.bit.platformer.models;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;

import ru.mipt.bit.platformer.interfaces.TileObjectPositioner;

public class TileMovement {

    private final TiledMapTileLayer tileLayer;
    private final Interpolation interpolation;
    private final TileObjectPositioner tileObjectPositioner;

    public TileMovement(TiledMapTileLayer tileLayer, Interpolation interpolation, TileObjectPositioner tileObjectPositioner) {
        this.tileLayer = tileLayer;
        this.interpolation = interpolation;
        this.tileObjectPositioner = tileObjectPositioner;
    }

    public Rectangle moveRectangleBetweenTileCenters(Rectangle rectangle, GridPoint2 fromTileCoordinates, GridPoint2 toTileCoordinates, float progress) {
        tileObjectPositioner.moveAtTileCenter(tileLayer, rectangle, fromTileCoordinates);
        float fromTileBottomLeftX = rectangle.x;
        float fromTileBottomLeftY = rectangle.y;

        tileObjectPositioner.moveAtTileCenter(tileLayer, rectangle, toTileCoordinates);
        float toTileBottomLeftX = rectangle.x;
        float toTileBottomLeftY = rectangle.y;

        float intermediateBottomLeftX = interpolation.apply(fromTileBottomLeftX, toTileBottomLeftX, progress);
        float intermediateBottomLeftY = interpolation.apply(fromTileBottomLeftY, toTileBottomLeftY, progress);

        return rectangle
                .setX(intermediateBottomLeftX)
                .setY(intermediateBottomLeftY);
    }
}
