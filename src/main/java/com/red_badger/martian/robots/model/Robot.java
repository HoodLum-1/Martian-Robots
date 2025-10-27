package com.red_badger.martian.robots.model;

import jakarta.persistence.*;
import lombok.ToString;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "robots", indexes = {
        @Index(name = "idx_robot_position", columnList = "x, y, orientation")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class Robot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Position position;

    @Column(nullable = false, length = 1000)
    private String instructions;

}