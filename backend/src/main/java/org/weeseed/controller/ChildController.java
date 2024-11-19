package org.weeseed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.weeseed.entity.Child;
import org.weeseed.service.ChildService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChildController {

    @Autowired
    private final ChildService childService;

    /**
     * 보호자 계정에 등록된 아동 정보를 조회
     *
     * @param nokId 보호자ID
     * @return 아동 정보 목록과 HTTP 상태 코드 200
     */
    @GetMapping("/NokChildInfo/{nokId}")
    public ResponseEntity<List<Child>> getNokChildInfo(@PathVariable("nokId") String nokId) {
        List<Child> children = childService.getChildByUser(nokId);
        return new ResponseEntity<>(children, HttpStatus.OK);
    }

    /**
     * 재활사 계정에 등록된 아동 정보를 조회
     *
     * @param pathId 재활사ID
     * @return 아동 정보 목록과 HTTP 상태 코드 200
     */
    @GetMapping("/PathChildInfo/{pathId}")
    public ResponseEntity<List<Child>> getPathologistChildInfo(@PathVariable("pathId") String pathId) {
        List<Child> children = childService.getChildByUser(pathId);
        return new ResponseEntity<>(children, HttpStatus.OK);
    }
}