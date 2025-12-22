package ru.mipt.bit.platformer.ai;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ru.mipt.bit.platformer.commands.MoveCommandAdapter;
import ru.mipt.bit.platformer.commands.ShootCommand;
import ru.mipt.bit.platformer.interfaces.Command;
import ru.mipt.bit.platformer.interfaces.Tickable;
import ru.mipt.bit.platformer.interfaces.TickContext;
import ru.mipt.bit.platformer.models.Direction;
import ru.mipt.bit.platformer.models.Tank;

public class RandomAiController implements Tickable {

    public static final int RANDOM_ENEMIES_COUNT = 5;
    public static final float MOVES_PER_SECOND = 2.0f;
    public static final float SHOOT_PROBABILITY = 0.3f;
    public static final float AVERAGE_SHOOTS_PER_SECOND = 3.0f;
    
    private final Random rnd = new Random(System.currentTimeMillis());
    private final float movesPerSecond;
    private final Map<Tank, Float> secondsSinceLastMove = new HashMap<>();
    private final Map<Tank, Float> secondsSinceLastShoot = new HashMap<>();
    private final Map<Tank, Float> nextShootInterval = new HashMap<>();
    private final float shootProbability;
    private final float averageShootsPerSecond;
    private List<Tank> tanks;

    public RandomAiController() {
        this(MOVES_PER_SECOND, SHOOT_PROBABILITY, AVERAGE_SHOOTS_PER_SECOND);
    }

    public RandomAiController(float movesPerSecond, float shootProbability, float averageShootsPerSecond) {
        this.movesPerSecond = movesPerSecond;
        this.shootProbability = shootProbability;
        this.averageShootsPerSecond = averageShootsPerSecond;
    }
    
    private float generateRandomShootInterval() {
        if (averageShootsPerSecond <= 0f)
            return Float.MAX_VALUE;
        float averageInterval = 1f / averageShootsPerSecond;
        float minInterval = averageInterval * 0.5f;
        float maxInterval = averageInterval * 1.5f;
        return minInterval + rnd.nextFloat() * (maxInterval - minInterval);
    }

    public void setTanks(List<Tank> tanks) {
        this.tanks = tanks;
    }
    
    @Override
    public boolean isAlive() {
        return true;
    }
    
    @Override
    public void tick(TickContext context) {
        if (tanks == null) {
            return;
        }
        
        float deltaSeconds = context.getDeltaTime();
        for (Tank t : tanks) {
            if (averageShootsPerSecond > 0f) {
                float currentInterval = nextShootInterval.getOrDefault(t, generateRandomShootInterval());
                float acc = secondsSinceLastShoot.getOrDefault(t, 0f) + deltaSeconds;
                if (acc >= currentInterval) {
                    Command shootCmd = new ShootCommand(t);
                    shootCmd.execute();
                    nextShootInterval.put(t, generateRandomShootInterval());
                    acc = 0f;
                }
                secondsSinceLastShoot.put(t, acc);
            }
            
            if (movesPerSecond > 0f) {
                float interval = 1f / movesPerSecond;
                float acc = secondsSinceLastMove.getOrDefault(t, 0f) + deltaSeconds;
                if (acc >= interval) {
                    if (rnd.nextDouble() < shootProbability) {
                        Command cmd = new ShootCommand(t);
                        cmd.execute();
                    } else {
                        Direction dir = Direction.values()[rnd.nextInt(Direction.values().length)];
                        Command cmd = new MoveCommandAdapter(t, dir);
                        cmd.execute();
                    }
                    acc -= interval;
                }
                secondsSinceLastMove.put(t, acc);
            }
        }
    }
}
