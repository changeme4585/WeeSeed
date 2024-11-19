package org.weeseed.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDto {

    private String id;
    private String password;
    private String email;
    private String state;
    private String name;

    /**
     * UserDto 생성자
     *
     * @param id 사용자 ID
     * @param password 비밀번호
     * @param email 이메일 주소
     * @param state 사용자 상태
     * @param name 사용자 이름
     */
    @Builder
    public UserDto(String id, String password, String email, String state, String name) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.state = state;
        this.name = name;
    }
}
