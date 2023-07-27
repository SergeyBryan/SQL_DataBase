package com.example.sql_database.service;

import com.example.sql_database.entity.Avatar;
import com.example.sql_database.entity.Student;
import com.example.sql_database.repository.AvatarRepository;
import com.example.sql_database.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class AvatarService {

    private final Logger logger = LoggerFactory.getLogger(AvatarService.class);

    @Value(value = "${students.avatars.dir.path}")
    private String fileDir;
    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;

    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }

    public void saveAvatar(Long id, MultipartFile file) throws IOException {
        logger.debug("File size: {}", file.getSize());
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("The student with id = {} is not found", id);
                    return new OpenApiResourceNotFoundException("Пользователь c id " + id + " не найден");
                });
        Path path = createPath(id, file);
        checkAndUpload(path, file);
        Avatar avatar = prepareAvatar(id, student, path, file);
        logger.info("Was invoked method for save avatar");
        avatarRepository.save(avatar);
    }

    public Avatar findAvatar(Long id) {
        return avatarRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Avatar with id = {} is not found", id);
                    return new OpenApiResourceNotFoundException("Аватар по id " + id + " не был найден");
                });
    }

    @Transactional
    private void checkAndUpload(Path path, MultipartFile file) throws IOException {
        Files.createDirectories(path.getParent());
        Files.deleteIfExists(path);
        try (
                InputStream is = file.getInputStream();
                OutputStream os = Files.newOutputStream(path, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }
    }

    private Path createPath(Long id, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName != null) {
            logger.info("The path was created successfully");
            return Path.of(fileDir, id + "." + StringUtils.getFilenameExtension(fileName));
        } else {
            logger.error("The file is not found");
            throw new OpenApiResourceNotFoundException("Не найден");
        }
    }

    private Avatar prepareAvatar(Long id, Student student, Path path, MultipartFile file) throws IOException {
        Avatar avatar = avatarRepository.findById(id).orElse(new Avatar());
        avatar.setId(student.getId());
        avatar.setFileSize(file.getSize());
        avatar.setFilePath(path.toString());
        avatar.setMediaType(file.getContentType());
        avatar.setData(generateImagePreview(path));
        logger.info("Avatar was created successfully");
        return avatar;
    }

    private byte[] generateImagePreview(Path filePath) throws IOException {
        try (
                InputStream is = Files.newInputStream(filePath);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
            BufferedImage image = ImageIO.read(bis);
            int height = image.getWidth() / (image.getHeight() / 100);
            BufferedImage preview = new BufferedImage(100, height, image.getType());
            Graphics2D graphics2D = preview.createGraphics();
            graphics2D.drawImage(image, 0, 0, 100, height, null);
            graphics2D.dispose();
            String extension = StringUtils.getFilenameExtension(filePath.getFileName().toString());
            if (extension != null) {
                ImageIO.write(preview, extension, baos);
            }
            logger.info("Image preview is ready");
            return baos.toByteArray();
        }
    }

}
