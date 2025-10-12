package ru.mipt.bit.platformer.adapters;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import ru.mipt.bit.platformer.interfaces.TileObjectPositioner;

import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public class TileObjectPositionerAdapter implements TileObjectPositioner {

    @Override
    public void moveAtTileCenter(TiledMapTileLayer groundLayer, Rectangle rect, GridPoint2 coords) {
        moveRectangleAtTileCenter(groundLayer, rect, coords);
    }
}
