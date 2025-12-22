package ru.mipt.bit.platformer.interfaces;

public interface Tickable {
    boolean isAlive();
    void tick(TickContext context);
}
