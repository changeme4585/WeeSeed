package com.example.weeseed_test.dto;

public class GrowthDto {
    /* 개별 목록? 인듯
     *
     * */
    private String cardName;
    private String image;
    private String color;
    private String creationDate;

    public String getCardName() {
        return cardName;
    }

    public String getImage() {
        return image;
    }

    public String getColor() {
        return color;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}
