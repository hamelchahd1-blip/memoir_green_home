package com.green_home_project.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

        File folder = new File(UPLOAD_DIR);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        File destination = new File(UPLOAD_DIR + fileName);
        file.transferTo(destination);

        return "/uploads/" + fileName;
    }
}
