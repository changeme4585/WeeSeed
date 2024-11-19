package com.example.weeseed_test.cardlist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.weeseed_test.activity.MainActivity;
import com.example.weeseed_test.databinding.FragmentLockBinding;
import com.example.weeseed_test.util.Viewmodel;

import java.util.Random;

public class LockFragment extends Fragment {


    Viewmodel viewModel;
    private FragmentLockBinding binding;
    int rannum1, rannum2, nowAnswer=0;
    String nowAnswerStr;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = ((MainActivity) requireActivity()).getViewModel();
//        viewModel = ((IntroActivity) requireActivity()).getViewModel();
        binding = FragmentLockBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        //랜덤 수 & 정답 생성
        Random random = new Random();

        rannum1 = random.nextInt(4)+2;
        rannum2 = random.nextInt(4)+2;

        binding.tvLockQuestion.setText(rannum1+" × "+rannum2+" = ?");

        //버튼 입력
        binding.btnLockOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nowAnswerStr != null){
                    nowAnswer=Integer.parseInt(nowAnswerStr);
                    if(nowAnswer==rannum1*rannum2) {
                        viewModel.setLocked(false);
                        Toast.makeText(getActivity(), "화면 잠금이 해제되었습니다", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    }
                    else {
//                    Toast.makeText(getActivity(), "정답이 아닙니다", Toast.LENGTH_SHORT).show();
                        binding.layoutForLockRetry.setVisibility(View.VISIBLE);
                        nowAnswerStr=null;
                        binding.tvLockAnswer.setText(" ");
                    }
                }
                else
                    Toast.makeText(getActivity(), "정답을 입력해주세요", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnLockBack.setOnClickListener(v -> getActivity().onBackPressed());

        //자판들
        binding.btnLockTrash.setOnClickListener(v -> {
            nowAnswerStr=null;
            binding.tvLockAnswer.setText(" ");
        });


        binding.btnLock0.setOnClickListener(v -> {

            if(nowAnswerStr == null)
                binding.tvLockAnswer.setText("0");
            else if (nowAnswerStr.length()>=5)
                Log.e("input",nowAnswerStr);
            else {
                nowAnswerStr = nowAnswerStr + "0";
                binding.tvLockAnswer.setText(nowAnswerStr);
            }
        });

        binding.btnLock1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nowAnswerStr == null)
                    nowAnswerStr="1";
                else if (nowAnswerStr.length()>=5)
                    Log.e("input",nowAnswerStr);
                else
                    nowAnswerStr = nowAnswerStr + "1";
                binding.tvLockAnswer.setText(nowAnswerStr);
            }
        });
        binding.btnLock2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nowAnswerStr == null)
                    nowAnswerStr="2";
                else if (nowAnswerStr.length()>=5)
                    Log.e("input",nowAnswerStr);
                else
                    nowAnswerStr = nowAnswerStr + "2";
                binding.tvLockAnswer.setText(nowAnswerStr);
            }
        });
        binding.btnLock3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nowAnswerStr == null)
                    nowAnswerStr="3";
                else if (nowAnswerStr.length()>=5)
                    Log.e("input",nowAnswerStr);
                else
                    nowAnswerStr = nowAnswerStr + "3";
                binding.tvLockAnswer.setText(nowAnswerStr);
            }
        });
        binding.btnLock4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nowAnswerStr == null)
                    nowAnswerStr="4";
                else if (nowAnswerStr.length()>=5)
                    Log.e("input",nowAnswerStr);
                else
                    nowAnswerStr = nowAnswerStr + "4";
                binding.tvLockAnswer.setText(nowAnswerStr);

            }
        });
        binding.btnLock5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nowAnswerStr == null)
                    nowAnswerStr="5";
                else if (nowAnswerStr.length()>=5)
                    Log.e("input",nowAnswerStr);
                else
                    nowAnswerStr = nowAnswerStr + "5";
                binding.tvLockAnswer.setText(nowAnswerStr);
            }
        });
        binding.btnLock6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nowAnswerStr == null)
                    nowAnswerStr="6";
                else if (nowAnswerStr.length()>=5)
                    Log.e("input",nowAnswerStr);
                else
                    nowAnswerStr = nowAnswerStr + "6";
                binding.tvLockAnswer.setText(nowAnswerStr);
            }
        });
        binding.btnLock7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nowAnswerStr == null)
                    nowAnswerStr="7";
                else if (nowAnswerStr.length()>=5)
                    Log.e("input",nowAnswerStr);
                else
                    nowAnswerStr = nowAnswerStr + "7";
                binding.tvLockAnswer.setText(nowAnswerStr);
            }
        });
        binding.btnLock8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nowAnswerStr == null)
                nowAnswerStr="8";
                else if (nowAnswerStr.length()>=5)
                    Log.e("input",nowAnswerStr);
                else
                nowAnswerStr = nowAnswerStr + "8";
            binding.tvLockAnswer.setText(nowAnswerStr);
            }
        });
        binding.btnLock9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nowAnswerStr == null)
                nowAnswerStr="9";
                else if (nowAnswerStr.length()>=5)
                    Log.e("input",nowAnswerStr);
                else
                nowAnswerStr = nowAnswerStr + "9";
            binding.tvLockAnswer.setText(nowAnswerStr);
            }
        });

        binding.btnLockOkForRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rannum1 = random.nextInt(4)+2;
                rannum2 = random.nextInt(4)+2;

                binding.tvLockQuestion.setText(rannum1+" × "+rannum2+" = ?");
                binding.layoutForLockRetry.setVisibility(View.GONE);
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }
}