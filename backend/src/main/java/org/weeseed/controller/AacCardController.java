package org.weeseed.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.weeseed.dto.AacCardDto;
import org.weeseed.service.AacCardService;

import java.io.IOException;
import java.util.List;

/**
 * 카드 생성 시 이미지 파일에 대한 유효성 점검 필요
 * createAacCard의 MultipartFile image를 검사할 것
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/aac")
/*
    GET /aac/get
    POST /aac/upload
    POST /aac/update
    POST /aac/delete
 */
public class AacCardController {

    @Autowired
    private final AacCardService aacCardService;
    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(AacCardController.class);

    /**
     * 특정 아동과 사용자 계정의 AAC 카드 목록 검색
     *
     * @param childCode     카드 목록이 등록된 계정의 아동 코드
     * @param constructorId 카드 목록이 등록된 계정의 ID
     * @return              ResponseEntity<AAC card DTO>
     */

    @GetMapping("/get")
    public ResponseEntity<List<AacCardDto>> getAacCard(
            @RequestParam("childCode") String childCode,
            @RequestParam("constructorId") String constructorId
    ) {
        logger.info("*** Fetching AAC cards for child code: {} and constructor ID: {} ***", childCode, constructorId);
        List<AacCardDto> aacDto = aacCardService.getAacCard(childCode, constructorId);
        return new ResponseEntity<>(aacDto, HttpStatus.OK);
    }

    /**
     * 새로운 AAC 카드 생성 처리 항목
     *
     * @param image         AAC 카드에 등록된 이미지 파일
     * @param cardName      AAC 카드의 이름
     * @param audio         AAC 카드에 등록된 음성 파일
     * @param color         AAC 카드의 색상 카테고리
     * @param childCode     카드가 생성된 계정의 아동 코드
     * @param constructorId 카드가 생성된 계정의 ID (보호자 또는 재활사)
     * @param share         카드 생성자가 설정한 공유 여부
     * @return              ResponseEntity<성공 또는 에러 메시지>
     */

    @PostMapping("upload")
    public ResponseEntity<String> createAacCard(
            @RequestParam("image") MultipartFile image,
            @RequestParam("cardName") String cardName,
            @RequestParam("audio") MultipartFile audio,
            @RequestParam("color") String color,
            @RequestParam("childCode") String childCode,
            @RequestParam("constructorId") String constructorId,
            @RequestParam("share") int share
    ) {
        logger.info("*** Creating AAC card for child code: {} ***", childCode);

        try {
            aacCardService.saveAacCard(image, cardName, audio, color, childCode, constructorId, share);
            return new ResponseEntity<>("*** AAC card created successfully ***", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to create AAC card: {}", e.getMessage());
            return new ResponseEntity<>("*** Failed to create AAC card ***", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 특정 아동과 계정에 등록된 단일 카드 삭제
     *
     * @param cardId        삭제할 카드 ID
     * @return              ResponseEntity<성공 또는 에러 메시지>
     */
    @PostMapping("delete")
    public void deleteAacCard(@RequestParam("aacCardId") Long cardId) {
        aacCardService.deleteAacCard(cardId);
    }

    /**
     * 특정 아동과 계정에 등록된 단일 카드 수정
     *
     * @param image
     * @param childCode
     * @param constructorId 카드 목록이 등록된 계정의 ID
     * @param aacCardId     수정할 카드 ID
     * @param cardName      수정할 카드 이름
     * @param newCardName   수정된 카드 이름
     */
    @PostMapping("update")
    public void updateAacCard(@RequestParam("image") MultipartFile image,
                              @RequestParam("childCode") String childCode,
                              @RequestParam("constructorId") String constructorId,
                              @RequestParam("aacCardId") Long aacCardId,
                              @RequestParam("cardName") String cardName,
                              @RequestParam("newCardName") String newCardName) throws Exception {
        aacCardService.updateAacCard(image, childCode, constructorId, aacCardId, cardName, newCardName);
    }
}
