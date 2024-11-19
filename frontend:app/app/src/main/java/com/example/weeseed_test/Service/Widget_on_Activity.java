package com.example.weeseed_test.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weeseed_test.R;

public class Widget_on_Activity extends AppCompatActivity{

    String TAG = "Widget_on_Activity";

    private BroadcastReceiver serviceStartedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(MyForegroundService.ACTION_SERVICE_STARTED.equals(intent.getAction()))
                finish();
        }
    };
    private void updateWidgetDrawable(Context context) {
        // TestWidget 클래스의 updateWidgetDrawable 메소드 호출
        TestWidget widget = new TestWidget();
        widget.updateWidgetDrawable(context, true);  // 위젯 업데이트
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_on);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        registerReceiver(serviceStartedReceiver, new IntentFilter(MyForegroundService.ACTION_SERVICE_STARTED), Context.RECEIVER_EXPORTED);

        startMyService2(this);
        updateWidgetDrawable(this);
    }
    private void startMyService2(Context context) {
        Log.e(TAG, "서비스 시작1: ");
        if (isServiceRunning(this, MyForegroundService.class)) return;
        Log.e(TAG, "서비스 시작2: ");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent serviceIntent = new Intent(context, MyForegroundService.class);
            context.startService(serviceIntent);
        } else {
            Intent serviceIntent = new Intent(context, MyForegroundService.class);
            context.startService(serviceIntent);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(serviceStartedReceiver);
    }

    private boolean isServiceRunning(Context context, Class<?> serviceClass) {
        Log.d(TAG, "서비스 실행 여부 검사: " + serviceClass.getName());

        android.app.ActivityManager manager = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (android.app.ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.d(TAG, "서비스 실행 중: " + serviceClass.getName());
                return true;
            }
        }

        Log.d(TAG, "서비스 미실행: " + serviceClass.getName());
        return false;
    }
}
