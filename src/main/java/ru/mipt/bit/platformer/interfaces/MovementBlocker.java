package ru.mipt.bit.platformer.interfaces;

import com.badlogic.gdx.math.GridPoint2;

public interface MovementBlocker {
    void occupy(GridPoint2 coordinates);
    boolean reserveMove(GridPoint2 from, GridPoint2 to);
    void finishMove(GridPoint2 from, GridPoint2 to);
}
