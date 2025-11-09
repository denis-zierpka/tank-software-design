package ru.mipt.bit.platformer.commands;

import ru.mipt.bit.platformer.interfaces.Command;
import ru.mipt.bit.platformer.models.HealthBarToggleState;

public class ToggleHealthBarCommand implements Command {
    private final HealthBarToggleState state;

    public ToggleHealthBarCommand(HealthBarToggleState state) {
        this.state = state;
    }

    @Override
    public void execute() {
        state.toggle();
    }
}
