package com.example.weeseed_test.util;

import com.example.weeseed_test.dto.AacDto_url;
import com.example.weeseed_test.dto.Aac_default;
import com.example.weeseed_test.dto.ChildDto_ADD;
import com.example.weeseed_test.dto.ChildDto_forUSE;
import com.example.weeseed_test.dto.ExtendedAacCardDto;
import com.example.weeseed_test.dto.ExtendedAacCardSendDto;
import com.example.weeseed_test.dto.GrowthDto;
import com.example.weeseed_test.dto.GrowthDiaryDto;
import com.example.weeseed_test.dto.LoginDto;
import com.example.weeseed_test.dto.NokDto;
import com.example.weeseed_test.dto.PathologistDto;
import com.example.weeseed_test.dto.VideoDto;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Multipart;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Path;

import okhttp3.MultipartBody;


public interface RetrofitAPI {


    /////////241004 SignInController/////////

    @POST("/nokSignIn") // 보호자 회원가입
//@POST("/sign-in/nok") // 보호자 회원가입
    Call<String> nokSignIn(@Body NokDto user);

    @POST("/pathSignIn") // 재활사 회원가입
    Call<String> pathSignIn(@Body PathologistDto user);

    /////////LoginController/////////

    @POST("/login") // 로그인
    Call<String> login(@Body LoginDto user);

    @POST("/logout") // 로그아웃
    Call<String> logout();

    @POST("/checkSession") // 세션 체크
    Call<Map<String, String>> checkSession();

    //////FindUserInfoController///////
    @GET("/user/find-id")
    Call<String> findUserId(
            @Query("name") String name,
            @Query("email") String email
    );

    @POST("/user/change-password")
    Call<String> changePassword(
            @Query("userId") String userId,
            @Query("password") String password
    );


    ///////////no.png  Download image//////////

    @GET("/api/image/{fileName}")
    Call<ResponseBody> downloadImage(
            @Path("fileName") String fileName
    );

    //////////////RegistChildController/////////////////

//    @POST("/registchild") // 아동 등록
    @POST("/child/register") // 아동 등록
    Call<String> registchild(@Body ChildDto_ADD dto);


//    @POST("/linkchild")
    @POST("/child/link")
    Call<String> linkChild(@Query("childCode") String childCode, @Query("userId") String userId);


    //////////////ChildController/////////////////

    @GET("/NokChildInfo/{userId}")
    Call<List<ChildDto_forUSE>> getNokChildInfo(@Path("userId") String nokId);

    ////////ImageUploadController///////


    /////////////////AacController//////////////////
    @Multipart
    @POST("/aac/upload")
    Call<String> uploadAACCard(
            @Part MultipartBody.Part image,
            @Part("cardName") RequestBody cardName,
            @Part MultipartBody.Part audio,
            @Part("color") RequestBody color,
            @Part("childCode") RequestBody childCode,
            @Part("constructorId") RequestBody constructorId,
            @Part("share") RequestBody share
    );

    //실제 사용할 버전
    @GET("/aac/get")
    Call<List<AacDto_url>> getAacCards_web(
            @Query("childCode") String childCode,
            @Query("constructorId") String constructorId
    );

    @POST("/aac/delete")
    Call<Void> delete_aac2(
            @Query("aacCardId") Long cardId
    );

    @Multipart
    @POST("/aac/update")
    Call<Void> updateAacCard(
            @Part MultipartBody.Part image,
            @Part("childCode") RequestBody childCode,
            @Part("constructorId") RequestBody constructorId,
            @Part("aacCardId") RequestBody aacCardId,
            @Part("cardName") RequestBody cardName,
            @Part("newCardName") RequestBody newCardName
    );



    /////////////////default aac card///////////////
    @GET("/default-card/get")
    Call<List<Aac_default>> getAacCards_default(
            @Query("constructorId") String constructorId
    );
    @POST("/default-card/delete")
    Call<Void> delete_aac_default(
            @Query("constructorId") String constructorId,
            @Query("cardName") String cardName
    );

