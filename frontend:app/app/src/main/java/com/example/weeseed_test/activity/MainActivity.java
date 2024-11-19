package com.example.weeseed_test.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.weeseed_test.R;
import com.example.weeseed_test.child.SelectChildFragment;
import com.example.weeseed_test.util.IsOkViewmodel;
import com.example.weeseed_test.util.Viewmodel;

public class MainActivity extends AppCompatActivity {

    Viewmodel viewModel;
    IsOkViewmodel isOkViewmodel;
    View dimmed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        viewModel = new ViewModelProvider(this).get(Viewmodel.class);
        isOkViewmodel = new ViewModelProvider(this).get(IsOkViewmodel.class);
        setStatusBarColor(R.color.black);    //상태바 색상 변경

        Intent intent = getIntent();
        viewModel.setSessionID(intent.getStringExtra("SESSION_ID"));
        viewModel.setUserID(intent.getStringExtra("USER_ID"));
        viewModel.setUserType(intent.getStringExtra("USER_TYPE"));
        viewModel.setSvaddr(intent.getStringExtra("SVADDR"));
        viewModel.setVoice_svaddr(intent.getStringExtra("VOICESVADDR"));
        Log.e("MainActivity","3userID: "+ viewModel.getUserID()+"  세션ID: " +viewModel.getSessionID() + "  유저 타입:  "+viewModel.getUserType()+"  서버 addr"+viewModel.getSvaddr());

        //초기 frag: selectchild
        SelectChildFragment fragment = new SelectChildFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_MainActi, fragment);
        transaction.commit();

        dimmed = findViewById(R.id.v_for_PupUp_dimmed);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    public void addStackFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_MainActi, fragment)
                .addToBackStack(null) // 뒤로가기 스택에 추가
                .commit();
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_MainActi, fragment)
                .commit();
    }

    public void addStackFragmentWithAnim_zoomIn(Fragment fragment) {
        dimmed.setAlpha(0f);
        if (!dimmed.isShown())
            dimmed.setVisibility(View.VISIBLE);
        dimmed.animate()
                .alpha(1f)
                .setDuration(50)
                .start();

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.zoom_in,
                        R.anim.zoom_out,
                        R.anim.zoom_in,
                        R.anim.zoom_out
                )
                .replace(R.id.frame_PopUp, fragment, "FRAG_zoomin")
                .addToBackStack(null) // 뒤로가기 스택에 추가
                .commit();
    }



    public Viewmodel getViewModel() {
        return viewModel;
    }
    public IsOkViewmodel getIsOkViewmodel() {return isOkViewmodel;}


    @Override
    public void onBackPressed() {
        // 현재 스택에 Fragment가 남아있다면 pop
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(); // 스택에서 Fragment를 제거하고 이전 Fragment를 표시
        } else {
            super.onBackPressed(); // 현재 Fragment가 마지막 Fragment일 때 앱을 종료
        }

        if (dimmed.isShown())
            dimmed.setVisibility(View.INVISIBLE);
    }



    public void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(color));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // Android 11 (API 30) 이상
                WindowInsetsController insetsController = window.getInsetsController();
                if (insetsController != null) {
                    insetsController.setSystemBarsAppearance(
                            0, // Clear any light mode (icons white)
                            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    );
                }
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);   // Android 6.0 (API 23) ~ Android 10 (API 29)
            }
        }
    }

}