package com.example.WeeSeed.controller;

import com.example.WeeSeed.dto.NokDto;
import com.example.WeeSeed.dto.PathologistDto;
import com.example.WeeSeed.entity.Pathologist;
import com.example.WeeSeed.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping(value="/getNokInfo/{nokId}")  //보호자 정보를 가져오는 url
    public  @ResponseBody ResponseEntity<NokDto> getNokInfo(@PathVariable("nokId")String nokId){
        NokDto nokDto = userService.updateNok(nokId);
        return new ResponseEntity<>(nokDto,HttpStatus.OK);
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


    //회원탈퇴 컨트롤러
    @PostMapping (value =  "/resign-user")
    public  void resignUser(@RequestParam("constructorId")String constructorId,
                            @RequestParam("state")String state){ //state는 재활사인지 보호자인지(Nok 또는 Path 둘 중 하나를 전송)
            userService.removeUser(constructorId);
            if(state.equals("Nok")){
                userService.removeNok(constructorId);
            }else{
                userService.removePathologist(constructorId);
            }
    }

}
