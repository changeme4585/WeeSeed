package com.example.weeseed_test.dto;

public class VideoDto {

    private Long videoCardId;
    private String cardName;
    private String creationTime;
    private String videoUrl;
    private String childId;
    private String userId;
    private String color;
    private String thumbnailUrl;
    private String state;
    private int clickCnt;

    public VideoDto(VideoDto videoDto){
        this.videoCardId=videoDto.getVideoCardId();
        this.cardName=videoDto.getCardName();
        this.creationTime=videoDto.getCreationTime();
        this.childId=videoDto.getChildId();
        this.userId =videoDto.getUserId();
        this.videoUrl =videoDto.getVideoUrl();
        this.thumbnailUrl =videoDto.getThumbnailUrl();
        this.color=videoDto.getColor();
        this.state=videoDto.getState();
        this.clickCnt=videoDto.getClickCnt();
    }
    public VideoDto(Long videoCardId,
                    String cardName,
                    String creationTime,
                    String videoUrl,
                    String childId,
                    String userId,
                    String color,
                    String thumbnailUrl,
                    String state,
                    int clickCnt){
        this.videoCardId=videoCardId;
        this.cardName=cardName;
        this.creationTime=creationTime;
        this.videoUrl =videoUrl;
        this.childId=childId;
        this.userId =userId;
        this.color=color;
        this.thumbnailUrl =thumbnailUrl;
        this.state=state;
        this.clickCnt=clickCnt;
    }

    //////////


    public String getThumbnailUrl() {return thumbnailUrl;}
    public String getColor() {return color;}
    public Long getVideoCardId() {return videoCardId;}
    public String getCardName() {return cardName;}
    public String getChildId() {return childId;}
    public String getUserId() {return userId;}
    public String getCreationTime() {return creationTime;}
    public String getVideoUrl() {return videoUrl;}
    public String getState() {return state;}
    public int getClickCnt() {return clickCnt;}
    public void setCardName(String cardName) {this.cardName = cardName;}
    public void setChildId(String childId) {this.childId = childId;}
    public void setUserId(String userId) {this.userId = userId;}
    public void setCreationTime(String creationTime) {this.creationTime = creationTime;}
    public void setVideoUrl(String videoUrl) {this.videoUrl = videoUrl;}
    public void setVideoCardId(Long videoCardId) {this.videoCardId = videoCardId;}
    public void setThumbnailUrl(String thumbnailUrl) {this.thumbnailUrl = thumbnailUrl;}
    public void setState(String state) {this.state = state;}
    public void setClickCnt(int clickCnt) {this.clickCnt = clickCnt;}
    public void setColor(String color) {this.color = color;}

}
