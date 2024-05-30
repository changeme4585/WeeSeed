package com.example.WeeSeed.controller;


import com.example.WeeSeed.dto.ChildDto;
import com.example.WeeSeed.entity.Child;
import com.example.WeeSeed.service.RegistChildService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@Slf4j
@RestController  //그냥 Controller를 사용하면 상태코드 404를 반환한다.
@RequiredArgsConstructor
public class RegistChildController {
    private final RegistChildService registChildService;

    @PostMapping(value = "/registchild") //보호자가 아동추가
    public String registChild(@RequestBody ChildDto dto){
        System.out.println("아동: "+ dto.getName());
        System.out.println("아동: "+ dto.getUserId());
        System.out.println("아동: "+ dto.getDisabilityType());
        //System.out.println("아동: "+ dto.getBirth());
        System.out.println("아동: "+ dto.getGrade());
        registChildService.registChild(dto);
        return "ok";
    }
    @PostMapping(value = "/linkchild") //재활사가 아동연동
    public String linkChild(@RequestParam("childCode") String childCode,@RequestParam("userId") String userId){
//        Child child = registChildService.getChild(childCode);
//        ChildDto childDto = ChildDto.builder().
//
//                build();
//        registChildService.registChild(childDto);
        System.out.println("userId: "+ userId);
        registChildService.linkChild(childCode,userId);
        return "ok";
    }
}
