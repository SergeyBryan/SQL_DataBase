package com.example.sql_database.controller;

import com.example.sql_database.entity.FacultyDTO;
import com.example.sql_database.entity.StudentDTO;
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
    public ResponseEntity<FacultyDTO> getFaculty(@PathVariable Long id) {
        return ResponseEntity.ok(facultyService.getFaculty(id));
    }

    @GetMapping("/colors")
    public ResponseEntity<List<FacultyDTO>> getFacultyByColor(@RequestParam String color) {
        return ResponseEntity.ok(facultyService.getFacultyByColor(color));
    }

    @GetMapping("/colorIgnoreCase")
    public ResponseEntity<List<FacultyDTO>> getFacultyByColorIgnoreCase(@RequestParam String color) {
        return ResponseEntity.ok(facultyService.getFacultyByColorIgnoreCase(color));
    }

    @GetMapping("/studentsFromFaculty")
    public ResponseEntity<List<StudentDTO>> getStudentsByFacultyId(@RequestParam Long id) {
        return ResponseEntity.ok(facultyService.getStudentsByFacultyId(id));
    }

    @PostMapping
    public ResponseEntity<String> createFaculty(@RequestBody FacultyDTO facultyDTO) {
        if (facultyDTO != null) {
            facultyService.create(facultyDTO);
            return ResponseEntity.ok("Факультет добавлен");
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping

    public ResponseEntity<String> editFaculty(@RequestParam Long id, @RequestBody FacultyDTO faculty) {
        if (faculty != null) {
            facultyService.update(id, faculty);
            return ResponseEntity.ok("Факультет изменён");
        } else {
            return ResponseEntity.badRequest().build();
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
