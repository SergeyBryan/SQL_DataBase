package com.example.sql_database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "student")
@Setter
@Getter
public class Student {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private int age;
    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

}
