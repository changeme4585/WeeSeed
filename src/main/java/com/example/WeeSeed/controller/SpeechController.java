package com.example.WeeSeed.controller;


import com.example.WeeSeed.service.SpeechService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SpeechController {
    private  final SpeechService speechService;


    @PostMapping(value = "/save-speech-result")
    public void saveResult(@RequestParam("childId") String childId,
                           @RequestParam("cardName") String cardName,
                           @RequestParam("score") String score){
        speechService.saveResult(childId,cardName,score);
    }
}
