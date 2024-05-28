package com.example.WeeSeed.entity;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
public class Child {
//    @Id
//    @GeneratedValue
//    @Column
//    private Long childId;
    @Id
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
    private String birth;

    @Column
    private String name;

    @Builder
    public Child(String childCode,String nokId,String disabilityType,int grade,String gender,String birth,String name){

        this.childCode = childCode;
        this.nokId = nokId;
        this.disabilityType =disabilityType;
        this.grade = grade;
        this.gender = gender;
        this.birth = birth;
        this.name = name;
    }
}
