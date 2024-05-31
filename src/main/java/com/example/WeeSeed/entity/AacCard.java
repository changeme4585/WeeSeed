package com.example.WeeSeed.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AacCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private  Long aacCardId;

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
    private String imageUrl;

    @Column
    private String voiceUrl;

    @Column
    private  int share; // 0이면 공유 안함 , 1이며 공유


    @Builder
    public AacCard(String cardName,String creationTime,String color,String childId,String constructorId,String imageUrl,String voiceUrl,int share){
        this.cardName = cardName;
        this.creationTime = creationTime;
        this.color = color;
        this.childId = childId;
        this.constructorId = constructorId;
        this.imageUrl = imageUrl;
        this.voiceUrl = voiceUrl;
        this.share = share;
    }
}
