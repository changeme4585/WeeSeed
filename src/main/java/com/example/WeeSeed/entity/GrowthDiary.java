package com.example.WeeSeed.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    @OneToMany(mappedBy = "charge")
    private List<LearningDiary> learnedCard = new ArrayList<>(); //학습한 카드 id

    @Builder
    public GrowthDiary(String childCode,int imageCardNum,int videoCardNum,String childId,String userId,String creationTime,List<LearningDiary> learnedCard){

        this.childCode = childCode;
        this.imageCardNum = imageCardNum;
        this.videoCardNum = videoCardNum;
        this.childId = childId;
        this.userId = userId;
        this.creationTime = creationTime;
        this.learnedCard =  learnedCard;
    }

}

