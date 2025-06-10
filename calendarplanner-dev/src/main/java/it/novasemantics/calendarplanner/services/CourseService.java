package it.novasemantics.calendarplanner.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.novasemantics.calendarplanner.models.Course;
import it.novasemantics.calendarplanner.repos.CourseRepository;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public List<Course> findAllByOrderByName() {
        return courseRepository.findAllByOrderByName();
    }

    public List<Course> findByNameContainsOrderByName(String txt) {
        return courseRepository.findByNameContainsOrderByName(txt);
    }

    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    public Course save(Course course) {
        return courseRepository.save(course);
    }

    public void deleteById(Long id) {
        courseRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return courseRepository.existsById(id);
    }
} 