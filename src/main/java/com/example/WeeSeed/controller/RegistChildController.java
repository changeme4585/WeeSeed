package com.example.WeeSeed.controller;


import com.example.WeeSeed.dto.ChildDto;
import com.example.WeeSeed.entity.Child;
import com.example.WeeSeed.service.RegistChildService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@Slf4j
@RestController  //그냥 Controller를 사용하면 상태코드 404를 반환한다.
@RequiredArgsConstructor
public class RegistChildController {
    private final RegistChildService registChildService;

    @PostMapping(value = "/registchild")
    public String registChild(@RequestBody ChildDto dto){
        System.out.println("아동: "+ dto.getName());
        System.out.println("아동: "+ dto.getNokId());
        System.out.println("아동: "+ dto.getDisabilityType());
        //System.out.println("아동: "+ dto.getBirth());
        System.out.println("아동: "+ dto.getGrade());

        registChildService.registChild(dto);
        return "ok";
    }
}
