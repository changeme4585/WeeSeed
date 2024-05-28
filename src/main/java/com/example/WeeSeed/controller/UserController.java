package com.example.WeeSeed.controller;

import com.example.WeeSeed.dto.PathologistDto;
import com.example.WeeSeed.entity.Pathologist;
import com.example.WeeSeed.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping(value="/getNokInfo/{nokId}")  //보호자 정보를 가져오는 url
    public  @ResponseBody ResponseEntity getNokInfo(@PathVariable("nokId")String nokId){
        return userService.updateNok(nokId);
    }

    @GetMapping(value = "/getPathInfo/{pathId}") //재활사 정보를
    public  @ResponseBody ResponseEntity<PathologistDto> getPathInfo(@PathVariable("pathId")String pathId){


        Pathologist pathInfo = userService.updatePath(pathId) ;
        System.out.println("재활사 정보 가져오기"+ pathInfo.getEmail()+"d "+pathInfo.getOrganizationName());
        PathologistDto pathDto = PathologistDto.
                builder().
                pId(pathInfo.getPathologistId()).
                password(pathInfo.getPassword()).
                email(pathInfo.getEmail()).
                oName(pathInfo.getOrganizationName()).
                name(pathInfo.getName()).
                build();
        System.out.println(pathDto.getEmail()+pathDto.getOrganizationName()+pathDto.getPassword());
        return new ResponseEntity<>(pathDto, HttpStatus.OK);
    }


}
