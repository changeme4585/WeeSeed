package org.weeseed.dto;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StatisticDto {

    @Column
    private int imageCardNum;

    @Column
    private int videoCardNum;

    @Column
    private String cardName;  //가장 많이 학습한 카드 이름

    @Column
    private String color ; //가장 많이 학습한 카드 색깔

    @Column
    private String image; //가장 많이 학습한 카드 이미지

    @Builder
    public StatisticDto(int imageCardNum,int videoCardNum,String cardName,String color,String image){
        this.imageCardNum = imageCardNum ;
        this.videoCardNum = videoCardNum;
        this.cardName = cardName;
        this.color = color;
        this.image = image;
    }
}