package ru.mipt.bit.platformer.models;

public class HealthBarToggleState {
    private boolean isBarVisible = true;

    public void toggle() {
        isBarVisible = !isBarVisible;
    }

    public boolean isBarVisible() {
        return isBarVisible;
    }
}
