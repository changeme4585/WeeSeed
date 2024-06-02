package com.example.WeeSeed.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Growth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long growthId;

    @Column
    private String imageUrl ;


    @Column
    private String userId;

    @Column
    private String color;


    @Column
    private String cardName;

    @Column
    private String creationTime;

    @Builder
    public Growth(String imageUrl,String userId,String color,String cardName,String creationTime){
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.color = color;
        this.cardName = cardName;
        this.creationTime = creationTime;
    }
}
