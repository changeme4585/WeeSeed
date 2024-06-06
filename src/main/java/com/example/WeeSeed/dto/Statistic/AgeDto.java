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
    private String zeroAac;

    @Column
    private String fiveAac;

    @Column
    private String tenAac;

    @Column
    private String fifteenAac;

    @Column
    private String zeroVideo;

    @Column
    private String fiveVideo;

    @Column
    private String tenVideo;

    @Column
    private String fifteenVideo;


    @Builder
    public AgeDto(String zeroAac,String fiveAac,String tenAac,String fifteenAac,String zeroVideo,String fiveVideo,String tenVideo,String  fifteenVideo){
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
