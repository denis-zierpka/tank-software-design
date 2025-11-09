package ru.mipt.bit.platformer.models;

public class HealthBarToggleState {
    private boolean isBarVisible = false;

    public void toggle() {
        isBarVisible = !isBarVisible;
    }

    public boolean isBarVisible() {
        return isBarVisible;
    }
}
