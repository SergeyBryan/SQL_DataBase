package com.example.sql_database.service;

import com.example.sql_database.entity.Faculty;
import com.example.sql_database.entity.FacultyDTO;
import com.example.sql_database.entity.Student;
import com.example.sql_database.entity.StudentDTO;
import com.example.sql_database.repository.FacultyRepository;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public void create(FacultyDTO facultyDTO) {
        Faculty faculty = facultyDTO.toFaculty();
        facultyRepository.save(faculty);
    }

    public void update(Long id, FacultyDTO facultyDTO) {
        if (getFaculty(id) != null) {
            Faculty faculty = facultyDTO.toFaculty();
            faculty.setId(id);
            facultyRepository.save(faculty);
        } else {
            throw new OpenApiResourceNotFoundException("Факультет с id " + id + " не найден");
        }
    }

    public void delete(Long id) {
        facultyRepository.deleteById(id);
    }

    public FacultyDTO getFaculty(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElse(null);
        if (faculty != null) {
            return FacultyDTO.fromFaculty(faculty);
        } else {
            throw new OpenApiResourceNotFoundException("Студент с id " + id + " не найдён");
        }
    }


    public List<FacultyDTO> getFacultyByColor(String color) {
        List<Faculty> faculties = facultyRepository.findFacultiesByColor(color);
        List<FacultyDTO> facultyDTOS = new ArrayList<>();
        faculties.forEach(faculty -> facultyDTOS.add(FacultyDTO.fromFaculty(faculty)));
        return facultyDTOS;
    }

    public List<FacultyDTO> getFacultyByColorIgnoreCase(String color) {
        List<Faculty> faculties = facultyRepository.findFacultiesByColorIgnoreCase(color);
        List<FacultyDTO> facultyDTOS = new ArrayList<>();
        faculties.forEach(faculty -> facultyDTOS.add(FacultyDTO.fromFaculty(faculty)));
        return facultyDTOS;
    }

    public List<StudentDTO> getStudentsByFacultyId(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElse(null);
        if (faculty != null) {
            List<Student> students = faculty.getStudents();
            return students.stream().map(StudentDTO::fromStudent).collect(Collectors.toList());
        } else {
            throw new OpenApiResourceNotFoundException("Факультет с id " + id + " не найден");
        }
    }
}
