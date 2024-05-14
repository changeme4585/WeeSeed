package com.example.WeeSeed.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class sequenceVideoCard {
    @Id
    @Column
    private String svcId;

    @Column
    private String structedVideoId;

    @Column
    private int sequence ;

    @Column
    private String color;
    @Builder
    public  sequenceVideoCard(String svcId,String structedVideoId,int sequence,String color){
        this.svcId = svcId;
        this.structedVideoId = structedVideoId;
        this.sequence = sequence;
        this.color = color;
    }
}
