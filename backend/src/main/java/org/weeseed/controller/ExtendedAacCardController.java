package org.weeseed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.weeseed.dto.AacCardDto;
import org.weeseed.dto.ExtendedAacCardDto;
import org.weeseed.service.ExtendedAacCardService;

import java.util.List;

@RestController
@RequestMapping("/extend-aac")
/*
    GET /extend-aac/get
    POST /extend-aac/update
 */
public class ExtendedAacCardController {

    @Autowired
    private ExtendedAacCardService extendedAacCardService;


    /**
     * 확장 카드 목록과 기본 AAC 카드 정보를 불러오는 메서드
     *
     * @param repCardId 학습이 수행된 대표 AAC 카드 ID
     * @return 확장 카드 목록
     */
    @GetMapping("/get/{repCardId}")
    public ResponseEntity<?> getExtendedAacCards(@PathVariable Long repCardId) {
        try {
            List<ExtendedAacCardDto> extendedCards = extendedAacCardService.getExtendedAacCards(repCardId);
            if (extendedCards.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 repCardId에 대한 확장 카드가 없습니다.");
            }
            return ResponseEntity.ok(extendedCards);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다: " + e.getMessage());
        }
    }


    /**
     * 학습이 수행된 카드에 유사 이미지 카드 연결
     *
     * @param repCardId     학습이 수행된 대표 AAC 카드 ID
     * @param cardName      학습이 수행된 대표 AAC 카드명
     * @param imagePath     학습이 수행된 대표 AAC 카드 이미지 경로
     * @return              ResponseEntity<AAC card DTO>
     */
    @PostMapping("/update")
    public ResponseEntity<?> updateImage(@RequestParam Long repCardId, @RequestParam String cardName, @RequestParam String imagePath) {
        try {
            extendedAacCardService.addExtendedImage(repCardId, cardName, imagePath);
            return ResponseEntity.ok("유사 이미지가 성공적으로 추가되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
