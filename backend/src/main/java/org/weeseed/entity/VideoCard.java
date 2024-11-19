package org.weeseed.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "video_card")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class VideoCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_card_id")
    private Long videoCardId;

    @Column(name = "card_name")
    private String cardName;

    @Column(name = "creation_time")
    private String creationTime;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "child_id")
    private String childId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "color")
    private String color;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "state")
    private String state;

    @Column(name = "click_count")
    private int clickCnt;

    public void updateClick() {
        this.clickCnt += 1;
    }

    @Builder
    public VideoCard(
            String cardName,
            String creationTime,
            String videoUrl,
            String childId,
            String userId,
            String color,
            String thumbnailUrl,
            String state,
            int clickCnt
    ) {
        this.cardName = cardName;
        this.creationTime = creationTime;
        this.videoUrl = videoUrl;
        this.childId = childId;
        this.userId = userId;
        this.color = color;
        this.thumbnailUrl = thumbnailUrl;
        this.state = state;
        this.clickCnt = clickCnt;
    }
}
