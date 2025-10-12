package ru.mipt.bit.platformer.interfaces;

import com.badlogic.gdx.math.GridPoint2;

public interface MoveChecker {
    boolean playerCanMoveTo(GridPoint2 tile);
}
