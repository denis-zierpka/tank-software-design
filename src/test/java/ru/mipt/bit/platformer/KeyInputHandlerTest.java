package ru.mipt.bit.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import ru.mipt.bit.platformer.models.Direction;
import ru.mipt.bit.platformer.models.KeyInputHandler;
import ru.mipt.bit.platformer.models.Tank;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class KeyInputHandlerTest {

    private Input originalInput;

    @BeforeEach
    void saveInput() {
        originalInput = Gdx.input;
    }

    @AfterEach
    void restoreInput() {
        Gdx.input = originalInput;
    }

    @Test
    void handleMovementCallsTryMoveForPressedKey() {
        KeyInputHandler handler = new KeyInputHandler();
        Tank tank = mock(Tank.class);
        Map<Integer, Direction> keyToDir = Map.of(1, Direction.UP, 2, Direction.DOWN);

        Input input = mock(Input.class);
        Gdx.input = input;

        when(input.isKeyPressed(1)).thenReturn(true);
        when(input.isKeyPressed(2)).thenReturn(false);

        var cmd = handler.getMoveCommand(tank, keyToDir);
        if (cmd != null) 
            cmd.execute();

        verify(tank, times(1)).tryMove(Direction.UP);
        verifyNoMoreInteractions(tank);
    }

    @Test
    void handleMovementDoesNothingWhenKeysNotPressed() {
        KeyInputHandler handler = new KeyInputHandler();
        Tank tank = mock(Tank.class);
        Map<Integer, Direction> keyToDir = Map.of(1, Direction.UP, 2, Direction.DOWN);

        Input input = mock(Input.class);
        Gdx.input = input;

        when(input.isKeyPressed(anyInt())).thenReturn(false);

        var cmd = handler.getMoveCommand(tank, keyToDir);
        if (cmd != null) 
            cmd.execute();

        verifyNoInteractions(tank);
    }

    @Test
    void handleMovementStopsAfterFirstPressedKey() {
        Map<Integer, Direction> keyToDir = new LinkedHashMap<>();
        keyToDir.put(1, Direction.UP);
        keyToDir.put(2, Direction.LEFT);

        Input input = mock(Input.class);
        Gdx.input = input;

        Tank tank = mock(Tank.class);
        KeyInputHandler handler = new KeyInputHandler();

        when(input.isKeyPressed(1)).thenReturn(true);
        when(input.isKeyPressed(2)).thenReturn(true);

        var cmd = handler.getMoveCommand(tank, keyToDir);
        if (cmd != null) 
            cmd.execute();

        verify(tank, times(1)).tryMove(Direction.UP);
        verify(tank, never()).tryMove(Direction.LEFT);
    }
}
