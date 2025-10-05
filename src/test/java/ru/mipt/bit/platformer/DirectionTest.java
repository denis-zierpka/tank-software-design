package ru.mipt.bit.platformer;

import org.junit.jupiter.api.Test;

import ru.mipt.bit.platformer.models.Direction;

import static org.junit.jupiter.api.Assertions.*;

class DirectionTest {

    @Test
    void hasExpectedVectorsAndRotations() {
        assertAll(
            () -> { assertEquals(0, Direction.UP.dx);    assertEquals(1, Direction.UP.dy);    assertEquals(90f,  Direction.UP.rotation, 1e-4f); },
            () -> { assertEquals(-1, Direction.LEFT.dx); assertEquals(0, Direction.LEFT.dy);  assertEquals(-180f, Direction.LEFT.rotation, 1e-4f); },
            () -> { assertEquals(0, Direction.DOWN.dx);  assertEquals(-1, Direction.DOWN.dy); assertEquals(-90f, Direction.DOWN.rotation, 1e-4f); },
            () -> { assertEquals(1, Direction.RIGHT.dx); assertEquals(0, Direction.RIGHT.dy); assertEquals(0f,   Direction.RIGHT.rotation, 1e-4f); }
        );
    }

    @Test
    void unitStepVectors() {
        for (Direction d : Direction.values()) {
            int sum = Math.abs(d.dx) + Math.abs(d.dy);
            assertEquals(1, sum, "Should be a grid");
        }
    }

    @Test
    void directionsAreOrthogonal() {
        assertEquals(0, dot(Direction.UP, Direction.LEFT));
        assertEquals(0, dot(Direction.UP, Direction.RIGHT));
        assertEquals(0, dot(Direction.DOWN, Direction.LEFT));
        assertEquals(0, dot(Direction.DOWN, Direction.RIGHT));
    }

    @Test
    void oppositeDirectionsHaveNegatedVectors() {
        assertOpposite(Direction.UP, Direction.DOWN);
        assertOpposite(Direction.LEFT, Direction.RIGHT);
    }

    @Test
    void rotationsDifferByRightAngles() {
        for (Direction d : Direction.values()) {
            float mod = Math.abs(d.rotation % 90f);
            assertTrue(mod < 1e-4f || Math.abs(90f - mod) < 1e-4f, "Rotation has wrong angle");
        }
    }

    private static int dot(Direction a, Direction b) {
        return a.dx * b.dx + a.dy * b.dy;
    }

    private static void assertOpposite(Direction a, Direction b) {
        assertEquals(a.dx, -b.dx, a + " vs " + b);
        assertEquals(a.dy, -b.dy, a + " vs " + b);
        float diff = Math.abs(a.rotation - b.rotation);
        diff = (diff % 360f);
        if (diff > 180f) diff = 360f - diff;
        assertEquals(180f, diff, 1e-4, "Should differ by 180");
    }
}
