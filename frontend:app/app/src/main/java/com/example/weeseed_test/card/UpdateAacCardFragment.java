package com.example.weeseed_test.card;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.weeseed_test.activity.MainActivity;
import com.example.weeseed_test.databinding.FragmentUpdateAacBinding;
import com.example.weeseed_test.dto.Aac_item;
import com.example.weeseed_test.util.RetrofitAPI;
import com.example.weeseed_test.util.RetrofitService;
import com.example.weeseed_test.util.RetrofitService3;
import com.example.weeseed_test.util.Viewmodel;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UpdateAacCardFragment extends Fragment {

    Viewmodel viewModel;
    private FragmentUpdateAacBinding binding;

    //
    private Aac_item initAacCard, newAacCard;


    //이미지 관련
    private static final int REQUEST_IMAGE_PICK = 1; //img
    String imagePath;
    ///카메라 촬영 관련
    final private static String TAG = "TAKE_PICTURE";
    final static int REQUEST_TAKE_PHOTO = 100;

    //////////////////////


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdateAacBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        viewModel = ((MainActivity) requireActivity()).getViewModel();
        initAacCard = new Aac_item(viewModel.getSltd_aacItem());
        newAacCard = new Aac_item(viewModel.getSltd_aacItem());
        imagePath = viewModel.getSltd_aacItem().getImage();

        Log.e("initAacCard", "1.5: "
                +initAacCard.getImage()
                +initAacCard.getAacCardId()
                +initAacCard.getCardType()
        );
        Log.e("newAacCard", "1.5: "
                +newAacCard.getImage()
                +newAacCard.getAacCardId()
                +newAacCard.getCardType()
        );
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUp_loadImageWithGlide_forInit();
        setupButtons();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    ///////UI setting////////
    private void setupButtons(){
        binding.udAacEtCardName.setText(initAacCard.getCardName());
        binding.udAacBtnGo.setOnClickListener(v -> {
            updateAacModule();
        });
        binding.ivUdAacImg.setOnClickListener(v -> {
            if(initAacCard.getCardType()==0){
                binding.layoutAcIsOK.setVisibility(View.VISIBLE);
                //카메라 권한 얻기
                if(isCameraPresent()){
                    getCameraPermission();
                }
                else {Toast.makeText(getActivity(), "카메라가 없습니다", Toast.LENGTH_SHORT).show();}
            }
            else {Toast.makeText(getActivity(), "기본카드는 이미지를 수정할 수 없습니다.", Toast.LENGTH_SHORT).show();}

        });
        binding.btnCraLeft.setOnClickListener(v -> pickImageFromGallery());
        binding.btnCraRight.setOnClickListener(v -> dispatchTakePictureIntent());
        binding.layoutAcIsOK.setOnClickListener(v -> binding.layoutAcIsOK.setVisibility(View.GONE));
    }
    private void setUp_loadImageWithGlide_forInit() {
        if(initAacCard.getCardType()==2) {
            Bitmap bitmap = BitmapFactory
                    .decodeByteArray(initAacCard.getCardImage_def(), 0, initAacCard.getCardImage_def().length);
            Glide.with(this)
                    .load(bitmap)
                    .into(binding.ivUdAacImg);
        }
        else{
            Glide.with(this)
                    .load(initAacCard.getImage())
                    .into(binding.ivUdAacImg);
        }
    }


    //////////////////////////

    /////////IMAGE AI TEST/////////
    public void testImageAI(File imageFile) {
        if (!imageFile.exists()) {
            Log.e("testImageAI", "File does not exist: " + imageFile.getPath());
            return;
        }
        Log.e("testImageAI", "1: "+imageFile.getPath());
        RetrofitService3 retrofitService = new RetrofitService3(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI = retrofitService.getRetrofit().create(RetrofitAPI.class);
        Log.e("testImageAI", "2: ");


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
                                Toast.makeText(getActivity(), response.body().toString(), Toast.LENGTH_SHORT).show();
                            } else {
//                                Toast.makeText(getActivity(), "전송실패", Toast.LENGTH_SHORT).show();   // 요청 실패 시 처리
                                Log.e("testImageAI", "ERR6: "+ response.code());
                            }
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("testImageAIERR", t.getMessage());
                            Toast.makeText(getActivity(), "오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        catch (Exception e){
            Log.e("aacStudy_ServerERR","ERR) "+e.getMessage());
        }
    }

    //////////UPDATE AAC CARD////////

    private void updateAacModule(){
        String cardName = binding.udAacEtCardName.getText().toString().trim();
        if (cardName.isEmpty()){
            Toast.makeText(getActivity(), "카드 이름을 입력하세요", Toast.LENGTH_SHORT).show();
        }
        else {
            newAacCard.setCardName(cardName);
            newAacCard.setImage(imagePath);

            Log.e("update", "이미지: " + imagePath
                    + "  아동ID: " + newAacCard.getChildId()
                    + "  카드명: " + newAacCard.getCardName()
            );

            //동일할 시 걍 뒤로가기
            if(compareAacCard(initAacCard,newAacCard))      getActivity().onBackPressed();
            else{
                //ㄹㅇ 수정
                if(initAacCard.getCardType()==0)
                    updateAACCard(initAacCard, newAacCard);
                else
                    updateAACCard_default(initAacCard, newAacCard);
            }
        }
    }

    private boolean compareAacCard(Aac_item old_item, Aac_item new_item){
        Log.e("compare", "1: "
                +old_item.getImage()
                +old_item.getAacCardId()
                +old_item.getCardType()
        );
        Log.e("compare", "1.5: "
                +new_item.getImage()
                +new_item.getAacCardId()
                +new_item.getCardType()
        );

        if(!(old_item.getImage().equals(new_item.getImage())))
        {
            Log.e("compare", "2: ");
            return false;
        }
        if(!(old_item.getCardName().equals(new_item.getCardName())))
        {
            Log.e("compare", "3: ");
            return false;
        }
        if(old_item.getCardType() != (new_item.getCardType()))
        {
            Log.e("compare", "4: ");
            return false;
        }
        Log.e("compare", "5: it's same");

        return true;
    }

    public void updateAACCard(Aac_item old_item, Aac_item new_item) {

        File imageFile = new File(new_item.getImage());
        String childCode = old_item.getColor();
        String constructorId = old_item.getConstructorId();
        String cardName = old_item.getCardName();
        String newCardName = new_item.getCardName();

        //
        RetrofitService3 retrofitService = new RetrofitService3(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI = retrofitService.getRetrofit().create(RetrofitAPI.class);

        Log.e("retrofit", "1: ");

        //for img
        RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), imageBody);
        Log.e("retrofit", "2: ");

        // ect
        RequestBody childCodeBody = RequestBody.create(MediaType.parse("text/plain"), childCode);
        RequestBody constructorIdBody = RequestBody.create(MediaType.parse("text/plain"), constructorId);
        RequestBody aacCardIdBody = RequestBody.create(MediaType.parse("text/plain"), childCode);
        RequestBody cardNameBody = RequestBody.create(MediaType.parse("text/plain"), cardName);
        RequestBody newCardNameBody = RequestBody.create(MediaType.parse("text/plain"), newCardName);

        Log.e("retrofit", "4: ");

        Call<Void> call = retrofitAPI.
                updateAacCard(imagePart, childCodeBody, constructorIdBody, aacCardIdBody, cardNameBody, newCardNameBody);
        Log.e("retrofit", "5: ");

        call.enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "카드를 수정했습니다", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                } else {Log.e("retrofit", "ERR11: ");}
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("uploadAACCardERR", t.getMessage());
                if(t.getMessage().equals("JSON document was not fully consumed.")||t.getMessage().equals("End of input at line 1 column 1 path $")){
                    Toast.makeText(getActivity(), "카드를 수정했습니다", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
                else
                    Toast.makeText(getActivity(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void updateAACCard_default(Aac_item old_item, Aac_item new_item) {

        String dName;
        switch (old_item.getCardName()) {
            case "아빠": dName = "dad"; break;
            case "엄마": dName = "mom"; break;
            case "선생님": dName = "teacher"; break;
            case "네": dName = "yes"; break;
            case "아니요": dName = "no"; break;
            case "밥": dName = "rice"; break;
            case "잠": dName = "sleep"; break;
            case "화장실": dName = "toilet"; break;
            case "아파요": dName = "sick"; break;
            case "안녕하세요": dName = "hello"; break;
            case "주세요": dName = "giveme"; break;
            default: dName = old_item.getCardName();
        }

        RetrofitService retrofitService=new RetrofitService(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI=retrofitService.getRetrofit().create(RetrofitAPI.class);
        Log.e("updateAACCard_default","1: "+ dName+" -> "+new_item.getCardName());

        try{
            retrofitAPI.update_aac_default(old_item.getConstructorId(), dName, new_item.getCardName())
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Log.e("updateAACCard_default","성공: " +response.code());
                                Toast.makeText(getActivity(), "카드명을 수정했습니다", Toast.LENGTH_SHORT).show();
                                getActivity().onBackPressed();
                            } else {
                                // 서버 오류 등의 이유로 요청 실패
                                Log.e("updateAACCard_default","서버 오류: " +response.code());
                                Toast.makeText(getActivity(), "서버 오류: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("updateAACCard_default", t.getMessage());
                            Toast.makeText(getActivity(), "오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        catch (Exception e){
            Log.e("updateAACCard_default","ERR) "+e.getMessage());
            Toast.makeText(getActivity(), "오류", Toast.LENGTH_SHORT).show();
        }
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
                        .into(binding.ivUdAacImg);

                imagePath = getRealPathFromURI(selectedImageUri);
                Log.d("Image Path", "Path: " + imagePath);
                binding.tvUdAacSelectImg.setText(imagePath);

//                try{
//                    File imageFile = new File(imagePath);   //방법1
//                    testImageAI(imageFile);    //imageAI
//                }
//                catch (Exception e){Log.e("onActivityResult", "ERR: "+e.getMessage());}

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

                binding.tvUdAacSelectImg.setText(imagePath);
//                try{
//                    testImageAI(imageFile);
//                }
//                catch (Exception e){Log.e("onActivityResult", "ERR: "+e.getMessage());}
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
                .into(binding.ivUdAacImg);
    }


}