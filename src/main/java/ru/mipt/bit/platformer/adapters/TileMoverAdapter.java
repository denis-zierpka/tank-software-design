package ru.mipt.bit.platformer.adapters;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import ru.mipt.bit.platformer.interfaces.TileMover;
import ru.mipt.bit.platformer.models.TileMovement;

public class TileMoverAdapter implements TileMover {

    private final TileMovement delegate;

    public TileMoverAdapter(TileMovement delegate) {
        this.delegate = delegate;
    }

    @Override
    public void moveBetweenTileCenters(Rectangle rect, GridPoint2 from, GridPoint2 to, float progress) {
        delegate.moveRectangleBetweenTileCenters(rect, from, to, progress);
    }
}
