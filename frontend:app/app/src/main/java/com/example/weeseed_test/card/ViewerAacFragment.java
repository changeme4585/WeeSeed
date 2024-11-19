package com.example.weeseed_test.card;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.weeseed_test.R;
import com.example.weeseed_test.activity.MainActivity;
import com.example.weeseed_test.card.adpater.ImageSliderAdapter;
import com.example.weeseed_test.databinding.FragmentViewerAacBinding;
import com.example.weeseed_test.dto.ExtendedAacCardDto;
import com.example.weeseed_test.dto.ExtendedAacCardSendDto;
import com.example.weeseed_test.util.RetrofitAPI;
import com.example.weeseed_test.util.RetrofitAPI2;
import com.example.weeseed_test.util.RetrofitService;
import com.example.weeseed_test.util.RetrofitService2;
import com.example.weeseed_test.util.RetrofitService3;
import com.example.weeseed_test.util.RetrofitService4;
import com.example.weeseed_test.util.Viewmodel;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewerAacFragment extends Fragment {

    Viewmodel viewModel;
    private FragmentViewerAacBinding binding;

    //img 관련 (pager)
    int clickCount=0;
    protected List<String> allImgs;   //기본 이미지 + 추가된 이미지들. -> pager로

    ///녹음 및 오디오 재생 관련
    final private static int MICROPHONE_PERMISSION_CODE = 200;

    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ((MainActivity) requireActivity()).getViewModel();

        //이미지 목록 처리
        allImgs = new ArrayList<>();    //초기화
        allImgs.add(viewModel.getSltd_aacItem().getImage());    //1st는 항상 orig

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentViewerAacBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        binding.tvAvAacCardName.setText(viewModel.getSltd_aacItem().getCardName()); //카드명

        //카드 선택 기록 남기기
        sendCardClicked_Server(viewModel.getSltd_aacItem().getAacCardId(),"aac");

        //mic 요청
        if(!isMicrophonePresent())
            getMicrophonePermission();

        //확장이미지 호출(아직 be 미구현)
        getSimilarImgs_Server(viewModel.getSltd_aacItem().getAacCardId());
        if(viewModel.getSltd_aacItem().getCardType()==2) {
            binding.wvSliderViewPager.setVisibility(View.GONE);
            binding.ivAvAacImgDefault.setVisibility(View.VISIBLE);
            Bitmap bitmap = BitmapFactory
                    .decodeByteArray(viewModel.getSltd_aacItem().getCardImage_def(), 0, viewModel.getSltd_aacItem().getCardImage_def().length);
            Glide.with(view.getContext())
                    .load(bitmap)
                    .into(binding.ivAvAacImgDefault);
        }
        else {
            binding.wvSliderViewPager.setVisibility(View.VISIBLE);
            binding.ivAvAacImgDefault.setVisibility(View.GONE);
            refreshPager();        //pager에 imgs들 불러오기
        }

        setupButtons();
        return view;
    }

    private void setupButtons(){
        binding.btnAvAacStudyRecord.setOnClickListener(v -> studyModule());
        binding.btnBackCardList.setOnClickListener(v -> getActivity().onBackPressed());

        binding.btnAvAacListen.setOnClickListener(v -> {
            if(viewModel.getSltd_aacItem().getCardType()==2) btnCARDAudioPressed_default();
            else btnCARDAudioPressed();
        });        //카드 음성 재생
        //이미지 목록 재호출
        binding.btnTempRefreshImgList.setOnClickListener(v -> getSimilarImgs_Server(viewModel.getSltd_aacItem().getAacCardId()));
        //유사 이미지 추가 요청
        binding.btnTempAddImgList.setOnClickListener(v -> {
//            addSimilarImg_Server(viewModel.getSltd_aacItem().getAacCardId(),
//                    viewModel.getSltd_childdto().getChildCode(),
//                    viewModel.getSltd_aacItem().getImage());
            addSimilarImg_Server(viewModel.getSltd_aacItem().getAacCardId(),
                    viewModel.getSltd_aacItem().getCardName(),
                    viewModel.getSltd_aacItem().getImage());
        });
    }

    ////////////////////////////////////////////////////////////////////

    public static String formatToTwoDecimalPlaces(String input) {
        BigDecimal number = new BigDecimal(input);
        number = number.setScale(2, RoundingMode.HALF_UP);  // 소수점 둘째 자리에서 반올림
        return number.toString();
    }

    private void saveSeepchResult(String origScore){
        String TAG = "saveSeepchResult";

        String childCode = viewModel.getSltd_childdto().getChildCode();
        String cardName = viewModel.getSltd_aacItem().getCardName();
        String score = formatToTwoDecimalPlaces(origScore);

        Log.e(TAG, "1: "+childCode+" "+cardName+" "+score);

        RetrofitService retrofitService = new RetrofitService(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI = retrofitService.getRetrofit().create(RetrofitAPI.class);
        try{
            retrofitAPI.saveSpeechResult(childCode, cardName, score)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.isSuccessful())
                                Log.e(TAG, "success: "+ response.code());
                            else Log.e(TAG, "failed: "+ response.code());
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {Log.e(TAG, "onFailure: "+t.getMessage());}
                    });
        }
        catch (Exception e){
            Log.e(TAG,"ERR) "+e.getMessage());
        }
    }

    ///////study func

    private void studyModule(){
        //학습버튼 클릭 시 동작 only my card

        /*
         * 1. 녹음 시작 (애니메이션 시작)
         * 2. 녹음 중단 (버튼 재클릭 OR time over(5s))
         * 3. 녹음물 전송 (checkVoice_Server)
         *       - waiting anime 시작 (in checkVoice)
         *   점수 수신하면,
         *       - waiting anime 종료 (in checkVoice)
         *     4. 결과 이펙트 frag 가시화 (닫기 시 비가시화)
         *     5. 유사 이미지 add 요청 (addSimilar)
         *       결과 수신하면,
         *           6. string의 종류에 따라 Toast 표시
         *           7. 확장 이미지 목록 호출 (getSimilar)
         * */

        //1. 녹음 시작
        btnRecordPressed();

        //부가처리
        //버튼 비활성화
        binding.btnAvAacStudyRecord.setClickable(false);
        binding.btnBackCardList.setClickable(false);    //상황 보고 이건 빼도됨
        binding.btnAvAacListen.setClickable(false);
        binding.btnAvAacListen.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.abtn_play_dis)); //들어보자 visual 변경

        //progress start
        ProgressBar progressBar = binding.pbAvDetermineForStudy;
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int progressStatus = 0;
            @Override
            public void run() {
                if (progressStatus < 100) {
                    progressStatus += 1;
                    progressBar.setProgress(progressStatus);
                    handler.postDelayed(this, 30); // 현재 3초
                } else {
                    // ProgressBar 완료 후 처리
                    btnStopPressed();
                    checkVoice_Server_score(); // 녹음물 전송      //1003 아직 주석.
//                    Toast.makeText(getActivity(), "개발 중인 기능입니다!", Toast.LENGTH_SHORT).show();   //1003
                    progressBar.setVisibility(View.INVISIBLE); // ProgressBar 숨기기
                    binding.btnBackCardList.setClickable(true);
                    binding.btnAvAacListen.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.abtn_play)); //들어보자 visual 변경
                }
            }
        };
        handler.post(runnable);
    }


    //////////temp_score (240919)
    private void checkVoice_Server_score(){
        /*
        * 3. 녹음물 전송 (checkVoice_Server)
         *       - waiting anime 시작 (in checkVoice)
         *   점수 수신하면,
         *       - waiting anime 종료 (in checkVoice)
         *     4. 결과 이펙트 frag 가시화 (닫기 시 비가시화)
         *     5. 유사 이미지 add 요청 (addSimilar)
         *       결과 수신하면,
         *           6. string의 종류에 따라 Toast 표시
         *           7. 확장 이미지 목록 호출 (getSimilar)
         * */

        binding.ltLoadingDots.setVisibility(View.VISIBLE);  //로딩 eff

        //[step1] packing list: name, score, file
        String card_name= binding.tvAvAacCardName.getText().toString();
        Log.d("checkVoice_Server_score[aacv]", "NM/SC: "+card_name+", ");

        File audioFile = new File(getRecordingFilePath());
        Log.d("checkVoice_Server_score[aacv]", "all packed");


        RetrofitService2 retrofitService = new RetrofitService2(viewModel.getVoice_svaddr());
        RetrofitAPI2 retrofitAPI2 = retrofitService.getRetrofit().create(RetrofitAPI2.class);

        // 오디오 파일의 RequestBody 생성
        RequestBody audioBody = RequestBody.create(MediaType.parse("audio/*"), audioFile);
        MultipartBody.Part audioPart = MultipartBody.Part.createFormData("audio", audioFile.getName(), audioBody);

        try{
            retrofitAPI2.soundcompare(audioPart, card_name)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            binding.ltLoadingDots.setVisibility(View.GONE);  //로딩 eff off
                            Log.e("aacStudy_Server_score[aacv]", "4: 카드명: "+card_name+" RESPONSE: "+response.body());
                            saveSeepchResult(response.body());

                            //학습 결과 처리
                            double doubleScore = Double.parseDouble(response.body());
                            int score = (int) doubleScore;

                            Log.e("aacStudy_Server_score[aacv]", "5 doubleScore: "+doubleScore + "  score: "+score);

                            //4. 결과 effect frag 가시화
                            int res_mode;
                            if(score>=70){      res_mode=1;
                                //5. 유사 이미지 add 요청
                                if (viewModel.getSltd_aacItem().getCardType() != 2)
                                    addSimilarImg_Server(
                                            viewModel.getSltd_aacItem().getAacCardId(),
                                            viewModel.getSltd_aacItem().getCardName(),
                                            viewModel.getSltd_aacItem().getImage()
                                    );
                            }
                            else if (score>=30) res_mode=2;
                            else                res_mode=3;

                            binding.btnAvAacStudyRecord.setClickable(true);
                            binding.btnAvAacListen.setClickable(true);
                            Bundle bundle = new Bundle();
                            bundle.putInt("res_mode",res_mode);
                            StudyResultFragment studyResultFragment = new StudyResultFragment();
                            studyResultFragment.setArguments(bundle);

                            ((MainActivity)getActivity()).addStackFragmentWithAnim_zoomIn(studyResultFragment);

                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            binding.ltLoadingDots.setVisibility(View.GONE);  //로딩 eff off
                            Log.e("aacStudy_ServerERR_ok", Objects.requireNonNull(t.getMessage()));
                            Toast.makeText(getActivity(), "오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            binding.btnAvAacStudyRecord.setClickable(true);
                            binding.btnAvAacListen.setClickable(true);
                        }
                    });
        }
        catch (Exception e){
            Log.e("aacStudy_ServerERR","ERR) "+e.getMessage());
            binding.btnAvAacStudyRecord.setClickable(true);
        }
    }

    public void onDestroyView() {super.onDestroyView();binding = null;}





    //////////////Record AUDIO[학습 및 재생]////////////////
    public void btnRecordPressed(){
        try{
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);    //출력 포맷
            mediaRecorder.setAudioSamplingRate(16000);
            mediaRecorder.setOutputFile(getRecordingFilePath());
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);   //오디오 인코딩

            mediaRecorder.prepare();
            mediaRecorder.start();
        }
        catch(Exception e){
            Log.e("btnRecordPressed", e.getMessage());
        }
    }
    public void btnStopPressed(){
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }
    public void btnCARDAudioPressed(){
        //학습 시 녹음한 발음 듣기
        try {
            mediaPlayer =new MediaPlayer();
            mediaPlayer.setDataSource(viewModel.getSltd_aacItem().getVoice());
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        catch (Exception e){Log.e("btnCARDAudioPressed", e.getMessage());}
    }

    public void btnCARDAudioPressed_default(){
        //학습 시 녹음한 발음 듣기 (기본카드의 경우)
        int defVoice;
        String dName=viewModel.getSltd_aacItem().getVoice();
        switch (dName) {
            case "dad":     defVoice=R.raw.ba_father;break;
            case "mom":     defVoice=R.raw.ba_mother;break;
            case "teacher": defVoice=R.raw.ba_teacher;break;

            case "yes":     defVoice=R.raw.ba_yes;break;
            case "no":      defVoice=R.raw.ba_no;break;

            case "rice":    defVoice=R.raw.ba_dish;break;
            case "sleep":   defVoice=R.raw.ba_sleep;break;
            case "toilet":  defVoice=R.raw.ba_toilet;break;
            case "sick":    defVoice=R.raw.ba_sick;break;

            case "hello":   defVoice=R.raw.ba_hello;break;
            case "giveme":  defVoice=R.raw.ba_gimme;break;
            default:        defVoice=R.raw.ba_yes; break;
        }
        Log.e("btnCARDAudioPressed_def", "dName=" + dName+" defVoice");


        try {
            mediaPlayer = MediaPlayer.create(getContext(),defVoice);
            mediaPlayer.start();
            binding.btnAvAacListen.setClickable(true);
        }
        catch (Exception e){Log.e("btnCARDAudioPressed_def", e.getMessage());}
    }


    private boolean isMicrophonePresent(){
        return this.getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
    }
    private void getMicrophonePermission(){
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)
                ==PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(getActivity(), new String[]
                    {Manifest.permission.RECORD_AUDIO},MICROPHONE_PERMISSION_CODE);
        }
    }
    private String getRecordingFilePath(){
        ContextWrapper contextWrapper = new ContextWrapper(getActivity().getApplicationContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(musicDirectory, "3gp_testRecordingFile"+".3gp");
        return file.getPath();
    }

    //////////서버로 click 정보 보내기////////////

    private void sendCardClicked_Server(Long cardId, String cardType){
        Log.e("sendAACClicked_Server", "1: ");

        RetrofitService retrofitService = new RetrofitService(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI = retrofitService.getRetrofit().create(RetrofitAPI.class);
        try{
            retrofitAPI.sendClickLog(cardId,cardType)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Log.e("sendAACClicked_Server", "3: 카드ID: "+viewModel.getSltd_aacItem().getAacCardId().toString());
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {Log.e("sendAACClicked_ServerERR", t.getMessage());}
                    });
        }
        catch (Exception e){
            Log.e("getAACCardFromServerERR","ERR) "+e.getMessage());
        }
    }


    ///유사 이미지 목록 가져오기
    private void getSimilarImgs_Server(Long cardId){
        /*
        * 용도: 확장 이미지 목록 호출
        * 결과: URL이 List<String> 형식으로 옴
        * 처리:
        *   case1: res에 item이 있음 => 전체 이미지 목록 = 기본 이미지 + 확장이미지 , pager에 등록.
        *   case2: res가 null (즉, 확장이미지가 0개) => 아무것도 안함
        * */
        Long newCardId = viewModel.getSltd_aacItem().getAacCardId();
//        newCardId = 65L;

        Log.e("getSimilarImgs_Server", "1: "+ newCardId.toString());

        RetrofitService retrofitService = new RetrofitService(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI = retrofitService.getRetrofit().create(RetrofitAPI.class);
        Log.e("getSimilarImgs_Server", "2: "+cardId.toString());

        try{
            retrofitAPI.getExtendedAacCards(newCardId)
                    .enqueue(new Callback<List<ExtendedAacCardDto>>() {
                        @Override
                        public void onResponse(Call<List<ExtendedAacCardDto>> call, Response<List<ExtendedAacCardDto>> response) {
                            Log.e("getSimilarImgs_Server", "4: (get) RESPONSE CODE: "+response.code());
                            //성공 시!
                            try{
                                //전체 이미지 목록 = 디폴트 + 확장이미지
                                if(!response.body().isEmpty()) {
                                    allImgs.clear();
                                    allImgs.add(viewModel.getSltd_aacItem().getImage());    //1st는 항상 orig

                                    for(ExtendedAacCardDto item : response.body())
                                    {
                                        allImgs.add("https://weeseed-uploads-ap-northeast-2.s3.amazonaws.com/"+item.getImageUrl());
                                        Log.e("getSimilarImgs_Server",item.getImageUrl());
                                    }
                                    refreshPager();
                                }
                            }
                            catch (Exception e) {
                                Log.e("getSimilarImgs_ServerERR", "5: " + e.getMessage());
                            }
                        }
                        @Override
                        public void onFailure(Call<List<ExtendedAacCardDto>> call, Throwable t) {
                            Log.e("getSimilarImgs_ServerERR", t.getMessage());
                            Toast.makeText(getActivity(), "ERR1: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        catch (Exception e){
            Log.e("getSimilarImgs_ServerERR","ERR2) "+e.getMessage());
        }
    }

    private String yameImagePathEdit(String str){
        if (str != null && str.endsWith(".jpg.jpg")) {
            return str.substring(0, str.length() - 4);
        }
        if (str != null && str.endsWith(".png.png")) {
            return str.substring(0, str.length() - 4);
        }
        return str;
    }
    //이미지 추가 요청 보내기
    private void addSimilarImg_Server(Long cardId, String cardName,String imagePath ){
        ExtendedAacCardSendDto cardDto = new ExtendedAacCardSendDto();
        cardDto.setRepCardId(cardId);
        cardDto.setCardName(cardName);
        cardDto.setImagePath(imagePath);

        Log.e("addSimilarImg_Server", "1: "+cardId+" "+cardName);

        RetrofitService4 retrofitService = new RetrofitService4(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI = retrofitService.getRetrofit().create(RetrofitAPI.class);
        Log.e("addSimilarImg_Server", "2: "+imagePath);

//        imagePath = yameImagePathEdit(imagePath);
        Log.e("addSimilarImg_Server", "2.1: "+imagePath);

        try{
            retrofitAPI.updateExtendedAacCards(cardId, cardName, imagePath)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if(response.isSuccessful()){
                                Log.e("addSimilarImg_Server", "4: (성공) RESPONSE: "+response.body());
                                //6. string의 종류에 따라 Toast 표시
//                            Toast.makeText(getActivity(), "결과: "+response.body(), Toast.LENGTH_SHORT).show();

                                //7. 확장 이미지 목록 호출
                                if (response.body() != null)
                                    Log.e("addSimilarImg_ServerERR1", "null 아님");
                                else
                                    Log.e("addSimilarImg_Server", "4: (성공) It's null");
                            }
                            else
                                Log.e("addSimilarImg_Server", "4: (update 실패) RESPONSE: " + response.code() + " " + response.message());
                            getSimilarImgs_Server(viewModel.getSltd_aacItem().getAacCardId());


                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("addSimilarImg_ServerERR1", t.getMessage());
                            getSimilarImgs_Server(viewModel.getSltd_aacItem().getAacCardId());
                            Toast.makeText(getActivity(), "오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        catch (Exception e){
            Log.e("addSimilarImg_ServerERR2","ERR) "+e.getMessage());
        }
    }


    ////////pager test 240923
    private void setupIndicators(int count){
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,0,10,0);

        binding.llLayoutIndicators.removeAllViews();  //indic view cnt = 0 으로 초기화

        for(int i=0; i<indicators.length; i++){
            indicators[i]=new ImageView(getContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.bg_indicator_inactive));
            indicators[i].setLayoutParams(params);
            binding.llLayoutIndicators.addView(indicators[i]);
        }
        setCurrentIndicator(0);
    }

    private void setCurrentIndicator(int position){
        int childCount = binding.llLayoutIndicators.getChildCount();
        Log.e("inditcount[cur]",clickCount++ +"  ::  "+childCount);
        for(int i = 0; i<childCount; i++){
            ImageView imageView = (ImageView) binding.llLayoutIndicators.getChildAt(i);
            if(i==position){
                imageView.setImageDrawable(ContextCompat.getDrawable(requireActivity(),R.drawable.bg_indicator_active));
            }
            else{
                imageView.setImageDrawable(ContextCompat.getDrawable(requireActivity(),R.drawable.bg_indicator_inactive));
            }
        }
    }

    //
    private void refreshPager (){
        //pager에 imgs들 불러오기
        binding.wvSliderViewPager.setOffscreenPageLimit(1);   //상태 유지할 page 개수
        binding.wvSliderViewPager.setAdapter(new ImageSliderAdapter(getContext(), getListToArr(allImgs)));   //어댑터 연결
        binding.wvSliderViewPager.setPadding(100,0,100,0);
        binding.wvSliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {    //page변경 시 callback ->
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });
        setupIndicators(allImgs.size());
    }

    //
    private String[] getListToArr(List<String> list){
        String[] imgarr = new String[list.size()];
        for(int i = 0; i<list.size();i++){
            imgarr[i]=list.get(i);
        }
        return imgarr;
    }

}