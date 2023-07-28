package com.example.sql_database.service;

import com.example.sql_database.entity.FacultyDTO;
import com.example.sql_database.entity.Student;
import com.example.sql_database.entity.StudentDTO;
import com.example.sql_database.repository.FacultyRepository;
import com.example.sql_database.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {
    private final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public StudentDTO getStudent(Long id) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student != null) {
            logger.info("Was invoked method for get student");
            return StudentDTO.fromStudent(student);
        } else {
            logger.error("There is not student with id = " + id);
            throw new OpenApiResourceNotFoundException("This id doesn't exist");
        }
    }

    public List<StudentDTO> getAll() {
        List<Student> students = studentRepository.findAll();
        List<StudentDTO> studentDTOS = new ArrayList<>();
        students.forEach(student -> studentDTOS.add(StudentDTO.fromStudent(student)));
        logger.info("Was invoked method for get all students");
        return studentDTOS;
    }

    public List<StudentDTO> getByAge(int age) {
        logger.info("Was invoked method for get student by age");
        List<Student> students = studentRepository.findStudentByAge(age);
        List<StudentDTO> studentDTOS = new ArrayList<>();
        students.forEach(student -> studentDTOS.add(StudentDTO.fromStudent(student)));
        return studentDTOS;
    }

    public List<StudentDTO> getByAgeBetween(int min, int max) {
        logger.info("Was invoked method for get student by age between min and max value");
        List<Student> students = studentRepository.findStudentsByAgeBetween(min, max);
        List<StudentDTO> studentDTOS = new ArrayList<>();
        students.forEach(student -> studentDTOS.add(StudentDTO.fromStudent(student)));
        return studentDTOS;
    }

    public FacultyDTO getStudentFaculty(Long id) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student != null) {
            logger.info("Was invoked method for get student faculty");
            FacultyDTO facultyDTO = new FacultyDTO();
            facultyDTO.setId(student.getFaculty().getId());
            facultyDTO.setName(student.getFaculty().getName());
            facultyDTO.setColor(student.getFaculty().getColor());
            return facultyDTO;
        } else {
            logger.error("There is not student with id = " + id);
            throw new OpenApiResourceNotFoundException("This id doesn't exist");
        }
    }

    public void update(Long id, StudentDTO studentDTO) {
        if (getStudent(id) != null) {
            Student student = studentDTO.toStudent();
            student.setId(id);
            student.setFaculty(facultyRepository.findById(studentDTO.getFacultyId()).orElse(null));
            logger.info("Was invoked method for update student");
            studentRepository.save(student);
        } else {
            logger.error("There is not student with id = " + id);
            throw new OpenApiResourceNotFoundException("This id doesn't exist");
        }
    }

    public void delete(Long id) {
        logger.info("Was invoked method for delete student");
        studentRepository.deleteById(id);
    }

    public void add(StudentDTO studentDTO) {
        Student student = new Student();
        student.setAge(studentDTO.getAge());
        student.setName(studentDTO.getName());
        student.setFaculty((facultyRepository.findById(studentDTO.getFacultyId()).orElse(null)));
        logger.info("Was invoked method for create student");
        studentRepository.save(student);
    }


    public Integer getNumberOfStudents() {
        logger.info("Was invoked method for count number of students");
        return studentRepository.getNumberOfStudents();
    }

    public Integer getAverageAgeByStudent() {
        logger.info("Was invoked method for count average age by student");
        return studentRepository.getAverageAgeByStudent();
    }

    public List<StudentDTO> getYoungStudents() {
        logger.info("Was invoked method for get five young students");
        List<Student> students = studentRepository.getFiveYoungStudents();
        List<StudentDTO> studentDTOS = new ArrayList<>();
        students.forEach(student -> studentDTOS.add(StudentDTO.fromStudent(student)));
        return studentDTOS;
    }

    public List<StudentDTO> getAllBySort(Integer pageNumber, Integer studentQuantity) {
        logger.info("Was invoked method for get students by page and quantity per each page");
        if (studentQuantity > 50 || studentQuantity <= 0) {
            logger.warn("It's not possible to display more than 50 students or a negative value. The quantity per page has been adjusted to show 50 students.");
            studentQuantity = 50;
        }
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, studentQuantity);
        List<Student> students = studentRepository.findAll(pageRequest).getContent();
        List<StudentDTO> studentDTOS = new ArrayList<>();
        students.forEach(student -> studentDTOS.add(StudentDTO.fromStudent(student)));
        return studentDTOS;
    }
}