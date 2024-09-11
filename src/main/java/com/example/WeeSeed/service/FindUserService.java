package com.example.WeeSeed.service;


import com.example.WeeSeed.Encrypt;
import com.example.WeeSeed.entity.User;
import com.example.WeeSeed.repository.FindUserInfoRepository;
import com.example.WeeSeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FindUserService {
    private  final FindUserInfoRepository findUserInfoRepository;
    private  final UserRepository userRepository;
    public String findUserId(String name,String email){
        return findUserInfoRepository.findUserId(name,email);
    }
    public void  changeUserPass(String userId,String password){
       User  user = userRepository.findByUserId(userId).get();
        Encrypt en = new Encrypt(password);
        String encryptedPassword = en.getEncryptedPassword();
       user.updateUserPass(encryptedPassword);
    }
}
