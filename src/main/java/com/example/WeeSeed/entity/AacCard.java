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
public class AacCard {
    @Id
    @Column
    private  String aacCardId;

    @Column
    private  String cardName;

    @Column
    private Date creationTime;

    @Column
    private String color;

    @Column
    private String childId;

    @Column
    private  String constructorId;

    @Column
    private String imageUrl;

    @Column
    private String voiceUrl;

    @Column
    private  boolean share;

    @Builder
    public AacCard(String aacCardId,String cardName,Date cTime,String color,String childId,String cId,String iUrl,String vUrl,boolean share){
        this.aacCardId = aacCardId;
        this.cardName = cardName;
        this.creationTime = cTime;
        this.color = color;
        this.childId = childId;
        this.constructorId = cId;
        this.imageUrl = iUrl;
        this.voiceUrl = vUrl;
        this.share = share;
    }
}
