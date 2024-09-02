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
    private  String cardName;


    @Builder
    public  DefaultImageDto(String constructorId,String cardName){
        this.constructorId = constructorId;
        this.cardName = cardName;
    }
}
