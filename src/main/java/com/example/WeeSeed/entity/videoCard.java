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
public class videoCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private  Long videoCardID;

    
    @Column
    private String cardName;
    
    @Column
    private String creationTime;
    
    @Column
    private String videoUrl;
    
    @Column
    private String childId;
    
    @Column
    private String userId;

    @Column
    private String color;


    @Column
    private String thumbnailUrl;

    @Column
    private String state; //사용자의 상태를 구분
    @Builder
    public videoCard(String cardName,String creationTime,String videoUrl,String childId,String userId,String color,String thumbnailUrl,String state){

        this.cardName = cardName;
        this.creationTime = creationTime;
        this.videoUrl = videoUrl;
        this.childId = childId;
        this.userId = userId;
        this.color = color;
        this.thumbnailUrl = thumbnailUrl;
        this.state = state;
    }
}
