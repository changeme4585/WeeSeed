package com.example.WeeSeed.controller;


import com.example.WeeSeed.dto.SignInDto;
import com.example.WeeSeed.dto.UserDto;
import com.example.WeeSeed.service.SignInService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class SignInController {
    private final SignInService service;
//    private boolean validateUser(String id,String password) {
//
//    }
    @PostMapping("/signIn")
    public String receiveMessage(@RequestBody UserDto dto) {
        service.registUser(dto);
        System.out.println("Received message: " + dto.getId());
        System.out.println("Received message: " + dto.getPassword());
        System.out.println("Received message: " + dto.getState());
        System.out.println("Received message: " + dto.getEmail());
        return "Message received";
    }
//    @PostMapping(value = "/signIn")
//    public String signIn()
}
