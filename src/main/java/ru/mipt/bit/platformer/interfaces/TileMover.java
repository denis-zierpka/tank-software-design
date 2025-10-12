package ru.mipt.bit.platformer.interfaces;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

public interface TileMover {
    void moveBetweenTileCenters(Rectangle rect, GridPoint2 from, GridPoint2 to, float progress);
}
