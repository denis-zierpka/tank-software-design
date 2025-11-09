package ru.mipt.bit.platformer.models;

import com.badlogic.gdx.Gdx;
import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.L;
import java.util.Map;

import ru.mipt.bit.platformer.commands.MoveCommandAdapter;
import ru.mipt.bit.platformer.commands.ToggleHealthBarCommand;
import ru.mipt.bit.platformer.interfaces.Command;

public class KeyInputHandler {

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

    public void handleActions(Tank player) {
        if (Gdx.input.isKeyJustPressed(SPACE)) {
            // TODO: shoot logic
        }
    }
}
