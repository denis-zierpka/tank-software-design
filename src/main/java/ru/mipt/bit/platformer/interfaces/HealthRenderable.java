package ru.mipt.bit.platformer.interfaces;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public interface HealthRenderable {
    void render(Batch batch);
    Rectangle getRectangle();
    int getMaxHealth();
    int getCurrentHealth();

    default float getHealthPercentage() {
        int maxHealth = getMaxHealth();
        return maxHealth > 0 ? (float) getCurrentHealth() / maxHealth : 0f;
    }
    default void setHealthBarVisible(boolean showHealth) {
        // no operation in base implementation
    }
}
