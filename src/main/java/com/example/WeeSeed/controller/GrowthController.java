package com.example.WeeSeed.controller;

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
public class GrowthController implements HandlerInterceptor {
    private final GrowthService growthService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        String requestUrl = request.getRequestURI();
        System.out.println("URL: " + requestUrl + " | Time Taken: " + duration + " ms");
    }

    @GetMapping (value = "/growth")
    public String aa(){

        return "1";
    }

    @PostMapping (value = "/clicklog")
    public void  clicklog(
            @RequestParam("cardId") Long cardId,
            @RequestParam("cardType") String cardType
    ){
        growthService.clicklog(cardId,cardType);
    }

    @GetMapping (value =  "/growth")
    public ResponseEntity<List<GrowthDto>> growth(
            @RequestParam("userId") String userId
    ){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd");
        String formattedDate = now.format(formatter); //현재시간을 String 형으로
        List<GrowthDto> growthDtoList = growthService.getGrowthList(formattedDate,userId);
        return  new ResponseEntity<>(growthDtoList, HttpStatus.OK);
    }

}
