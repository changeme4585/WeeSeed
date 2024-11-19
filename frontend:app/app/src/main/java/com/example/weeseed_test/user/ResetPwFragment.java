package com.example.weeseed_test.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.weeseed_test.activity.IntroActivity;
import com.example.weeseed_test.databinding.FragmentResetPwBinding;
import com.example.weeseed_test.util.RetrofitAPI;
import com.example.weeseed_test.util.RetrofitService3;
import com.example.weeseed_test.util.Viewmodel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ResetPwFragment extends Fragment {

    Viewmodel viewModel;
    private FragmentResetPwBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = ((IntroActivity) requireActivity()).getViewModel();
        binding = FragmentResetPwBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnResetpwGo.setOnClickListener(v -> {
            String id = binding.etId.getText().toString();
            String pw = binding.etNewPassword.getText().toString();
            if(checkFormResetPw(id, pw))
                resetPw(id, pw);
        });

        binding.btnBack.setOnClickListener(v -> getActivity().onBackPressed());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // 바인딩 해제
    }

    private void resetPw(String id, String pw) {
        String TAG = "resetPw";
        RetrofitService3 retrofitService = new RetrofitService3(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI = retrofitService.getRetrofit().create(RetrofitAPI.class);
        Log.e(TAG, "1: ");


        retrofitAPI.changePassword(id, pw)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            Log.e(TAG, "log10: ");
                            if (response.body() != null) {
                                Log.e(TAG, "성공: "+response.body());
                                Toast.makeText(getActivity(), "비밀번호를 변경했습니다", Toast.LENGTH_SHORT).show();
                                getActivity().onBackPressed();
                            }
                            else
                                Toast.makeText(getActivity(), "아이디가 옳지 않습니다", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG+"ERR", "서버 오류: " + response.code());
                            Toast.makeText(getActivity(), "아이디가 옳지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e(TAG+"ERR2", t.getMessage());
                        Toast.makeText(getActivity(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean checkFormResetPw(String id, String pw){
        if (id.isEmpty()) {
            binding.tvDescResetPwId.setText("아이디를 입력해주세요");
            return false;
        }
        if (pw.isEmpty() || pw.length() < 10) {
            binding.tvDescResetPwNewPw.setText("10자 이상의 비밀번호를 입력해주세요");
            return false;
        }

        return true;
    }

}