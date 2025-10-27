package com.red_badger.martian.robots.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.With;

@Embeddable
@With
public record Position(
        int x,
        int y,
        @Enumerated(EnumType.STRING) Orientation orientation,
        boolean lost
) {
    public Position(int x, int y, Orientation orientation) {
        this(x, y, orientation, false);
    }

    public String toOutputString() {
        return lost ? "%d %d %s LOST".formatted(x, y, orientation)
                : "%d %d %s".formatted(x, y, orientation);
    }
}