    //이거 안되면 put으로 호출해볼것
    @POST("/default-card/update")
    Call<Void> update_aac_default(
            @Query("constructorId") String constructorId,
            @Query("cardName") String cardName,
            @Query("newCardName") String newCardName
    );

    //////////VideoController//////////

    @Multipart
    @POST("/video/upload")
    Call<String> uploadVideoCard(
            @Part("cardName") RequestBody cardName,
            @Part MultipartBody.Part video,
            @Part("color") RequestBody color,
            @Part("childCode") RequestBody childCode,
            @Part("constructorId") RequestBody constructorId,
            @Part MultipartBody.Part thumbnail
    );
    @GET("/video/get")
    Call<List<VideoDto>> getVideoCards(
            @Query("childCode") String childCode,
            @Query("constructorId") String constructorId
    );

    @POST("/video/delete")
    Call<String> delete_video(
            @Query("videoCardId") Long videoCardId
    );


    /////////UserController/////////
//    @GET("/getNokInfo/{nokId}")
    @GET("/user/nok/{nokId}")
    Call<NokDto> getNokInfo(@Path("nokId") String nokId);

    //    @GET("/getPathInfo/{pathId}")
    @GET("/user/pathologist/{pathId}")
    Call<PathologistDto> getPathInfo(@Path("pathId") String pathId);

    @POST("user/resign")
    Call<Void> unregisterUser(
            @Query("constructorId") String constructorId,
            @Query("state") String userType
    );

    @POST("/user/nok/update")
    Call<Void> updateNok(@Body NokDto nokDto);

    @POST("/user/pathologist/update")
    Call<Void> updatePath(@Body PathologistDto pathologistDto);


    ////////////GrowthController////////////
    @POST("growth/log-click")
    Call<Void> sendClickLog(
            @Query("cardId") Long cardId,
            @Query("cardType") String cardType
    );  //클릭로그 보내기

    @GET("/growth/data")
    Call<List<GrowthDto>> getGrowthData(
            @Query("userId") String userId,
            @Query("childCode") String childCode
    );  //특정일의 생성 혹은 열람 카드의 목록인듯...

    @GET("/growth/diary")
    Call<List<GrowthDiaryDto>> getGrowthDiary(
            @Query("userId") String userId,
            @Query("childCode") String childCode
    );  //모든 일자의 성장일지를 부르는거인듯..

    @POST("/growth/create")
    Call<Void> createGrowth(
            @Query("userId") String userId,
            @Query("childCode") String childCode
    );  //성장일지 생성

    ////////////학습기능[TEMP]////////////

    //유사이미지 불러오기.   "이 카드의 확장이미지 목록 주세요(이 카드의 이미지는 빼고)"
    @GET("extend-aac/get/{repCardId}")
    Call<List<ExtendedAacCardDto>> getExtendedAacCards(
            @Path("repCardId") Long repCardId
    );

    //임의의 학습 기능용 api. score 70 이상, 호출. "이 카드의 확장카드 목록에 사진 하나 추가해주세요"
    @FormUrlEncoded
    @POST("extend-aac/update")
    Call<String> updateExtendedAacCards(
            @Field("repCardId") Long repCardId,
            @Field("cardName") String cardName,
            @Field("imagePath") String imagePath
    );
    @POST("extend-aac/update")
    Call<String> new_updateExtendedAacCards(@Body ExtendedAacCardSendDto cardDto);

    //이미지AI
    @Multipart
    @POST("/image/upload")
    Call<String> testImageAI(
            @Part MultipartBody.Part file
    );

    ///////////////////

    //gpt
    @GET("/gpt_test")
    Call<String> getGptResponse(
            @Query("gpt") String gpt
    );


    @POST("/save-speech-result")
    Call<Void> saveSpeechResult(
            @Query("childCode") String childCode,
            @Query("cardName") String cardName,
            @Query("score") String score
    );

    @GET("/feed-back")
    Call<String> askGptResponse(
            @Query("childCode") String childCode
    );




}