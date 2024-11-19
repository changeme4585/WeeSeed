package com.example.weeseed_test.util;

import androidx.lifecycle.ViewModel;

import com.example.weeseed_test.dto.Aac_item;
import com.example.weeseed_test.dto.GrowthDiaryDto;
import com.example.weeseed_test.dto.Video_item;
import com.example.weeseed_test.dto.ChildDto_ADD;
import com.example.weeseed_test.dto.ChildDto_forUSE;
import com.example.weeseed_test.dto.NokDto;
import com.example.weeseed_test.dto.PathologistDto;

public class Viewmodel extends ViewModel {
    //용도: frag 이동 시 굳이 번들로 싸기엔 귀찮은 정보들 모아두는 곳

    private String svaddr;  //서버주소 저장용
    private String voice_svaddr;  //서버주소 저장용
    private String gptsvaddr;  //서버주소 저장용

    //////기본적으로 사용되는 정보들!//////
    private String sessionID;
    private String userID;
    private String userType;    //유저가 Nok인지 Path인지

    //////로그인한 유저////// (생성 시기: settingFrag 진입)
    private NokDto userNok;                     //nok로 로그인한 유저의 정보
    private PathologistDto userPatho;           //patho로 로그인한 유저의 정보
    private ChildDto_forUSE sltd_childdto;    //선택 아동

    private Boolean isLocked;              //현재 잠금 여부
    private Boolean isVideoList=false;    //현재 표시하는 카드 목록 종류: AAC OR 비디오

    //현재 선택한 aac/vid 카드/성장일지
    private Aac_item sltd_aacItem;
    private Video_item sltd_videoCard;
    private GrowthDiaryDto sltd_diary;

    //유틸
    private String situation_datePicker;
    private String dateForData;
    private String dateForStr;
    private int stepAddChild=0;

    private ChildDto_ADD addingChildDto; //아동 추가 시 사용하는 dto...를 굳이 여기 넣을 필요가 있나?

    ////////////////////


    public void setSvaddr(String ip, String port){this.svaddr = "http://"+ip+":"+port;}
    public void setSvaddr(String svaddr) {this.svaddr = svaddr;}    //"http://있는 형태
    public String getSvaddr(){return svaddr;}


    public void setSvaddr_g(String ip, String port){this.gptsvaddr = "http://"+ip+":"+port;}
    public void setSvaddr_g(String svaddr) {this.gptsvaddr = svaddr;}    //"http://있는 형태
    public String getSvaddr_g(){return gptsvaddr;}

    //////


    public void setVoice_svaddr(String ip, String port){this.voice_svaddr = "http://"+ip+":"+port;}
    public void setVoice_svaddr(String voice_svaddr) {this.voice_svaddr = voice_svaddr;}
    public String getVoice_svaddr() {return voice_svaddr;}

    ////////////
    public void setStepAddChild(int stepAddChild) {this.stepAddChild = stepAddChild;}

    public int getStepAddChild() {return stepAddChild;}

    ///////////////////

    public ChildDto_ADD getAddingChildDto() {return addingChildDto;}
    public void setAddingChildDto(ChildDto_ADD addingChildDto) {this.addingChildDto = addingChildDto;}
    public void setDateForData(String dateForData) {
        this.dateForData = dateForData;
    }
    public void setDateForStr(String dateForStr) {
        this.dateForStr = dateForStr;
    }
    public String getDateForData() {
        return dateForData;
    }
    public String getDateForStr() {
        return dateForStr;
    }
    public String getSituation_datePicker() {
        return situation_datePicker;
    }
    public void setSituation_datePicker(String situation_datePicker) {this.situation_datePicker = situation_datePicker;}
    public Boolean getIsVideoList() {
        return isVideoList;
    }
    public void setIsVideoList(Boolean videoList) {isVideoList = videoList;}
    public Video_item getSltd_videoCard() {
        return sltd_videoCard;
    }
    public void setSltd_videoCard(Video_item sltd_videoCard) {this.sltd_videoCard = sltd_videoCard;}
    public Aac_item getSltd_aacItem() {return sltd_aacItem;}//// 선택 카드로 설정 [현재 공통포맷]
    public void setSltd_aacItem(Aac_item sltd_aacItem) {this.sltd_aacItem = sltd_aacItem;}
    public Boolean getLocked() {
        return isLocked;
    }
    public void setLocked(Boolean locked) {
        isLocked = locked;
    }
    public ChildDto_forUSE getSltd_childdto() {
        return sltd_childdto;
    }
    public void setSltd_childdto(ChildDto_forUSE sltd_childdto) {this.sltd_childdto = sltd_childdto;}
    public String getUserType() {
        return userType;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }
    public void setSessionID(String sessionID) { this.sessionID = sessionID;}
    public String getSessionID() { return sessionID;}
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }



    ///얘네는 쓸모를 좀 연구해보고 없으면 지워야...
    public NokDto getUserNok() {
        return userNok;
    }
    public void setUserNok(NokDto userNok) {
        this.userNok = userNok;
    }

    public PathologistDto getUserPatho() {
        return userPatho;
    }
    public void setUserPatho(PathologistDto userPatho) {
        this.userPatho = userPatho;
    }

    /////


    public GrowthDiaryDto getSltd_diary() {
        return sltd_diary;
    }

    public void setSltd_diary(GrowthDiaryDto sltd_diary) {
        this.sltd_diary = sltd_diary;
    }
}


