package com.example.WeeSeed.controller;

import com.example.WeeSeed.ImageWebSocketHandler;

import com.example.WeeSeed.SFTPService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@RestController
@RequestMapping("/api")
public class ImageUploadController {

    @Value("${upload.directory}")
    private String uploadDirectory;

//    private final ImageWebSocketHandler imageWebSocketHandler;
//
//    public ImageUploadController(ImageWebSocketHandler imageWebSocketHandler) {
//        this.imageWebSocketHandler = imageWebSocketHandler;
//    }
    private final SFTPService sftpService;

    public ImageUploadController(SFTPService sftpService) {
        this.sftpService = sftpService;
    }
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please select a file to upload", HttpStatus.BAD_REQUEST);
        }

        try {

            // Save the uploaded file to the server
//            byte[] bytes = file.getBytes();
//            String filePath = uploadDirectory + file.getOriginalFilename();
//            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
//            stream.write(bytes);
//            stream.close();
            // Send image to Raspberry Pi via WebSocket
            //imageWebSocketHandler.sendImage(bytes);


            byte[] bytes = file.getBytes();
            String remoteFilePath = uploadDirectory + file.getOriginalFilename();
            sftpService.uploadFile(bytes, remoteFilePath);
            // Return the file path or any other response
            return new ResponseEntity<>("File uploaded successfully and sent via WebSocket", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to upload file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
