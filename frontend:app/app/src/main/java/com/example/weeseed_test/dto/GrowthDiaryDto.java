package com.example.weeseed_test.dto;

import java.util.ArrayList;
import java.util.List;

public class GrowthDiaryDto {

    private int imageCardNum;   //생성한 이미지 카드 수
    private int videoCardNum;   //생성한 비디오 카드 수
    private List<DailyLearningLogDto> dailyLearningLogDtoList;  //클릭한 카드 목록
    private String creationTime;    //성장일지 생성일
    private String userName;    //성장일지 생성한 유저명


    public GrowthDiaryDto(){
        this.imageCardNum=0;
        this.videoCardNum=0;
        DailyLearningLogDto dto = new DailyLearningLogDto();
        this.dailyLearningLogDtoList = new ArrayList<>();
        this.dailyLearningLogDtoList.add(dto);

        this.creationTime = "2000:11:18";
        this.userName = "DEF_USERNAME";

    }

    public int getImageCardNum() {
        return imageCardNum;
    }

    public int getVideoCardNum() {
        return videoCardNum;
    }

    public List<DailyLearningLogDto> getDailyLearningLogDtoList() {
        return dailyLearningLogDtoList;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public void setDailyLearningLogDtoList(List<DailyLearningLogDto> dailyLearningLogDtoList) {
        this.dailyLearningLogDtoList = dailyLearningLogDtoList;
    }

    public void setImageCardNum(int imageCardNum) {
        this.imageCardNum = imageCardNum;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setVideoCardNum(int videoCardNum) {
        this.videoCardNum = videoCardNum;
    }
}
