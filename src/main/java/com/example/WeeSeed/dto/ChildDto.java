package com.example.WeeSeed.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class ChildDto {

    private String nokId;

    private String disabilityType;

    private int grade;

    private String gender;

    private String birth;

    private String name;
    @Builder
    public ChildDto(String nokId, String disabilityType, int grade, String gender, String birth, String name){

        this.nokId = nokId;
        this.disabilityType =disabilityType;
        this.grade = grade;
        this.gender = gender;
        this.birth = birth;
        this.name = name;
    }
}
