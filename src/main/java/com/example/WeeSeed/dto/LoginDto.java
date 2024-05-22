package com.example.WeeSeed.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginDto {
    private String id;
    private String password;
    @Builder
    public  LoginDto(String id,String password){
        this.id = id;
        this.password = password;
    }
}
