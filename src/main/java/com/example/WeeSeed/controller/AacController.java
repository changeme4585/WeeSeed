package com.example.WeeSeed.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController

public class AacController {
    @PostMapping("/uploadaac")
    public void createAACCard(
            @RequestParam("image") MultipartFile image,
            @RequestParam("cardName") String cardName,
            @RequestParam("audio") MultipartFile audio,
            @RequestParam("color") String color,
            @RequestParam("childCode") String childCode) {
    System.out.println("아동코드 :"+ childCode);
//        try {
//            aacService.saveAACCard(image, cardName, audio, color, childCode);
//            return new ResponseEntity<>("AAC card created successfully", HttpStatus.OK);
//        } catch (IOException e) {
//            return new ResponseEntity<>("Failed to create AAC card", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
    }
}
