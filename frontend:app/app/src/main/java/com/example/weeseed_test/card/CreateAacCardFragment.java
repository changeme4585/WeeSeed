package com.example.weeseed_test.card;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.airbnb.lottie.LottieDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.weeseed_test.R;
import com.example.weeseed_test.activity.MainActivity;
import com.example.weeseed_test.card.adpater.LabelColorAdapter;
import com.example.weeseed_test.databinding.FragmentCreateAacCardBinding;
import com.example.weeseed_test.dto.LabelColor;
import com.example.weeseed_test.util.RetrofitAPI2;
import com.example.weeseed_test.util.RetrofitService2;
import com.example.weeseed_test.util.RetrofitService3;
import com.example.weeseed_test.util.Viewmodel;
import com.example.weeseed_test.util.RetrofitAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import android.net.Uri;
import android.app.Activity;
import androidx.annotation.Nullable;

import android.Manifest;

import org.json.JSONObject;


public class CreateAacCardFragment extends Fragment {

    Viewmodel viewModel;
    private FragmentCreateAacCardBinding binding;


    //이미지 관련
    private static final int REQUEST_IMAGE_PICK = 1; //img
    String imagePath;


    //녹음 관련 변수들
    private static int MICROPHONE_PERMISSION_CODE = 200;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    String audioPath; // 오디오 녹음 생성 파일 이름    //getRecordingFilePath()

    private int isRecording = 0;    // 현재 녹음 상태를 확인하기 위함.
    String audioUrl;
    static String ttsUrl;

    private Boolean isPlaying = false;

    ///카메라 촬영 관련

    final private static String TAG = "TAKE_PICTURE";
    String mCurrentPhotoPath;
    final static int REQUEST_TAKE_PHOTO = 100;

    //나머지

    String selectedLabelColorCode;
    Boolean isShare=true;
    int sharedStat=1;
    private LabelColorAdapter adapter;
    private boolean isTTS = false;

    ////
    enum Result_code {
        NONE, LOADING, WORD, PROPER, WRONG, WRONG_DESC
    }

    //////////////////////


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = ((MainActivity) requireActivity()).getViewModel();

        binding = FragmentCreateAacCardBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

      return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //init
        binding.btnCrAacImg.setVisibility(View.VISIBLE);
        controlImageAiResult(Result_code.NONE,"none");

        //마이크 권한 얻기
        if(isMicrophonePresent()){
            getMicrophonePermission();
        }
        else {Toast.makeText(getActivity(), "마이크가 없습니다", Toast.LENGTH_SHORT).show();}

