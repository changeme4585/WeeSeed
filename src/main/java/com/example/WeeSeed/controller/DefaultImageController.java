package com.example.WeeSeed.controller;

import com.example.WeeSeed.dto.DefaultImageDto;
import com.example.WeeSeed.service.DefaultImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DefaultImageController {

    private  final DefaultImageService defaultImageService;

    //기본 이미지를 불러오는 로직
    @GetMapping(value = "/get-default-image")
    public ResponseEntity<List<DefaultImageDto>> getDefualtImage (@RequestParam("constructorId")String constructorId) throws IOException {
        return new ResponseEntity<>(defaultImageService.getUserDefaultImageList(constructorId), HttpStatus.OK);
    }


}
