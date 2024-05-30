package com.example.WeeSeed.entity;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long childId;

    @Column
    private String childCode;

    @Column
    private String userId;

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
    public Child(String childCode,String userId,String disabilityType,int grade,String gender,String birth,String name){

        this.childCode = childCode;
        this.userId = userId;
        this.disabilityType =disabilityType;
        this.grade = grade;
        this.gender = gender;
        this.birth = birth;
        this.name = name;
    }
}
