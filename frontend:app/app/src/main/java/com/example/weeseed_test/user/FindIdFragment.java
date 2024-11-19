package com.example.weeseed_test.user;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weeseed_test.activity.IntroActivity;
import com.example.weeseed_test.databinding.FragmentFindIdBinding;
import com.example.weeseed_test.util.RetrofitAPI;
import com.example.weeseed_test.util.RetrofitService3;
import com.example.weeseed_test.util.Viewmodel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FindIdFragment extends Fragment {

    Viewmodel viewModel;
    private FragmentFindIdBinding binding;
    String resultStr;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = ((IntroActivity) requireActivity()).getViewModel();
        binding = FragmentFindIdBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        resultStr = "NOTHING";

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnFindidGo.setOnClickListener(v -> {
            String name = binding.etName.getText().toString();
            String email = binding.etEmail.getText().toString();
            if(checkFormFindId(name, email))
                findId(name, email);
        });

        binding.btnCopy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("childCode", resultStr);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getActivity(), " 아이디가 복사되었습니다.",Toast.LENGTH_SHORT).show();
        });

        binding.btnBack.setOnClickListener(v -> getActivity().onBackPressed());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // 바인딩 해제
    }

    private void findId(String name, String email) {
        String TAG = "findId";
        RetrofitService3 retrofitService = new RetrofitService3(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI = retrofitService.getRetrofit().create(RetrofitAPI.class);
        Log.e(TAG, "1: ");


        retrofitAPI.findUserId(name, email)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            Log.e(TAG, "log10: ");
                            Log.e(TAG, "log11.1: "+response.body());
                            if (response.body() != null) {
                                Log.e(TAG, "성공: "+response.body());

                                binding.layoutFindidResult.setVisibility(View.VISIBLE);
                                binding.tvResultFindID.setText(response.body());

                            } else Toast.makeText(getActivity(), "아이디 혹은 이메일이 옳지 않습니다", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG+"ERR", "서버 오류: " + response.code());
                            Toast.makeText(getActivity(), "아이디 혹은 이메일이 옳지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e(TAG+"ERR2", t.getMessage());
                        Toast.makeText(getActivity(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean checkFormFindId(String name, String email){
        if (name.isEmpty()) {
            binding.tvDescFindIDForId.setText("이름을 입력해주세요");
            return false;
        }

        if (email.isEmpty()) {
            binding.tvDescFindID.setText("이메일을 입력해주세요");
            return false;
        }
        if (!isValidEmail(email)) {
            binding.tvDescFindID.setText("적절한 이메일을 입력해주세요");
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}