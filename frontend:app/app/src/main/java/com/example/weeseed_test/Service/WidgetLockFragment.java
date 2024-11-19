package com.example.weeseed_test.Service;

import android.content.Context;
import android.content.Intent;
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
import com.example.weeseed_test.activity.MainActivity;
import com.example.weeseed_test.databinding.FragmentLockBinding;
import com.example.weeseed_test.util.PreferenceManager;

import java.util.Random;

public class WidgetLockFragment extends Fragment {

    private FragmentLockBinding binding;
    int rannum1, rannum2, nowAnswer=0;
    String nowAnswerStr;
    Random random;

    public interface OnWidgetButtonClickListener {
        void onButtonClick();
    }

    private OnWidgetButtonClickListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLockBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        //랜덤 수 & 정답 생성
        random = new Random();
        rannum1 = random.nextInt(4)+2;
        rannum2 = random.nextInt(4)+2;

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupButtons();
    }
    private void updateWidgetDrawable(Context context) {
        // TestWidget 클래스의 updateWidgetDrawable 메소드 호출
        TestWidget widget = new TestWidget();
        widget.updateWidgetDrawable(context, false);  // 위젯 업데이트
    }

    private void stopService(){
        Intent serviceIntent = new Intent(getContext(), MyForegroundService.class);
        getActivity().stopService(serviceIntent);
    }

    private void setupButtons(){
        binding.tvLockQuestion.setText(rannum1+" × "+rannum2+" = ?");
        binding.layoutForLockRetry.setVisibility(View.GONE);
        nowAnswerStr=null;
        binding.tvLockAnswer.setText(" ");

        //버튼 입력



        binding.btnLockOk.setOnClickListener(v -> {
            if(nowAnswerStr != null){
                nowAnswer=Integer.parseInt(nowAnswerStr);
                if(nowAnswer==rannum1*rannum2) {
                    stopService();
                    updateWidgetDrawable(getContext());
                    if (listener !=null)
                        listener.onButtonClick();   //widget에 알림
                    if (getActivity() instanceof IntroActivity ||getActivity() instanceof MainActivity)
                        ;
                    else
                        Toast.makeText(getActivity().getApplicationContext(), "유해 어플 잠금이 해제되었습니다", Toast.LENGTH_SHORT).show();

                    if (getActivity() instanceof IntroActivity)
                        getActivity().onBackPressed();
                    else if (getActivity() instanceof MainActivity)
                        getActivity().onBackPressed();
                    else
                        getActivity().finish();
                }
                else {
                    binding.layoutForLockRetry.setVisibility(View.VISIBLE);
                    nowAnswerStr=null;
                    binding.tvLockAnswer.setText(" ");
                }
            }
            else
                Toast.makeText(getActivity(), "정답을 입력해주세요", Toast.LENGTH_SHORT).show();
        });
        binding.btnLockBack.setOnClickListener(v -> getActivity().onBackPressed());
        binding.btnLockTrash.setOnClickListener(v -> {
            nowAnswerStr=null;
            binding.tvLockAnswer.setText(" ");
        });
        binding.btnLockOkForRetry.setOnClickListener(v -> {
            rannum1 = random.nextInt(4)+2;
            rannum2 = random.nextInt(4)+2;

            binding.tvLockQuestion.setText(rannum1+" × "+rannum2+" = ?");
            binding.layoutForLockRetry.setVisibility(View.GONE);
        });


        //자판들
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
        binding.btnLock1.setOnClickListener(v -> {
            if(nowAnswerStr == null)
                nowAnswerStr="1";
            else if (nowAnswerStr.length()>=5)
                Log.e("input",nowAnswerStr);
            else
                nowAnswerStr = nowAnswerStr + "1";
            binding.tvLockAnswer.setText(nowAnswerStr);
        });
        binding.btnLock2.setOnClickListener(v -> {
            if(nowAnswerStr == null)
                nowAnswerStr="2";
            else if (nowAnswerStr.length()>=5)
                Log.e("input",nowAnswerStr);
            else
                nowAnswerStr = nowAnswerStr + "2";
            binding.tvLockAnswer.setText(nowAnswerStr);
        });
        binding.btnLock3.setOnClickListener(v -> {
            if(nowAnswerStr == null)
                nowAnswerStr="3";
            else if (nowAnswerStr.length()>=5)
                Log.e("input",nowAnswerStr);
            else
                nowAnswerStr = nowAnswerStr + "3";
            binding.tvLockAnswer.setText(nowAnswerStr);
        });
        binding.btnLock4.setOnClickListener(v -> {
            if(nowAnswerStr == null)
                nowAnswerStr="4";
            else if (nowAnswerStr.length()>=5)
                Log.e("input",nowAnswerStr);
            else
                nowAnswerStr = nowAnswerStr + "4";
            binding.tvLockAnswer.setText(nowAnswerStr);

        });
        binding.btnLock5.setOnClickListener(v -> {
            if(nowAnswerStr == null)
                nowAnswerStr="5";
            else if (nowAnswerStr.length()>=5)
                Log.e("input",nowAnswerStr);
            else
                nowAnswerStr = nowAnswerStr + "5";
            binding.tvLockAnswer.setText(nowAnswerStr);
        });
        binding.btnLock6.setOnClickListener(v -> {
            if(nowAnswerStr == null)
                nowAnswerStr="6";
            else if (nowAnswerStr.length()>=5)
                Log.e("input",nowAnswerStr);
            else
                nowAnswerStr = nowAnswerStr + "6";
            binding.tvLockAnswer.setText(nowAnswerStr);
        });
        binding.btnLock7.setOnClickListener(v -> {
            if(nowAnswerStr == null)
                nowAnswerStr="7";
            else if (nowAnswerStr.length()>=5)
                Log.e("input",nowAnswerStr);
            else
                nowAnswerStr = nowAnswerStr + "7";
            binding.tvLockAnswer.setText(nowAnswerStr);
        });
        binding.btnLock8.setOnClickListener(v -> {
            if(nowAnswerStr == null)
                nowAnswerStr="8";
            else if (nowAnswerStr.length()>=5)
                Log.e("input",nowAnswerStr);
            else
                nowAnswerStr = nowAnswerStr + "8";
            binding.tvLockAnswer.setText(nowAnswerStr);
        });
        binding.btnLock9.setOnClickListener(v -> {
            if(nowAnswerStr == null) {
                nowAnswerStr="9";
            }
            else if (nowAnswerStr.length()>=5) {
                Log.e("input",nowAnswerStr);
            }
            else {
                nowAnswerStr = nowAnswerStr + "9";
            }
            binding.tvLockAnswer.setText(nowAnswerStr);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null; // 메모리 누수 방지
    }
}