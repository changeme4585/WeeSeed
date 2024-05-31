package com.example.WeeSeed.controller;


import com.example.WeeSeed.FileName;
import com.example.WeeSeed.dto.AacDto;
import com.example.WeeSeed.service.AacService;
import lombok.RequiredArgsConstructor;
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
public class AacController {

    private final AacService  aacService;
    @PostMapping("/uploadaac")
    public ResponseEntity<String> createAACCard(
            @RequestParam("image") MultipartFile image,
            @RequestParam("cardName") String cardName,
            @RequestParam("audio") MultipartFile audio,
            @RequestParam("color") String color,
            @RequestParam("childCode") String childCode,
            @RequestParam("constructorId")String constructorId,
            @RequestParam("share")int share
           // @RequestParam("")
            ) {
        System.out.println("아동코드 :"+ childCode);
        try {
            aacService.saveAACCard(image, cardName, audio, color, childCode,constructorId,share);
            return new ResponseEntity<>("AAC card created successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to create AAC card", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/getaac")
    public ResponseEntity<List<AacDto>> getAacCard(@RequestParam("childCode") String childCode, @RequestParam("constructorId")String constructorId){
    //public ResponseEntity<List<AacDto>> getAacCard(){
        //System.out.println("이미지 불러오기: "+childCode+" : "+constructorId);


        List<AacDto> aacDto = aacService.getAacCard(childCode,constructorId);
        //List<AacDto> aacDto = aacService.getAacCard("QQWW11","ZNuoQ");
        return new ResponseEntity<>(aacDto, HttpStatus.OK);
    }
}
