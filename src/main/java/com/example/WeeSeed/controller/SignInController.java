package com.example.WeeSeed.controller;


import com.example.WeeSeed.dto.SignInDto;
import com.example.WeeSeed.dto.UserDto;
import com.example.WeeSeed.service.SignInService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SignInController {
    private final SignInService service;
    public int  validatePassword(String password){
        if(password.length()<10){ //문자열이 10미만이면 위반
            return 2;
        }
        int numFlag = 0;
        int strFlag = 0;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                numFlag = 1;
                continue;
            }
                strFlag = 1;
        }
        if (numFlag != 1 && strFlag != 1){ //비밀번호가 숫자 문자 둘다 포함하지 않으면 위반
            return 1;
        }
        return 0;
    }
    @PostMapping(value = "/signIn")
    public String receiveMessage(@RequestBody UserDto dto) {
        int passFlag = validatePassword(dto.getPassword());
        if(passFlag == 1 ){
            return "비밀번호에 숫자 문자가 모두 포함되어야 합니다.";
        }
        if (passFlag == 2){
            return "비밀번호 길이를 10이상으로 만드세요.";
        }
        if (!service.registUser(dto)){
            return "중복된 ID 입니다.";
        }

        System.out.println("Received message: " + dto.getId());
        System.out.println("Received message: " + dto.getPassword());
        System.out.println("Received message: " + dto.getState());
        System.out.println("Received message: " + dto.getEmail());
        return "yes";
    }

}
