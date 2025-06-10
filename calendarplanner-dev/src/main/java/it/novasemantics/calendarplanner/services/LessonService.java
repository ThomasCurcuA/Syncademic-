package it.novasemantics.calendarplanner.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.novasemantics.calendarplanner.models.Lesson;
import it.novasemantics.calendarplanner.repos.LessonRepository;

@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    public List<Lesson> findAllByOrderByName() {
        return lessonRepository.findAllByOrderByName();
    }

    public List<Lesson> findByNameContainsOrderByName(String txt) {
        return lessonRepository.findByNameContainsOrderByName(txt);
    }

    public Optional<Lesson> findById(Long id) {
        return lessonRepository.findById(id);
    }

    public Lesson save(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    public void deleteById(Long id) {
        lessonRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return lessonRepository.existsById(id);
    }
} 