package ru.mipt.bit.platformer.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import ru.mipt.bit.platformer.interfaces.MoveChecker;
import ru.mipt.bit.platformer.interfaces.MovementBlocker;
import ru.mipt.bit.platformer.interfaces.TileMover;

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

    private final TileMover tileMover;
    private final MoveChecker moveChecker;
    private final MovementBlocker movementBlocker;

    public Tank(Texture texture, GridPoint2 start, MoveChecker moveChecker, TileMover tileMover, MovementBlocker movementBlocker) {
        this.texture = texture;
        this.playerGraphics = new TextureRegion(texture);
        this.playerRectangle = createBoundingRectangle(playerGraphics);
        this.moveChecker = moveChecker;
        this.tileMover = tileMover;
        this.movementBlocker = movementBlocker;

        this.playerDestinationCoordinates.set(start);
        this.playerCoordinates.set(start);
        if (movementBlocker != null) {
            movementBlocker.occupy(new GridPoint2(start));
        }
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
        if (moveChecker.playerCanMoveTo(next)) {
            boolean reserved = movementBlocker == null || movementBlocker.reserveMove(new GridPoint2(playerCoordinates), next);
            if (reserved) {
                playerDestinationCoordinates.set(next);
                playerMovementProgress = 0f;
            }
        }
        rotation = direction.rotation;
    }

    public void update(float delta, float movementSpeed) {
        tileMover.moveBetweenTileCenters(playerRectangle, playerCoordinates, playerDestinationCoordinates, playerMovementProgress);
        playerMovementProgress = continueProgress(playerMovementProgress, delta, movementSpeed);

        if (isEqual(playerMovementProgress, 1f)) {
            if (movementBlocker != null && !(playerCoordinates.equals(playerDestinationCoordinates))) {
                movementBlocker.finishMove(new GridPoint2(playerCoordinates), new GridPoint2(playerDestinationCoordinates));
            }
            playerCoordinates.set(playerDestinationCoordinates);
        }
    }

    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, playerGraphics, playerRectangle, rotation);
    }
}
