package com.example.weeseed_test.dto;

public class DailyLearningLogDto {
    /*
    * 아마
    * */
    private String cardName;
    private String image;
    private String color;

    public DailyLearningLogDto(){
        this.cardName = "DEF_NAME";
        this.image = "DEF_IMG";
        this.color = "DEF_COLOR";
    }

    public String getColor() {
        return color;
    }

    public String getCardName() {
        return cardName;
    }

    public String getImage() {
        return image;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
