package ru.mipt.bit.platformer.interfaces;

import com.badlogic.gdx.math.GridPoint2;

import ru.mipt.bit.platformer.models.Direction;
import ru.mipt.bit.platformer.models.Tank;

public interface BulletCreator {
    void createBullet(GridPoint2 startCoordinates, Direction direction, Tank shooter);
}

