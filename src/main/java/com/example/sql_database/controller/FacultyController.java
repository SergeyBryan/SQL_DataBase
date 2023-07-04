package com.example.sql_database.controller;

import com.example.sql_database.entity.Faculty;
import com.example.sql_database.service.FacultyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {


    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long id) {
        return ResponseEntity.ok(facultyService.getFaculty(id));
    }

    @GetMapping("/color/{color}")
    public ResponseEntity<List<Faculty>> getFacultyByColor(@PathVariable String color) {
        return ResponseEntity.ok(facultyService.getFacultyByColor(color));
    }

    @PostMapping
    public ResponseEntity<String> createFaculty(@RequestBody Faculty faculty) {
        if (faculty != null) {
            facultyService.create(faculty);
            return ResponseEntity.ok("Факультет добавлен");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping
    public ResponseEntity<String> editFaculty(@RequestParam Long id, @RequestBody Faculty faculty) {
        if (faculty != null) {
            facultyService.update(id, faculty);
            return ResponseEntity.ok("Факультет изменён");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteFaculty(@RequestParam Long id) {
        if (facultyService.getFaculty(id) != null) {
            facultyService.delete(id);
            return ResponseEntity.ok("Факультет удалён");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
