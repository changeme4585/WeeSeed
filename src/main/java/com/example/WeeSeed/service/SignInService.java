package com.example.WeeSeed.service;

import com.example.WeeSeed.dto.SignInDto;
import com.example.WeeSeed.dto.UserDto;
import com.example.WeeSeed.entity.User;
import com.example.WeeSeed.repository.SignInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SignInService {
    private final SignInRepository repository;
    public void registUser(UserDto userDto){
        User user = User.builder().
                userId(userDto.getId()).
                password(userDto.getPassword()).
                email(userDto.getEmail()).
                state(userDto.getState()).
                build();
        repository.registUser(user);
    }
}
