package com.example.weeseed_test.util;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weeseed_test.activity.IntroActivity;
import com.example.weeseed_test.databinding.DialogIsOkBinding;
import com.example.weeseed_test.user.SigninFragment;

/**
여기저기서 사용되는 커스텀 dialog
 */
public class IsOkDialog extends Fragment {

    DialogIsOkBinding binding;
    Viewmodel userviewModel;
    IsOkViewmodel isOkViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogIsOkBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        userviewModel = ((IntroActivity) requireActivity()).getViewModel();
        isOkViewModel = ((IntroActivity) requireActivity()).getIsOkViewModel();

        //text 설정

        binding.titleIsOK.setText(isOkViewModel.getStr_title_isOk());
        binding.descIsOK.setText(isOkViewModel.getStr_desc_isOk());
        binding.btnLeft.setText(isOkViewModel.getStr_left_isOk());
        binding.btnRight.setText(isOkViewModel.getStr_right_isOk());

        //버튼 처리

        binding.btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result=1;

                //situ에 따라 다른 처리
                //situ: sign in
                if (isOkViewModel.getSituation_isOk().equals("signIn")) {
                    // 선택한 결과를 전달
                    Bundle args = new Bundle();
                    args.putInt("isOKChoice", result);
                    SigninFragment signinFragment = new SigninFragment();
                    signinFragment.setArguments(args);
                    ((IntroActivity) requireActivity()).addStackFragment(signinFragment);
                }
            }
        });

        binding.btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result =2;

                //situ: sign in
                if (isOkViewModel.getSituation_isOk().equals("signIn")) {
                    // 선택한 결과를 전달
                    Bundle args = new Bundle();
                    args.putInt("isOKChoice", result);
                    SigninFragment signinFragment = new SigninFragment();
                    signinFragment.setArguments(args);
                    ((IntroActivity) requireActivity()).addStackFragment(signinFragment);
                }
            }
        });

        return view;
    }
}