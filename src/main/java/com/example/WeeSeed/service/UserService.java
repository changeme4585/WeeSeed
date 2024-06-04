package com.example.WeeSeed.service;


import com.example.WeeSeed.dto.NokDto;
import com.example.WeeSeed.dto.PathologistDto;
import com.example.WeeSeed.entity.Nok;
import com.example.WeeSeed.entity.Pathologist;
import com.example.WeeSeed.entity.User;
import com.example.WeeSeed.repository.UserInfoRepository;
import com.example.WeeSeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserInfoRepository userRepository ;
    public List<User> checkUserLogIn(String userID,String password){
        return  userRepository.checkUserLogIn(userID,password);
    }

    public  String getUserInfo(String userId){
        return userRepository.getUserState(userId);
    }
    public  NokDto updateNok(String nokId){

        Nok nokInfo = userRepository.getNokInfo(nokId);
        NokDto nokDto = NokDto.builder().
                nokId(nokInfo.getNokId()).
                password(nokInfo.getPassword()).
                email(nokInfo.getEmail()).
                name(nokInfo.getName()).
                build();

       return nokDto;
    }

    public Pathologist updatePath(String pathId){

        return userRepository.getPathInfo(pathId);
    }
}
