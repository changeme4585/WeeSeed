package com.example.weeseed_test.dto;

public class ExtendedAacCardSendDto {
    private Long repCardId;
    private String cardName;
    private String imagePath;


    public Long getRepCardId() {
        return repCardId;
    }

    public String getCardName() {
        return cardName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setRepCardId(Long repCardId) {
        this.repCardId = repCardId;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
