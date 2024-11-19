package org.weeseed.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginDto {
    private String id;
    private String password;

    /**
     * LoginDto 객체 생성
     *
     * @param id       사용자 ID
     * @param password 비밀번호
     */
    @Builder
    public LoginDto(String id, String password) {
        this.id = id;
        this.password = password;
    }
}
