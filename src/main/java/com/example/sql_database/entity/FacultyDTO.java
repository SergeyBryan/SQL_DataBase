package com.example.sql_database.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class FacultyDTO {
    private Long id;
    private String name;
    private String color;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<StudentDTO> studentList;


    public static FacultyDTO fromFaculty(Faculty faculty) {
        FacultyDTO facultyDTO = new FacultyDTO();
        facultyDTO.setId(faculty.getId());
        facultyDTO.setName(faculty.getName());
        facultyDTO.setColor(faculty.getColor());
        facultyDTO.setStudentList(faculty.getStudents().stream()
                .map(StudentDTO::fromStudent)
                .collect(Collectors.toList()));
        return facultyDTO;
    }

    public Faculty toFaculty() {
        Faculty faculty = new Faculty();
        faculty.setId(this.getId());
        faculty.setName(this.getName());
        faculty.setColor(this.getColor());
        return faculty;
    }
}
