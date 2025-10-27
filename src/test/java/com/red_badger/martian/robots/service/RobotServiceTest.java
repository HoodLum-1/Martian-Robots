package com.red_badger.martian.robots.service;

import com.red_badger.martian.robots.model.Grid;
import com.red_badger.martian.robots.model.Orientation;
import com.red_badger.martian.robots.model.Position;
import com.red_badger.martian.robots.model.RobotInput;
import com.red_badger.martian.robots.repository.RobotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RobotServiceTest {

    @Mock
    private RobotRepository robotRepository;

    @InjectMocks
    private RobotService service;

    private Grid grid;

    @BeforeEach
    void setUp() {
        grid = new Grid(5, 3);
    }

    @Test
    @DisplayName("Sample 1: Robot returns to start after loop")
    void testSample1() {
        var start = new Position(1, 1, Orientation.E);
        var result = service.processRobotInstructions(grid, start, "RFRFRFRF");

        assertAll("Sample 1 result",
                () -> assertEquals(1, result.x()),
                () -> assertEquals(1, result.y()),
                () -> assertEquals(Orientation.E, result.orientation()),
                () -> assertFalse(result.lost())
        );
    }

    @Test
    @DisplayName("Sample 2: Robot falls off and gets LOST")
    void testSample2() {
        var start = new Position(3, 2, Orientation.N);
        var result = service.processRobotInstructions(grid, start, "FRRFLLFFRRFLL");

        assertAll("Sample 2 result",
                () -> assertEquals(3, result.x()),
                () -> assertEquals(3, result.y()),
                () -> assertEquals(Orientation.N, result.orientation()),
                () -> assertTrue(result.lost())
        );
    }

    @Test
    @DisplayName("Sample 3: Robot avoids fall due to scent")
    void testSample3() {
        // Sample 2 must run first to leave scent
        service.processRobotInstructions(grid, new Position(3, 2, Orientation.N), "FRRFLLFFRRFLL");

        var start = new Position(0, 3, Orientation.W);
        var result = service.processRobotInstructions(grid, start, "LLFFFLFLFL");

        assertAll("Sample 3 result",
                () -> assertEquals(2, result.x()),
                () -> assertEquals(3, result.y()),
                () -> assertEquals(Orientation.S, result.orientation()),
                () -> assertFalse(result.lost())
        );
    }

    @Test
    @DisplayName("Scent prevents second robot from falling")
    void testScentPreventsFall() {
        var first = new Position(3, 3, Orientation.N);
        var firstResult = service.processRobotInstructions(grid, first, "F");
        assertTrue(firstResult.lost());
        assertTrue(grid.hasScent(3, 3, Orientation.N));

        var second = new Position(3, 3, Orientation.N);
        var secondResult = service.processRobotInstructions(grid, second, "F");

        assertAll("Second robot survives",
                () -> assertEquals(3, secondResult.x()),
                () -> assertEquals(3, secondResult.y()),
                () -> assertEquals(Orientation.N, secondResult.orientation()),
                () -> assertFalse(secondResult.lost())
        );
    }

    @Test
    void shouldSaveRobotToDatabase() {
        var input = new RobotInput(5, 3, 1, 1, "E", "RFRFRFRF");

        service.processMultipleRobots(input);

        verify(robotRepository).save(argThat(robot ->
                robot.getPosition().x() == 1 &&
                        robot.getPosition().y() == 1 &&
                        robot.getPosition().orientation() == Orientation.E &&
                        !robot.getPosition().lost()
        ));
    }

    @ParameterizedTest
    @MethodSource("turnLeftProvider")
    @DisplayName("Turn Left 90°")
    void testTurnLeft(Orientation input, Orientation expected) {
        assertEquals(expected, input.turnLeft());
    }

    @ParameterizedTest
    @MethodSource("turnRightProvider")
    @DisplayName("Turn Right 90°")
    void testTurnRight(Orientation input, Orientation expected) {
        assertEquals(expected, input.turnRight());
    }

    @ParameterizedTest
    @MethodSource("moveForwardProvider")
    @DisplayName("Move Forward")
    void testMoveForward(Position input, Position expected) {
        var result = service.processRobotInstructions(grid, input, "F");
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Invalid instruction is ignored")
    void testInvalidInstructionIgnored() {
        var start = new Position(1, 1, Orientation.N);
        var result = service.processRobotInstructions(grid, start, "XYZF");

        assertEquals(new Position(1, 2, Orientation.N), result);
    }

    @Test
    @DisplayName("Empty instructions return start position")
    void testEmptyInstructions() {
        var start = new Position(2, 2, Orientation.S);
        var result = service.processRobotInstructions(grid, start, "");

        assertEquals(start, result);
    }

    @Test
    @DisplayName("processMultipleRobots returns list with one result")
    void testProcessMultipleRobots() {
        var input = new RobotInput(5, 3, 1, 1, "E", "RFRFRFRF");
        var results = service.processMultipleRobots(input);

        assertEquals(1, results.size());
        var result = results.get(0);
        assertEquals(new Position(1, 1, Orientation.E), result);
    }

    // === Data Providers ===
    private static Stream<Arguments> turnLeftProvider() {
        return Stream.of(
                Arguments.of(Orientation.N, Orientation.W),
                Arguments.of(Orientation.W, Orientation.S),
                Arguments.of(Orientation.S, Orientation.E),
                Arguments.of(Orientation.E, Orientation.N)
        );
    }

    private static Stream<Arguments> turnRightProvider() {
        return Stream.of(
                Arguments.of(Orientation.N, Orientation.E),
                Arguments.of(Orientation.E, Orientation.S),
                Arguments.of(Orientation.S, Orientation.W),
                Arguments.of(Orientation.W, Orientation.N)
        );
    }

    private static Stream<Arguments> moveForwardProvider() {
        return Stream.of(
                Arguments.of(new Position(1, 1, Orientation.N), new Position(1, 2, Orientation.N)),
                Arguments.of(new Position(1, 1, Orientation.S), new Position(1, 0, Orientation.S)),
                Arguments.of(new Position(1, 1, Orientation.E), new Position(2, 1, Orientation.E)),
                Arguments.of(new Position(1, 1, Orientation.W), new Position(0, 1, Orientation.W))
        );
    }
}