package org.weeseed.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="User")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "password")
    private String password;

    @Column(name = "state")
    private String state;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    /**
     * 사용자 정보 업데이트
     *
     * @param password 새 비밀번호
     * @param email 새 이메일 주소
     * @param name 새 사용자 이름
     */
    public void updateUser(String password, String email, String name) {
        this.password = password;
        this.email = email;
        this.name = name;
    }

    /**
     * 사용자 비밀번호 업데이트
     *
     * @param password 새 비밀번호
     */
    public void updateUserPass(String password) {
        this.password = password;
    }

    /**
     * 사용자 엔티티 생성자
     *
     * @param userId 사용자 ID
     * @param password 비밀번호
     * @param state 사용자 상태
     * @param email 이메일 주소
     * @param name 사용자 이름
     */
    @Builder
    public User(String userId, String password, String state, String email, String name) {
        this.userId = userId;
        this.password = password;
        this.state = state;
        this.email = email;
        this.name = name;
    }
}
