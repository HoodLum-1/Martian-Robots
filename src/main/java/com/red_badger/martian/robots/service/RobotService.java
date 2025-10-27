package com.red_badger.martian.robots.service;

import com.red_badger.martian.robots.model.Grid;
import com.red_badger.martian.robots.model.Orientation;
import com.red_badger.martian.robots.model.Position;
import com.red_badger.martian.robots.model.Robot;
import com.red_badger.martian.robots.model.RobotInput;
import com.red_badger.martian.robots.repository.RobotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RobotService {

    private final RobotRepository robotRepository;

    public List<Position> processMultipleRobots(RobotInput input) {
        var grid = new Grid(input.gridMaxX(), input.gridMaxY());
        var start = new Position(input.startX(), input.startY(), Orientation.valueOf(input.startOrientation()));
        var result = processRobotInstructions(grid, start, input.instructions());

        Robot robot = new Robot(null, result.withLost(result.lost()), input.instructions());
        robotRepository.save(robot);

        return List.of(result);
    }

    public Position processRobotInstructions(Grid grid, Position start, String instructions) {
        Position current = start;

        for (char cmd : instructions.toCharArray()) {
            Position next = switch (cmd) {
                case 'L' -> new Position(current.x(), current.y(), current.orientation().turnLeft(), current.lost());
                case 'R' -> new Position(current.x(), current.y(), current.orientation().turnRight(), current.lost());
                case 'F' -> moveForward(current);
                default -> current;
            };

            if (!grid.isWithinBounds(next.x(), next.y())) {
                if (grid.hasScent(current.x(), current.y(), current.orientation())) {
                    continue;
                } else {
                    grid.addScent(current.x(), current.y(), current.orientation());
                    Position lost = new Position(current.x(), current.y(), current.orientation(), true);
                    log.info("Final position: {}", lost.toOutputString());
                    return lost;
                }
            }

            current = next;
        }

        log.info("Final position: {}", current.toOutputString());
        return current;
    }

    private Position moveForward(Position p) {
        return switch (p.orientation()) {
            case N -> new Position(p.x(), p.y() + 1, p.orientation(), p.lost());
            case S -> new Position(p.x(), p.y() - 1, p.orientation(), p.lost());
            case E -> new Position(p.x() + 1, p.y(), p.orientation(), p.lost());
            case W -> new Position(p.x() - 1, p.y(), p.orientation(), p.lost());
        };
    }
}