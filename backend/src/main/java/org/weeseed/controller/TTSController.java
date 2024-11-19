package org.weeseed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.weeseed.service.TTSService;

@RestController
@RequestMapping("/api")
/*
    POST /api/tts
 */
public class TTSController {

    private final TTSService ttsService;

    @Autowired
    public TTSController(TTSService ttsService) {
        this.ttsService = ttsService;
    }

    // TTS API 요청을 프록시로 전달하는 엔드포인트
    @PostMapping("/tts")
    public ResponseEntity<byte[]> getTTS(@RequestParam("cardName") String cardName) {
        return ttsService.getTTS(cardName);
    }

//    // SoundCompare API 요청을 프록시로 전달하는 엔드포인트
//    @PostMapping("/soundcompare")
//    public ResponseEntity<String> soundCompare(
//            @RequestParam("card_name") String cardName,
//            @RequestPart("audio") MultipartFile audioFile) {
//        return ttsService.soundCompare(cardName, audioFile);
//    }
}
