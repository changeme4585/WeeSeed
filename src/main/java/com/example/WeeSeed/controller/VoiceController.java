package com.example.WeeSeed.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VoiceController {
    @GetMapping ("/checkvoice") //음성 유사도 검사
    public ResponseEntity<String> checkVoice () {

        return new ResponseEntity<>("1.", HttpStatus.OK);
    }
}
