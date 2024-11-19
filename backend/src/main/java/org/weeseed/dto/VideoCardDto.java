package org.weeseed.dto;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VideoCardDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private  Long videoCardId;

    @Column
    private String cardName;

    @Column
    private String creationTime;

    @Column
    private String videoUrl;

    @Column
    private String childId;

    @Column
    private String userId;

    @Column
    private String color;


    @Column
    private String thumbnailUrl;

    @Column
    private String state; //사용자의 상태를 구분

    @Column
    private int clickCnt;

    public void updateClick(){
        this.clickCnt+=1;
    }
    @Builder
    public VideoCardDto(Long videoCardId, String cardName, String creationTime, String videoUrl, String childId, String userId, String color, String thumbnailUrl, String state, int clickCnt){
        this.videoCardId = videoCardId;
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