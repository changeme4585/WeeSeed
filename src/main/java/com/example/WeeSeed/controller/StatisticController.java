package com.example.WeeSeed.controller;

import com.example.WeeSeed.dto.Statistic.AgeDto;
import com.example.WeeSeed.dto.Statistic.GenderDto;
import com.example.WeeSeed.dto.StatisticDto;
import com.example.WeeSeed.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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

    @GetMapping (value = "/genderstatistic")
    public ResponseEntity<GenderDto>  genderStatistic ()
    {
        GenderDto genderDto =statisticService.genderStatistic();
        return new ResponseEntity<>(genderDto,HttpStatus.OK);
    }


    @GetMapping (value = "/agestatistic")
    public ResponseEntity<AgeDto> ageStatistic(){
        AgeDto ageDto =  statisticService.
        return new ResponseEntity<>(genderDto,HttpStatus.OK);
    }

//    @GetMapping (value =  "/agestatistic")
//    public ResponseEntity<AgeDto> ageStatistice(){
//        AgeDto ageDto = statisticService.
//        return new ResponseEntity<>(AgeDto,HttpStatus.OK);
//    }


}
