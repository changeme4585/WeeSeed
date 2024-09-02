package com.example.WeeSeed.service;


import com.example.WeeSeed.repository.FindUserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindUserService {
    private  final FindUserInfoRepository findUserInfoRepository;

    public String findUserId(String name,String email){
        return findUserInfoRepository.findUserId(name,email);
    }
}
