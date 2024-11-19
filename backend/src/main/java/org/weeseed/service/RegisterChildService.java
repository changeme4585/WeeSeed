package org.weeseed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weeseed.dto.ChildDto;
import org.weeseed.entity.Child;
import org.weeseed.repository.ChildRepository;
import org.weeseed.repository.SignInRepository;

import java.util.List;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterChildService {
    private final SignInRepository signInRepository;
    private final ChildRepository childRepository;

    // 아동 코드를 생성하는 메소드
    public String generateChildCode() {
        int length = 5; // 생성할 코드 길이
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            result.append(characters.charAt(index)); // 랜덤 문자 추가
        }

        return result.toString();
    }

    // 아동 등록 메소드
    public void registerChild(ChildDto dto) {
        String childCode = generateChildCode();

        // 중복된 아동 코드가 있을 경우 재생성
        while (childRepository.checkChildDuplicate(childCode) != 0) {
            childCode = generateChildCode();
        }

        // 아동 객체 생성
        Child child = Child.builder()
                .childCode(childCode)
                .userId(dto.getUserId())
                .disabilityType(dto.getDisabilityType())
                .gender(dto.getGender())
                .birth(dto.getBirth())
                .grade(dto.getGrade())
                .name(dto.getName())
                .build();

        signInRepository.registerChild(child);
    }

    // 아동과 사용자 연동 메소드
    public void linkChild(String childCode, String userId) {
        // childCode로 아동 목록 조회
        List<Child> childList = childRepository.getChilds(childCode);
        Child child = childList.get(0);

        // 아동 정보에 사용자 ID 추가
        Child linkedChild = Child.builder()
                .childCode(child.getChildCode())
                .userId(userId)
                .disabilityType(child.getDisabilityType())
                .gender(child.getGender())
                .birth(child.getBirth())
                .grade(child.getGrade())
                .name(child.getName())
                .build();

        signInRepository.registerChild(linkedChild);
    }
}
