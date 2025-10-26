package com.red_badger.Martian.Robots.model;

import lombok.Getter;

@Getter
public enum Orientation {
    N, S, E, W;

    public Orientation turnLeft() {
        return switch (this) {
            case N -> W;
            case W -> S;
            case S -> E;
            case E -> N;
        };
    }

    public Orientation turnRight() {
        return switch (this) {
            case N -> E;
            case E -> S;
            case S -> W;
            case W -> N;
        };
    }
}