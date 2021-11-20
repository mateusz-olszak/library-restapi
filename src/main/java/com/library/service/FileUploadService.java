package com.library.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Slf4j
public class FileUploadService {

    public void saveFile(String uploadDir, String filename, MultipartFile multipartFile) {
        log.info("Preparing to save thumbnail file");
        Path uploadPath = Paths.get(uploadDir);
        try(InputStream inputStream = multipartFile.getInputStream()) {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(filename);
            Files.copy(inputStream,filePath, StandardCopyOption.REPLACE_EXISTING);
            log.info("Thumbnail file has been proccessed");
        } catch (IOException e) {
              log.error("Error when saving thumbnail file. " + e.getMessage());
        }
    }

}
