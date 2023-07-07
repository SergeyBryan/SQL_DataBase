package com.example.sql_database.controller;

import com.example.sql_database.entity.FacultyDTO;
import com.example.sql_database.entity.StudentDTO;
import com.example.sql_database.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/filterByAge")
    public ResponseEntity<List<StudentDTO>> getByAge(@RequestParam int age) {
        return ResponseEntity.ok(studentService.getByAge(age));
    }

    @GetMapping("/filterByAgeBetween/")
    public ResponseEntity<List<StudentDTO>> getByAgeBetween(@RequestParam int min, @RequestParam int max) {
        return ResponseEntity.ok(studentService.getByAgeBetween(min, max));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudent(id));
    }


    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAll() {
        return ResponseEntity.ok(studentService.getAll());
    }

    @GetMapping("/studentFaculty")
    public ResponseEntity<FacultyDTO> getFacultyByStudentId(@RequestParam Long id) {
        return ResponseEntity.ok(studentService.getStudentFaculty(id));
    }

    @PutMapping
    public ResponseEntity<String> updateStudent(@RequestParam Long id, @RequestBody StudentDTO studentDTO) {
        if (studentService.getStudent(id) != null) {
            studentService.update(id, studentDTO);
            return ResponseEntity.ok("Данные о студенте были изменены");
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<String> createStudent(@RequestBody StudentDTO studentDTO) {
        studentService.add(studentDTO);
        return ResponseEntity.ok("Студент был добавлен");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteStudent(@RequestParam Long id) {
        if (studentService.getStudent(id) != null) {
            studentService.delete(id);
            return ResponseEntity.ok("Студент был удалён");
        }
        return ResponseEntity.notFound().build();
    }
}
