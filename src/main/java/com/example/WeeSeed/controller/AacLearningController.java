package com.example.WeeSeed.controller;


import com.example.WeeSeed.dto.AacDto;
import com.example.WeeSeed.dto.ExtendedCardDto;
import com.example.WeeSeed.entity.AacCard;
import com.example.WeeSeed.entity.ExtendedCard;
//import com.example.WeeSeed.repository.ExtendedCardRepository;
import com.example.WeeSeed.service.AacLearningService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class AacLearningController {
    private final AacLearningService aaclearningService;

    @PostMapping("/findSimilars")
    public void findSimilarImage(@RequestParam("cardId") Long cardId, @RequestParam("childCode") String childCode
    ) {
        System.out.println("아동코드 :" + childCode);
        try {
            System.out.println("ok");
            aaclearningService.findSimilarImage(cardId, childCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


