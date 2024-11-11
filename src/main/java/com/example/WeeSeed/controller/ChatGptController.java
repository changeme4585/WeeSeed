package com.example.WeeSeed.controller;

import com.example.WeeSeed.service.ChatGptService;
import com.example.WeeSeed.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatGptController {
    private  final ChatService chatService;


    @GetMapping (value = "/gpt_test")
    public  String gptTest(@RequestParam("gpt") String gpt){

        return chatService.question(gpt);
    }
}
