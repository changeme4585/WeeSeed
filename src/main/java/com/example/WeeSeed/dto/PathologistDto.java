package com.example.WeeSeed.dto;


import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PathologistDto {
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
    public PathologistDto(String pId,String password,String email,String oName,String name){
        this.pathologistId = pId;
        this.password = password;
        this.email = email;
        this.organizationName = oName;
        this.name = name;
    }
}
