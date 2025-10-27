package com.red_badger.Martian.Robots.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RobotInput(
        @Min(1) @Max(50) int gridMaxX,
        @Min(1) @Max(50) int gridMaxY,
        @Min(0) @Max(50) int startX,
        @Min(0) @Max(50) int startY,
        @Pattern(regexp = "[NSEW]") String startOrientation,
        @Size(max = 100) @Pattern(regexp = "[LRF]+") String instructions
) {}