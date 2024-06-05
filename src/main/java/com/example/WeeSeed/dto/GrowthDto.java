package com.example.WeeSeed.dto;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GrowthDto {
    @Column
    private String cardName;
    @Column
    private String image;
    @Column
    private  String color;

    @Column
    private String creationDate;
    @Builder
    public  GrowthDto(String cardName,String image,String color,String creationDate){
        this.cardName = cardName;
        this.image = image;
        this.color =color;
        this.creationDate = creationDate;

    }
}
