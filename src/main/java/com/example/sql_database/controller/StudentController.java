package com.example.sql_database.controller;

import com.example.sql_database.entity.Student;
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

    @GetMapping("/filterByAge/{age}")
    public ResponseEntity<List<Student>> getByAge(@PathVariable int age) {
        return ResponseEntity.ok(studentService.getByAge(age));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudent(id));
    }


    @GetMapping
    public ResponseEntity<List<Student>> getAll() {
        return ResponseEntity.ok(studentService.getAll());
    }

    @PutMapping
    public ResponseEntity<String> updateStudent(@RequestParam Long id, @RequestBody Student student) {
        if (studentService.getStudent(id) != null) {
            studentService.update(id, student);
            return ResponseEntity.ok("Данные о студенте были изменены");
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<String> createStudent(@RequestBody Student student) {
        studentService.add(student);
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
