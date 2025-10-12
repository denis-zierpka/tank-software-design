package ru.mipt.bit.platformer.interfaces;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

public interface TileObjectPositioner {
    void moveAtTileCenter(TiledMapTileLayer groundLayer, Rectangle rect, GridPoint2 coords);
}
