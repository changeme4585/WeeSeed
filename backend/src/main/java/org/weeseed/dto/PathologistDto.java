package org.weeseed.dto;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PathologistDto {

    private String pathologistId;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private String organizationName;

    @Column
    private String name;

    /**
     * PathologistDto 생성자
     *
     * @param pathologistId 재활사 ID
     * @param password 비밀번호
     * @param email 이메일
     * @param organizationName 소속 기관 이름
     * @param name 재활사 이름
     */
    @Builder
    public PathologistDto(String pathologistId, String password, String email, String organizationName, String name) {
        this.pathologistId = pathologistId; // 매개변수 이름을 일관되게 사용
        this.password = password;
        this.email = email;
        this.organizationName = organizationName;
        this.name = name;
    }
}
