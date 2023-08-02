package com.example.sql_database.controller;

import com.example.sql_database.entity.AppInfoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    @Value("${app.env}")
    private String environment;

    @GetMapping("/appInfo")
    public ResponseEntity<AppInfoDTO> appInfo() {
        return ResponseEntity.ok(AppInfoDTO.info(environment));
    }
}