package com.example.weeseed_test.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService2 {

    private Retrofit retrofit;
    String voiceBaseURL;

    public RetrofitService2(){
        this.voiceBaseURL="http://116.126.197.110:30010";   //걍 암거나
        initializeRetrofit();
    }
    public RetrofitService2(String baseURL_from_viewmodel_getVoice_svaddr){
        this.voiceBaseURL=baseURL_from_viewmodel_getVoice_svaddr;
        initializeRetrofit();
    }


    private void initializeRetrofit(){

        //response 에러 해결 목적으로  JsonReader.setLenient(true) 사용해서 response 받기
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit=new Retrofit.Builder()
                .baseUrl(voiceBaseURL) //오디오 ai용
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }


    public Retrofit getRetrofit(){
        return retrofit;
    }
}
