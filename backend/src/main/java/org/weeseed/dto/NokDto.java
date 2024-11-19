package org.weeseed.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NokDto {
    private String nokId;
    private String password;
    private String email;
    private String name;

    /**
     * NokDto 객체 생성
     *
     * @param nokId    NOK ID
     * @param password 비밀번호
     * @param email    이메일
     * @param name     이름
     */
    @Builder
    public NokDto(String nokId, String password, String email, String name) {
        this.nokId = nokId;
        this.password = password;
        this.email = email;
        this.name = name;
    }
}
