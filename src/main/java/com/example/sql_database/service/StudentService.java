package com.example.sql_database.service;

import com.example.sql_database.entity.Student;
import com.example.sql_database.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student getStudent(Long id) {
        return studentRepository.findById(id).get();
    }

    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    public List<Student> getByAge(int age) {
        return studentRepository.findStudentByAge(age);
    }

    public void update(Long id, Student newStudent) {
        if (getStudent(id) != null) {
            newStudent.setId(id);
            studentRepository.save(newStudent);
        }
    }

    public void delete(Long id) {
        studentRepository.deleteById(id);
    }

    public void add(Student student) {
        studentRepository.save(student);
    }

}
