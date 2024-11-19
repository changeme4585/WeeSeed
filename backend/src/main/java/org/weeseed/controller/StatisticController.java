package org.weeseed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.weeseed.dto.Statistic.AgeDto;
import org.weeseed.dto.Statistic.GenderDto;
import org.weeseed.dto.Statistic.GradeDto;
import org.weeseed.dto.Statistic.TypeDto;
import org.weeseed.dto.StatisticDto;
import org.weeseed.service.StatisticService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statistics")
/*
    GET /statistics/personal
    GET /statistics/gender
    GET /statistics/age
    GET /statistics/disabilityType
    GET /statistics/disabilityGrade
 */
public class StatisticController {
    private final StatisticService statisticService;

    /**
     * 개인 통계를 가져오는 API
     *
     * @param childCode 아동 코드
     * @param userId    사용자 ID
     * @return 개인 통계 데이터
     */
    @GetMapping("/personal")
    public ResponseEntity<List<StatisticDto>> getPersonalStatistics(
            @RequestParam("childCode") String childCode,
            @RequestParam("userId") String userId) {
        List<StatisticDto> statistics = statisticService.getPersonalStatistic(childCode, userId);
        return ResponseEntity.ok(statistics);
    }

    /**
     * 성별 통계를 가져오는 API
     *
     * @return 성별 통계 데이터
     */
    @GetMapping("/gender")
    public ResponseEntity<GenderDto> getGenderStatistics() {
        GenderDto genderStatistics = statisticService.genderStatistic();
        return ResponseEntity.ok(genderStatistics);
    }

    /**
     * 연령 통계를 가져오는 API
     *
     * @return 연령 통계 데이터
     */
    @GetMapping("/age")
    public ResponseEntity<AgeDto> getAgeStatistics() {
        AgeDto ageStatistics = statisticService.ageStatistic();
        return ResponseEntity.ok(ageStatistics);
    }

    /**
     * 장애 유형 통계를 가져오는 API
     *
     * @return 장애 유형 통계 데이터
     */
    @GetMapping("/disabilityType")
    public ResponseEntity<TypeDto> getDisabilityTypeStatistics() {
        TypeDto disabilityTypeStatistics = statisticService.disabilityType();
        return ResponseEntity.ok(disabilityTypeStatistics);
    }

    /**
     * 장애 등급 통계를 가져오는 API
     *
     * @return 장애 등급 통계 데이터
     */
    @GetMapping("/disabilityGrade")
    public ResponseEntity<GradeDto> getDisabilityGradeStatistics() {
        GradeDto disabilityGradeStatistics = statisticService.disabilityGrade();
        return ResponseEntity.ok(disabilityGradeStatistics);
    }
}
