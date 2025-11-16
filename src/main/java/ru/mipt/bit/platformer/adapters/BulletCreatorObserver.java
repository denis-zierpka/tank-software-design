package ru.mipt.bit.platformer.adapters;

import ru.mipt.bit.platformer.interfaces.BulletCreator;
import ru.mipt.bit.platformer.interfaces.FieldObserver;
import ru.mipt.bit.platformer.models.Bullet;
import ru.mipt.bit.platformer.models.Field;
import ru.mipt.bit.platformer.models.Tank;
import ru.mipt.bit.platformer.models.Tree;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BulletCreatorObserver implements FieldObserver {
    private final Field field;
    private final ShapeRenderer shapeRenderer;
    private BulletCreator bulletCreator;
    
    public BulletCreatorObserver(Field field, ShapeRenderer shapeRenderer) {
        this.field = field;
        this.shapeRenderer = shapeRenderer;

        field.addObserver(this);
    }
    
    public void setBulletCreator(BulletCreator bulletCreator) {
        this.bulletCreator = bulletCreator;
    }
    
    private void updateBulletCreatorTanks() {
        bulletCreator = new BulletCreatorAdapter(field, shapeRenderer);
        
        Tank player = field.getPlayer();
        if (player != null) {
            player.setBulletCreator(bulletCreator);
        }
        for (Tank enemy : field.getEnemies()) {
            enemy.setBulletCreator(bulletCreator);
        }
    }
    
    @Override
    public void onTankAdded(Tank tank) {
        if (bulletCreator != null) {
            updateBulletCreatorTanks();
        }
    }

    @Override
    public void onTankRemoved(Tank tank) {
        if (bulletCreator != null) {
            updateBulletCreatorTanks();
        }
    }

    @Override
    public void onBulletAdded(Bullet bullet) {
        // No action needed for bullet creator
    }

    @Override
    public void onBulletRemoved(Bullet bullet) {
        // No action needed for bullet creator
    }

    @Override
    public void onTreeAdded(Tree tree) {
        // No action needed for bullet creator
    }

    @Override
    public void onTreeRemoved(Tree tree) {
        // No action needed for bullet creator
    }
    
    public BulletCreator getBulletCreator() {
        return bulletCreator;
    }
}

