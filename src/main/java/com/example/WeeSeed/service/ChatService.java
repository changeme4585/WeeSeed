package com.example.WeeSeed.service;


import com.example.WeeSeed.dto.ChatRequest;
import com.example.WeeSeed.dto.ChatResponse;
import com.example.WeeSeed.dto.Message;
import com.example.WeeSeed.entity.Child;
import com.example.WeeSeed.entity.SpeechAccuracy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    @Value("${openai.model.chat}")
    private String chatModel ;

    @Value("${openai.api.url.chat}")
    private String chatApiURL;

    @Autowired
    private RestTemplate template;


    public String chat(String prompt){
        ChatRequest request = new ChatRequest(chatModel, prompt);
        request.getMessages().add(new Message(
                "system",
                ""));
        try {
            ChatResponse response  = template.postForObject(chatApiURL, request, ChatResponse.class);
            return response.getChoices().get(0).getMessage().getContent();
        }catch (Exception e){
            System.out.println("gpt에러: "+e);
        }
        return "w";
    }
    public String question(String prompt){

        String chatAnswer  =chat(prompt);
        System.out.println("gpt 응답: "+chatAnswer);
        return chatAnswer;
    }
}
