package com.example.sql_database.controller;

import com.example.sql_database.entity.Avatar;
import com.example.sql_database.service.AvatarService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;


@RestController
@RequestMapping("/avatar")
public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> saveAvatar(@RequestParam Long id, @RequestParam MultipartFile file) throws IOException {
        if (file.getSize() > 1024 * 500) {
            return ResponseEntity.badRequest().body("Файл слишком много весит");
        }
        avatarService.saveAvatar(id, file);
        return ResponseEntity.ok("Аватар сохранён");
    }

    @GetMapping(value = "{id}/preview")
    public ResponseEntity<byte[]> getAvatar(@PathVariable Long id) {
        Avatar avatar = avatarService.findAvatar(id);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        httpHeaders.setContentLength(avatar.getData().length);

        return ResponseEntity.status((200)).headers(httpHeaders).body(avatar.getData());
    }

    @GetMapping(value = "{id}")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Avatar avatar = avatarService.findAvatar(id);

        Path path = Path.of(avatar.getFilePath());

        try (
                InputStream is = Files.newInputStream(path);
                OutputStream os = response.getOutputStream();) {

            response.setContentType(avatar.getMediaType());
            Long value = avatar.getFileSize();
            response.setContentLength(value.intValue());
            is.transferTo(os);
        }
    }
}
