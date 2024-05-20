package com.example.WeeSeed.service;

import com.example.WeeSeed.Encrypt;
import com.example.WeeSeed.dto.SignInDto;
import com.example.WeeSeed.dto.UserDto;
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
                build();
        repository.registUser(user);
        return true;
    }
}
