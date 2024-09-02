package com.example.WeeSeed.service;


import com.example.WeeSeed.entity.User;
import com.example.WeeSeed.repository.FindUserInfoRepository;
import com.example.WeeSeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FindUserService {
    private  final FindUserInfoRepository findUserInfoRepository;
    private  final UserRepository userRepository;
    public String findUserId(String name,String email){
        return findUserInfoRepository.findUserId(name,email);
    }
    public void  changeUserPass(String userId,String password){
       User  user = userRepository.findByUserId(userId).get();
       user.updateUserPass(password);
    }
}
