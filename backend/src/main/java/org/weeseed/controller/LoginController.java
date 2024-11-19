package org.weeseed.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.weeseed.PasswordEncryptor;
import org.weeseed.dto.LoginDto;
import org.weeseed.entity.User;
import org.weeseed.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    /**
     * 로그인 처리 메서드
     *
     * @param dto     사용자 로그인 정보
     * @param request HttpServletRequest 객체
     * @return        로그인 결과 메시지 및 상태 코드
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto dto, HttpServletRequest request) {
        String rawPassword = dto.getPassword();  // 사용자 입력 비밀번호
        String encryptedPassword;

        try {
            PasswordEncryptor encryptor = new PasswordEncryptor(rawPassword);
            encryptedPassword = encryptor.getEncryptedPassword();
        } catch (RuntimeException e) {
            // 암호화 과정에서 오류 발생 시
            System.out.println("비밀번호 암호화 오류: " + e.getMessage());
            return new ResponseEntity<>("로그인 실패: 암호화 오류", HttpStatus.UNAUTHORIZED);
        }

        // 로그 확인용 메시지 출력
        System.out.println("로그인 시도 아이디: " + dto.getId() + ", 비밀번호: " + dto.getPassword());

        // 사용자 정보 확인
        List<User> userInfo = userService.checkUserLogIn(dto.getId(), encryptedPassword);

        // 사용자 인증 성공 시
        if (!userInfo.isEmpty()) {
            HttpSession session = request.getSession();
            session.setAttribute("user", dto);

            System.out.println("로그인 성공 - 아이디: " + dto.getId());
            System.out.println("사용자 상태: " + userInfo.get(0).getState());

            if (userInfo.get(0).getState().equals("Nok")) {
                return new ResponseEntity<>("Nok", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Path", HttpStatus.OK);
            }

        } else {
            // 인증 실패 시
            System.out.println("로그인 실패 - 사용자 정보 없음");
            return new ResponseEntity<>("로그인 실패: 잘못된 아이디 또는 비밀번호입니다.", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 로그아웃 처리 메서드
     *
     * @param request HttpServletRequest 객체
     * @return        로그아웃 성공 메시지 및 상태 코드
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);  // 기존 세션이 있는 경우에만 가져옴

        if (session != null) {
            session.invalidate();  // 세션 무효화
            System.out.println("로그아웃 성공");
            return new ResponseEntity<>("로그아웃 성공", HttpStatus.OK);
        } else {
            System.out.println("로그아웃 실패 - 세션 없음");
            return new ResponseEntity<>("로그아웃 실패: 세션이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 세션 유효성 검사 메서드
     *
     * @param request HttpServletRequest 객체
     * @return 세션 유효성 상태 및 메시지
     */
    @PostMapping("/checkSession")
    public ResponseEntity<Map<String, String>> checkSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);  // 기존 세션이 있는지 확인
        Map<String, String> response = new HashMap<>();

        if (session != null && session.getAttribute("user") != null) {
            response.put("status", "valid");  // 세션 유효함
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "invalid");  // 세션이 없거나 유효하지 않음
            System.out.println("세션 유효성 검사 실패 - 세션 없음");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
