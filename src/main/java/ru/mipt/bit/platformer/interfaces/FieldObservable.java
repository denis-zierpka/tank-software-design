package ru.mipt.bit.platformer.interfaces;

public interface FieldObservable {
    void addObserver(FieldObserver observer);
    void removeObserver(FieldObserver observer);
    void notifyTankAdded(ru.mipt.bit.platformer.models.Tank tank);
    void notifyTankRemoved(ru.mipt.bit.platformer.models.Tank tank);
    void notifyBulletAdded(ru.mipt.bit.platformer.models.Bullet bullet);
    void notifyBulletRemoved(ru.mipt.bit.platformer.models.Bullet bullet);
    void notifyTreeAdded(ru.mipt.bit.platformer.models.Tree tree);
    void notifyTreeRemoved(ru.mipt.bit.platformer.models.Tree tree);
}

