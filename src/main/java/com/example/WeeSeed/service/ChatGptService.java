package com.example.WeeSeed.service;



import com.example.WeeSeed.dto.ChatRequest;
import com.example.WeeSeed.dto.ChatResponse;
import com.example.WeeSeed.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service

public class ChatGptService {



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
                "Please answer in great detail"));
        try {
            ChatResponse response  = template.postForObject(chatApiURL, request, ChatResponse.class);
             return response.getChoices().get(0).getMessage().getContent();
        }catch (Exception e){
            System.out.println("gpt에러: "+e);
        }
        return "w";
    }
    public String recommend(String prompt){

                ;
        String chatAnswer  =chat(prompt);
        System.out.println("gpt 응답: "+chatAnswer);
        return chatAnswer;
    }
}
