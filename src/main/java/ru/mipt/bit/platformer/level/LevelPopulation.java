package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import java.util.List;

public class LevelPopulation {
    
    public final GridPoint2 playerStart;
    public final List<GridPoint2> trees;

    public LevelPopulation(GridPoint2 playerStart, List<GridPoint2> trees) {
        this.playerStart = playerStart;
        this.trees = trees;
    }
}
