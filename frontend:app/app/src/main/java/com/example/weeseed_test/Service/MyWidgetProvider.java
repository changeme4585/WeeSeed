package com.example.weeseed_test.Service;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.weeseed_test.R;

import java.util.Arrays;

/*이거 안씀. testwidget 씀*/

public class MyWidgetProvider extends AppWidgetProvider {
    String TAG = "MyWidgetProvider";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.e(TAG, "onUpdate called");
        // 위젯의 버튼에 PendingIntent 설정
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_block_switch);

        // Toggle action을 위한 PendingIntent 생성
        Intent intent = new Intent(context, MyWidgetProvider.class);
        intent.setAction("TOGGLE_SERVICE");  // TOGGLE_SERVICE 액션을 지정
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Log.d(TAG, "PendingIntent created: " + pendingIntent);

        views.setOnClickPendingIntent(R.id.switch_widget, pendingIntent);

        // 위젯 업데이트
        appWidgetManager.updateAppWidget(appWidgetIds, views);
        Log.d(TAG, "AppWidget updated for ids: " + Arrays.toString(appWidgetIds));
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive called with action: " + intent.getAction());

        super.onReceive(context, intent);

        if ("TOGGLE_SERVICE".equals(intent.getAction())) {
            Log.d(TAG, "TOGGLE_SERVICE action received");

            // 서비스 상태 토글
            Intent serviceIntent = new Intent(context, MyForegroundService.class);

            // 서비스가 실행 중이면 종료, 실행 중이지 않으면 시작
            if (isServiceRunning(context, MyForegroundService.class)) {
                Log.d(TAG, "Service is running, stopping service...");
                context.stopService(serviceIntent);
            } else {
                Log.d(TAG, "Service is not running, starting service...");
                context.startService(serviceIntent);
            }

            // 위젯을 다시 업데이트하여 스위치 상태 반영
            ComponentName thisWidget = new ComponentName(context, MyWidgetProvider.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(thisWidget), R.id.switch_widget);
        }
    }

    private boolean isServiceRunning(Context context, Class<?> serviceClass) {
        Log.d(TAG, "Checking if service is running: " + serviceClass.getName());

        android.app.ActivityManager manager = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (android.app.ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.d(TAG, "Service is running: " + serviceClass.getName());
                return true;
            }
        }

        Log.d(TAG, "Service is not running: " + serviceClass.getName());
        return false;
    }

}
