package com.example.WeeSeed.controller;

import com.example.WeeSeed.Encrypt;
import com.example.WeeSeed.dto.LoginDto;
import com.example.WeeSeed.dto.UserDto;
import com.example.WeeSeed.entity.User;
import com.example.WeeSeed.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController //그냥 Controller를 사용하면 상태코드 404를 반환한다.
@RequiredArgsConstructor
public class LogInController {
    private final UserService userService;


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto dto, HttpServletRequest request) {
        String pwd  = dto.getPassword();
        Encrypt en = new Encrypt(pwd);
        String encryptedPassword = en.getEncryptedPassword();
        System.out.println("로그인 한 아이디 : "+dto.getId()+", 비번: "+dto.getPassword());
        List<User> userInfo = userService.checkUserLogIn(dto.getId(), encryptedPassword);
        if (userInfo.size()!=0) {
            HttpSession session = request.getSession();
            session.setAttribute("user", dto);
            System.out.println("로그인 한 아이디 : "+dto.getId()+", 비번: "+dto.getPassword());
            if(userInfo.get(0).getState().equals("NOK")){
                return new ResponseEntity<>("Nok", HttpStatus.OK);
            }
            // Path
            return new ResponseEntity<>("Path", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("failed", HttpStatus.UNAUTHORIZED);
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 기존 세션이 있는 경우에만 가져옴
        if (session != null) {
            session.invalidate(); // 세션 무효화
            System.out.println("로그아웃 성공");
            return new ResponseEntity<>("로그아웃 성공",HttpStatus.OK);
        } else {
            return new ResponseEntity<>("failed", HttpStatus.UNAUTHORIZED);
        }
    }



    @PostMapping("/checkSession")
    public ResponseEntity<Map<String, String>> checkSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 기존 세션이 있는지 확인
        Map<String, String> response = new HashMap<>();

        if (session != null && session.getAttribute("user") != null) {
            response.put("status", "valid");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "invalid");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
