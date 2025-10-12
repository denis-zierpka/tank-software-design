package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import ru.mipt.bit.platformer.models.Direction;
import ru.mipt.bit.platformer.models.Tank;
import ru.mipt.bit.platformer.util.GdxGameUtils;
import ru.mipt.bit.platformer.adapters.TileMoverAdapter;
import ru.mipt.bit.platformer.interfaces.MoveChecker;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static ru.mipt.bit.platformer.util.GdxGameUtils.*;


public class TankTest {

    @Test
    void movesToFreeCellAndUpdatesData() {
        Texture texture = mock(Texture.class);
        MoveChecker field = mock(MoveChecker.class);
        TileMoverAdapter mover = mock(TileMoverAdapter.class);

        GridPoint2 start = new GridPoint2(1, 1);

        Rectangle rect = new Rectangle();
        try (MockedStatic<GdxGameUtils> utils = mockStatic(GdxGameUtils.class)) {
            utils.when(() -> createBoundingRectangle(any(TextureRegion.class))).thenReturn(rect);
            when(field.playerCanMoveTo(new GridPoint2(2, 1))).thenReturn(true);

            utils.when(() -> continueProgress(anyFloat(), anyFloat(), anyFloat())).thenReturn(1f);

            Tank tank = new Tank(texture, start, field, mover);

            tank.tryMove(Direction.RIGHT);
            tank.update(0.016f, 0.4f);

            GridPoint2 pos = tank.getCoordinates();
            assertEquals(2, pos.x);
            assertEquals(1, pos.y);

            verify(mover, atLeastOnce())
                    .moveBetweenTileCenters(eq(rect), any(GridPoint2.class), any(GridPoint2.class), anyFloat());
        }
    }

    @Test
    void doesNotMoveToBlockedCell() {
        Texture texture = mock(Texture.class);
        MoveChecker field = mock(MoveChecker.class);
        TileMoverAdapter mover = mock(TileMoverAdapter.class);

        GridPoint2 start = new GridPoint2(3, 4);

        Rectangle rect = new Rectangle();
        try (MockedStatic<GdxGameUtils> utils = mockStatic(GdxGameUtils.class)) {
            utils.when(() -> createBoundingRectangle(any(TextureRegion.class))).thenReturn(rect);
            when(field.playerCanMoveTo(new GridPoint2(2, 4))).thenReturn(false);
            utils.when(() -> continueProgress(anyFloat(), anyFloat(), anyFloat())).thenReturn(1f);

            Tank tank = new Tank(texture, start, field, mover);
            tank.tryMove(Direction.LEFT);
            tank.update(0.016f, 0.4f);

            GridPoint2 pos = tank.getCoordinates();
            assertEquals(3, pos.x);
            assertEquals(4, pos.y);

            verify(mover, atLeastOnce())
                    .moveBetweenTileCenters(eq(rect), any(GridPoint2.class), any(GridPoint2.class), anyFloat());
        }
    }

    @Test
    void rotationDoesNotChangeWhenMoveIsInProgress() {
        Texture texture = mock(Texture.class);
        MoveChecker field = mock(MoveChecker.class);
        TileMoverAdapter mover = mock(TileMoverAdapter.class);

        GridPoint2 start = new GridPoint2(1, 1);
        Batch batch = mock(Batch.class);
        Rectangle rect = new Rectangle();

        try (MockedStatic<GdxGameUtils> utils = mockStatic(GdxGameUtils.class)) {
            utils.when(() -> createBoundingRectangle(any(TextureRegion.class))).thenReturn(rect);
            when(field.playerCanMoveTo(new GridPoint2(2, 1))).thenReturn(true);
            utils.when(() -> continueProgress(anyFloat(), anyFloat(), anyFloat())).thenReturn(0.2f);

            Tank tank = new Tank(texture, start, field, mover);
            tank.tryMove(Direction.RIGHT);
            tank.update(0.016f, 0.4f);

            tank.render(batch);
            utils.verify(() -> drawTextureRegionUnscaled(eq(batch), any(TextureRegion.class), eq(rect), eq(0f)), times(1));

            tank.tryMove(Direction.UP);
            tank.render(batch);
            utils.verify(() -> drawTextureRegionUnscaled(eq(batch), any(TextureRegion.class), eq(rect), eq(90f)), never());
        }
    }
}
