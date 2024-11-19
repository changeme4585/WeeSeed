package com.example.weeseed_test.util;

import com.example.weeseed_test.dto.NokDto;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;


public interface RetrofitAPI2 {

    //오디오 ai 테스트용
    @Multipart
    @POST("/api/soundcompare")
    Call<String> soundcompare (
            @Part MultipartBody.Part audio,  //녹음한 audio
            @Part("card_name") String card_name
    );

    //TTS

    @Streaming
    @POST("/api/tts")
    Call<ResponseBody> getTTS(
            @Query("cardName") String cardName
    );






    //////////
    //무조건 OK
    @Multipart
    @POST("/api/soundcompare/ok")
    Call<String> soundcompare_ok (
            @Part MultipartBody.Part audio,  //녹음한 audio
            @Part("card_name") String card_name
    );

    //무조건 FAIL
    @Multipart
    @POST("/api/soundcompare/notok")
    Call<String> soundcompare_notok (
            @Part MultipartBody.Part audio,  //녹음한 audio
            @Part("card_name") String card_name
    );

    //test/숫자
//    @POST("/api/voice/similarity-check")
    @Multipart
    @POST("/api/soundcompare/test/{score}")
    Call<String> soundcompare_score (
            @Path("score") String score,
            @Part MultipartBody.Part audio,  //녹음한 audio
            @Part("card_name") String card_name
    );


    /////////////시각




}