package org.weeseed.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "growth_diary")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GrowthDiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "growth_id")
    private Long growthId;

    @Column(name = "child_code")
    private String childCode;

    @Column(name = "image_card_num")
    private int imageCardNum;

    @Column(name = "video_card_num")
    private int videoCardNum;

    @Column(name = "child_id")
    private String childId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "creation_time")
    private String creationTime;

    public GrowthDiary(String childCode, int imageCardNum, int videoCardNum, String childId, String userId, String creationTime) {
        this.childCode = childCode;
        this.imageCardNum = imageCardNum;
        this.videoCardNum = videoCardNum;
        this.childId = childId;
        this.userId = userId;
        this.creationTime = creationTime;
    }
}
