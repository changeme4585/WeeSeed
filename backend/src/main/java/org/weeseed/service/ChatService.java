package org.weeseed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.weeseed.dto.ChatRequest;
import org.weeseed.dto.ChatResponse;
import org.weeseed.dto.Message;

@Service
@RequiredArgsConstructor
public class ChatService {
    @Value("${openai.model.chat}")
    private String chatModel ;

    @Value("${openai.api.url.chat}")
    private String chatApiURL;

    @Autowired
    @Qualifier("openAiRestTemplate")
    private RestTemplate openAiRestTemplate;


    /**
     * 주어진 프롬프트를 기반으로 ChatGPT와 통신하여 응답 반환
     * 프롬프트로 초기 GPT에게 Role을 부여
     *
     * @param prompt    GPT에게 전달할 요청 문구
     * @return          GPT가 생성한 응답 문자열
     */
    public String chat(String prompt){
        ChatRequest request = new ChatRequest(chatModel, prompt);
        request.getMessages().add(new Message("system", ""));

        try {
            ChatResponse response  = openAiRestTemplate.postForObject(chatApiURL, request, ChatResponse.class);
            return response.getChoices().get(0).getMessage().getContent();
        } catch (Exception e){
            System.err.println("GPT 호출 오류: " + e.getMessage());
            return "응답을 받을 수 없습니다.";
        }
    }

    public String question(String prompt){

        String chatAnswer = chat(prompt);
        System.out.println("GPT 응답: " + chatAnswer);
        return chatAnswer;
    }
}
