package ru.mipt.bit.platformer.adapters;

import com.badlogic.gdx.math.GridPoint2;

import ru.mipt.bit.platformer.interfaces.MovementBlocker;
import ru.mipt.bit.platformer.models.Field;

public class MovementBlockerAdapter implements MovementBlocker {

    private final Field field;

    public MovementBlockerAdapter(Field field) {
        this.field = field;
    }

    @Override
    public void occupy(GridPoint2 coordinates) {
        field.occupy(coordinates);
    }

    @Override
    public boolean reserveMove(GridPoint2 from, GridPoint2 to) {
        return field.reserveMove(from, to);
    }

    @Override
    public void finishMove(GridPoint2 from, GridPoint2 to) {
        field.finishMove(from, to);
    }
}
