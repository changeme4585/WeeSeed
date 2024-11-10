package com.example.WeeSeed.controller;


import com.example.WeeSeed.service.ChatGptService;
import com.example.WeeSeed.service.SpeechService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SpeechController {
    private  final SpeechService speechService;
    private  final ChatGptService chatGptService;

    @PostMapping(value = "/save-speech-result") //발음 테스트 결과 저장
    public void saveResult(@RequestParam("childId") String childId,
                           @RequestParam("cardName") String cardName,
                           @RequestParam("score") String score){
        speechService.saveResult(childId,cardName,score);
    }

    @GetMapping (value = "/feed-back") // 지피티로부터 피드백
    public String getFeedback(@RequestParam("childId") String childId){
        return chatGptService.question("");
    }
}
