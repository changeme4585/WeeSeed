package com.example.weeseed_test.dto;

public class ExtendedAacCardDto implements Cloneable  {
    private Long extendedCardId;
    private Long repCardId;    // 대표카드 ID
    private String imageUrl;   // 이미지 주소
    private int order;         // 순서


    @Override
    public ExtendedAacCardDto clone() {
        try {
            return (ExtendedAacCardDto) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }


    public ExtendedAacCardDto(){
        this.extendedCardId = 999999L;
        this.repCardId = 999999L;
        this.imageUrl = "IMG_URL_EXT";
        this.order = 888;
    }

    public ExtendedAacCardDto(Aac_item item){
        this.extendedCardId =item.getAacCardId();
        this.repCardId = item.getAacCardId();
        this.imageUrl = item.getImage();
        this.order = 999;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getExtendedCardId() {
        return extendedCardId;
    }

    public int getOrder() {
        return order;
    }

    public Long getRepCardId() {
        return repCardId;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setExtendedCardId(Long extendedCardId) {
        this.extendedCardId = extendedCardId;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setRepCardId(Long repCardId) {
        this.repCardId = repCardId;
    }
}
