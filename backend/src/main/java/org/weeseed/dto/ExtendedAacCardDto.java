package org.weeseed.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExtendedAacCardDto {
    private Long extendedCardId;
    private Long repCardId;    // 대표카드 ID
    private String imageUrl;   // 이미지 주소
    private int order_num;         // 순서

    /**
     * Extended AAC DTO 객체 속성
     *
     * @param extendedCardId            Extended AAC 카드의 ID
     * @param repCardId     대표카드 ID
     * @param imageUrl      이미지 주소
     * @param order_num         이미지 순서
     */
    @Builder
    public ExtendedAacCardDto(Long extendedCardId, Long repCardId, String imageUrl, int order_num) {
        this.extendedCardId = extendedCardId;
        this.repCardId = repCardId;
        this.imageUrl = imageUrl;
        this.order_num = order_num;
    }
}
