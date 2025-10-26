package com.red_badger.Martian.Robots.service;

import com.red_badger.Martian.Robots.model.Grid;
import com.red_badger.Martian.Robots.model.Orientation;
import com.red_badger.Martian.Robots.model.Position;
import com.red_badger.Martian.Robots.model.RobotInput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RobotService {

    public List<Position> processMultipleRobots(RobotInput input) {
        log.info("Processing robot: {}", input);
        var grid = new Grid(input.gridMaxX(), input.gridMaxY());
        var start = new Position(input.startX(), input.startY(), Orientation.valueOf(input.startOrientation()));
        var result = processRobotInstructions(grid, start, input.instructions());
        return List.of(result);
    }

    private Position processRobotInstructions(Grid grid, Position pos, String instructions) {
        var current = pos;
        for (char cmd : instructions.toCharArray()) {
            var next = switch (cmd) {
                case 'L' -> current.withOrientation(current.orientation().turnLeft());
                case 'R' -> current.withOrientation(current.orientation().turnRight());
                case 'F' -> moveForward(current);
                default -> current;
            };

            if (!grid.isWithinBounds(next.x(), next.y())) {
                if (!grid.hasScent(current.x(), current.y())) {
                    grid = grid.addScent(current.x(), current.y());
                    current = current.withLost(true);
                    break;
                }
            } else {
                current = next;
            }
        }
        log.info("Final position: {}", current.toOutputString());
        return current;
    }

    private Position moveForward(Position p) {
        return switch (p.orientation()) {
            case N -> p.withY(p.y() + 1);
            case S -> p.withY(p.y() - 1);
            case E -> p.withX(p.x() + 1);
            case W -> p.withX(p.x() - 1);
        };
    }
}