package org.weeseed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weeseed.PasswordEncryptor;
import org.weeseed.dto.NokDto;
import org.weeseed.dto.PathologistDto;
import org.weeseed.dto.UserDto;
import org.weeseed.entity.DefaultImage;
import org.weeseed.entity.Nok;
import org.weeseed.entity.Pathologist;
import org.weeseed.entity.User;
import org.weeseed.repository.SignInRepository;
import org.weeseed.repository.UserRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SignInService {
    private final SignInRepository signInRepository; // 회원가입 관련 데이터베이스 작업을 수행하는 리포지토리
    private final UserRepository userRepository; // 사용자 관련 데이터베이스 작업을 수행하는 리포지토리

    /**
     * NOK 사용자 등록 메소드
     *
     * @param nokDto NOK 사용자 정보를 담고 있는 DTO
     * @return 등록 성공 여부
     */
    public boolean registerNok(NokDto nokDto) {
        if (isUserIdExists(nokDto.getNokId())) { // 중복된 ID가 있으면 리턴
            return false;
        }

        String encryptedPassword = encryptPassword(nokDto.getPassword()); // 비밀번호 암호화
        Nok nok = Nok.builder()
                .nokId(nokDto.getNokId())
                .password(encryptedPassword)
                .name(nokDto.getName())
                .email(nokDto.getEmail())
                .build();

        signInRepository.registerNok(nok); // NOK 사용자 등록
        return true;
    }

    /**
     * 재활사 등록 메소드
     *
     * @param pathologistDto 재활사 정보를 담고 있는 DTO
     * @return 등록 성공 여부
     */
    public boolean registerPathologist(PathologistDto pathologistDto) {
        if (isUserIdExists(pathologistDto.getPathologistId())) { // 중복된 ID가 있으면 리턴
            return false;
        }

        String encryptedPassword = encryptPassword(pathologistDto.getPassword()); // 비밀번호 암호화
        Pathologist pathologist = Pathologist.builder()
                .pathologistId(pathologistDto.getPathologistId())
                .password(encryptedPassword)
                .name(pathologistDto.getName())
                .email(pathologistDto.getEmail())
                .organizationName(pathologistDto.getOrganizationName())
                .build();

        signInRepository.registerPathologist(pathologist); // 재활사 등록
        return true;
    }

    /**
     * 일반 사용자 등록 메소드
     *
     * @param userDto 사용자 정보를 담고 있는 DTO
     * @return 등록 성공 여부
     */
    public boolean registerUser(UserDto userDto) {
        if (isUserIdExists(userDto.getId())) { // 중복된 ID가 있으면 리턴
            return false;
        }

        String encryptedPassword = encryptPassword(userDto.getPassword()); // 비밀번호 암호화
        User user = User.builder()
                .userId(userDto.getId())
                .password(encryptedPassword)
                .email(userDto.getEmail())
                .state(userDto.getState())
                .name(userDto.getName())
                .build();

        signInRepository.registerUser(user); // 일반 사용자 등록
        return true;
    }

    /**
     * 기본 이미지를 저장하는 메소드
     *
     * @param constructorId 생성자 ID
     */
    public void saveDefaultImage(String constructorId) {
        String[] imageNames = {"dad", "giveme", "hello", "mom", "no", "rice", "sick", "sleep", "teacher", "toilet", "yes"};

        for (String imageName : imageNames) {
            DefaultImage defaultImage = DefaultImage.builder()
                    .constructorId(constructorId)
                    .cardName(imageName)
                    .build();
            signInRepository.saveDefaultImage(defaultImage); // 기본 이미지 저장
        }
    }

    /**
     * 주어진 사용자 ID가 이미 존재하는지 확인하는 메소드
     *
     * @param userId 확인할 사용자 ID
     * @return 중복 여부
     */
    private boolean isUserIdExists(String userId) {
        return userRepository.findByUserId(userId).isPresent(); // 존재하면 true 반환
    }

    /**
     * 비밀번호 암호화 메소드
     *
     * @param password 평문 비밀번호
     * @return 암호화된 비밀번호
     */
    private String encryptPassword(String password) {
        PasswordEncryptor encryptor = new PasswordEncryptor(password); // 비밀번호 암호화 클래스
        return encryptor.getEncryptedPassword(); // 암호화된 비밀번호 반환
    }
}
