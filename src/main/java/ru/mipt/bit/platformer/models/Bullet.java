package ru.mipt.bit.platformer.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import ru.mipt.bit.platformer.interfaces.Tickable;
import ru.mipt.bit.platformer.interfaces.TickContext;

import java.util.List;

public class Bullet implements Tickable {
    
    private static final float BULLET_SPEED = 5.0f;
    private static final float BULLET_SIZE = 8f;
    private static final int DEFAULT_DAMAGE = 10;

    private final GridPoint2 coordinates;
    private final Direction direction;
    private final int damage;
    private final Rectangle bulletRectangle;
    private final ShapeRenderer shapeRenderer;
    private final Tank shooter;
    
    private float movementProgress = 0f;
    private boolean alive = true;
    private boolean hasCheckedInitialCollision = false;
    
    private final Field field;
    private final List<Tank> tanks;

    public Bullet(GridPoint2 startCoordinates, Direction direction, Field field, List<Tank> tanks, ShapeRenderer shapeRenderer, Tank shooter) {
        this(startCoordinates, direction, field, tanks, shapeRenderer, DEFAULT_DAMAGE, shooter);
    }

    public Bullet(GridPoint2 startCoordinates, Direction direction, Field field, List<Tank> tanks, ShapeRenderer shapeRenderer, int damage, Tank shooter) {
        this.coordinates = new GridPoint2(startCoordinates);
        this.direction = direction;
        this.damage = damage;
        this.field = field;
        this.tanks = tanks;
        this.shapeRenderer = shapeRenderer;
        this.shooter = shooter;
        
        this.bulletRectangle = new Rectangle();
        this.bulletRectangle.width = BULLET_SIZE;
        this.bulletRectangle.height = BULLET_SIZE;
    }
    
    private void checkCollision(GridPoint2 tileToCheck) {
        if (!alive)
            return;
        
        if (!field.isInsideBounds(tileToCheck)) {
            destroy();
            return;
        }
        
        for (Tank tank : tanks) {
            if (tank == null)
                continue;
            if (tank.getCurrentHealth() <= 0)
                continue;
            
            GridPoint2 tankPos = tank.getCoordinates();
            if (tankPos.equals(tileToCheck)) {
                Tank player = field.getPlayer();
                boolean shooterIsEnemy = (shooter != null && shooter != player);
                boolean targetIsEnemy = (tank != player);
                
                if (shooterIsEnemy && targetIsEnemy && !field.isEnemyFriendlyFire()) {
                    destroy();
                    return;
                }
                
                tank.takeDamage(damage);
                destroy();
                return;
            }
        }
        
        if (field.isBlocked(tileToCheck)) {
            destroy();
            return;
        }
    }

    public void tick(float deltaTime) {
        if (!alive)
            return;

        if (!hasCheckedInitialCollision) {
            hasCheckedInitialCollision = true;
            
            checkCollision(new GridPoint2(coordinates));
            if (!alive)
                return;
        }
        
        movementProgress += deltaTime * BULLET_SPEED;
        updateRectanglePositionWithProgress();
        
        while (movementProgress >= 1.0f && alive) {
            movementProgress -= 1.0f;
            coordinates.x += direction.dx;
            coordinates.y += direction.dy;
            
            GridPoint2 currentTile = new GridPoint2(coordinates.x, coordinates.y);
            checkCollision(currentTile);
            if (!alive)
                return;
            
        }
    }
    
    @Override
    public void tick(TickContext context) {
        tick(context.getDeltaTime());
    }
    
    private void updateRectanglePositionWithProgress() {
        if (!alive)
            return;
        
        float tileSize = field.ground().getTileWidth();
        float baseX = coordinates.x * tileSize + tileSize / 2f - BULLET_SIZE / 2f;
        float baseY = coordinates.y * tileSize + tileSize / 2f - BULLET_SIZE / 2f;
        float offsetX = direction.dx * tileSize * movementProgress;
        float offsetY = direction.dy * tileSize * movementProgress;
        baseX += offsetX;
        baseY += offsetY;
        
        bulletRectangle.setPosition(baseX, baseY);
    }

    public void render(Batch batch) {
        if (!alive)
            return;
        
        batch.end();
        
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(bulletRectangle.x, bulletRectangle.y, bulletRectangle.width, bulletRectangle.height);
        shapeRenderer.end();
        
        batch.begin();
    }

    public GridPoint2 getCoordinates() {
        return coordinates;
    }

    public Rectangle getRectangle() {
        return bulletRectangle;
    }

    public boolean isAlive() {
        return alive;
    }

    public void destroy() {
        if (alive) {
            alive = false;
        }
    }

    public Direction getDirection() {
        return direction;
    }
}

