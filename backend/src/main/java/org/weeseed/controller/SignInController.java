package org.weeseed.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.weeseed.dto.NokDto;
import org.weeseed.dto.PathologistDto;
import org.weeseed.dto.UserDto;
import org.weeseed.service.SignInService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SignInController {
    private final SignInService signInService; // SignInService 인스턴스
    private static final Logger logger = LoggerFactory.getLogger(SignInController.class);


    /**
     * 사용자 정보를 담은 UserDto 객체를 생성하는 메소드
     *
     * @param id 사용자 ID
     * @param password 비밀번호
     * @param email 사용자 이메일
     * @param state 사용자 상태
     * @param name 사용자 이름
     * @return 생성된 UserDto 객체
     */
    private UserDto createUserInfo(String id, String password, String email, String state, String name) {
        return UserDto.builder()
                .id(id)
                .password(password)
                .email(email)
                .state(state)
                .name(name)
                .build();
    }

    /**
     * 비밀번호 유효성을 검사하는 메소드
     *
     * @param password 검증할 비밀번호
     * @return 비밀번호 유효성 상태 코드
     */
    private int validatePassword(String password) {
        // 비밀번호 길이 체크
        if (password.length() < 10)
            return 2;

        boolean hasDigit = false; // 숫자 포함 여부
        boolean hasLetter = false; // 문자 포함 여부

        // 비밀번호에서 숫자와 문자의 존재 여부를 체크
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) hasDigit = true;
            else if (Character.isLetter(c)) hasLetter = true;

            // 숫자와 문자가 모두 포함되었으면 조기 종료
            if (hasDigit && hasLetter)
                return 0; // 유효성 통과
        }

        return (hasDigit || hasLetter) ? 1 : 0; // 둘 중 하나만 포함된 경우 유효성 불합격
    }


    /**
     * NOK 사용자 로그인 메소드
     *
     * @param nokDto NOK 사용자 정보를 담고 있는 DTO
     * @return 응답 메시지
     */
    @PostMapping(value = "/nokSignIn")
    public String nokSignIn(@RequestBody NokDto nokDto) {
        logger.info("NOK 사용자 로그인 요청: {}", nokDto);

        int passwordValidationResult = validatePassword(nokDto.getPassword());

        // 비밀번호 유효성 검사 결과에 따른 메시지 반환
        if (passwordValidationResult == 1) {
            logger.warn("비밀번호 유효성 검사 실패: 숫자와 문자 모두 포함되어야 합니다.");
            return "비밀번호에 숫자와 문자가 모두 포함되어야 합니다.";
        }
        if (passwordValidationResult == 2) {
            logger.warn("비밀번호 유효성 검사 실패: 길이가 10자 이상이어야 합니다.");
            return "비밀번호 길이를 10자 이상으로 설정하세요.";
        }

        // 중복 ID 체크 및 등록 처리
        if (!signInService.registerNok(nokDto)) {
            logger.warn("중복된 ID 시도: {}", nokDto.getNokId());
            return "중복된 ID입니다.";
        }

        // 사용자 정보 등록
        signInService.registerUser(createUserInfo(nokDto.getNokId(), nokDto.getPassword(), nokDto.getEmail(), "Nok", nokDto.getName()));
        signInService.saveDefaultImage(nokDto.getNokId());
        logger.info("NOK 사용자 등록 성공: {}", nokDto.getNokId());

        return "등록 성공";
    }


    /**
     * 재활사 로그인 메소드
     *
     * @param pathologistDto 재활사 정보를 담고 있는 DTO
     * @return 응답 메시지
     */
    @PostMapping(value = "/pathSignIn")
    public String pathSignIn(@RequestBody PathologistDto pathologistDto) {
        log.info("재활사 회원가입: {}", pathologistDto.getPathologistId());

        int passwordValidationResult = validatePassword(pathologistDto.getPassword());

        // 비밀번호 유효성 검사 결과에 따른 메시지 반환
        if (passwordValidationResult == 1)
            return "비밀번호에 숫자와 문자가 모두 포함되어야 합니다.";
        if (passwordValidationResult == 2)
            return "비밀번호 길이를 10자 이상으로 설정하세요.";

        // 중복 ID 체크 및 등록 처리
        if (!signInService.registerPathologist(pathologistDto))
            return "중복된 ID입니다.";

        // 사용자 정보 등록
        signInService.registerUser(createUserInfo(pathologistDto.getPathologistId(), pathologistDto.getPassword(), pathologistDto.getEmail(), "Pathologist", pathologistDto.getName()));
        signInService.saveDefaultImage(pathologistDto.getPathologistId());

        return "등록 성공";
    }
}
