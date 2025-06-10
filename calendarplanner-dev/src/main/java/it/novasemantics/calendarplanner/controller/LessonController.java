package it.novasemantics.calendarplanner.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import it.novasemantics.calendarplanner.models.Lesson;
import it.novasemantics.calendarplanner.services.LessonService;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @GetMapping
    public ResponseEntity<List<Lesson>> getAllLessons() {
        return ResponseEntity.ok(lessonService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lesson> getLessonById(@PathVariable Long id) {
        return lessonService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Lesson> createLesson(@RequestBody Lesson lesson) {
        return ResponseEntity.ok(lessonService.save(lesson));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Lesson> updateLesson(@PathVariable Long id, @RequestBody Lesson lesson) {
        if (!lessonService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        lesson.setId(id);
        return ResponseEntity.ok(lessonService.save(lesson));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        if (!lessonService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        lessonService.deleteById(id);
        return ResponseEntity.ok().build();
    }
} 