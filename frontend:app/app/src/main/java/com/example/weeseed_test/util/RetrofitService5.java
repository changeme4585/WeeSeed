package com.example.weeseed_test.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class RetrofitService5 {

    private Retrofit retrofit;
    String baseURL;
    public RetrofitService5(){
        this.baseURL="http://172.20.47.160:8080";   //걍 암거나
        initializeRetrofit();
    }
    public RetrofitService5(String baseURL_from_viewmodel_getsvaddr){
        this.baseURL=baseURL_from_viewmodel_getsvaddr;
        initializeRetrofit();
    }

    private void initializeRetrofit(){
        OkHttpClient client = new OkHttpClient.Builder().build();


        retrofit=new Retrofit.Builder()
                .baseUrl(baseURL) //서버Url
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create()) // 문자열 응답 처리를 위해 ScalarsConverterFactory 사용
                .build();
    }

    public Retrofit getRetrofit(){
        return retrofit;
    }
}

