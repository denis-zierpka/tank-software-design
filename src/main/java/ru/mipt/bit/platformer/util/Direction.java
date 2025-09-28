package ru.mipt.bit.platformer.util;

public enum Direction {
    UP(0, 1, 90f),
    LEFT(-1, 0, -180f),
    DOWN(0, -1, -90f),
    RIGHT(1, 0, 0f);

    public final int dx;
    public final int dy;
    public final float rotation;

    Direction(int dx, int dy, float rotation) {
        this.dx = dx;
        this.dy = dy;
        this.rotation = rotation;
    }
}
