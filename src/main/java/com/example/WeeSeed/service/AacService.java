package com.example.WeeSeed.service;


import com.example.WeeSeed.FileName;
import com.example.WeeSeed.SFTPService;
import com.example.WeeSeed.entity.AacCard;
import com.example.WeeSeed.repository.AacRepository;
import com.example.WeeSeed.repository.ChildRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Transactional
@RequiredArgsConstructor
public class AacService {
    private final AacRepository aacRepository;
    //private static final String UPLOAD_DIR = "uploads/";


    @Value("${upload.directory}")
    private String uploadDirectory;
    private final SFTPService sftpService;


    public void saveAACCard(MultipartFile image, String cardName, MultipartFile audio,
                            String color, String childCode,String constructorId,boolean share)
            throws IOException {
        // Create directory if not exists

        FileName imageName = new FileName(image.getOriginalFilename());
        FileName audioName = new FileName(audio.getOriginalFilename());
        String imageUrl = imageName.getFileName();
        String voiceUrl = audioName.getFileName();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd");
        String formattedDate = now.format(formatter); //현재시간을 String 형으로

        AacCard aacCard  = AacCard.builder().
                cardName(cardName).
                creationTime(formattedDate).
                color(color).
                childId(childCode).
                constructorId(constructorId).
                imageUrl(imageUrl).
                voiceUrl(voiceUrl).
                share(share).
                build();
        aacRepository.aacCardSave(aacCard);




        try {
            byte[] bytes = image.getBytes();
            String remoteFilePath = uploadDirectory +imageUrl;
            sftpService.uploadFile(bytes, remoteFilePath);

            byte[] audioBytesBytes = audio.getBytes();
            String audioRemoteFilePath = uploadDirectory+voiceUrl;
            sftpService.uploadFile(audioBytesBytes, audioRemoteFilePath);

           // return new ResponseEntity<>("File uploaded successfully and sent via WebSocket", HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("라즈베리파이 + :"+e);
            //return new ResponseEntity<>("Failed to upload file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //Path uploadPath = Paths.get(UPLOAD_DIR);
//        if (!Files.exists(uploadPath)) {
//            Files.createDirectories(uploadPath);
//        }
//        // Save image file
//        if (!image.isEmpty()) {
//            Path imagePath = uploadPath.resolve(image.getOriginalFilename());
//            Files.copy(image.getInputStream(), imagePath);
//        }
//
//        // Save audio file
//        if (!audio.isEmpty()) {
//            Path audioPath = uploadPath.resolve(audio.getOriginalFilename());
//            Files.copy(audio.getInputStream(), audioPath);
//        }

        // Save other data (for demonstration purposes, we're printing it)
        System.out.println("Card Name: " + cardName);
        System.out.println("Color: " + color);
        System.out.println("Child Code: " + childCode);
    }
}
