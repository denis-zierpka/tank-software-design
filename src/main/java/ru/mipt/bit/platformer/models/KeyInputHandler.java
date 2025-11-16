package ru.mipt.bit.platformer.models;

import com.badlogic.gdx.Gdx;
import static com.badlogic.gdx.Input.Keys.*;
import java.util.Map;

import ru.mipt.bit.platformer.commands.MoveCommandAdapter;
import ru.mipt.bit.platformer.commands.ShootCommand;
import ru.mipt.bit.platformer.commands.ToggleHealthBarCommand;
import ru.mipt.bit.platformer.interfaces.Command;

public class KeyInputHandler {
    
    public static final java.util.Map<Integer, Direction> KEY_TO_DIRECTION = java.util.Map.of(
        UP, Direction.UP,
        LEFT, Direction.LEFT,
        DOWN, Direction.DOWN,
        RIGHT, Direction.RIGHT,
        W, Direction.UP,
        A, Direction.LEFT,
        S, Direction.DOWN,
        D, Direction.RIGHT
    );

    public Command getMoveCommand(Tank player, Map<Integer, Direction> keyToDirection) {
        for (var entry : keyToDirection.entrySet()) {
            if (Gdx.input.isKeyPressed(entry.getKey())) {
                return new MoveCommandAdapter(player, entry.getValue());
            }
        }
        return null;
    }

    public Command handleHealthBarToggle(HealthBarToggleState state) {
        if (Gdx.input.isKeyJustPressed(L)) {
            return new ToggleHealthBarCommand(state);
        }
        return null;
    }

    public Command getShootCommand(Tank player) {
        if (Gdx.input.isKeyJustPressed(SPACE)) {
            return new ShootCommand(player);
        }
        return null;
    }
}
