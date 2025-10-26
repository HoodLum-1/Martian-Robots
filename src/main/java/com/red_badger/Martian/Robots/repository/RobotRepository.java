package com.red_badger.Martian.Robots.repository;

import com.red_badger.Martian.Robots.model.Robot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RobotRepository extends JpaRepository<Robot, Long> {
}