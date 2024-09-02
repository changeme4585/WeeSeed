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
public class User {
    @Id
    @Column
    private String userId;

    @Column
    private String password;
    @Column
    private String state;
    @Column
   private String email;

    @Column
    private  String name;
    public void updateUser(String email,String name){
        this.email = email;
        this.name = name;
    }
    public  void updateUserPass(String password){
        this.password = password;
    }
    @Builder
    public User(String userId,String password,String state,String email,String name){
        this.userId = userId;
        this.password = password;
        this.state = state;
        this.email = email;
        this.name = name;
    }
}
