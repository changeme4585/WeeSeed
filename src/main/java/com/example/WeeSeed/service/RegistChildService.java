package com.example.WeeSeed.service;


import com.example.WeeSeed.dto.ChildDto;
import com.example.WeeSeed.entity.Child;
import com.example.WeeSeed.repository.SignInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class RegistChildService {
    private  final SignInRepository signInRepository;
    public  String generateChildCode() {
        int length = 5;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            result.append(characters.charAt(index));
        }

        return result.toString();
    }

    public void registChild(ChildDto dto){
        Child child = Child.builder().
                childCode(generateChildCode()).
                nokId(dto.getNokId()).
                disabilityType(dto.getDisabilityType()).
                gender(dto.getGender()).
                birth(dto.getBirth()).
                grade(dto.getGrade()).
                name(dto.getName()).
                build();
        signInRepository.registChild(child);
    }
}
