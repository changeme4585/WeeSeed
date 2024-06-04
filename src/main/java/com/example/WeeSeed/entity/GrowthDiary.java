package com.example.WeeSeed.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder

public class GrowthDiary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private  Long growthId;

    @Column
    private String childCode;

    @Column
    private int imageCardNum;

    @Column
    private int videoCardNum;



    @Column
    private  String childId;

    @Column
    private String userId;

    @Column
    private String creationTime; //생성일자


    @Column
    private Long learnedCard; //학습한 카드 id

    @Builder
    public GrowthDiary(String childCode,int imageCardNum,int videoCardNum,String childId,String userId,String creationTime,Long learnedCard){

        this.childCode = childCode;
        this.imageCardNum = imageCardNum;
        this.videoCardNum = videoCardNum;
        this.childId = childId;
        this.userId = userId;
        this.creationTime = creationTime;
        this.learnedCard =  learnedCard;
    }

}

