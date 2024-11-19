package org.weeseed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.weeseed.dto.VideoCardDto;
import org.weeseed.service.VideoCardService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/video")
/*
    GET /video/get
    POST /video/upload
    POST /video/delete
 */
public class VideoCardController {
    private final VideoCardService videoCardService;


    /**
     * 비디오 카드 목록 조회
     *
     * @param childCode      카드가 생성된 계정의 아동 코드
     * @param constructorId  카드가 생성된 계정의 ID
     * @return               ResponseEntity<비디오 카드 목록, 성공 또는 에러 메시지>
     */
    @GetMapping(value = "/get")
    public ResponseEntity<List<VideoCardDto>> getVideoCard(
            @RequestParam("childCode") String childCode,
            @RequestParam("constructorId") String constructorId
    ) {
        List<VideoCardDto> videoDtos = videoCardService.getVideoCard(childCode, constructorId);

        return new ResponseEntity<>(videoDtos, HttpStatus.OK);
    }

    /**
     * 비디오 카드 업로드
     *
     * @param video          비디오 카드에 등록된 비디오 파일
     * @param cardName       비디오 카드의 이름
     * @param color          비디오 카드의 색상 카테고리
     * @param childCode      카드가 생성된 계정의 아동 코드
     * @param constructorId  카드가 생성된 계정의 ID
     * @param thumbnail      비디오 카드의 썸네일 이미지
     * @return               ResponseEntity<성공 또는 에러 메시지>
     */
    @PostMapping(value = "/upload")
    public ResponseEntity<String> uploadVideoCard(
            @RequestParam("video") MultipartFile video,
            @RequestParam("cardName") String cardName,
            @RequestParam("color") String color,
            @RequestParam("childCode") String childCode,
            @RequestParam("constructorId") String constructorId,
            @RequestParam("thumbnail") MultipartFile thumbnail
    ) {
        try {
            videoCardService.saveVideoCard(video, cardName, color, childCode, constructorId, thumbnail);
            return new ResponseEntity<>("비디오 카드가 성공적으로 생성되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("비디오 카드 생성에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 비디오 카드 삭제
     *
     * @param videoCardId 삭제될 카드의 ID
     */
    @PostMapping(value = "/delete")
    public ResponseEntity<String> deleteVideoCard(@RequestParam("videoCardId") Long videoCardId) {
        try {
            videoCardService.deleteVideoCard(videoCardId);
            return new ResponseEntity<>("비디오 카드가 성공적으로 삭제되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("비디오 카드 삭제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}