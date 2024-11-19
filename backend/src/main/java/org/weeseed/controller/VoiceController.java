package org.weeseed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.weeseed.service.VoiceSimilarityService;

import java.io.IOException;

/**
 * 음성 관련 API를 처리하는 컨트롤러 클래스
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/voice")
/*
    POST /voice/similarity-check
 */
public class VoiceController {
    private VoiceSimilarityService voiceSimilarityService;

    /**
     * 음성 유사도를 검사하는 API 엔드포인트
     *
     * @param audio     비교할 음성 파일
     * @param cardName  비교할 텍스트 스크립트 (카드 이름)
     * @return 유사도 점수
     * @throws IOException 파일 처리 중 발생하는 예외
     */
    @PostMapping("/similarity-check") // 음성 유사도 검사
    public ResponseEntity<String> checkVoiceSimilarity(@RequestParam("audio") MultipartFile audio,
                                                       @RequestParam("card_name") String cardName) throws IOException {
        System.out.println("음성 유사도 검사 요청 수신");
        System.out.println("업로드된 음성 파일: " + audio + ", 카드 이름: " + cardName);

        // 음성 유사도 점수 계산
        String similarityScore = voiceSimilarityService.checkSimilarity(cardName, audio);
        System.out.println("유사도 점수: " + similarityScore);

        // 유사도 점수를 포함한 응답 반환
        return new ResponseEntity<>(similarityScore, HttpStatus.OK);
    }
}
