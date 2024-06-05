package com.example.WeeSeed.dto;


import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GrowthDiaryDto {
    @Column
    private int imageCardNum;

    @Column
    private int videoCardNum;

    @Column
    private List<LearningDto> learningDtoList;


    @Column
    private String creationTime;

    @Column
    private String userName;
    @Builder
    public GrowthDiaryDto(int imageCardNum,int videoCardNum,List<LearningDto> learningDtoList,String creationTime,String userName){
        this.imageCardNum = imageCardNum;
        this.videoCardNum = videoCardNum;
        this.learningDtoList = learningDtoList;
        this.creationTime = creationTime;
        this.userName = userName;
    }
}
