package com.example.weeseed_test.dto;

public class ChildDto_forUSE {

    private String childCode;

    private String userId;

    private String disabilityType;

    private int grade;

    private String gender;

    private String birth;

    private String name;


    public String getChildCode() {
        return childCode;
    }

    public void setChildCode(String childCode) {
        this.childCode = childCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirth() {
        return birth;
    }
    public void setBirth(String birth) {
        this.birth = birth;
    }

    public int getGrade() {
        return grade;
    }

    public String getDisabilityType() {
        return disabilityType;
    }

    public String getGender() {
        return gender;
    }

    public String getUserId() {
        return userId;
    }



    public ChildDto_forUSE(){
        this.childCode="00000";
        this.birth="0000:11:22";
        this.disabilityType="NULL_DTYPE";
        this.gender="N";
        this.name="NULL_NAME";
        this.grade=0;
        this.userId ="NULLNOK";
    }


    public void setDisabilityType(String disabilityType) {
        this.disabilityType = disabilityType;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }


}
