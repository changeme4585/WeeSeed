package com.example.WeeSeed.controller;


import com.example.WeeSeed.dto.NokDto;
import com.example.WeeSeed.dto.PathologistDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class modifyUserInfoController {

    @PostMapping (value= "/updateNok")
    public void updateNok(NokDto dto){

    }
    @PostMapping (value = "/updatePath")
    public void updatePath(PathologistDto dto){

    }

}
