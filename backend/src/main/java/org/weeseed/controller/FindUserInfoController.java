package org.weeseed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.weeseed.service.FindUserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")

/*
    GET /user/find-id?
    POST /user/change-password?
 */
public class FindUserInfoController {

    private final FindUserService findUserService;

    /**
     * 사용자 이름과 이메일을 기반으로 사용자 ID 찾기
     */
    @GetMapping("/find-id")
    public ResponseEntity<String> findUserId(@RequestParam("name") String name,
                                             @RequestParam("email") String email) {
        String userId = findUserService.findUserId(name, email);
        return new ResponseEntity<>(userId, HttpStatus.OK);
    }

    /**
     * 사용자 비밀번호 변경
     */
    @PostMapping("/change-password")
    public ResponseEntity<Void> changeUserPass(@RequestParam("userId") String userId,
                                               @RequestParam("password") String password) {
        findUserService.changeUserPass(userId, password);
        return new ResponseEntity<>(HttpStatus.OK); // 비밀번호 변경 후 OK 반환
    }
}
