package com.example.weeseed_test.dto;

public class Video_item extends VideoDto implements Cloneable {
    private boolean isSelected;
    private boolean isCheckBoxVisible;
    private int cardType;


    @Override
    public Aac_item clone() {
        try {
            return (Aac_item) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Video_item(int vidAddBtn){
        super( 0L,"add","1999-01-01",
                "add", "add", "add", "FFCE1B",
                "FFCE1B","add",0);
        this.isSelected=false;
        this.isCheckBoxVisible=false;
        this.cardType = 1;
    }

    public Video_item(VideoDto videoDto){
        super(videoDto);
        this.isSelected=false;
        this.isCheckBoxVisible=false;
        this.cardType = 0;
    }
    //////////

    public boolean isCheckBoxVisible() {
        return isCheckBoxVisible;
    }

    public void setCheckBoxVisible(boolean checkBoxVisible) {
        isCheckBoxVisible = checkBoxVisible;
    }

    public boolean isSelected() {return isSelected;}
    public void setSelected(boolean selected) {isSelected = selected;}

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }
}
