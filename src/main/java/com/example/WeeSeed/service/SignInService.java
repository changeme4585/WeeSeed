package com.example.WeeSeed.service;

import com.example.WeeSeed.Encrypt;
import com.example.WeeSeed.dto.NokDto;
import com.example.WeeSeed.dto.PathologistDto;
import com.example.WeeSeed.dto.SignInDto;
import com.example.WeeSeed.dto.UserDto;
import com.example.WeeSeed.entity.DefaultImage;
import com.example.WeeSeed.entity.Nok;
import com.example.WeeSeed.entity.Pathologist;
import com.example.WeeSeed.entity.User;
import com.example.WeeSeed.repository.SignInRepository;
import com.example.WeeSeed.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SignInService {
    private final SignInRepository repository;
    private final UserRepository userRepository;
    public User createUser(Object userDto){
        User user = User.builder().build();
        return user;
    }

    public boolean registNok(NokDto userDto){
        Optional<User> userInfo = userRepository.findByUserId(userDto.getNokId());
        if(userInfo.isPresent()){ // 중복된 id가 있으면 리턴
            System.out.println("중복된 id 입니다.");
            return false;
        }
        String pwd  = userDto.getPassword();
        Encrypt en = new Encrypt(pwd);
        String encryptedPassword = en.getEncryptedPassword();
        Nok nok = Nok.builder().
                nokId(userDto.getNokId()).
                password(encryptedPassword).
                name(userDto.getName()).
                email(userDto.getEmail()).
                build();
        User user = createUser(userDto);
        //repository.registUser(user);
        repository.registNok(nok);
        return true;
    }
    public  boolean registPath(PathologistDto userDto){
        System.out.println("service로직 들어옴");
        Optional<User> userInfo = userRepository.findByUserId(userDto.getPathologistId());
        if(userInfo.isPresent()){ // 중복된 id가 있으면 리턴
            System.out.println("중복된 id 입니다.");
            return false;
        }
        String pwd  = userDto.getPassword();
        Encrypt en = new Encrypt(pwd);
        String encryptedPassword = en.getEncryptedPassword();
        Pathologist pathologist = Pathologist.builder().
                pathologistId(userDto.getPathologistId()).
                password(encryptedPassword).
                name(userDto.getName()).
                email(userDto.getEmail()).
                organizationName(userDto.getOrganizationName()).
                build();
        //User user = createUser(userDto);
        //repository.registUser(user);
        repository.registPath(pathologist);
        return true;
    }
    public boolean registUser(UserDto userDto){
        Optional<User> userInfo = userRepository.findByUserId(userDto.getId());
        if(userInfo.isPresent()){ // 중복된 id가 있으면 리턴
            System.out.println("중복된 id 입니다.");
            return false;
        }
        String pwd  = userDto.getPassword();
        Encrypt en = new Encrypt(pwd);
        String encryptedPassword = en.getEncryptedPassword();
        User user = User.builder().
                userId(userDto.getId()).
                password(encryptedPassword).
                email(userDto.getEmail()).
                state(userDto.getState()).
                name(userDto.getName()).
                build();
        repository.registUser(user);

        System.out.println("userid: "+userDto.getId());
        System.out.println("password: "+userDto.getPassword());
        System.out.println("email: "+userDto.getEmail());
        System.out.println("state: "+userDto.getState());
        return true;
    }

    public  void saveDefaultImage(String constructorId){
        String imageNameList[] ={"dad","giveme","hello","mom","no","rice","sick","sleep","teacher","toilet","yes"};
        for(String imageName : imageNameList){
            DefaultImage defaultImage = DefaultImage.builder().
                    constructorId(constructorId).
                    cardName(imageName).
                    build();
            repository.saveDefaultImage(defaultImage);
        }
    }
}
