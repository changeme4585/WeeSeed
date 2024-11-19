package org.weeseed.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AacCardDto {
    private Long aacCardId;
    private String cardName;
    private String creationTime;
    private String color;
    private String childId;
    private String constructorId;
    private String image;
    private String voice;

    /**
     * AAC DTO 객체 속성
     *
     * @param aacCardId    AAC 카드 ID
     * @param cardName     AAC 카드의 이름
     * @param creationTime AAC 카드의 생성 시간
     * @param color        AAC 카드의 색상 카테고리
     * @param childId      카드가 생성된 계정의 아동 코드
     * @param constructorId 카드가 생성된 계정의 ID (보호자 또는 재활사)
     * @param image        AAC 카드에 등록된 이미지 파일의 URL 또는 Path
     * @param voice        AAC 카드에 등록된 음성 파일의 URL 또는 Path
     */

    @Builder
    public AacCardDto(Long aacCardId, String cardName, String creationTime, String color, String childId, String constructorId, String image, String voice) {
        this.aacCardId = aacCardId;
        this.cardName = cardName;
        this.creationTime = creationTime;
        this.color = color;
        this.childId = childId;
        this.constructorId = constructorId;
        this.image = image;
        this.voice = voice;
    }
}