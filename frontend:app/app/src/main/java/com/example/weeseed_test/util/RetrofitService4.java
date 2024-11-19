package com.example.weeseed_test.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class RetrofitService4 {

    private Retrofit retrofit;
    String baseURL;
    public RetrofitService4(){
        this.baseURL="http://3.37.116.233:8080";   //걍 암거나
        initializeRetrofit();
    }
    public RetrofitService4(String baseURL_from_viewmodel_getsvaddr){
        this.baseURL=baseURL_from_viewmodel_getsvaddr;
        initializeRetrofit();
    }

    private void initializeRetrofit(){

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(byte[].class,new ByteArrayToBase64TypeAdapter())
                .setLenient()
                .create();

        retrofit=new Retrofit.Builder()
                .baseUrl(baseURL) //서버Url
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public Retrofit getRetrofit(){
        return retrofit;
    }
}

