package com.example.sql_database.service;

import com.example.sql_database.entity.Faculty;
import com.example.sql_database.entity.FacultyDTO;
import com.example.sql_database.entity.Student;
import com.example.sql_database.entity.StudentDTO;
import com.example.sql_database.repository.FacultyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public void create(FacultyDTO facultyDTO) {
        Faculty faculty = facultyDTO.toFaculty();
        logger.info("Was invoked method for create faculty");
        facultyRepository.save(faculty);
    }

    public void update(Long id, FacultyDTO facultyDTO) {
        if (getFaculty(id) != null) {
            Faculty faculty = facultyDTO.toFaculty();
            faculty.setId(id);
            logger.info("Was invoked method for update faculty");
            facultyRepository.save(faculty);
        } else {
            logger.error("There is not faculty with id = " + id);
            throw new OpenApiResourceNotFoundException("Факультет с id " + id + " не найден");
        }
    }

    public void delete(Long id) {
        logger.info("Was invoked method for delete faculty");
        facultyRepository.deleteById(id);
    }

    public FacultyDTO getFaculty(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElse(null);
        if (faculty != null) {
            logger.info("Was invoked method for get faculty by id");
            return FacultyDTO.fromFaculty(faculty);
        } else {
            logger.error("There is not faculty with id = " + id);
            throw new OpenApiResourceNotFoundException("Студент с id " + id + " не найдён");
        }
    }


    public List<FacultyDTO> getFacultyByColor(String color) {
        logger.info("Was invoked method for get faculty by color");
        List<Faculty> faculties = facultyRepository.findFacultiesByColor(color);
        List<FacultyDTO> facultyDTOS = new ArrayList<>();
        faculties.forEach(faculty -> facultyDTOS.add(FacultyDTO.fromFaculty(faculty)));
        return facultyDTOS;
    }

    public List<FacultyDTO> getFacultyByColorIgnoreCase(String color) {
        logger.info("Was invoked method for get faculty by color and ignore case");
        List<Faculty> faculties = facultyRepository.findFacultiesByColorIgnoreCase(color);
        List<FacultyDTO> facultyDTOS = new ArrayList<>();
        faculties.forEach(faculty -> facultyDTOS.add(FacultyDTO.fromFaculty(faculty)));
        return facultyDTOS;
    }

    public List<StudentDTO> getStudentsByFacultyId(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElse(null);
        if (faculty != null) {
            logger.info("Was invoked method for get students by faculty id");
            List<Student> students = faculty.getStudents();
            return students.stream().map(StudentDTO::fromStudent).collect(Collectors.toList());
        } else {
            logger.error("There is not faculty with id = " + id);
            throw new OpenApiResourceNotFoundException("Факультет с id " + id + " не найден");
        }
    }
}
