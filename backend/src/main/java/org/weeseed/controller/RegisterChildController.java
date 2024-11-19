package org.weeseed.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.weeseed.dto.ChildDto;
import org.weeseed.service.RegisterChildService;

/**
 * 아동 등록 및 연동을 처리하는 컨트롤러
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/child")
/*
    POST /child/register
    POST /child/link
 */
public class RegisterChildController {
    private final RegisterChildService registerChildService;

    /**
     * 보호자가 아동을 등록하는 메서드
     *
     * @param dto 아동 등록에 필요한 정보
     * @return 성공 메시지
     */
    @PostMapping(value = "/register")
    public String registerChild(@RequestBody ChildDto dto) {
        logChildInfo(dto);
        registerChildService.registerChild(dto); // 아동 등록 서비스 호출
        return "ok"; // 등록 성공 메시지 반환
    }

    /**
     * 재활사가 아동과 연동하는 메서드
     *
     * @param childCode 아동 코드
     * @param userId    사용자 ID
     * @return 성공 메시지
     */
    @PostMapping(value = "/link")
    public String linkChild(@RequestParam("childCode") String childCode, @RequestParam("userId") String userId) {
        // 사용자 ID 로그 출력
        System.out.println("사용자 ID: " + userId);
        registerChildService.linkChild(childCode, userId); // 아동 연동 서비스 호출
        return "ok"; // 연동 성공 메시지 반환
    }

    /**
     * 아동 정보를 로그에 출력하는 메서드
     *
     * @param dto 아동 정보
     */
    private void logChildInfo(ChildDto dto) {
        System.out.println("아동 이름: " + dto.getName());
        System.out.println("사용자 ID: " + dto.getUserId());
        System.out.println("장애 유형: " + dto.getDisabilityType());
        System.out.println("학년: " + dto.getGrade());
    }
}
