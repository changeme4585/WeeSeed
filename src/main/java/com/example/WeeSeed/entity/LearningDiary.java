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
public class LearningDiary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private  Long learnId;

    @Column
    private Long cardId;

    @Column
    private String cardType;

    @Column
    private String childId;

    @Column
    private String date;

    @Column
    private int clickCnt;

    @Column
    private String imageUrl;

    @Column
    private String color;

    @Column
    private String cardName;
    public void updateClick() {this.clickCnt+=1;}


    @ManyToOne
    @JoinColumn(name="charge_id")
    private GrowthDiary charge;
    @Builder
    public LearningDiary(Long cardId,String cardType,String childId,String date,int clickCnt,String imageUrl,String color,String cardName){
            this.cardId = cardId;
            this.cardType = cardType;
            this.childId = childId;
            this.date = date;
            this.clickCnt = clickCnt;
            this.imageUrl = imageUrl;
            this.color = color;
            this.cardName =  cardName ;
    }
}
