package ru.mipt.bit.platformer.commands;

import ru.mipt.bit.platformer.interfaces.Command;
import ru.mipt.bit.platformer.models.Tank;

public class ShootCommand implements Command {
    private final Tank tank;

    public ShootCommand(Tank tank) {
        this.tank = tank;
    }

    @Override
    public void execute() {
        tank.tryShoot();
    }
}

