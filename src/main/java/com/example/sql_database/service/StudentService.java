package com.example.sql_database.service;

import com.example.sql_database.entity.FacultyDTO;
import com.example.sql_database.entity.Student;
import com.example.sql_database.entity.StudentDTO;
import com.example.sql_database.repository.FacultyRepository;
import com.example.sql_database.repository.StudentRepository;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public StudentDTO getStudent(Long id) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student != null) {
            return StudentDTO.fromStudent(student);
        } else {
            throw new OpenApiResourceNotFoundException("Студент с id " + id + " не найдён");
        }
    }

    public List<StudentDTO> getAll() {
        List<Student> students = studentRepository.findAll();
        List<StudentDTO> studentDTOS = new ArrayList<>();
        students.forEach(student -> studentDTOS.add(StudentDTO.fromStudent(student)));
        return studentDTOS;
    }

    public List<StudentDTO> getByAge(int age) {
        List<Student> students = studentRepository.findStudentByAge(age);
        List<StudentDTO> studentDTOS = new ArrayList<>();
        students.forEach(student -> studentDTOS.add(StudentDTO.fromStudent(student)));
        return studentDTOS;
    }

    public List<StudentDTO> getByAgeBetween(int min, int max) {
        List<Student> students = studentRepository.findStudentsByAgeBetween(min, max);
        List<StudentDTO> studentDTOS = new ArrayList<>();
        students.forEach(student -> studentDTOS.add(StudentDTO.fromStudent(student)));
        return studentDTOS;
    }

    public FacultyDTO getStudentFaculty(Long id) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student != null) {
            FacultyDTO facultyDTO = new FacultyDTO();
            facultyDTO.setId(student.getFaculty().getId());
            facultyDTO.setName(student.getFaculty().getName());
            facultyDTO.setColor(student.getFaculty().getColor());
            return facultyDTO;
        } else {
            throw new OpenApiResourceNotFoundException("Студент с id " + id + " не найден");
        }
    }

    public void update(Long id, StudentDTO studentDTO) {
        if (getStudent(id) != null) {
            Student student = studentDTO.toStudent();
            student.setId(id);
            student.setFaculty(facultyRepository.findById(studentDTO.getFacultyId()).orElse(null));
            studentRepository.save(student);
        } else {
            throw new OpenApiResourceNotFoundException("Студент с id " + id + " не найдён");
        }
    }

    public void delete(Long id) {
        studentRepository.deleteById(id);
    }

    public void add(StudentDTO studentDTO) {
        Student student = new Student();
        student.setAge(studentDTO.getAge());
        student.setName(studentDTO.getName());
        student.setFaculty((facultyRepository.findById(studentDTO.getFacultyId()).orElse(null)));
        studentRepository.save(student);
    }
}