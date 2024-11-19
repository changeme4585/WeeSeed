package com.example.weeseed_test.util;

import androidx.lifecycle.ViewModel;

public class IsOkViewmodel extends ViewModel {
    /*
    * 용도: isOk dig를 통한 fragment간 통신 시 내용 공유
    * */
    private String situation_isOk;  //버튼 처리 시 사용하는 태그
    // 종류:
    // signIn
    // deleteCARD

    private boolean isOkChoice;

    private String str_title_isOk;
    private String str_desc_isOk;
    private String str_left_isOk;
    private String str_right_isOk;

    //////
    public String getSituation_isOk() {
        return situation_isOk;
    }
    public void setSituation_isOk(String situation_isOk) {
        this.situation_isOk = situation_isOk;
    }

    public String getStr_desc_isOk() {
        return str_desc_isOk;
    }
    public void setStr_desc_isOk(String str_desc_isOk) {
        this.str_desc_isOk = str_desc_isOk;
    }

    public String getStr_left_isOk() {
        return str_left_isOk;
    }
    public void setStr_left_isOk(String str_left_isOk) {
        this.str_left_isOk = str_left_isOk;
    }

    public String getStr_right_isOk() {
        return str_right_isOk;
    }
    public void setStr_right_isOk(String str_right_isOk) {this.str_right_isOk = str_right_isOk;}

    public String getStr_title_isOk() {
        return str_title_isOk;
    }
    public void setStr_title_isOk(String str_title_isOk) {
        this.str_title_isOk = str_title_isOk;
    }

    public boolean getIsOkChoice() {return isOkChoice;}
    public void setIsOkChoice(boolean okChoice) {isOkChoice = okChoice;}
}
