package com.example.sql_database.service;

import com.example.sql_database.entity.Faculty;
import com.example.sql_database.repository.FacultyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public void create(Faculty faculty) {
        facultyRepository.save(faculty);
    }

    public void update(Long id, Faculty newFaculty) {
        if (getFaculty(id) != null) {
            newFaculty.setId(id);
            facultyRepository.save(newFaculty);
        }
    }

    public Faculty getFaculty(Long id) {
        return facultyRepository.findById(id).get();
    }


    public List<Faculty> getFacultyByColor(String color) {
        return facultyRepository.findFacultiesByColor(color);

    }

    public void delete(Long id) {
        facultyRepository.deleteById(id);
    }
}
