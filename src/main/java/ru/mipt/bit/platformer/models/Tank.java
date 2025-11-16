package ru.mipt.bit.platformer.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import ru.mipt.bit.platformer.interfaces.MoveChecker;
import ru.mipt.bit.platformer.interfaces.MovementBlocker;
import ru.mipt.bit.platformer.interfaces.TileMover;
import ru.mipt.bit.platformer.interfaces.HealthRenderable;
import ru.mipt.bit.platformer.interfaces.BulletCreator;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class Tank implements HealthRenderable {
    
    public static final float MOVEMENT_SPEED = 0.4f;
    public static final int DEFAULT_MIN_HEALTH = 80;
    public static final int DEFAULT_MAX_HEALTH = 100;

    private final Texture texture;
    private final TextureRegion playerGraphics;
    private final Rectangle playerRectangle;

    private final GridPoint2 playerCoordinates = new GridPoint2();
    private final GridPoint2 playerDestinationCoordinates = new GridPoint2();

    private float rotation = 0f;
    private float playerMovementProgress = 1f;

    private final TileMover tileMover;
    private final MoveChecker moveChecker;
    private final MovementBlocker movementBlocker;
    private BulletCreator bulletCreator;

    private final int maxHealth;
    private int currentHealth;

    public Tank(Texture texture, GridPoint2 start, MoveChecker moveChecker, TileMover tileMover, MovementBlocker movementBlocker) {
        this(texture, start, moveChecker, tileMover, movementBlocker, DEFAULT_MAX_HEALTH);
    }

    public Tank(Texture texture, GridPoint2 start, MoveChecker moveChecker, TileMover tileMover, MovementBlocker movementBlocker, int maxHealth) {
        this.texture = texture;
        this.playerGraphics = new TextureRegion(texture);
        this.playerRectangle = createBoundingRectangle(playerGraphics);
        this.moveChecker = moveChecker;
        this.tileMover = tileMover;
        this.movementBlocker = movementBlocker;
        this.maxHealth = maxHealth;
        int minHealth = (int) (maxHealth * 0.8f);
        this.currentHealth = minHealth + (int) (Math.random() * (maxHealth - minHealth + 1));

        this.playerDestinationCoordinates.set(start);
        this.playerCoordinates.set(start);
        if (movementBlocker != null) {
            movementBlocker.occupy(new GridPoint2(start));
        }
    }

    @Override
    public Rectangle getRectangle() {
        return playerRectangle; 
    }

    public GridPoint2 getCoordinates() {
        return playerCoordinates; 
    }
    
    public GridPoint2 getDestinationCoordinates() {
        return playerDestinationCoordinates;
    }
    
    public boolean isMoving() {
        return !isEqual(playerMovementProgress, 1f);
    }

    public Texture getTexture() {
        return texture;
    }

    @Override
    public int getMaxHealth() {
        return maxHealth;
    }

    @Override
    public int getCurrentHealth() {
        return currentHealth;
    }

    @Override
    public float getHealthPercentage() {
        return maxHealth > 0 ? (float) currentHealth / maxHealth : 0f;
    }
    
    public void takeDamage(int damage) {
        currentHealth -= damage;
        if (currentHealth < 0) {
            currentHealth = 0;
        }
    }
    
    public float getRotation() {
        return rotation;
    }
    
    public Direction getCurrentDirection() {
        if (isEqual(rotation, 90f)) return Direction.UP;
        if (isEqual(rotation, -180f)) return Direction.LEFT;
        if (isEqual(rotation, -90f)) return Direction.DOWN;
        return Direction.RIGHT;
    }
    
    public void setBulletCreator(BulletCreator bulletCreator) {
        this.bulletCreator = bulletCreator;
    }
    
    public void tryShoot() {
        if (bulletCreator == null)
            throw new IllegalStateException("Bullet creator is not set");
        
        Direction direction = getCurrentDirection();
        
        GridPoint2 bulletStart = new GridPoint2(
            playerCoordinates.x + direction.dx,
            playerCoordinates.y + direction.dy
        );
        
        bulletCreator.createBullet(bulletStart, direction, this);
    }

    public void tryMove(Direction direction) {
        if (!isEqual(playerMovementProgress, 1f)) return;

        GridPoint2 next = new GridPoint2(playerCoordinates.x + direction.dx, playerCoordinates.y + direction.dy);
        if (moveChecker.playerCanMoveTo(next)) {
            boolean reserved = movementBlocker == null || movementBlocker.reserveMove(new GridPoint2(playerCoordinates), next);
            if (reserved) {
                playerDestinationCoordinates.set(next);
                playerMovementProgress = 0f;
            }
        }
        rotation = direction.rotation;
    }

    public void tick(float delta, float movementSpeed) {
        tileMover.moveBetweenTileCenters(playerRectangle, playerCoordinates, playerDestinationCoordinates, playerMovementProgress);
        playerMovementProgress = continueProgress(playerMovementProgress, delta, movementSpeed);

        if (isEqual(playerMovementProgress, 1f)) {
            if (movementBlocker != null && !(playerCoordinates.equals(playerDestinationCoordinates))) {
                movementBlocker.finishMove(new GridPoint2(playerCoordinates), new GridPoint2(playerDestinationCoordinates));
            }
            playerCoordinates.set(playerDestinationCoordinates);
        }
    }

    public void renderBase(Batch batch) {
        drawTextureRegionUnscaled(batch, playerGraphics, playerRectangle, rotation);
    }

    @Override
    public void render(Batch batch) {
        renderBase(batch);
    }
}
