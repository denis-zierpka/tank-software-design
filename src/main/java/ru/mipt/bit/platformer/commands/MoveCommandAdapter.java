package ru.mipt.bit.platformer.commands;

import ru.mipt.bit.platformer.interfaces.Command;
import ru.mipt.bit.platformer.models.Direction;
import ru.mipt.bit.platformer.models.Tank;

public class MoveCommandAdapter implements Command {
    private final Tank tank;
    private final Direction direction;

    public MoveCommandAdapter(Tank tank, Direction direction) {
        this.tank = tank;
        this.direction = direction;
    }

    @Override
    public void execute() {
        tank.tryMove(direction);
    }
}
