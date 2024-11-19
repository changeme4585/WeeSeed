package org.weeseed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.weeseed.dto.DefaultImageDto;
import org.weeseed.service.DefaultImageService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/default-card")  // 공통 URL 경로 적용
/*
    GET /default-card/get
    POST /default-card/delete
    POST /default-card/update
 */
public class DefaultImageController {

    private final DefaultImageService defaultImageService;

    // 기본 카드 불러오기
    @GetMapping("/get")
    public ResponseEntity<List<DefaultImageDto>> getDefaultCard(@RequestParam("constructorId") String constructorId) {
        try {
            List<DefaultImageDto> defaultCards = defaultImageService.getUserDefaultImageList(constructorId);
            return new ResponseEntity<>(defaultCards, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // 기본 카드 삭제하기
    @PostMapping("/delete")
    public ResponseEntity<Void> deleteDefaultCard(@RequestParam("constructorId") String constructorId,
                                                  @RequestParam("cardName") String cardName) {
        defaultImageService.deleteDefaultImage(constructorId, cardName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // 삭제 후 No Content 반환
    }

    /*
    @DeleteMapping("/{cardName}")
        public ResponseEntity<Void> deleteDefaultCard(@PathVariable("cardName") String cardName,
                                                      @RequestParam("constructorId") String constructorId) {
            defaultImageService.deleteDefaultImage(constructorId, cardName);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
     */
    // 기본 카드명 변경하기
    @PostMapping("/update")
    public ResponseEntity<Void> updateDefaultCard(@RequestParam("constructorId") String constructorId,
                                                  @RequestParam("cardName") String cardName,
                                                  @RequestParam("newCardName") String newCardName) {
        defaultImageService.updateDefaultCard(constructorId, cardName, newCardName);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
