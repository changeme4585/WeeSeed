package org.weeseed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.weeseed.service.ChatBotService;
import org.weeseed.service.SpeechAccuracyService;

/**
 * 발음 정확도 검사와 GPT의 분석 결과를 관리하는 Controller
 */
@RestController
@RequiredArgsConstructor
/*
    GET /feed-back
    POST /save-speech-result
 */
public class SpeechAccuracyController {
    private  final SpeechAccuracyService speechAccuracyService;
    private  final ChatBotService chatBotService;

    /**
     * 특정 아동의 발음 정확도 검사 결과를 저장
     *
     * @param childCode     발음 정확도 검사가 수행된 계정의 아동 코드
     * @param cardName      발음 정확도 검사가 수행된 카드명
     * @param score         발음 정확도 결과로 반환된 점수
     */
    @PostMapping(value = "/save-speech-result")
    public void saveResult(@RequestParam("childCode") String childCode,
                           @RequestParam("cardName") String cardName,
                           @RequestParam("score") String score){
        speechAccuracyService.saveResult(childCode,cardName,score);
    }

    /**
     * GPT가 분석한 특정 아동의 발음 정확도 내역 분석 결과를 반환
     *
     * @param childCode     GPT 호출이 수행된 계정의 아동 코드
     * @return              GPT가 생성한 답변 문자열
     */
    @GetMapping (value = "/feed-back")
    public String getFeedBack(@RequestParam("childCode") String childCode){
        return chatBotService.question(childCode);
    }
}