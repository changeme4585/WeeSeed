package com.example.weeseed_test.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.weeseed_test.R;
import com.example.weeseed_test.user.LoginFragment;
import com.example.weeseed_test.util.IsOkViewmodel;
import com.example.weeseed_test.util.Viewmodel;

public class IntroActivity extends AppCompatActivity {

    Viewmodel viewModel;
    IsOkViewmodel isOkViewModel;
    final static int REQUEST_CODE_STORAGE_PERMISSION = 567;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_intro);
        viewModel = new ViewModelProvider(this).get(Viewmodel.class);
        isOkViewModel = new ViewModelProvider(this).get(IsOkViewmodel.class);
        setStatusBarColor(R.color.background_color);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_IntroActi,new LoginFragment())
                        .commit();
            }
        }, 500);
    }

    public void addStackFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_IntroActi, fragment)
                .addToBackStack(null) // 뒤로가기 스택에 추가
                .commit();
    }

    public Viewmodel getViewModel() {
        return viewModel;
    }
    public IsOkViewmodel getIsOkViewModel() {return isOkViewModel;}


    @Override
    public void onBackPressed() {
        // 현재 스택에 Fragment가 있다면
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack(); // 스택에서 Fragment를 제거하고 이전 Fragment를 표시
        } else {
            super.onBackPressed(); // 현재 Fragment가 마지막 Fragment일 때 앱을 종료
        }
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