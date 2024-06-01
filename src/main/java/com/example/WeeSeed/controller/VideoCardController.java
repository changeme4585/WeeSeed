package com.example.WeeSeed.controller;


import com.example.WeeSeed.dto.VideoDto;
import com.example.WeeSeed.service.VideoCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
            @RequestParam("constructorId")String constructorId,
            @RequestParam("thumbnail")  MultipartFile thumbnail
    ){
        try {
            videoCardService.saveVideoCard(cardName, video, color, childCode, constructorId,thumbnail);
            return new ResponseEntity<>("Video card created successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create Video card", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping (value ="/getvideocard")
    public ResponseEntity<List<VideoDto>> getAacCard(@RequestParam("childCode") String childCode, @RequestParam("constructorId")String constructorId) {
        List<VideoDto> videoDtos = videoCardService.getVideoCard(childCode,constructorId);
        return  new ResponseEntity<>(videoDtos,HttpStatus.OK);
    }
}
