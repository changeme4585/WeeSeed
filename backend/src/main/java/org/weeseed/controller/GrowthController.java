package org.weeseed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.weeseed.dto.GrowthDiaryDto;
import org.weeseed.dto.GrowthDto;
import org.weeseed.service.GrowthService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/growth")  // 공통 URL 경로 적용

/*
    POST /growth/log-click      // 클릭 로그 기록
    GET /growth/data            // 성장 데이터 조회
    POST /growth/create         // 성장 데이터 생성
    GET /growth/diary           // 성장 일기 조회

 */

public class GrowthController {
    private final GrowthService growthService;

    /**
     * 성장 데이터를 조회하는 메서드
     * 카드 정보를 가져옴
     * 카드명, 이미지, 색상 라벨링, 생성 일시
     *
     * @param userId 사용자 ID
     * @param childCode 아동 코드
     * @return 성장 데이터 리스트
     */
    @GetMapping("data")
    public ResponseEntity<List<GrowthDto>> getGrowthData(
            @RequestParam("userId") String userId,
            @RequestParam("childCode") String childCode
    ) {
        String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        List<GrowthDto> growthDtoList = growthService.getGrowthList(formattedDate, userId);
        return new ResponseEntity<>(growthDtoList, HttpStatus.OK);
    }

    /**
     * 성장 데이터를 생성하는 메서드
     *
     * @param userId 사용자 ID
     * @param childCode 아동 코드
     */
    @PostMapping("/create")
    public void createGrowth(
            @RequestParam("userId") String userId,
            @RequestParam("childCode") String childCode
    ) {
        growthService.makeGrowth(userId, childCode);
    }

    /**
     * 성장 일지를 조회하는 메서드
     *
     * @param userId 사용자 ID
     * @param childCode 아동 코드
     * @return 성장 일기 리스트
     */
    @GetMapping("/diary")
    public ResponseEntity<List<GrowthDiaryDto>> getGrowthDiary(
            @RequestParam("userId") String userId,
            @RequestParam("childCode") String childCode
    ) {
        List<GrowthDiaryDto> growthDiaryDto = growthService.getGrowthDiaryDto(userId, childCode);
        return new ResponseEntity<>(growthDiaryDto, HttpStatus.OK);
    }

    /**
     * 클릭 로그를 기록하는 메서드
     *
     * @param cardId 카드 ID
     * @param cardType 카드 유형 (따라해요, 이어봐요)
     */
    @PostMapping("/log-click")
    public void logClick(
            @RequestParam("cardId") Long cardId,
            @RequestParam("cardType") String cardType
    ) {
        growthService.clickLog(cardId, cardType);
    }

}
