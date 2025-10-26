package com.red_badger.Martian.Robots.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "robots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Robot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String instructions;

}
