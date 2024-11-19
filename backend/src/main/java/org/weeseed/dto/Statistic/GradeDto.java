package org.weeseed.dto.Statistic;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GradeDto {
    @Column
    private int oneAac;
    @Column
    private int twoAac;

    @Column
    private int threeAac;
    @Column
    private int fourAac;
    @Column
    private int fiveAac;
    @Column
    private int sixAac;

    @Builder
    public  GradeDto (int oneAac,int twoAac,int threeAac,int fourAac,int fiveAac,int sixAac){
        this.oneAac = oneAac;
        this.twoAac = twoAac;
        this.threeAac = threeAac;
        this.fourAac = fourAac;
        this.fiveAac = fiveAac;
        this.sixAac = sixAac;
    }
}