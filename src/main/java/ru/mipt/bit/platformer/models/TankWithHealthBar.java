package ru.mipt.bit.platformer.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;

import ru.mipt.bit.platformer.decorators.HealthBarRenderer;
import ru.mipt.bit.platformer.interfaces.MoveChecker;
import ru.mipt.bit.platformer.interfaces.MovementBlocker;
import ru.mipt.bit.platformer.interfaces.TileMover;

public class TankWithHealthBar extends Tank {
    private final HealthBarRenderer healthBarRenderer;
    private boolean showHealthBar = false;

    public TankWithHealthBar(Texture texture,
                             GridPoint2 start,
                             MoveChecker moveChecker,
                             TileMover tileMover,
                             MovementBlocker movementBlocker,
                             int maxHealth,
                             ShapeRenderer shapeRenderer) {
        super(texture, start, moveChecker, tileMover, movementBlocker, maxHealth);
        this.healthBarRenderer = new HealthBarRenderer(this, shapeRenderer);
    }

    @Override
    public void render(Batch batch) {
        renderBase(batch);
        if (showHealthBar) {
            healthBarRenderer.render(batch);
        }
    }

    @Override
    public void setHealthBarVisible(boolean showHealth) {
        this.showHealthBar = showHealth;
    }
}
