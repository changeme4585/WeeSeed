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
public class videoCard {
    @Id
    @Column
    private String videoCardID;
    
    @Column
    private String cardName;
    
    @Column
    private Date creationTime;
    
    @Column
    private String videoUrl;
    
    @Column
    private String childId;
    
    @Column
    private String userId;

    @Column
    private String color;
    
    @Builder
    public videoCard(String vCId,String cardName,Date cTime,String vUrl,String childId,String userId,String color){
        this.videoCardID = vCId;
        this.cardName = cardName;
        this.creationTime = cTime;
        this.videoUrl = vUrl;
        this.childId = childId;
        this.userId = userId;
        this.color = color;
    }
}
