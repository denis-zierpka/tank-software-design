package ru.mipt.bit.platformer.interfaces;

public class TickContext {
    private final float deltaTime;
    private final float movementSpeed;
    
    public TickContext(float deltaTime, float movementSpeed) {
        this.deltaTime = deltaTime;
        this.movementSpeed = movementSpeed;
    }
    
    public float getDeltaTime() {
        return deltaTime;
    }
    
    public float getMovementSpeed() {
        return movementSpeed;
    }
}
