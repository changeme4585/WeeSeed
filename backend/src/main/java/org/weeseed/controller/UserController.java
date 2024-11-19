package org.weeseed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.weeseed.dto.NokDto;
import org.weeseed.dto.PathologistDto;
import org.weeseed.entity.Pathologist;
import org.weeseed.service.UserService;


@RestController
@RequiredArgsConstructor
@RequestMapping("user")
/*
    GET /user/nok/{nokId}          // 보호자 정보를 가져옴
    GET /user/pathologist/{pathId} // 재활사 정보를 가져옴
    POST /user/resign              // 회원 탈퇴
    POST /user/nok/update          // 보호자 정보 업데이트
    POST /user/pathologist/update   // 재활사 정보 업데이트
*/

public class UserController {
    private final UserService userService;

    /**
     * 보호자 정보를 가져오는 API
     *
     * @param nokId 보호자의 ID
     * @return 보호자 정보 (NokDto)
     */
    @GetMapping(value = "/nok/{nokId}")
    public ResponseEntity<NokDto> getNokInfo(@PathVariable("nokId") String nokId) {
        NokDto nokDto = userService.updateNok(nokId);
        return new ResponseEntity<>(nokDto, HttpStatus.OK);
    }

    /**
     * 재활사 정보를 가져오는 API
     *
     * @param pathId 재활사의 ID
     * @return 재활사 정보 (PathologistDto)
     */
    @GetMapping(value = "/pathologist/{pathId}")
    public ResponseEntity<PathologistDto> getPathInfo(@PathVariable("pathId") String pathId) {
        Pathologist pathologist = userService.updatePathologist(pathId);

        // 재활사 정보를 DTO로 변환
        PathologistDto pathologistDto = PathologistDto.builder()
                .pathologistId(pathologist.getPathologistId())
                .password(pathologist.getPassword())
                .email(pathologist.getEmail())
                .organizationName(pathologist.getOrganizationName())
                .name(pathologist.getName())
                .build();

        return new ResponseEntity<>(pathologistDto, HttpStatus.OK);
    }

    /**
     * 회원 탈퇴 API
     *
     * @param constructorId 회원 ID
     * @param userType      사용자 유형 ("Nok" 또는 "Path")
     */
    @PostMapping(value = "/resign")
    public void resignUser(@RequestParam("constructorId") String constructorId,
                           @RequestParam("state") String userType) {
        userService.removeUser(constructorId);

        // 사용자 유형에 따라 삭제 처리
        if ("Nok".equals(userType)) {
            userService.removeNok(constructorId);
        } else {
            userService.removePathologist(constructorId);
        }
    }
    /**
     * 보호자 정보를 업데이트하는 API
     *
     * @param dto 보호자 정보 DTO
     */
    @PostMapping(value = "/nok/update")
    public ResponseEntity<Void> updateNok(@RequestBody NokDto dto) {
        userService.updateNokInfo(dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 재활사 정보를 업데이트하는 API
     *
     * @param dto 재활사 정보 DTO
     */
    @PostMapping(value = "/pathologits/update")
    public ResponseEntity<Void> updatePath(@RequestBody PathologistDto dto) {
        userService.updatePathologistInfo(dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
