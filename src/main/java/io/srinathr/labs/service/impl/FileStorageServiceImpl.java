package io.srinathr.labs.service.impl;

import io.srinathr.labs.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    private static final Logger logger = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    @Value("${file.upload-dir}")
    private String fileUploadDirectory;

    @Override
    public String saveFile(MultipartFile file) throws IOException {
        Path fileUploadLocation = Paths.get(fileUploadDirectory).toAbsolutePath().normalize();
        logger.info("Configured upload directory {}",fileUploadDirectory);
        Files.createDirectories(fileUploadLocation);

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        if(fileName.contains("..")) {
            logger.error("Invalid file name found");
            throw new IOException("Invalid file name.");
        }

        Path targetLocation = fileUploadLocation.resolve(fileName);
        Files.copy(file.getInputStream(),targetLocation, StandardCopyOption.REPLACE_EXISTING);
        logger.info("File successfully copied at {}",targetLocation.toString());
        return fileName;
    }
}
