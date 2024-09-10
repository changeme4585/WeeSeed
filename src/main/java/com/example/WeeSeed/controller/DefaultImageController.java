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

    //기본 카드를 불러오는 컨트롤러
    @GetMapping(value = "/get-default-card")
    public ResponseEntity<List<DefaultImageDto>> getDefualtCard (@RequestParam("constructorId")String constructorId) throws IOException {
        return new ResponseEntity<>(defaultImageService.getUserDefaultImageList(constructorId), HttpStatus.OK);
    }

    //기본 카드를 삭제하는 컨트롤러
    @PostMapping (value = "/delete-default-card")
    public void deleteDefaultCard(@RequestParam("constructorId")String constructorId,
                                    @RequestParam("cardName") String cardName){

        defaultImageService.deleteDefaultImage(constructorId,cardName);
    }
    //기본 카드의 제목을 변경하는 컨트롤러
    @PostMapping (value = "/update-default-card")
    public void updateDefaultCard(@RequestParam("constructorId")String constructorId,
                                  @RequestParam("cardName") String cardName,
                                  @RequestParam("newCardName") String newCardName ){

    }

}
