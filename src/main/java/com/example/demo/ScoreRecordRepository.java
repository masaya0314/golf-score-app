package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface ScoreRecordRepository extends JpaRepository<ScoreRecord, Integer> {
    List<ScoreRecord> findAllByOrderByScoreDesc();
    List<ScoreRecord> findByCourseContaining(String course);
    List<ScoreRecord> findByNameContaining(String name);
    List<ScoreRecord> findByNameContainingAndCourseContaining(
            String name,
            String course);
    List<ScoreRecord> findAllByOrderByScoreAsc();
}
