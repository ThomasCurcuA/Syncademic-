package it.novasemantics.calendarplanner.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.novasemantics.calendarplanner.models.Lesson;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    public List<Lesson> findAllByOrderByName();
    public List<Lesson> findByNameContainsOrderByName(String txt);
} 