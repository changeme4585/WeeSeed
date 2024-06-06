package com.example.WeeSeed.dto.Statistic;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GenderDto {
    @Column
    private int maleAac;
    @Column
    private int femaleAac;
    @Column
    private int maleVideo;
    @Column
    private int femaleVideo;

    @Builder
    public GenderDto(int maleAac,int femaleAac,int maleVideo,int femaleVideo){
        this.maleAac = maleAac;
        this.femaleAac = femaleAac;
        this.maleVideo = maleVideo;
        this.femaleVideo = femaleVideo;
    }


}
