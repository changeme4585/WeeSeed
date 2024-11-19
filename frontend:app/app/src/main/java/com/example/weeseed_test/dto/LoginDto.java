package com.example.weeseed_test.dto;

public class LoginDto {
    private String id;
    private String password;

    public  LoginDto(String id,String password){
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
