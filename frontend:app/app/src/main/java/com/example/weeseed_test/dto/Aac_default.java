package com.example.weeseed_test.dto;

public class Aac_default {

    private String cardName;
    private byte[] cardImage;

    public Aac_default(String cardName, byte[] cardImage){
        this.cardName=cardName; this.cardImage=cardImage;
    }

    public String getCardName() {return cardName;}
    public void setCardName(String cardName) {
        this.cardName = cardName;
    }
    public byte[] getImage() {
        return this.cardImage;
    }

    public void setImage(byte[] cardImage) {
        this.cardImage = cardImage;
    }
}
