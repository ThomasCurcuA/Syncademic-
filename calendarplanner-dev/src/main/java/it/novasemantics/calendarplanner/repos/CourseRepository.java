package it.novasemantics.calendarplanner.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.novasemantics.calendarplanner.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    public List<Course> findAllByOrderByName();
    public List<Course> findByNameContainsOrderByName(String txt);
} 