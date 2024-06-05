package com.example.WeeSeed.controller;

import com.example.WeeSeed.dto.StatisticDto;
import com.example.WeeSeed.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatisticController {
    private final StatisticService statisticService;
    @GetMapping (value="/personalstatistic")
    public ResponseEntity<List<StatisticDto>> getPersonalStatistic(
            @RequestParam("childCode") String childCode,
            @RequestParam("userId")String userId
    ){
        System.out.println("childCode: "+childCode);
        System.out.println("userId "+userId);
        List<StatisticDto> statisticDto = statisticService.getPersonalStatistic(childCode,userId);
        return new ResponseEntity<>(statisticDto, HttpStatus.OK);
    }
}
