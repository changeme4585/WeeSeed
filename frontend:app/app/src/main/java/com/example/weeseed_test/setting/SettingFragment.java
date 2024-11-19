package com.example.weeseed_test.setting;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.weeseed_test.R;
import com.example.weeseed_test.activity.IntroActivity;
import com.example.weeseed_test.activity.MainActivity;
import com.example.weeseed_test.child.AddChildFragment;
import com.example.weeseed_test.child.LinkChildFragment;
import com.example.weeseed_test.databinding.FragmentSettingBinding;
import com.example.weeseed_test.dto.NokDto;
import com.example.weeseed_test.dto.PathologistDto;
import com.example.weeseed_test.growthDiary.GrowthDiaryFragment;
import com.example.weeseed_test.user.TempFragment;
import com.example.weeseed_test.util.IsOkDialog_round;
import com.example.weeseed_test.util.Viewmodel;
import com.example.weeseed_test.util.RetrofitAPI;
import com.example.weeseed_test.util.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingFragment extends Fragment {

    Viewmodel viewModel;
    private FragmentSettingBinding binding;
    IsOkDialog_round dialog_forlogout, dialog_forunregister;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel = ((MainActivity) requireActivity()).getViewModel();
        binding = FragmentSettingBinding.inflate(inflater,container,false);
        View view = binding.getRoot();


        //init: 다 보임
        binding.settingsChildNok.setVisibility(View.VISIBLE);  //재활사면, 보호자 only hide
        binding.settingsChildPatholo.setVisibility(View.VISIBLE);
        binding.layoutChildCode.setVisibility(View.VISIBLE);


        //사용자, 선택 아동 계정 정보 불러오기 + 레이아웃 가시성 조정
        if(viewModel.getUserType().equals("Path")) {
            pathGetUserInfoFromServer(viewModel.getUserID());
            //재활사면, 보호자 only hide
            binding.settingsChildNok.setVisibility(View.GONE);
            binding.layoutChildCode.setVisibility(View.GONE);
        }
        else {
            nokGetUserInfoFromServer(viewModel.getUserID());
            //보호자면, 재활사 only hide
            binding.settingsChildPatholo.setVisibility(View.GONE);
        }

        binding.tvChildName.setText(viewModel.getSltd_childdto().getName().toString());
        binding.tvChildCode.setText(viewModel.getSltd_childdto().getChildCode().toString());
        binding.tvChildType.setText(viewModel.getSltd_childdto().getDisabilityType().toString());
        binding.tvChildGrade.setText(String.valueOf(viewModel.getSltd_childdto().getGrade()));


        String origBirth =viewModel.getSltd_childdto().getBirth().toString();
        String[] parts = origBirth.split(":");
        String year = parts[0];
        String month = parts[1];
        String day = parts[2];


        int profVal = Integer.parseInt(day);

        switch (profVal / 9) {
            case 0:
                binding.ivSeChildProfImg.setImageResource(R.drawable.prof_child9);
                binding.ivSeUserProfImg.setImageResource(R.drawable.prof_child1);
                break;
            case 1:
                binding.ivSeChildProfImg.setImageResource(R.drawable.prof_child1);
                binding.ivSeUserProfImg.setImageResource(R.drawable.prof_child2);
                break;
            case 2:
                binding.ivSeChildProfImg.setImageResource(R.drawable.prof_child2);
                binding.ivSeUserProfImg.setImageResource(R.drawable.prof_child3);
                break;
            case 3:
                binding.ivSeChildProfImg.setImageResource(R.drawable.prof_child3);
                binding.ivSeUserProfImg.setImageResource(R.drawable.prof_child4);
                break;
            case 4:
                binding.ivSeChildProfImg.setImageResource(R.drawable.prof_child4);
                binding.ivSeUserProfImg.setImageResource(R.drawable.prof_child5);
                break;
            case 5:
                binding.ivSeChildProfImg.setImageResource(R.drawable.prof_child5);
                binding.ivSeUserProfImg.setImageResource(R.drawable.prof_child6);
                break;
            case 6:
                binding.ivSeChildProfImg.setImageResource(R.drawable.prof_child6);
                binding.ivSeUserProfImg.setImageResource(R.drawable.prof_child7);
                break;
            case 7:
                binding.ivSeChildProfImg.setImageResource(R.drawable.prof_child7);
                binding.ivSeUserProfImg.setImageResource(R.drawable.prof_child8);
                break;
            case 8:
                binding.ivSeChildProfImg.setImageResource(R.drawable.prof_child8);
                binding.ivSeUserProfImg.setImageResource(R.drawable.prof_child9);
        }

        /////


        /////

        //아동 코드 복사
        binding.layoutBtnCopyChildCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //보호자만 복사 가능.
                if(viewModel.getUserType().equals("Nok")){
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("childCode", viewModel.getSltd_childdto().getChildCode());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getActivity(), viewModel.getSltd_childdto().getName()+ " 아동의 연계코드가 복사되었습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });


        binding.btnProfEdit.setOnClickListener(v -> ((MainActivity) requireActivity()).addStackFragment(new ModifyUserProfileFragment()));

        binding.btnGotoGrowthDiary.setOnClickListener(v -> ((MainActivity) requireActivity()).addStackFragment(new GrowthDiaryFragment()));
        binding.btnAddChild.setOnClickListener(v -> ((MainActivity) requireActivity()).addStackFragment(new AddChildFragment()));
        binding.btnLinkChild.setOnClickListener(v -> ((MainActivity) requireActivity()).addStackFragment(new LinkChildFragment()));
        binding.btnBlockApps.setOnClickListener(v -> ((MainActivity) requireActivity()).addStackFragment(new TempFragment()));
        binding.btnChatBot.setOnClickListener(v -> ((MainActivity) requireActivity()).addStackFragment(new ChatBotFragment()));

        binding.btnLogout.setOnClickListener(v -> dialog_forlogout.show());
        binding.btnUnregister.setOnClickListener(v -> dialog_forunregister.show());

        binding.seBtnBackTitleBar.setOnClickListener(v -> getActivity().onBackPressed());


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        set_UI_dialog();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // 바인딩 해제
    }

/////////////////
private void set_UI_dialog() {
    //for delete card

    dialog_forlogout = new IsOkDialog_round(getActivity());
    dialog_forunregister = new IsOkDialog_round(getActivity());
    dialog_forlogout.setDialogContent(
            "로그아웃 확인",
            "로그아웃하시겠습니까?",
            "취소",
            "확인",
            1, 1);
    dialog_forlogout.setOnDialogButtonClickListener(new IsOkDialog_round.OnDialogButtonClickListener() {
        @Override
        public void onLeftButtonClick() {
        }

        @Override
        public void onRightButtonClick() {
            logout();
        }
    });

    dialog_forunregister.setDialogContent(
            "회원 탈퇴 확인",
            "정말 계정을 삭제하시겠습니까?",
            "취소",
            "확인",
            1, 2);
    dialog_forunregister.setOnDialogButtonClickListener(new IsOkDialog_round.OnDialogButtonClickListener() {
        @Override
        public void onLeftButtonClick() {
        }  //걍 그대로

        @Override
        public void onRightButtonClick() {
            unregister();
        }
    });


}

    //////////////////////get User Info////////////////
    private void logout(){
        String TAG = "logout";

        RetrofitService retrofitService=new RetrofitService(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI=retrofitService.getRetrofit().create(RetrofitAPI.class);

        retrofitAPI.logout()
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()){
                            Log.e(TAG, "로그아웃 성공"+response.code()+"::"+response.body());
                            Toast.makeText(getActivity().getApplicationContext(), "로그아웃 성공", Toast.LENGTH_SHORT).show();
                            backToIntroActi();
                        }
                        else {
                            Log.e(TAG, "실패: 3. "+response.code());
                            backToIntroActi();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        t.printStackTrace();
                        // 실패 처리
                        Log.e(TAG, "ERR!!: 3. "+t.getMessage());
                        Toast.makeText(getActivity().getApplicationContext(), "에러!! : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void unregister(){
        String TAG = "unregister";

        RetrofitService retrofitService=new RetrofitService(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI=retrofitService.getRetrofit().create(RetrofitAPI.class);

        retrofitAPI.unregisterUser(viewModel.getUserID(),viewModel.getUserType())
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()){
                            Log.e(TAG, "회원탈퇴 성공"+response.code());
                            Toast.makeText(getActivity().getApplicationContext(), "그동안 이용해주셔서 감사합니다.", Toast.LENGTH_SHORT).show();
                            backToIntroActi();
                        }
                        else {
                            Log.e(TAG, "실패: 3. "+response.code());
                            Toast.makeText(getActivity(), "회원탈퇴에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        t.printStackTrace();
                        // 실패 처리
                        Log.e(TAG, "ERR!!: 3. "+t.getMessage());
                        Toast.makeText(getActivity().getApplicationContext(), "에러!! : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void backToIntroActi(){
        Intent intent = new Intent(getActivity(), IntroActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish();  // MainActivity를 백스택에서 제거

    }




    //////////////////////get User Info////////////////
    private void pathGetUserInfoFromServer(String userID){

        RetrofitService retrofitService=new RetrofitService(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI=retrofitService.getRetrofit().create(RetrofitAPI.class);

        retrofitAPI.getPathInfo(userID)
                .enqueue(new Callback<PathologistDto>() {
                    @Override
                    public void onResponse(Call<PathologistDto> call, Response<PathologistDto> response) {
                        PathologistDto responsePath = response.body();
                        viewModel.setUserPatho(responsePath);

                        // 가져온 데이터 처리
                        Log.e("pathGetUserInfoFromServer", "1: ");
                        Log.e("responcepath",
                                "  ID: "+responsePath.getPathologistId()+
                                        "  비번: "+responsePath.getPassword()+
                                        "  이메일: "+responsePath.getEmail()+
                                        "  소속 기관: "+responsePath.getOrganizationName()+
                                        "  이름: "+responsePath.getName()
                        );
                        Log.e("Viewmodelpath",
                                "  ID: "+viewModel.getUserPatho().getPathologistId()+
                                        "  비번: "+viewModel.getUserPatho().getPassword()+
                                        "  이메일: "+viewModel.getUserPatho().getEmail()+
                                        "  소속 기관: "+viewModel.getUserPatho().getOrganizationName()+
                                        "  이름: "+viewModel.getUserPatho().getName()
                        );
                        //textView에 사용자 이름 표시
                        binding.profName.setText(viewModel.getUserPatho().getName());
                        binding.profInfo.setText(viewModel.getUserPatho().getOrganizationName()+" 재활사");
                    }

                    @Override
                    public void onFailure(Call<PathologistDto> call, Throwable t) {
                        t.printStackTrace();
                        // 실패 처리
                        Log.e("pathGetUserInfoFromServer", "ERR!!: 3. "+t.getMessage());
                        Toast.makeText(getActivity(), "에러!! : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void nokGetUserInfoFromServer(String userID){

        RetrofitService retrofitService=new RetrofitService(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI=retrofitService.getRetrofit().create(RetrofitAPI.class);

        retrofitAPI.getNokInfo(userID)
                .enqueue(new Callback<NokDto>() {
                    @Override
                    public void onResponse(Call<NokDto> call, Response<NokDto> response) {
                        NokDto responseNok = response.body();
                        viewModel.setUserNok(responseNok);

                        // 가져온 데이터 처리
                        Log.e("nokGetUserInfoFromServer", "1: ");
                        Log.e("responceNok",
                                "  ID: "+responseNok.getNokId()+
                                        "  비번: "+responseNok.getPassword()+
                                        "  이메일: "+responseNok.getEmail()+
                                        "  이름: "+responseNok.getName()
                        );
                        Log.e("ViewmodelNok",
                                "  ID: "+viewModel.getUserNok().getNokId()+
                                        "  비번: "+viewModel.getUserNok().getPassword()+
                                        "  이메일: "+viewModel.getUserNok().getEmail()+
                                        "  이름: "+viewModel.getUserNok().getName()
                        );
                        //textView에 사용자 이름 표시
                        binding.profName.setText(viewModel.getUserNok().getName());
                        binding.profInfo.setText("보호자");
                    }

                    @Override
                    public void onFailure(Call<NokDto> call, Throwable t) {
                        t.printStackTrace();
                        // 실패 처리
                        Log.e("nokGetUserInfoFromServer", "ERR!!: 3. "+t.getMessage());
                        Toast.makeText(getActivity(), "에러!! : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}