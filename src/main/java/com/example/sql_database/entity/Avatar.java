package com.example.sql_database.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Avatar {

    @Id
    @Column(name = "avatar_id")
    private Long id;
    @Column(name = "file_path")
    private String filePath;
    @Column(name = "file_size")
    private Long fileSize;
    @Column(name = "media_type")
    private String mediaType;
    @Lob
    private byte[] data;

    @OneToOne
    @JoinColumn(name = "avatar_id", referencedColumnName = "id")
    @Setter(AccessLevel.NONE)
    private Student student;

}