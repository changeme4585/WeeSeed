package org.weeseed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.weeseed.service.ChatService;


/**
 * ChatBot service 요청 핸들링을 위한 REST controller
 */


@RestController
@RequiredArgsConstructor
/*
    GET /gpt_test
 */
public class ChatBotController {
    private final ChatService chatService;

    /**
     * ChatGPT를 기반으로 한 ChatBot에 요청을 보내고 응답 반환
     *
     * @param childCode     GPT 호출이 수행된 계정의 아동 코드
     * @return              GPT가 생성한 답변 문자열
     */
    @GetMapping(value="/gpt_test")
    public String gptTest(@RequestParam("gpt") String childCode) {
        return chatService.question(childCode);
    }
}
