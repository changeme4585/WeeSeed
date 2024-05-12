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
public class Nok {
    @Id
    @Column
    private String nokId;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private String name;

    @Builder
    public Nok(String nokId,String password,String email,String name){
        this.nokId = nokId;
        this.password = password;
        this.email = email;
        this.name = name;
    }
}
