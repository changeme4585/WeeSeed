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
@Table(name = "Pathologist")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pathologist {
    @Id
    @Column(name = "pathologist_id")
    private String pathologistId;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "organization_name")
    private String organizationName;

    @Column(name = "name")
    private String name;

    /**
     * 재활사 정보 업데이트
     *
     * @param password 새 비밀번호
     * @param email 새 이메일 주소
     * @param organizationName 새 기관 이름
     * @param name 새 재활사 이름
     */
    public void updatePathologist(String password, String email, String organizationName, String name) {
        this.password = password;
        this.email = email;
        this.organizationName = organizationName;
        this.name = name;
    }

    /**
     * 재활사 엔티티 생성자
     *
     * @param pathologistId 재활사 ID
     * @param password 비밀번호
     * @param email 이메일 주소
     * @param organizationName 기관 이름
     * @param name 재활사 이름
     */
    @Builder
    public Pathologist(String pathologistId, String password, String email, String organizationName, String name) {
        this.pathologistId = pathologistId;
        this.password = password;
        this.email = email;
        this.organizationName = organizationName;
        this.name = name;
    }
}
