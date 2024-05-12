package com.example.WeeSeed.entity;

import jakarta.persistence.*;

import lombok.*;

import java.util.Date;

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
public class Child {
    @Id
    @Column
    private String childId;

    @Column
    private String childCode;

    @Column
    private String nokId;

    @Column
    private String disabilityType;

    @Column
    private int grade;

    @Column
    private String gender;

    @Column
    private Date birth;

    @Column
    private String name;

    @Builder
    public Child(String childId,String childCode,String nokId,String dT,int grade,String gender,Date birth,String name){
        this.childId = childId;
        this.childCode = childCode;
        this.nokId = nokId;
        this.disabilityType =dT;
        this.grade = grade;
        this.gender = gender;
        this.birth = birth;
        this.name = name;
    }
}
