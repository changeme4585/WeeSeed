package org.weeseed.dto;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GrowthDiaryDto {

    @Column
    private int imageCardNum;
    @Column
    private int videoCardNum;
    @Column
    private List<DailyLearningLogDto> dailyLearningLogDtoList;
    @Column
    private String creationTime;
    @Column
    private String userName;

    /**
     * GrowthDiaryDto 생성자
     *
     * @param imageCardNum 이미지 카드 수
     * @param videoCardNum 비디오 카드 수
     * @param dailyLearningLogDtoList 학습 데이터 리스트
     * @param creationTime 생성 시간
     * @param userName 사용자 이름
     */
    @Builder
    public GrowthDiaryDto(int imageCardNum, int videoCardNum, List<DailyLearningLogDto> dailyLearningLogDtoList, String creationTime, String userName) {
        this.imageCardNum = imageCardNum;
        this.videoCardNum = videoCardNum;
        this.dailyLearningLogDtoList = dailyLearningLogDtoList;
        this.creationTime = creationTime;
        this.userName = userName;
    }
}
