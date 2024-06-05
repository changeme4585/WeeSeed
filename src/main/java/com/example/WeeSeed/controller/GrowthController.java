package com.example.WeeSeed.controller;

import com.example.WeeSeed.dto.GrowthDiaryDto;
import com.example.WeeSeed.dto.GrowthDto;
import com.example.WeeSeed.entity.AacCard;
import com.example.WeeSeed.entity.videoCard;
import com.example.WeeSeed.service.GrowthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GrowthController  {
    private final GrowthService growthService;

    @GetMapping(value = "/test")
    public void we(){
        growthService.test();

        System.out.println("테스트test");
    }


//    @GetMapping (value = "/growth")
//    public String aa(){
//
//        return "1";
//    }

    @PostMapping (value = "/clicklog")
    public void  clicklog(
            @RequestParam("cardId") Long cardId,
            @RequestParam("cardType") String cardType
    ){
        growthService.clicklog(cardId,cardType);
    }

    @GetMapping (value =  "/growth")
    public ResponseEntity<List<GrowthDto>> growth(
            @RequestParam("userId") String userId,
            @RequestParam("childCode") String childCode
    ){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd");
        String formattedDate = now.format(formatter); //현재시간을 String 형으로
        List<GrowthDto> growthDtoList = growthService.getGrowthList(formattedDate,userId);
        return  new ResponseEntity<>(growthDtoList, HttpStatus.OK);
    }
    @PostMapping (value = "/makegrowth")
    public void makegrowth(
            @RequestParam("userId") String userId,
            @RequestParam("childCode") String childCode
            ){

        System.out.println("userId "+userId);
        System.out.println("childCode "+childCode);

        growthService.makeGrowth(userId,childCode);
    }

    @GetMapping (value = "/growthdiary")
    public ResponseEntity<List<GrowthDiaryDto>> getGrowthDiary(
            @RequestParam("userId") String userId,
            @RequestParam("childCode") String childCode){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd");
        String creationTime = now.format(formatter); //현재시간을 String 형으로
        System.out.println("userId :"+ userId);
        System.out.println("childCode :"+ childCode);
        List<GrowthDiaryDto> growthDiaryDto = growthService.getGrowthDiaryDto(userId,childCode);
        return new ResponseEntity<>(growthDiaryDto,HttpStatus.OK);
    }
}
