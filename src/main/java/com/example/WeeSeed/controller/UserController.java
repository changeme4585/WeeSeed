package com.example.WeeSeed.controller;

import com.example.WeeSeed.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping(value="/getNokInfo")  //보호자 정보를 가져오는 url
    public  @ResponseBody ResponseEntity getNokInfo(@PathVariable("nokId")String nokId){
        return userService.updateNok(nokId);
    }

    @GetMapping(value = "/getPathInfo") //재활사 정보를
    public  @ResponseBody ResponseEntity getPathInfo(@PathVariable("pathId")String pathId){
        return userService.updatePath(pathId);
    }
}
