package ru.mipt.bit.platformer.adapters;

import com.badlogic.gdx.math.GridPoint2;

import ru.mipt.bit.platformer.interfaces.MoveChecker;
import ru.mipt.bit.platformer.models.Field;

public class MoveCheckerAdapter implements MoveChecker {

    private final Field field;

    public MoveCheckerAdapter(Field field) { 
        this.field = field; 
    }

    @Override public boolean playerCanMoveTo(GridPoint2 tile) { 
        return field.playerCanMoveTo(tile); 
    }
}
