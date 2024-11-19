package com.example.weeseed_test.user;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weeseed_test.activity.IntroActivity;
import com.example.weeseed_test.databinding.FragmentIntroBinding;
import com.example.weeseed_test.util.Viewmodel;


/*
인트로 frag. 로그인, 회원가입 frag로의 전환 가능.
 */

public class IntroFragment extends Fragment {
    private FragmentIntroBinding binding;
    Viewmodel userviewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        userviewModel = new ViewModelProvider(this).get(Viewmodel.class);
        binding = FragmentIntroBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //로그인 버튼 클릭 이벤트 : frag add
        binding.btnLogin.setOnClickListener(v -> ((IntroActivity) requireActivity()).addStackFragment(new LoginFragment()));

        //회원가입 버튼 클릭 이벤트 : frag add
        binding.btnSignin.setOnClickListener(v -> ((IntroActivity) requireActivity()).addStackFragment(new SigninFragment()));

        return view;
    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}