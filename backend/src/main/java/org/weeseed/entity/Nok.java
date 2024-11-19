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
@Table(name = "Nok")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Nok {
    @Id
    @Column(name = "nok_id")
    private String nokId;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    /**
     * NOK 정보 업데이트
     *
     * @param password 새 비밀번호
     * @param email 새 이메일 주소
     * @param name 새 NOK 이름
     */
    public void updateNok(String password, String email, String name) {
        this.password = password;
        this.email = email;
        this.name = name;
    }

    /**
     * NOK 엔티티 생성자
     *
     * @param nokId NOK ID
     * @param password 비밀번호
     * @param email 이메일 주소
     * @param name NOK 이름
     */
    @Builder
    public Nok(String nokId, String password, String email, String name) {
        this.nokId = nokId;
        this.password = password;
        this.email = email;
        this.name = name;
    }
}
