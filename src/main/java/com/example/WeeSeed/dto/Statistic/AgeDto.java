package com.example.WeeSeed.dto.Statistic;


import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AgeDto {

    @Column
    private int zeroAac;

    @Column
    private int fiveAac;

    @Column
    private int tenAac;

    @Column
    private int fifteenAac;

    @Column
    private int zeroVideo;

    @Column
    private int fiveVideo;

    @Column
    private int tenVideo;

    @Column
    private int fifteenVideo;


    @Builder
    public AgeDto(int zeroAac,int fiveAac,int tenAac,int fifteenAac,int zeroVideo,int fiveVideo,int tenVideo,int  fifteenVideo){
        this.zeroAac = zeroAac;
        this.fiveAac = fiveAac;
        this.tenAac = tenAac;
        this.fifteenAac = fifteenAac;
        this.zeroVideo = zeroVideo;
        this.fiveVideo = fiveVideo;
        this.tenVideo = tenVideo;
        this.fifteenVideo = fifteenVideo;
    }
}
