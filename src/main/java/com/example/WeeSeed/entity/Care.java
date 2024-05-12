package com.example.WeeSeed.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Care {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hireID;

    @Column
    private  String pId;

    @Column
    private String childId;

    @Builder
    public  Care(String pId,String childId){
        this.pId = pId;
        this.childId = childId;
    }
}
