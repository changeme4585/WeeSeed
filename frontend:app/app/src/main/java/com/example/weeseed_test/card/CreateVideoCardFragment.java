package com.example.weeseed_test.card;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.weeseed_test.activity.MainActivity;
import com.example.weeseed_test.card.adpater.LabelColorAdapter;
import com.example.weeseed_test.databinding.FragmentCreateVideoCardBinding;
import com.example.weeseed_test.dto.LabelColor;
import com.example.weeseed_test.util.RetrofitService3;
import com.example.weeseed_test.util.Viewmodel;
import com.example.weeseed_test.util.RetrofitAPI;
import com.example.weeseed_test.util.RetrofitService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class CreateVideoCardFragment extends Fragment {

    Viewmodel viewModel;
    private FragmentCreateVideoCardBinding binding;

    //비디오 관련
    private static final int REQUEST_VIDEO_PICK = 1; //vid
    String videoPath;

    //기타
    String selectedLabelColorCode;

    //비디오 썸네일
    Bitmap thumbnail;

    private LabelColorAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = ((MainActivity) requireActivity()).getViewModel();

        binding = FragmentCreateVideoCardBinding.inflate(inflater,container,false);
        View view = binding.getRoot();


        ///////


        binding.crVideoBtnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardName = binding.crVideoEtCardName.getText().toString().trim();
                if(!cardName.isEmpty()){
                    Log.e("crVideoBtnGo","비디오: "+videoPath
                            +"  카드명: "+binding.crVideoEtCardName.getText().toString()
                            +"  컬러코드: "+selectedLabelColorCode
                            +"  유저ID: "+viewModel.getUserID()
                            +"  아동코드: "+viewModel.getSltd_childdto().getChildCode()
                    );
                    File videoFile = new File(videoPath);
                    File thumbnailFile = bitmapToFile(thumbnail);

                    //비디오 추가
                    uploadvideocard(videoFile,
                            binding.crVideoEtCardName.getText().toString(),
                            selectedLabelColorCode,
                            viewModel.getSltd_childdto().getChildCode(),
                            viewModel.getUserID(),
                            thumbnailFile);
                }
                else
                    Toast.makeText(getActivity(), "카드 이름을 입력하세요", Toast.LENGTH_SHORT).show();
            }
        });

        /////비디오 선택////
        binding.btnCrVideo.setOnClickListener(v -> pickVideoFromGallery());
        binding.ivCrVideoVid.setOnClickListener(v -> pickVideoFromGallery());

        ///라벨////
        adapter = new LabelColorAdapter(getContext());
        binding.spinnerCrVideoLabel.setAdapter(adapter);

        binding.spinnerCrVideoLabel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        return view;
    }


    public void uploadvideocard(File videoFile, String cardName, String color, String childCode, String constructorId, File thumbnailFile) {
        RetrofitService3 retrofitService = new RetrofitService3(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI = retrofitService.getRetrofit().create(RetrofitAPI.class);

        Log.e("uploadVideoCard", "1: ");

        // 비디오 파일의 RequestBody 생성
        RequestBody videoBody = RequestBody.create(MediaType.parse("multipart/form-data"), videoFile);
        MultipartBody.Part videoPart = MultipartBody.Part.createFormData("video", videoFile.getName(), videoBody);
        Log.e("uploadVideoCard", "2: ");

        // Thumbnail 파일의 RequestBody 생성
        RequestBody thumbnailBody = RequestBody.create(MediaType.parse("multipart/form-data"), thumbnailFile);
        MultipartBody.Part thumbnailPart = MultipartBody.Part.createFormData("thumbnail", thumbnailFile.getName(), thumbnailBody);


        // 나머지 파라미터들의 RequestBody 생성
        RequestBody cardNameBody = RequestBody.create(MediaType.parse("text/plain"), cardName);
        RequestBody colorBody = RequestBody.create(MediaType.parse("text/plain"), color);
        RequestBody childCodeBody = RequestBody.create(MediaType.parse("text/plain"), childCode);
        RequestBody constructorIdBody = RequestBody.create(MediaType.parse("text/plain"), constructorId);

        Log.e("uploadVideoCard", "3: ");

        // Retrofit을 사용하여 서버로 요청 보내기
        Call<String> call = retrofitAPI.
                uploadVideoCard(cardNameBody,
                        videoPart,
                        colorBody,
                        childCodeBody,
                        constructorIdBody,
                        thumbnailPart);

        Log.e("uploadVideoCard", "4: ");

        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    Log.e("uploadVideoCard", "5: "+response.body());
                    // 요청 성공 시 처리
                    if(response.body().equals("비디오 카드가 성공적으로 생성되었습니다.")) {
                        Toast.makeText(getActivity(), "카드를 생성했습니다", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    }
                    else{
                        Toast.makeText(getActivity(), response.body(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("uploadVideoCard", "ERR6: ");
                    // 요청 실패 시 처리
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // 통신 실패 시 처리 (뭔가 에러는 뜨는데 아무튼 서버에 들어가기는 함)
                Log.e("uploadVideoCard", t.getMessage());
                if(t.getMessage().equals("JSON document was not fully consumed.")){
                    Toast.makeText(getActivity(), "카드를 생성했습니다!", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
                else
                    Toast.makeText(getActivity(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    ////////////pick Video//////////

    private void pickVideoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(intent, REQUEST_VIDEO_PICK);
    }

    //onActivityResult override하고 비디오 경로 얻는 메소드 추가할 것

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_VIDEO_PICK && resultCode == Activity.RESULT_OK) {
            Uri selectedVideoUri = data.getData();
            if (data != null) {
                // 선택한 비디오의 URI를 얻습니다.
                try {
                    videoPath = getRealPathFromUri(selectedVideoUri);
                    //비디오 경로 표시
                    binding.tvCrVideoSelectVid.setText(videoPath);
                    //비디오 썸네일 표시
                    thumbnail = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MINI_KIND);
                    binding.ivCrVideoVid.setImageBitmap(thumbnail);
                    binding.ivCrVideoVid.setVisibility(View.VISIBLE);
                    binding.btnCrVideo.setVisibility(View.GONE);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("video", "[비디오 로드 오류]" + e.getMessage());
                    if(!(e.getMessage().equals("JSON document was not fully consumed.")))
                        Toast.makeText(getActivity(), "비디오를 로드하는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public String getRealPathFromUri(Uri contentUri) {
        String[] filePathColumn = {MediaStore.Video.Media.DATA};
        Cursor cursor = requireActivity().getContentResolver().query(contentUri, filePathColumn, null, null, null);

        if (cursor == null)
            return contentUri.getPath();
        else{
            cursor.moveToFirst();
            int idx = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            String filePath = cursor.getString(idx);
            cursor.close();
            return filePath;
        }
    }

    //bitmap -> file 임시 파일로
    public File bitmapToFile(Bitmap bitmap) {
        try {
            // 임시 파일을 저장할 디렉토리 생성
            File cacheDir = getActivity().getCacheDir();
            File tempFile = File.createTempFile("thumbnail", ".jpg", cacheDir);

            // 파일을 JPEG 형식으로 압축하여 저장
            FileOutputStream fos = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}


