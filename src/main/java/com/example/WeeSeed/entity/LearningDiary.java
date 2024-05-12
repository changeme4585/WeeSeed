package com.example.WeeSeed.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LearningDiary {
    @Id
    @Column
    private String ldId;

    @Column
    private String cardType;

    @Column
    private String childId;

    @Column
    private Date date;

    @Column
    private int clickCnt;

    @Builder
    public LearningDiary(String ldId,String cardType,String childId,Date date,int clickCnt){
        this.ldId = ldId;
        this.cardType = cardType;
        this.childId = childId;
        this.date = date;
        this.clickCnt = clickCnt;
    }
}
