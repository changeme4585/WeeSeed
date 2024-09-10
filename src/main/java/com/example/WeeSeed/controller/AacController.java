package com.example.WeeSeed.controller;


import com.example.WeeSeed.dto.AacDto;
import com.example.WeeSeed.dto.DefaultImageDto;
import com.example.WeeSeed.service.AacService;
import com.example.WeeSeed.service.DefaultImageService;
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
            ) throws Exception {
        System.out.println("아동코드 :"+ childCode);

            String isSuitable =aacService.saveAACCard(image, cardName, audio, color, childCode,constructorId,share);
        System.out.println(isSuitable);

            return new ResponseEntity<>(isSuitable, HttpStatus.OK);

    }

    @GetMapping(value = "/getaac")
    public ResponseEntity<List<AacDto>> getAacCard(@RequestParam("childCode") String childCode, @RequestParam("constructorId")String constructorId){

        List<AacDto> aacDto = aacService.getAacCard(childCode,constructorId);
        return new ResponseEntity<>(aacDto, HttpStatus.OK);
    }
    // aac 카드를 삭제하는 컨트롤러
    @PostMapping (value = "/delete-aac")
    public void  removeAacCard(@RequestParam("childCode") String childCode, @RequestParam("constructorId")String constructorId,
                               @RequestParam("cardName")String cardName){
            aacService.removeAacCard(childCode,constructorId,cardName);
    }

    @PostMapping (value = "/update-aac")
    public void updateAacCard( @RequestParam("image") MultipartFile image,  //변경할 이미지
                               @RequestParam("childCode") String childCode, @RequestParam("constructorId")String constructorId,
                               @RequestParam("cardName")String cardName,
                               @RequestParam("newCardName") String newCardName) throws Exception {
            aacService.updateAacCard(image,childCode,constructorId,cardName,newCardName);
    }

}
