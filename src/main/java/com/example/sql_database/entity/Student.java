package com.example.sql_database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

}
