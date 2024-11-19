package org.weeseed.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatRequest {

    private String model;
    private List<Message> messages;
    private Double top_p;

    /**
     * ChatBot 호출 DTO 객체 속성
     *
     * @param model     사용할 모델의 이름 또는 종류
     * @param prompt    초기 메시지 또는 유저의 요청 문구
     */
    public ChatRequest(String model, String prompt) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("user", prompt));
        this.top_p = 0.3;   // top_p 기본 값 설정
    }
}