package ru.mipt.bit.platformer;

import org.junit.jupiter.api.Test;

import ru.mipt.bit.platformer.ai.RandomAiController;
import ru.mipt.bit.platformer.models.Tank;

import static org.mockito.Mockito.*;
import java.util.List;

public class AiControllerTest {
    @Test
    void aiGotMoveCommandForEnemy() {
        Tank enemy = mock(Tank.class);
        RandomAiController ai = new RandomAiController(5.0f, 0.0f, 0.0f);
        ai.tick(0.5f, List.of(enemy));
        verify(enemy, atLeastOnce()).tryMove(any());
    }

    @Test
    void aiDoesNothingWhenNoMovesExpected() {
        Tank enemy = mock(Tank.class);
        RandomAiController ai = new RandomAiController(0.0f, 0.0f, 0.0f);
        ai.tick(0.5f, List.of(enemy));
        verifyNoInteractions(enemy);
    }

    @Test
    void aiGotMoveCommandsForAllEnemies() {
        Tank enemy1 = mock(Tank.class);
        Tank enemy2 = mock(Tank.class);
        RandomAiController ai = new RandomAiController(5.0f, 0.0f, 0.0f);
        ai.tick(0.5f, List.of(enemy1, enemy2));
        verify(enemy1, atLeastOnce()).tryMove(any());
        verify(enemy2, atLeastOnce()).tryMove(any());
    }
}
