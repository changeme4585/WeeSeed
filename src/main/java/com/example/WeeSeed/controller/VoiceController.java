package com.example.WeeSeed.controller;


import com.example.WeeSeed.VoiceAi.VoiceCompareApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class VoiceController {
    @PostMapping  ("/checkvoice") //음성 유사도 검사
    public ResponseEntity<String> checkVoice (@RequestParam("audio")MultipartFile audio,
                                              @RequestParam("card_name") String card_name) throws IOException {
        VoiceCompareApi api=new VoiceCompareApi();
        System.out.println("음성 유사도 검사");
        System.out.println(audio+card_name);
        String score=api.checkSimilarity(card_name,audio);
        System.out.println(score);
        return new ResponseEntity<>(score, HttpStatus.OK);
    }
}
