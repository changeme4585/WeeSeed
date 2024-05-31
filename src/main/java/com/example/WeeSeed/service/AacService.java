package com.example.WeeSeed.service;


import com.example.WeeSeed.FileName;
import com.example.WeeSeed.SFTPService;
import com.example.WeeSeed.dto.AacDto;
import com.example.WeeSeed.entity.AacCard;
import com.example.WeeSeed.repository.AacRepository;
import com.example.WeeSeed.repository.ChildRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AacService {
    private final AacRepository aacRepository;
    //private static final String UPLOAD_DIR = "uploads/";


    @Value("${upload.directory}")
    private String uploadDirectory;
    @Value("${raspberry.pi.url}")
    private String raspberryPiUrl;
    private final SFTPService sftpService;


    public void saveAACCard(MultipartFile image, String cardName, MultipartFile audio,
                            String color, String childCode,String constructorId,int share)
            throws IOException {
        // Create directory if not exists
        String imageFormat = "";
        String imageFileName = image.getOriginalFilename();
        int imageI = imageFileName.lastIndexOf('.');
        if (imageI > 0) {
            imageFormat = imageFileName.substring(imageI + 1);
        }

        String voiceFormat = "";
        String voiceFileName = audio.getOriginalFilename();
        int voiceI = voiceFileName.lastIndexOf('.');
        if (voiceI > 0) {
            voiceFormat = voiceFileName.substring(voiceI + 1);
        }


        FileName imageName = new FileName(image.getOriginalFilename());
        FileName audioName = new FileName(audio.getOriginalFilename());
        String imageUrl = imageName.getFileName()+"."+imageFormat;
        String voiceUrl = audioName.getFileName()+"."+voiceFormat;

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
    public List<AacDto> getAacCard(String childCode, String constructorId){
        System.out.println("childcode + constructorId " + childCode+" + "+constructorId);
        List<AacCard> aacCardList= aacRepository.getAacCardList(childCode,constructorId);
        List<AacDto> aacDtoList = new ArrayList<>(aacCardList.size());
        for (AacCard aacCard:aacCardList){
            try {
                System.out.println("이미지 이름: " + aacCard.getImageUrl());
                String imageUrl = raspberryPiUrl + aacCard.getImageUrl();
                RestTemplate imageTemplate = new RestTemplate();
                byte[] imageBytes = imageTemplate.getForObject(URI.create(imageUrl), byte[].class);
//                InputStream imageInputStream = new ByteArrayInputStream(imageBytes);
//                InputStreamResource image = new InputStreamResource(imageInputStream);

                String voiceUrl = raspberryPiUrl + aacCard.getVoiceUrl();
                System.out.println("오디오 이름: " + aacCard.getVoiceUrl());
                RestTemplate voiceTemplate = new RestTemplate();
                byte[] voiceBytes = voiceTemplate.getForObject(URI.create(voiceUrl), byte[].class);
//                InputStream voiceInputStream = new ByteArrayInputStream(voiceBytes);
//                InputStreamResource voice = new InputStreamResource(voiceInputStream);


                AacDto aacDto = AacDto.builder().
                        aacCardId(aacCard.getAacCardId()).
                        cardName(aacCard.getCardName()).
                        creationTime(aacCard.getCreationTime()).
                        color(aacCard.getColor()).
                        childId(aacCard.getChildId()).
                        constructorId(aacCard.getConstructorId()).
                        image(imageUrl).
                        voice(voiceUrl).
//                        image(imageBytes).
//                        voice(voiceBytes).
                        build();

                aacDtoList.add(aacDto);
            }catch (Exception e){
                System.out.println("aac 카드 목록 조회 에러: "+e);
            }
        }

        return aacDtoList;
    }
}
