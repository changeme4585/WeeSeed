package org.weeseed.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DefaultImageDto {

    private String constructorId;
    private byte[] cardImage;
    private String cardName;
    private byte[] cardVoice;

    /**
     * DefaultImageDto 생성자
     *
     * @param constructorId  카드가 생성된 계정의 ID (보호자 또는 재활사)
     * @param cardImage      AAC 카드에 등록된 이미지 파일의 URL 또는 Path
     * @param cardName       AAC 카드의 이름
    *  @param cardVoice          AAC 카드에 등록된 음성 파일의 URL 또는 Path
     */
    @Builder
    public DefaultImageDto(String constructorId, byte[] cardImage, String cardName, byte[] cardVoice) {
        this.constructorId = constructorId;
        this.cardImage = cardImage;
        this.cardName = cardName;
        this.cardVoice = cardVoice;
    }
}
