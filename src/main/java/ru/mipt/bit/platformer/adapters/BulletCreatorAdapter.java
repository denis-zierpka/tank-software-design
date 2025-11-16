package ru.mipt.bit.platformer.adapters;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;

import ru.mipt.bit.platformer.interfaces.BulletCreator;
import ru.mipt.bit.platformer.models.Bullet;
import ru.mipt.bit.platformer.models.Direction;
import ru.mipt.bit.platformer.models.Field;
import ru.mipt.bit.platformer.models.Tank;

import java.util.ArrayList;
import java.util.List;

public class BulletCreatorAdapter implements BulletCreator {
    private final Field field;
    private final ShapeRenderer shapeRenderer;

    public BulletCreatorAdapter(Field field, ShapeRenderer shapeRenderer) {
        this.field = field;
        this.shapeRenderer = shapeRenderer;
    }

    @Override
    public void createBullet(GridPoint2 startCoordinates, Direction direction, Tank shooter) {
        List<Tank> currentTanks = new ArrayList<>(field.getTanks());
        
        Bullet bullet = new Bullet(startCoordinates, direction, field, currentTanks, shapeRenderer, shooter);
        
        field.addBullet(bullet);
    }
}

