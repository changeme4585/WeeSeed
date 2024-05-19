package com.example.WeeSeed.controller;

import com.example.WeeSeed.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class LogInController {
    @PostMapping("/login")
    public String login(@RequestBody UserDto user, HttpServletRequest request) {

        if ("user".equals(user.getId()) && "password".equals(user.getPassword())) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            System.out.println("로그인 한 아이디 : "+user.getId()+", 비번: "+user.getPassword());
            return "로그인 성공";
        } else {
            return "Login failed";
        }
    }

    @PostMapping("/checkSession")
    public String checkSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            return "Session is valid";
        } else {
            return "No valid session";
        }
    }

}
