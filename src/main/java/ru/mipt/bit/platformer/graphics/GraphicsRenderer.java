package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import ru.mipt.bit.platformer.interfaces.FieldObserver;
import ru.mipt.bit.platformer.models.Bullet;
import ru.mipt.bit.platformer.models.Field;
import ru.mipt.bit.platformer.models.HealthBarToggleState;
import ru.mipt.bit.platformer.models.Tank;
import ru.mipt.bit.platformer.models.Tree;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GraphicsRenderer implements FieldObserver {
    
    private final Batch batch;
    private final Field field;
    
    private final List<Tank> tanks = new ArrayList<>();
    private final List<Bullet> bullets = new ArrayList<>();
    
    public GraphicsRenderer(Batch batch, Field field) {
        this.batch = batch;
        this.field = field;
        
        field.addObserver(this);
    }
    
    public void render(HealthBarToggleState healthBarToggleState) {
        // Clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        
        field.render();
        batch.begin();
        
        boolean showHealth = healthBarToggleState.isBarVisible();
        
        Tank player = field.getPlayer();
        if (player != null) {
            player.setHealthBarVisible(showHealth);
            player.render(batch);
        }
        for (Tank tank : tanks) {
            if (tank == player)
                continue;
            
            tank.setHealthBarVisible(showHealth);
            tank.render(batch);
        }
        for (Bullet bullet : bullets) {
            bullet.render(batch);
        }
        field.renderTrees(batch);
        batch.end();
    }
    
    @Override
    public void onTankAdded(Tank tank) {
        if (!tanks.contains(tank)) {
            tanks.add(tank);
        }
    }
    
    @Override
    public void onTankRemoved(Tank tank) {
        tanks.remove(tank);
    }
    
    @Override
    public void onBulletAdded(Bullet bullet) {
        if (!bullets.contains(bullet)) {
            bullets.add(bullet);
        }
    }
    
    @Override
    public void onBulletRemoved(Bullet bullet) {
        bullets.remove(bullet);
    }
    
    @Override
    public void onTreeAdded(Tree tree) {
        // No action needed for level graphics renderer
    }
    
    @Override
    public void onTreeRemoved(Tree tree) {
        // No action needed for level graphics renderer
    }
}

