package ru.mipt.bit.platformer.decorators;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import ru.mipt.bit.platformer.interfaces.HealthRenderable;

public class HealthBarRenderer implements HealthRenderable {
    private final HealthRenderable wrapped;
    private final ShapeRenderer shapeRenderer;

    public HealthBarRenderer(HealthRenderable wrapped, ShapeRenderer shapeRenderer) {
        this.wrapped = wrapped;
        this.shapeRenderer = shapeRenderer;
    }
    
    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    @Override
    public void render(Batch batch) {
        renderHealthBar(batch);
    }

    private void renderHealthBar(Batch batch) {
        Rectangle rect = wrapped.getRectangle();
        float healthPercent = wrapped.getHealthPercentage();
        
        float barWidth = rect.width;
        float barHeight = 5f;
        float barX = rect.x;
        float barY = rect.y + rect.height + 2f;
        
        batch.end();
        
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(barX, barY, barWidth * healthPercent, barHeight);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);
        shapeRenderer.end();
        
        batch.begin();
    }

    @Override
    public Rectangle getRectangle() {
        return wrapped.getRectangle();
    }

    @Override
    public int getMaxHealth() {
        return wrapped.getMaxHealth();
    }

    @Override
    public int getCurrentHealth() {
        return wrapped.getCurrentHealth();
    }
}
