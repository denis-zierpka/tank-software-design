package ru.mipt.bit.platformer.models;

import com.badlogic.gdx.Gdx;
import static com.badlogic.gdx.Input.Keys.SPACE;
import java.util.Map;

import ru.mipt.bit.platformer.adapters.CommandAdapter;
import ru.mipt.bit.platformer.interfaces.Command;

public class KeyInputHandler {

    public Command getMoveCommand(Tank player, Map<Integer, Direction> keyToDirection) {
        for (var entry : keyToDirection.entrySet()) {
            if (Gdx.input.isKeyPressed(entry.getKey())) {
                return new CommandAdapter(player, entry.getValue());
            }
        }
        return null;
    }

    public void handleActions(Tank player) {
        if (Gdx.input.isKeyJustPressed(SPACE)) {
            // TODO: shoot logic
        }
    }
}
