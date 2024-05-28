package com.example.WeeSeed.controller;


import com.example.WeeSeed.dto.NokDto;
import com.example.WeeSeed.dto.PathologistDto;
import com.example.WeeSeed.dto.SignInDto;
import com.example.WeeSeed.dto.UserDto;
import com.example.WeeSeed.service.SignInService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController  //그냥 Controller를 사용하면 상태코드 404를 반환한다.
@RequiredArgsConstructor
public class SignInController {
    private final SignInService service;

    public  UserDto createUserInfo(String id,String password,String email,String state,String name){
        UserDto userDto = UserDto.builder().
                id(id).
                password(password).
                email(email).
                state(state).
                name(name).
                build();
                return userDto;
    }
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
        if (numFlag == 0  ||strFlag == 0){ //비밀번호가 숫자 문자 둘다 포함하지 않으면 위반
            return 1;
        }
        return 0;
    }

//    @PostMapping(value ="/idCheck")
//    public String idCheck(@RequestBody IdDto){
//
//        return
//    }
    @PostMapping(value = "/nokSignIn")
    public String nokSignIn(@RequestBody NokDto dto) {
        int passFlag = validatePassword(dto.getPassword());
        if(passFlag == 1 ){
            return "비밀번호에 숫자 문자가 모두 포함되어야 합니다.";
        }
        if (passFlag == 2){
            return "비밀번호 길이를 10이상으로 만드세요.";
        }

        if (!service.registNok(dto)){
            return "중복된 ID 입니다.";
        }
        service.registUser(
                createUserInfo(dto.getNokId(),dto.getPassword(), dto.getEmail(),"Nok" ,dto.getName())
        );
        return "yes";
    }
//    @PostMapping(value = "/signIn")
//    public String SignIn(@RequestBody UserDto dto) {
//        int passFlag = validatePassword(dto.getPassword());
//        if(passFlag == 1 ){
//            return "비밀번호에 숫자 문자가 모두 포함되어야 합니다.";
//        }
//        if (passFlag == 2){
//            return "비밀번호 길이를 10이상으로 만드세요.";
//        }
//        if (!service.registUser(dto)){
//            return "중복된 ID 입니다.";
//        }
//
//        return "yes";
//    }
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/pathSignIn")
    public String pathSignIn(@RequestBody PathologistDto dto) {
        System.out.println("재활사 회원가입: "+ dto.getPathologistId());
        System.out.println("재활사 회원가입: "+ dto.getPassword());
        System.out.println("재활사 회원가입: "+ dto.getName());
        System.out.println("재활사 회원가입: "+ dto.getEmail());
        System.out.println("재활사 회원가입: "+ dto.getOrganizationName());
        int passFlag = validatePassword(dto.getPassword());
        if(passFlag == 1 ){
            return "비밀번호에 숫자 문자가 모두 포함되어야 합니다.";
        }
        if (passFlag == 2){
            return "비밀번호 길이를 10이상으로 만드세요.";
        }
        if(!service.registPath(dto)){
            return "중복된 ID 입니다.";
        }

        service.registUser(
                createUserInfo(dto.getPathologistId(), dto.getPassword(),dto.getEmail(),"Pathologist",dto.getName())
        );

        return "yes";
    }
}
