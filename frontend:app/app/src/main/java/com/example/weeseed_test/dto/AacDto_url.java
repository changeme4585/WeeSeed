package com.example.weeseed_test.dto;

public class AacDto_url {

    private Long aacCardId;
    private String cardName;
    private String creationTime;
    private String color;
    private String childId;
    private String constructorId;
    private String image;
    private String voice;



    //////////////////////
    public AacDto_url(){
        this.aacCardId=0L;
        this.cardName="null";
        this.creationTime="2000-01-01";
        this.color="767676";
        this.childId="null";
        this.constructorId="null";
        this.image="null";
        this.voice="null";
    }

    public AacDto_url(Aac_default aacDefault){

        //card name & color 설정
        String dName=aacDefault.getCardName();

        //red
        if(dName=="dad"){this.cardName="아빠";this.color="E67C73";}
        else if(dName=="mom"){this.cardName="엄마";this.color="E67C73";}
        else if(dName=="teacher"){this.cardName="선생님";this.color="E67C73";}

        //green/blue
        else if(dName=="yes"){this.cardName="네";this.color="7CB342";}
        else if(dName=="no"){this.cardName="아니요";this.color="039BE5";}

        //orange/deepblue/yellow
        else if(dName=="rice"){this.cardName="밥";this.color="F09300";}
        else if(dName=="sleep"){this.cardName="졸려요";this.color="F09300";}
        else if(dName=="toilet"){this.cardName="화장실";this.color="F6BF26";}
        else if(dName=="sick"){this.cardName="아파요";this.color="767676";}

        //purple
        else if(dName=="hello"){this.cardName="안녕하세요";this.color="9E69AF";}
        else if(dName=="giveme"){this.cardName="주세요";this.color="9E69AF";}

        this.aacCardId=0L;
        this.creationTime="2000-01-01";
        this.childId="def";
        this.constructorId="def";
        this.image="def";
        this.voice="def";
    }

    public AacDto_url(int aacAddBtn){
        this.aacCardId=0L;
        this.cardName="add";
        this.creationTime="1999-01-01";
        this.color="FFF6EC";
        this.childId="add";
        this.constructorId="add";
        this.image="add";
        this.voice="add";

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

    public void setImage(String image) {this.image = image;}
    public void setVoice(String voice) {this.voice = voice;}
    public void setCardName(String cardName) {this.cardName = cardName;}
    public void setConstructorId(String constructorId) {this.constructorId = constructorId;}
    public void setColor(String color) {this.color = color;}
    public void setChildId(String childId) {this.childId = childId;}
    public void setCreationTime(String creationTime) {this.creationTime = creationTime;}
    public void setAacCardId(Long aacCardId) {this.aacCardId = aacCardId;}
}
