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

    public void updateClick() {this.clickCnt+=1;}
    @Builder
    public LearningDiary(Long cardId,String cardType,String childId,String date,int clickCnt){
            this.cardId = cardId;
            this.cardType = cardType;
            this.childId = childId;
            this.date = date;
            this.clickCnt = clickCnt;
    }
}
