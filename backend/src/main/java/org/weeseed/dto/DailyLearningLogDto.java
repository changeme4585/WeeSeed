package org.weeseed.dto;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyLearningLogDto {

    @Column
    private String cardName;

    @Column
    private String image;

    @Column
    private String color;

    @Column
    private String type;

    @Column
    private int clickCnt;

    /*
     * @param cardName  카드의 이름
     * @param image     카드에 등록된 이미지 파일의 URL 또는 Path
     * @param color     카드의 색상 카테고리
     * @param type      카드 타입 (aac 또는 video)
     * @param clickCnt  카드 학습 횟수
     * */
    @Builder
    public DailyLearningLogDto(String cardName, String image, String color, String type, int clickCnt) {
        this.cardName = cardName;
        this.image = image;
        this.color = color;
        this.type = type;
        this.clickCnt = clickCnt;
    }
}
