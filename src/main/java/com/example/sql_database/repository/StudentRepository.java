package com.example.sql_database.repository;

import com.example.sql_database.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {


    List<Student> findStudentByAge(int age);

    List<Student> findStudentsByAgeBetween(int min, int max);

    @Query(value = "SELECT COUNT(id) FROM student", nativeQuery = true)
    Integer getNumberOfStudents();

    @Query(value = "SELECT avg(age) FROM student", nativeQuery = true)
    Integer getAverageAgeByStudent();

    @Query(value = "SELECT * FROM student order by age limit 5", nativeQuery = true)
    List<Student> getFiveYoungStudents();

}
