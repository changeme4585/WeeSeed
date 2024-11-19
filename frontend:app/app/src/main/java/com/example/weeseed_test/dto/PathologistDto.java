package com.example.weeseed_test.dto;

public class PathologistDto {
    private String pathologistId;

    private String password;

    private String email;

    private String organizationName;

    private String name;
    public PathologistDto(){
        this.pathologistId = null;
        this.password = null;
        this.email = null;
        this.name = null;
        this.organizationName = null;
    }


//////////////////////
    public String getPathologistId() {
        return pathologistId;
    }
    public void setPathologistId(String pathologistId) {
        this.pathologistId = pathologistId;
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
    public String getOrganizationName() {
        return organizationName;
    }
    public void setOrganizationName(String organizationName) {this.organizationName = organizationName;}
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}