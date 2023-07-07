package com.example.sql_database.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "faculty")
@Setter
@Getter
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    @OneToMany(mappedBy = "faculty")
    private List<Student> students;


}