        setUpButtons_onCreateView(view);
        setupButtons();

    }

    private void setUpButtons_onCreateView(View view){

        //녹음 시작
        binding.btnCrAacStartRecord.setOnClickListener(v -> btnRecordPressed(view));

        //녹음 종료
        binding.btnCrAacStopRecord.setOnClickListener(v -> {
            btnStopPressed(view);
            audioUrl=getRecordingFilePath();
        });

        //녹음 재생
        binding.btnCrAacPlayRecord.setOnClickListener(v -> btnPlayPressed(view));
        //통합 버전
        binding.btnCrAacRecord.setOnClickListener(v -> {
            //현재 녹음 상태에 따라 record 혹은 stop 혹은 trash
            if(isRecording==0) {
                btnRecordPressed(view);
                isRecording=1;
                binding.ivRecordIcon.setVisibility(View.INVISIBLE);
                binding.tvRecordMsg.setText("녹음 중입니다...");
                binding.btnCrAacRecord.setImageResource(R.drawable.icon_check);
            }
            else if(isRecording==1) {
                btnStopPressed(view);
                audioUrl=getRecordingFilePath();
                isRecording=2;
                binding.tvRecordMsg.setText("녹음되었습니다!");
                binding.btnCrAacRecord.setImageResource(R.drawable.icon_s_trash);
            }
            else {
                audioUrl=null;
                isRecording=0;
                binding.ivRecordIcon.setVisibility(View.VISIBLE);
                binding.tvRecordMsg.setText("음성을 녹음해주세요.");
                binding.btnCrAacRecord.setImageResource(R.drawable.icon_s_mike);
            }
        });
    }

    private void setupButtons(){
        adapter = new LabelColorAdapter(getContext());
        binding.spinnerCrAacLabel.setAdapter(adapter);

        binding.crAacBtnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardName = binding.crAacEtCardName.getText().toString().trim();
                if (!cardName.isEmpty()) {

                    if (binding.switchCreAacShare.isChecked())
                        sharedStat = 1;
                    else
                        sharedStat = 0;

                    if (isTTS)
                        audioUrl = ttsUrl;

                    Log.e("createAac", "이미지: " + imagePath
                            + "  오디오: " + audioUrl
                            + "  컬러코드: " + selectedLabelColorCode
                            + "  유저ID: " + viewModel.getUserID()
                            + "  아동코드: " + viewModel.getSltd_childdto().getChildCode()
                            + "  공유여부: " + sharedStat
                    );
                    Log.e("IMGPATH","imgToSend: "+imagePath);
                    File imageFile = new File(imagePath);
                    if (audioUrl != null) {
                        File audioFile = new File(audioUrl);
                        uploadAACCard(imageFile, audioFile,
                                binding.crAacEtCardName.getText().toString(),
                                selectedLabelColorCode,
                                viewModel.getSltd_childdto().getChildCode(),
                                viewModel.getUserID(),
                                sharedStat);
                    } else
                        Toast.makeText(getActivity(), "음성을 녹음해주세요!", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getActivity(), "카드 이름을 입력하세요", Toast.LENGTH_SHORT).show();
            }
        });

        //이미지 적절성 테스트
        binding.btnTestAacImg.setOnClickListener(v -> {

            String imagePath = binding.tvCrAacSelectImg.getText().toString();
            File imageFile = new File(imagePath);

            //안되면 밑으로 교체 (OutOfMemoryError 우려로 가급적 사용 삼가)
//            BitmapDrawable drawable = (BitmapDrawable) binding.ivCrAacImg.getDrawable();
//            Bitmap bitmap = drawable.getBitmap();
//            File imageFile = bitmapToFile(bitmap, "file");

            testImageAI(imageFile);
        });

        //초기 이미지 선택
        binding.btnCrAacImg.setOnClickListener(v -> {
            binding.layoutAcIsOK.setVisibility(View.VISIBLE);
            //카메라 권한 얻기
            if(isCameraPresent()){
                getCameraPermission();
            }
            else {Toast.makeText(getActivity(), "카메라가 없습니다", Toast.LENGTH_SHORT).show();}
        });

        //초기 선택 이후
        binding.ivCrAacImg.setOnClickListener(v -> {
//            pickImageFromGallery();
            binding.layoutAcIsOK.setVisibility(View.VISIBLE);
        });


        binding.btnCraLeft.setOnClickListener(v -> pickImageFromGallery());
        binding.btnCraRight.setOnClickListener(v -> CreateAacCardFragment.this.dispatchTakePictureIntent());


        binding.layoutAcIsOK.setOnClickListener(v -> binding.layoutAcIsOK.setVisibility(View.GONE));

        ////////////////////


        binding.spinnerCrAacLabel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 선택된 항목 처리
                LabelColor selectedLabelColor = (LabelColor) parent.getItemAtPosition(position);
                int colorInt = ContextCompat.getColor(getContext(), selectedLabelColor.getColor());
                selectedLabelColorCode = String.format("%06X",(0xFFFFFF & colorInt));
                Log.e("spinnerCrAacLabel", "[선택색상]" + selectedLabelColor.getName() + colorInt+" "+selectedLabelColorCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {selectedLabelColorCode = "767676";}// 아무 것도 선택되지 않은 경우
        });

        binding.btnImageAIApply.setOnClickListener(v -> binding.crAacEtCardName.setText(binding.tvImageAIWord.getText().toString()));

        binding.checkCreAacTts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //TTS 요청 넣기 (그 안에서 isTTS = true 할 것)
                    String cardn = binding.crAacEtCardName.getText().toString();
                    if (!cardn.isEmpty())
                        getTTSaudio(cardn);
                }
                else
                    isTTS = false;  //요청넣을 때 음성 경오를 isTTS에 따라 설정.
            }
        });

    }

    private void controlImageAiResult(Result_code code, String result_word){
        switch (code){
            case NONE:
            default:
                binding.ltImageAIAnime.setVisibility(View.INVISIBLE);
                binding.tvImageAIWord.setVisibility(View.GONE);
                binding.btnImageAIApply.setVisibility(View.INVISIBLE);

                binding.tvImageAIWord.setText("");
                binding.tvImageAIDesc.setText(" ");

                binding.ltImageAIAnime.setRepeatCount(LottieDrawable.INFINITE);
                binding.ltImageAIAnime.setAnimation(R.raw.lottie_loading_dots_small);

                break;
            case LOADING:
                binding.ltImageAIAnime.setVisibility(View.VISIBLE);
                binding.tvImageAIWord.setVisibility(View.INVISIBLE);
                binding.btnImageAIApply.setVisibility(View.INVISIBLE);

                binding.tvImageAIWord.setText("");
                binding.tvImageAIDesc.setText("사진을 검사하고 있습니다.");

                binding.ltImageAIAnime.setRepeatCount(LottieDrawable.INFINITE);
                binding.ltImageAIAnime.setAnimation(R.raw.lottie_loading_dots_small);

                break;
            case WORD:
                binding.ltImageAIAnime.setVisibility(View.VISIBLE);
                binding.tvImageAIWord.setVisibility(View.VISIBLE);
                binding.btnImageAIApply.setVisibility(View.VISIBLE);

                try {
                    JSONObject jsonObject = new JSONObject(result_word);
                    String detectedObject = jsonObject.getString("detected_object");
                    if (detectedObject.equals("Footwear"))
                        result_word = "신발";
                    binding.tvImageAIWord.setText(result_word);
                    binding.tvImageAIDesc.setText("사진인가요?");
                }
                catch (Exception e){
                    Log.e("controlImageAiResult","detect error");
                }

                binding.ltImageAIAnime.setRepeatCount(1);
                binding.ltImageAIAnime.setAnimation(R.raw.lottie_check);

                break;
            case PROPER:
                binding.ltImageAIAnime.setVisibility(View.VISIBLE);
                binding.tvImageAIWord.setVisibility(View.INVISIBLE);
                binding.btnImageAIApply.setVisibility(View.INVISIBLE);

                binding.tvImageAIWord.setText("");
                binding.tvImageAIDesc.setText("적절한 사진입니다!");

                binding.ltImageAIAnime.setRepeatCount(1);
                binding.ltImageAIAnime.setAnimation(R.raw.lottie_check);

                break;
            case WRONG:
                binding.ltImageAIAnime.setVisibility(View.VISIBLE);
                binding.tvImageAIWord.setVisibility(View.INVISIBLE);
                binding.btnImageAIApply.setVisibility(View.INVISIBLE);

                binding.tvImageAIWord.setText("");
                binding.tvImageAIDesc.setText("부적절한 사진입니다");

                binding.ltImageAIAnime.setRepeatCount(1);
                binding.ltImageAIAnime.setAnimation(R.raw.lottie_alert);

                break;
            case WRONG_DESC:
                binding.ltImageAIAnime.setVisibility(View.VISIBLE);
                binding.tvImageAIWord.setVisibility(View.INVISIBLE);
                binding.btnImageAIApply.setVisibility(View.INVISIBLE);

                binding.tvImageAIWord.setText("");
                switch (result_word) {
                    case "The image is too bright.":
                        binding.tvImageAIDesc.setText("사진이 너무 밝습니다");
                        break;
                    case "The image is too dark.":
                        binding.tvImageAIDesc.setText("사진이 너무 어둡습니다");
                        break;
                    default:
                        binding.tvImageAIDesc.setText("부적절한 사진입니다");
                        break;
                }
                binding.ltImageAIAnime.setRepeatCount(1);
                binding.ltImageAIAnime.setAnimation(R.raw.lottie_alert);

                break;
        }
    }

    private boolean isProperWord(String string){
        if(string == null || string.isEmpty())
            return false;

        if (string.equals("사용 불가능"))
            return false;

        String[] words = string.trim().split("\\s+");
        return words.length <= 2;   //이건 상황보고 제어
    }

    /////////IMAGE AI TEST/////////
    //testImageAI
    public void testImageAI(File imageFile) {
        controlImageAiResult(Result_code.LOADING,"none");
        if (!imageFile.exists()) {
            Log.e("testImageAI", "File does not exist: " + imageFile.getPath());
            return;
        }
        Log.e("testImageAI", "1: "+imageFile.getPath());
        RetrofitService3 retrofitService = new RetrofitService3(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI = retrofitService.getRetrofit().create(RetrofitAPI.class);

        // 이미지 파일의 RequestBody 생성
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);

        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("file", imageFile.getName(), requestFile); //protocol 맞춰줘야함 (name)
        // Retrofit을 사용하여 서버로 요청 보내기
        Log.e("testImageAI", "3: ");
        try{
            retrofitAPI.testImageAI(imagePart)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            if (response.isSuccessful()) {
                                // 요청 성공 시 처리
                                Log.e("testImageAI", "4: "+response.body());
                                if(isProperWord(response.body()))
                                    controlImageAiResult(Result_code.WORD,response.body());
                                else
                                    controlImageAiResult(Result_code.WRONG_DESC,response.body());

                            } else {
                                controlImageAiResult(Result_code.PROPER,"none");
                                Log.e("testImageAI", "ERR6: "+ response.code());
                            }
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("testImageAIERR", t.getMessage());
                            controlImageAiResult(Result_code.PROPER,"none");
                        }
                    });
        }
        catch (Exception e){
            Log.e("aacStudy_ServerERR","ERR) "+e.getMessage());
            controlImageAiResult(Result_code.PROPER,"none");
        }
    }

    //////////CREATE AAC CARD////////

    public void uploadAACCard(File imageFile, File audioFile, String cardName, String color, String childCode, String constructorId, int sharedStat) {
        RetrofitService3 retrofitService = new RetrofitService3(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI = retrofitService.getRetrofit().create(RetrofitAPI.class);

        Log.e("uploadAACCard", "1: ");

        // 이미지 파일의 RequestBody 생성
        RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), imageBody);
        Log.e("uploadAACCard", "2: ");

        // 오디오 파일의 RequestBody 생성
        RequestBody audioBody = RequestBody.create(MediaType.parse("audio/*"), audioFile);
        MultipartBody.Part audioPart = MultipartBody.Part.createFormData("audio", audioFile.getName(), audioBody);
        Log.e("uploadAACCard", "3: ");

        // 나머지 파라미터들의 RequestBody 생성
        RequestBody cardNameBody = RequestBody.create(MediaType.parse("text/plain"), cardName);
        RequestBody colorBody = RequestBody.create(MediaType.parse("text/plain"), color);
        RequestBody childCodeBody = RequestBody.create(MediaType.parse("text/plain"), childCode);
        RequestBody constructorIdBody = RequestBody.create(MediaType.parse("text/plain"), constructorId);
        RequestBody shareBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(sharedStat));

        Log.e("uploadAACCard", "4: ");

        // Retrofit을 사용하여 서버로 요청 보내기
        Call<String> call = retrofitAPI.
                uploadAACCard(imagePart, cardNameBody, audioPart, colorBody, childCodeBody, constructorIdBody, shareBody);
        Log.e("uploadAACCard", "5: ");

        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("uploadAACCard", "10: "+response.body());
                if (response.isSuccessful()) {
                    if(response.body().equals("*** AAC card created successfully ***")) {
                        Toast.makeText(getActivity(), "카드를 생성했습니다", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    }
                    else{
                        Toast.makeText(getActivity(), response.body(), Toast.LENGTH_SHORT).show();
                    }
                } else {Log.e("uploadAACCard", "ERR11: ");}
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // 통신 실패 시 처리 (뭔가 에러는 뜨는데 아무튼 서버에 들어가기는 함)
                Log.e("uploadAACCardERR", t.getMessage());
                if(t.getMessage().equals("JSON document was not fully consumed.")||t.getMessage().equals("End of input at line 1 column 1 path $")){
                    Toast.makeText(getActivity(), "카드를 생성했습니다!", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
                else
                    Toast.makeText(getActivity(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /////////pick Image//////////////
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();

            binding.layoutAcIsOK.setVisibility(View.GONE);
            try {
                Glide.with(this)
                        .load(selectedImageUri)
                        .into(binding.ivCrAacImg);

                imagePath = getRealPathFromURI(selectedImageUri);
                Log.d("Image Path", "Path: " + imagePath);
                binding.tvCrAacSelectImg.setText(imagePath);
                binding.btnCrAacImg.setVisibility(View.GONE);
                binding.ivCrAacImg.setVisibility(View.VISIBLE);

                 try{
                     File imageFile = new File(imagePath);   //방법1
                     testImageAI(imageFile);    //imageAI
                 }
                 catch (Exception e){Log.e("onActivityResult", "ERR: "+e.getMessage());}

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("onActivityResult", "ERR[ALB]: "+e.getMessage());
                Toast.makeText(getActivity(), "이미지를 로드하는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            binding.layoutAcIsOK.setVisibility(View.GONE);
            try {
                File imageFile = new File(imagePath);
                loadImageWithGlide(Uri.fromFile(imageFile));
                saveImageToGallery(imageFile);
                Log.e("onActivityResult", "[CAM1]: "+imagePath);

                binding.tvCrAacSelectImg.setText(imagePath);
                binding.btnCrAacImg.setVisibility(View.GONE);
                binding.ivCrAacImg.setVisibility(View.VISIBLE);
                try{
                    testImageAI(imageFile);
                }
                catch (Exception e){Log.e("onActivityResult", "ERR: "+e.getMessage());}
            }
            catch (Exception e) {
                e.printStackTrace();
                Log.e("onActivityResult", "ERR[CAM]: "+e.getMessage());
                Toast.makeText(getActivity(), "이미지 촬영 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    //img 경로 얻기
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = requireActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null)
            return contentUri.getPath();
        else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String filePath = cursor.getString(idx);
            cursor.close();
            return filePath;
        }
    }

    //////////////Record AUDIO////////////////
    public void btnRecordPressed(View v){
        try{
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(getRecordingFilePath());
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.prepare();
            mediaRecorder.start();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void btnStopPressed(View v){
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }
    public void btnPlayPressed(View v){
        try {
            mediaPlayer =new MediaPlayer();
            mediaPlayer.setDataSource(getRecordingFilePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(getActivity(), "녹음물 재생", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    private boolean isMicrophonePresent(){
        if(this.getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)){
            return true;
        }
        else return false;
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
        File file = new File(musicDirectory, "testRecordingFile"+".mp3");
        return file.getPath();
    }


    ///////////////////////bitmap -> file 변환 (240930)
    public File bitmapToFile(Bitmap bitmap, String fileName) {
        // 파일을 저장할 디렉토리 선택
        File storageDir = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "MyAppImages");

        // 디렉토리가 없으면 생성
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        // 새로운 파일을 생성
        File file = new File(storageDir, fileName + ".jpg");

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            // Bitmap을 JPG 포맷으로 압축하여 파일에 저장
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    ///////////카메라 촬영 관련
    private boolean isCameraPresent() {
        PackageManager packageManager = getActivity().getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }
    private void getCameraPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_TAKE_PHOTO);
        } else {
            Log.d(TAG, "카메라 및 저장소 권한이 이미 부여되었습니다.");
        }
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (Exception e) {
                Log.e(TAG, "Error creating image file"+e.getMessage());
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(requireActivity(), "com.example.weeseed_test.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    private File createImageFile() throws IOException {
        String timeStamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String imageFileName = "WeeSeed_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "WeeSeed");
        if (!storageDir.exists())
            storageDir.mkdirs();

        Log.e("TEST", "Storage Directory: " + storageDir.getAbsolutePath());
        File image = File.createTempFile( imageFileName, ".jpg", storageDir );
        imagePath = image.getAbsolutePath();
        Log.e("TEST", "Image Path: " + imagePath);
        return image;
    }
    private void saveImageToGallery(File file) {
        MediaScannerConnection.scanFile(requireContext(), new String[]{file.getAbsolutePath()}, null,
                (path, uri) -> Log.e(TAG, "Image saved to gallery: " + path));
    }
    private void loadImageWithGlide(Uri uri) {
        Glide.with(this)
                .load(uri)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e(TAG, "Image load failed: " + e.getMessage());
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false; // Glide가 이미지를 설정하도록 허용
                    }
                })
                .into(binding.ivCrAacImg);
    }

    //////

    private void getTTSaudio(String cardName){
        //retrofit
        String TAG = "getTTS";
        Log.e(TAG, "1: "+ cardName);

//        if (ttsUrl != null){
//            Log.e(TAG, "1.1: tts 파일이 이미 존재합니다. "+ttsUrl);
//            return;
//        }

        RetrofitService2 retrofitService = new RetrofitService2(viewModel.getVoice_svaddr());
        RetrofitAPI2 retrofitAPI2 = retrofitService.getRetrofit().create(RetrofitAPI2.class);

        try{
            retrofitAPI2.getTTS(cardName)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.isSuccessful() && response.body() != null){
//                                ttsUrl = saveTTSFileToTemp(requireContext(), response.body());
                                saveTTSFileToTemp(getContext(), response.body(), new FileSaveCallback() {
                                    @Override
                                    public void onFileSaved(String filePath) {
                                        // 파일 저장 성공 후 UI 작업 등 수행
                                        Log.d("TAG", "파일 저장 성공: " + filePath);
                                    }

                                    @Override
                                    public void onError(String error) {
                                        // 오류 처리
                                        Log.e("TAG", "파일 저장 오류: " + error);
                                    }
                                });

                                isTTS = true;
                            }
                            else
                                Log.e(TAG, "1: " + response.code());
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e(TAG, "onFailure 4: "+ t.getMessage());
                        }
                    });
        }
        catch (Exception e){
            Log.e(TAG, "ERR: "+ e.getMessage());
        }
    }

    public interface FileSaveCallback {
        void onFileSaved(String filePath);
        void onError(String error);
    }

    private static void saveTTSFileToTemp(Context context, ResponseBody body, FileSaveCallback callback) {
        new Thread(() -> {
            File tempFile = new File(context.getCacheDir(), "tts_temp.mp3");

            try (InputStream inputStream = body.byteStream();
                 OutputStream outputStream = new FileOutputStream(tempFile)) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                // 파일 저장 완료 후 콜백 호출 (메인 스레드에서 실행)
                new Handler(Looper.getMainLooper()).post(() -> {
                    // Update the global variable str with the file path
                    ttsUrl = tempFile.getAbsolutePath();
                    callback.onFileSaved(ttsUrl); // pass the file path to the callback
                });

            } catch (IOException e) {
                Log.e("TAG", "TTS 음성 저장 실패: " + e.getMessage());
                new Handler(Looper.getMainLooper()).post(() -> callback.onError(e.getMessage()));
            }
        }).start();
    }


}