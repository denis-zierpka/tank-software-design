package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;

public class Tank {

    private final Texture texture;
    private final TextureRegion playerGraphics;
    private final Rectangle playerRectangle;

    private final GridPoint2 playerCoordinates = new GridPoint2();
    private final GridPoint2 playerDestinationCoordinates = new GridPoint2();

    private float rotation = 0f;
    private float playerMovementProgress = 1f;

    private final TileMovement tileMovement;
    private final Field field;

    public Tank(Texture texture, GridPoint2 start, Field field, Interpolation interpolation) {
        this.texture = texture;
        this.playerGraphics = new TextureRegion(texture);
        this.playerRectangle = createBoundingRectangle(playerGraphics);
        this.field = field;
        this.tileMovement = new TileMovement(field.ground(), interpolation);

        this.playerDestinationCoordinates.set(start);
        this.playerCoordinates.set(start);
    }

    public Rectangle getRectangle() {
        return playerRectangle; 
    }

    public GridPoint2 getCoordinates() {
        return playerCoordinates; 
    }

    public Texture getTexture() {
        return texture;
    }

    public void tryMove(Direction direction) {
        if (!isEqual(playerMovementProgress, 1f)) return;

        GridPoint2 next = new GridPoint2(playerCoordinates.x + direction.dx, playerCoordinates.y + direction.dy);
        if (field.playerCanMoveTo(next)) {
            playerDestinationCoordinates.set(next);
            playerMovementProgress = 0f;
        }
        rotation = direction.rotation;
    }

    public void update(float delta, float movementSpeed) {
        tileMovement.moveRectangleBetweenTileCenters(playerRectangle, playerCoordinates, playerDestinationCoordinates, playerMovementProgress);
        playerMovementProgress = continueProgress(playerMovementProgress, delta, movementSpeed);

        if (isEqual(playerMovementProgress, 1f)) {
            playerCoordinates.set(playerDestinationCoordinates);
        }
    }

    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, playerGraphics, playerRectangle, rotation);
    }
}
