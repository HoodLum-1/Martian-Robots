package com.red_badger.Martian.Robots.model;

import lombok.With;


@With
public record Grid(int maxX, int maxY, boolean[][] scent) {

    public Grid(int maxX, int maxY) {
        this(maxX, maxY, new boolean[maxX + 1][maxY + 1]);
    }

    public boolean isWithinBounds(int x, int y) {
        return x >= 0 && x <= maxX && y >= 0 && y <= maxY;
    }

    public boolean hasScent(int x, int y) {
        return isWithinBounds(x, y) && scent[x][y];
    }

    public Grid addScent(int x, int y) {
        if (!isWithinBounds(x, y)) return this;
        boolean[][] newScent = scent.clone();
        for (int i = 0; i <= maxX; i++) newScent[i] = scent[i].clone();
        newScent[x][y] = true;
        return new Grid(maxX, maxY, newScent);
    }
}