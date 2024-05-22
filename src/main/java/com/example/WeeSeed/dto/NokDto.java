package com.example.WeeSeed.dto;


import jakarta.persistence.Column;
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

    @Builder
    public NokDto(String nokId,String password,String email,String name){
        this.nokId = nokId;
        this.password = password;
        this.email = email;
        this.name = name;
    }
}
