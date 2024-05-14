package com.example.WeeSeed.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pathologist {
    @Id
    @Column
    private String pathologistId;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private String organizationName;

    @Column
    private String name;

    @Builder
    public Pathologist(String pId,String password,String email,String oName,String name){
        this.pathologistId = pId;
        this.password = password;
        this.email = email;
        this.organizationName = oName;
        this.name = name;
    }
}
