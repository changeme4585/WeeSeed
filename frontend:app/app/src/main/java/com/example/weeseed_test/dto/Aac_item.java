package com.example.weeseed_test.dto;

import java.util.ArrayList;
import java.util.List;

public class Aac_item implements Cloneable {

    private Long aacCardId;
    private String cardName;
    private String creationTime;
    private String color;
    private String childId;
    private String constructorId;
    private String image;
    private String voice;
    private int cardType;   //0: 일반카드, 1: aac Add, 2: default cards
    private byte[] cardImage_def;
    private boolean isSelected;
    private boolean checkBoxVisible;


    //////////////////////
    @Override
    public Aac_item clone() {
        try {
            return (Aac_item) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Aac_item(AacDto_url aacDtoUrl){
        this.aacCardId=aacDtoUrl.getAacCardId();
        this.cardName=aacDtoUrl.getCardName();
        this.creationTime=aacDtoUrl.getCreationTime();
        this.color=aacDtoUrl.getColor();
        this.childId=aacDtoUrl.getChildId();
        this.constructorId=aacDtoUrl.getConstructorId();
        this.image=aacDtoUrl.getImage();
        this.voice=aacDtoUrl.getVoice();
        this.cardType=0;
        this.cardImage_def=null;
        this.isSelected=false;
        this.checkBoxVisible=false;
    }

    public Aac_item(Aac_item aacDtoUrl){
        this.aacCardId=aacDtoUrl.getAacCardId();
        this.cardName=aacDtoUrl.getCardName();
        this.creationTime=aacDtoUrl.getCreationTime();
        this.color=aacDtoUrl.getColor();
        this.childId=aacDtoUrl.getChildId();
        this.constructorId=aacDtoUrl.getConstructorId();
        this.image=aacDtoUrl.getImage();
        this.voice=aacDtoUrl.getVoice();
        this.cardType=aacDtoUrl.getCardType();
        this.cardImage_def=aacDtoUrl.getCardImage_def();
        this.isSelected=aacDtoUrl.isSelected();
        this.checkBoxVisible=aacDtoUrl.checkBoxVisible;
    }

    public Aac_item(Aac_default aacDefault){
        //card name 설정
        String dName=aacDefault.getCardName();

        switch (dName) {
            case "dad": this.cardName = "아빠";break;
            case "mom": this.cardName = "엄마";break;
            case "teacher": this.cardName = "선생님";break;
            case "yes": this.cardName = "네";break;
            case "no": this.cardName = "아니요";break;
            case "rice": this.cardName = "밥";break;
            case "sleep": this.cardName = "잠";break;
            case "toilet": this.cardName = "화장실";break;
            case "sick": this.cardName = "아파요";break;
            case "hello": this.cardName = "안녕하세요";break;
            case "giveme": this.cardName = "주세요";break;
        }

        this.color="767676";    //일괄 color

        this.aacCardId=0L;
        this.creationTime="2000-01-01";
        this.childId="def";
        this.constructorId="def";
        this.image="def";
        this.voice=dName;
        this.cardType=2;
        this.cardImage_def=aacDefault.getImage();
        this.isSelected=false;
        this.checkBoxVisible=false;
    }

    public Aac_item(Aac_default aacDefault, int color){
        //card name & color 설정
        String dName=aacDefault.getCardName();

        //red
        switch (dName) {
            //red
            case "dad": this.cardName = "아빠";this.color = "F9A69E";break;
            case "mom": this.cardName = "엄마";this.color = "F9A69E";break;
            case "teacher": this.cardName = "선생님";this.color = "F9A69E";break;

            //green
            case "yes": this.cardName = "네";this.color = "92D79E";break;
            //blue
            case "no": this.cardName = "아니요";this.color = "A9CDE5";break;

            //orange/deepblue/yellow
            case "rice": this.cardName = "밥";this.color = "F3B98B";break;
            case "sleep": this.cardName = "잠";this.color = "F3B98B";break;
            case "toilet": this.cardName = "화장실";this.color = "B1C8FE";break;
            case "sick": this.cardName = "아파요";this.color = "F4E097";break;

            //purple
            case "hello": this.cardName = "안녕하세요";this.color = "F4AFEF";break;
            case "giveme": this.cardName = "주세요";this.color = "F4AFEF";break;
        }

        this.aacCardId=0L;
        this.creationTime="2000-01-01";
        this.childId="def";
        this.constructorId="def";
        this.image="def";
        this.voice=dName;
        this.cardType=2;
        this.cardImage_def=aacDefault.getImage();
        this.checkBoxVisible=false;

    }



    public Aac_item(int aacAddBtn){
        ///aac 카드추가버튼
        this.aacCardId=0L;
        this.cardName="add";
        this.creationTime="1999-01-01";
        this.color="FFCE1B";
        this.childId="add";
        this.constructorId="add";
        this.image="add";
        this.voice="add";
        this.cardType=1;
        this.cardImage_def=null;
        this.isSelected=false;
        this.checkBoxVisible=false;

    }

    public boolean isSelected() {return isSelected;}
    public void setSelected(boolean selected) {isSelected = selected;}

    public void setCheckBoxVisible(boolean checkBoxVisible) {
        this.checkBoxVisible = checkBoxVisible;
    }

    public boolean isCheckBoxVisible() {
        return checkBoxVisible;
    }
    ////

    public String getImage() {return image;}
    public String getVoice() {return voice;}
    public String getCardName() {return cardName;}
    public String getConstructorId() {return constructorId;}
    public String getColor() {return color;}
    public String getChildId() {return childId;}
    public String getCreationTime() {return creationTime;}
    public Long getAacCardId() {return aacCardId;}
    public int getCardType() {return cardType;}
    public byte[] getCardImage_def() {return cardImage_def;}

    public void setImage(String image) {this.image = image;}
    public void setVoice(String voice) {this.voice = voice;}
    public void setCardName(String cardName) {this.cardName = cardName;}
    public void setConstructorId(String constructorId) {this.constructorId = constructorId;}
    public void setColor(String color) {this.color = color;}
    public void setChildId(String childId) {this.childId = childId;}
    public void setCreationTime(String creationTime) {this.creationTime = creationTime;}
    public void setAacCardId(Long aacCardId) {this.aacCardId = aacCardId;}
    public void setCardType(int cardType) {this.cardType = cardType;}
    public void setCardImage_def(byte[] cardImage_def) {this.cardImage_def = cardImage_def;}
}
