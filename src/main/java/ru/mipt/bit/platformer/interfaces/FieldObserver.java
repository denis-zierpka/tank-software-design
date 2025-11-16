package ru.mipt.bit.platformer.interfaces;

import ru.mipt.bit.platformer.models.Bullet;
import ru.mipt.bit.platformer.models.Tank;
import ru.mipt.bit.platformer.models.Tree;

public interface FieldObserver {
    void onTankAdded(Tank tank);
    void onTankRemoved(Tank tank);
    void onBulletAdded(Bullet bullet);
    void onBulletRemoved(Bullet bullet);
    void onTreeAdded(Tree tree);
    void onTreeRemoved(Tree tree);
}

