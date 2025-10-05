package ru.mipt.bit.platformer.models;

import com.badlogic.gdx.Gdx;
import static com.badlogic.gdx.Input.Keys.SPACE;

public class KeyInputHandler {

    public void handleMovement(Tank player, java.util.Map<Integer, Direction> keyToDirection) {
        for (var entry : keyToDirection.entrySet()) {
            if (Gdx.input.isKeyPressed(entry.getKey())) {
                player.tryMove(entry.getValue());
                break;
            }
        }
    }

    public void handleActions(Tank player) {
        if (Gdx.input.isKeyJustPressed(SPACE)) {
            // TODO: shoot logic
        }
    }
}
