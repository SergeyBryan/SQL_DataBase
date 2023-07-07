package com.example.sql_database.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String name;
    private int age;
    private Long facultyId;

    public static StudentDTO fromStudent(Student student) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(student.getId());
        studentDTO.setAge(student.getAge());
        studentDTO.setName(student.getName());
        studentDTO.setFacultyId(student.getFaculty().getId());
        return studentDTO;
    }

    public Student toStudent() {
        Student student = new Student();
        student.setId(this.getId());
        student.setAge(this.getAge());
        student.setName(this.getName());
        Faculty faculty = new Faculty();
        faculty.setId(this.getFacultyId());
        student.setFaculty(faculty);
        return student;
    }

}
