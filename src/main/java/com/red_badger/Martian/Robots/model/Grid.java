package com.red_badger.Martian.Robots.model;

import java.util.HashSet;
import java.util.Set;

public class Grid {
    private final int maxX, maxY;
    private final Set<Scent> scents = new HashSet<>();

    public Grid(int maxX, int maxY) {
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public boolean isWithinBounds(int x, int y) {
        return x >= 0 && x <= maxX && y >= 0 && y <= maxY;
    }

    public void addScent(int x, int y, Orientation dir) {
        scents.add(new Scent(x, y, dir));
    }

    public boolean hasScent(int x, int y, Orientation dir) {
        return scents.contains(new Scent(x, y, dir));
    }

    private record Scent(int x, int y, Orientation dir) {}
}