package org.weeseed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weeseed.PasswordEncryptor;
import org.weeseed.entity.User;
import org.weeseed.repository.FindUserInfoRepository;
import org.weeseed.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FindUserService {
    private final FindUserInfoRepository findUserInfoRepository; // 사용자 정보 조회 리포지토리
    private final UserRepository userRepository; // 사용자 리포지토리

    /**
     * 사용자 이름과 이메일을 기반으로 사용자 ID를 찾기
     * @param name 사용자 이름
     * @param email 사용자 이메일
     * @return 사용자 ID
     */
    public String findUserId(String name, String email) {
        return findUserInfoRepository.findUserId(name, email);
    }

    /**
     * 주어진 사용자 ID에 대해 비밀번호를 변경
     * 비밀번호는 암호화하여 저장
     * @param userId 사용자 ID
     * @param password 새 비밀번호
     */
    public void changeUserPass(String userId, String password) {
        Optional<User> userOptional = userRepository.findByUserId(userId);

        if (userOptional.isPresent()) { // 사용자가 존재하는 경우
            User user = userOptional.get();
            String encryptedPassword = new PasswordEncryptor(password).getEncryptedPassword(); // 비밀번호 암호화
            user.updateUserPass(encryptedPassword); // 비밀번호 업데이트
        } else {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다."); // 사용자 미존재 시 예외 발생
        }
    }
}
