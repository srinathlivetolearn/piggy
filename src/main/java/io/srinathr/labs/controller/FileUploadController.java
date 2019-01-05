package io.srinathr.labs.controller;

import io.srinathr.labs.dto.FileUploadResponse;
import io.srinathr.labs.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/file-api")
public class FileUploadController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private FileStorageService storageService;

    @PostMapping(value = "/upload",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FileUploadResponse> handleFileUpload(@RequestParam(value = "file") MultipartFile file,
                                                               HttpServletRequest request, HttpServletResponse response) {
        try {
            String savedFile = storageService.saveFile(file);
            FileUploadResponse fuResponse = new FileUploadResponse();
            fuResponse.setStatus("Upload success");
            fuResponse.setBody(savedFile);
            LOGGER.info("File upload success.");
            return ResponseEntity.ok(fuResponse);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
        }
    }

    @GetMapping("/ping")
    public String ping(HttpServletRequest request,HttpServletResponse response) {
        return "pong!!";
    }
}
