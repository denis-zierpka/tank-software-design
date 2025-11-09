package ru.mipt.bit.platformer.ai;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ru.mipt.bit.platformer.adapters.CommandAdapter;
import ru.mipt.bit.platformer.interfaces.Command;
import ru.mipt.bit.platformer.models.Direction;
import ru.mipt.bit.platformer.models.Tank;

public class RandomAiController {
    private final Random rnd = new Random(System.currentTimeMillis());
    private final float movesPerSecond;
    private final Map<Tank, Float> secondsSinceLastMove = new HashMap<>();

    public RandomAiController(float movesPerSecond) {
        this.movesPerSecond = movesPerSecond;
    }

    public void tick(float deltaSeconds, List<Tank> tanks) {
        if (movesPerSecond <= 0f) return;
        float interval = 1f / movesPerSecond;
        for (Tank t : tanks) {
            float acc = secondsSinceLastMove.getOrDefault(t, 0f) + deltaSeconds;
            if (acc >= interval) {
                Direction dir = Direction.values()[rnd.nextInt(Direction.values().length)];
                Command cmd = new CommandAdapter(t, dir);
                cmd.execute();
                acc -= interval;
            }
            secondsSinceLastMove.put(t, acc);
        }
    }
}
