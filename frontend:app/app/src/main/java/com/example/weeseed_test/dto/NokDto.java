package com.example.weeseed_test.dto;

public class NokDto {
    private String nokId;
    private String password;
    private String email;
    private String name;

    public NokDto() {
        this.nokId = null;
        this.password = null;
        this.email = null;
        this.name = null;
    }

    /////////////////////////
    public String getNokId() {
        return nokId;
    }

    public void setNokId(String nokId) {
        this.nokId = nokId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}