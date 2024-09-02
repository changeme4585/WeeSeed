package com.example.WeeSeed.dto;


import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DefaultImageDto {
    @Column
    private String constructorId;


    @Column
    private  byte[] cardImage;

    @Column
    private String cardName;


    @Builder
    public  DefaultImageDto(String constructorId,byte[] cardImage,String cardName){
        this.constructorId = constructorId;
        this.cardImage = cardImage;
        this.cardName = cardName;
    }
}
