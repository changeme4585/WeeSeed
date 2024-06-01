package com.example.WeeSeed.controller;


import com.example.WeeSeed.service.VideoCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class VideoCardController {
    private  final VideoCardService videoCardService;


    @PostMapping (value =  "/uploadvideocard")
    public ResponseEntity<String> uploadVideoCard(
            @RequestParam("cardName") String cardName,
            @RequestParam("video") MultipartFile video,
            @RequestParam("color") String color,
            @RequestParam("childCode") String childCode,
            @RequestParam("constructorId")String constructorId
    ){
        try {
            videoCardService.saveVideoCard(cardName, video, color, childCode, constructorId);
            return new ResponseEntity<>("Video card created successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create Video card", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
