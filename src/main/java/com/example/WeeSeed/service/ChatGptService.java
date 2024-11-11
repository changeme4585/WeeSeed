package com.example.WeeSeed.service;



import com.example.WeeSeed.dto.ChatRequest;
import com.example.WeeSeed.dto.ChatResponse;
import com.example.WeeSeed.dto.Message;
import com.example.WeeSeed.entity.Child;
import com.example.WeeSeed.entity.SpeechAccuracy;
import com.example.WeeSeed.repository.ChildRepository;
import com.example.WeeSeed.repository.SpeechRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatGptService {
    private final ChildRepository childRepository;
    private final SpeechRepository speechRepository;


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
                "You will act as an expert providing feedback to children with disabilities." +
                        "We are helping the development of children with disabilities by showing them various random words, " +
                        "and when they repeat the words, we evaluate their pronunciation by assigning scores. " +
                        "Your role will be to analyze the score list of the respective child."));
        try {
            ChatResponse response  = template.postForObject(chatApiURL, request, ChatResponse.class);
             return response.getChoices().get(0).getMessage().getContent();
        }catch (Exception e){
            System.out.println("gpt에러: "+e);
        }
        return "w";
    }
    public String question(String childCode){
        Child child = childRepository.getChild(childCode);
        String prompt = "이 아동은 이름은 "+child.getName()+"이고,  생일은 "+child.getBirth()+"야.\n"+
                "이 아동은 "
                +child.getDisabilityType()+" 장애를 가지고 있어. " +
                "이 아동의 각 단어별 score는 다음과 같아.";
        List<SpeechAccuracy> speechAccuracyList = speechRepository.getSpeechList(childCode);
        for (SpeechAccuracy speechAccuracy : speechAccuracyList){
            prompt+=speechAccuracy.getCardName()+"라는 단어에 대한 점수는 "
                    +speechAccuracy.getScore()+"다. ";
        }
        prompt+="위에 기록을 바탕으로 아동의 학습상태를 분석(어려워하는 단어들의 패턴을 분석 및 그 원인을 추론)하고 " +
                "이 장애아동에 대한 수치(점수)위주로 피드백 및 학습방향(분석을 바탕으로)을 한국어로 번역해서 알려줘"+
            "반복되는 내용을 줄이고 반드시 수치적 통계를 바탕으로 피드백 해줘";
        String chatAnswer  = chat(prompt);
        System.out.println("gpt 응답: "+chatAnswer);
        return chatAnswer;
    }
}
