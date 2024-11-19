package org.weeseed.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChildDto {
    private String userId;
    private String disabilityType;
    private int grade;
    private String gender;
    private String birth;
    private String name;

    /**
     * ChildDto 생성자
     *
     * @param userId           사용자 ID
     * @param disabilityType   장애 유형
     * @param grade            학년
     * @param gender           성별
     * @param birth            생년월일
     * @param name             이름
     */
    @Builder
    public ChildDto(String userId, String disabilityType, int grade, String gender, String birth, String name) {
        this.userId = userId;
        this.disabilityType = disabilityType;
        this.grade = grade;
        this.gender = gender;
        this.birth = birth;
        this.name = name;
    }
}
