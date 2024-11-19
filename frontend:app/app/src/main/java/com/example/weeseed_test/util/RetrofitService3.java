package com.example.weeseed_test.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class RetrofitService3 {

    private Retrofit retrofit;
    String baseURL;
    public RetrofitService3(){
        this.baseURL="http://3.37.116.233:8080";   //걍 암거나
        initializeRetrofit();
    }
    public RetrofitService3(String baseURL_from_viewmodel_getsvaddr){
        this.baseURL=baseURL_from_viewmodel_getsvaddr;
        initializeRetrofit();
    }

    private void initializeRetrofit() {
        // Gson 인스턴스 설정
        Gson gson = new GsonBuilder().setLenient().create();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL) // 서버 URL
                .addConverterFactory(ScalarsConverterFactory.create()) // 문자열 응답 처리
                .addConverterFactory(GsonConverterFactory.create(gson)) // JSON 응답 처리
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}