package org.weeseed.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "AacCard")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AacCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aac_card_id")
    private Long aacCardId;

    @Column(name = "card_name", nullable = false)
    private String cardName;

    @Column(name = "creation_time", nullable = false)
    private String creationTime;

    @Column(name = "color")
    private String color;

    @Column(name = "child_id", nullable = false)
    private String childId;

    @Column(name = "constructor_id", nullable = false)
    private String constructorId;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "voice_url")
    private String voiceUrl;

    @Column(name = "share", nullable = false)
    private int share;  // Sharing status: 0 - not shared, 1 - shared

    @Column(name = "click_cnt", nullable = false)
    private int clickCnt;

    @Column(name = "state")
    private String state; // 사용자 계정 유형 (ex. 재활사, 보호자)

    public void updateClick() {
        this.clickCnt += 1;
    }

    @Builder
    public AacCard(
            String cardName,
            String creationTime,
            String color,
            String childId,
            String constructorId,
            String imageUrl,
            String voiceUrl,
            int share,
            int clickCnt,
            String state
    ) {
        this.cardName = cardName;
        this.creationTime = creationTime;
        this.color = color;
        this.childId = childId;
        this.constructorId = constructorId;
        this.imageUrl = imageUrl;
        this.voiceUrl = voiceUrl;
        this.share = share;
        this.clickCnt = clickCnt;
        this.state = state;
    }

    public void setCardName(String newCardName) {
        this.cardName = newCardName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
