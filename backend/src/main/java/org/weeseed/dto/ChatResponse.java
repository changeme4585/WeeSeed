package org.weeseed.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {

    private List<Choice> choices;

    /**
     * OpenAI GPT 응답을 담는 DTO 객체 속성
     * 선택지(Choice) 목록을 포함
     */

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Choice{
        private int index;
        private Message message;
    }
}