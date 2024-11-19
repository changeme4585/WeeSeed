package org.weeseed.dto.Statistic;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;

@Getter
@NoArgsConstructor
public class TypeDto {
    @Column
    private int autism; //자폐성 장애

    @Column
    private  int intellectual; //지적 장애

    @Column
    private  int behavioral; //행동 장애

    @Column
    private int pronunciation; // 발음 장애

    @Column
    private  int  brainLesion; //뇌병변 장애

    @Builder
    public  TypeDto (int autism,int intellectual,int behavioral,int pronunciation,int brainLesion){
        this.autism = autism;
        this.intellectual = intellectual;
        this.behavioral =behavioral;
        this.pronunciation = pronunciation;
        this.brainLesion = brainLesion;
    }
}