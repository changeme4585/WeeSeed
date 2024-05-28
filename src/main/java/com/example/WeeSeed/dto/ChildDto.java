package com.example.WeeSeed.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChildDto {



    private String nokId;

    private String disabilityType;

    private int grade;

    private String gender;

    private Date birth;

    private String name;
    public ChildDto(String nokId, String disabilityType, int grade, String gender, Date birth, String name){

        this.nokId = nokId;
        this.disabilityType =disabilityType;
        this.grade = grade;
        this.gender = gender;
        this.birth = birth;
        this.name = name;
    }
}
