package com.example.WeeSeed.dto;


import com.google.errorprone.annotations.CanIgnoreReturnValue;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VideoDto {
    @Column
    private  Long videoCardId;

    @Column
    private  String cardName;

    @Column
    private String creationTime;

    @Column
    private String childId;

    @Column
    private String constructorId;

    @Column
    private String video;



    @Column
    private String thumbNail;

    @Column
    private String color;
    @Builder
    public  VideoDto (Long videoCardId,String cardName,String creationTime,String childId,String constructorId,String video,String thumbNail,String color){
        this.videoCardId = videoCardId;
        this.cardName = cardName;
        this.creationTime = creationTime;
        this.childId = childId;
        this.constructorId = constructorId;
        this.video = video;
        this.thumbNail = thumbNail;
        this.color = color;
    }
}
