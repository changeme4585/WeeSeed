package com.example.WeeSeed.dto;


import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AacDto {
    @Column
    private Long aacCardId;
    @Column
    private  String cardName;

    @Column
    private String creationTime;

    @Column
    private String color;

    @Column
    private String childId;

    @Column
    private  String constructorId;  //사용자 id(재활사,보호자 구분)

    @Column
    private String image;

    @Column
    private String voice;

    @Builder
    public AacDto(Long aacCardId, String cardName,String creationTime,String color,String childId,String constructorId,String image,String voice){
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
