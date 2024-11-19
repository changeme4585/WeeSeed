package com.example.weeseed_test.Service;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import com.example.weeseed_test.R;
import com.example.weeseed_test.util.PreferenceManager;

public class TestWidget extends AppWidgetProvider {

    private static final String ACTION_TOGGLE_BUTTON_CLICKED = "com.example.weeseed_test.ACTION_TOGGLE_BUTTON_CLICKED";
    public static final String ACTION_UNLOCK_WIDGET = "com.example.weeseed_test.ACTION_UNLOCK_WIDGET";
    private static final boolean DEFAULT_VALUE_BOOLEAN = false;

    private boolean isLocked; // 초기 상태
    String TAG = "WID";



    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        SharedPreferences prefs = context.getSharedPreferences("weeseed_widget_prefs", Context.MODE_PRIVATE);
//        isLocked = prefs.getBoolean("IS_BLOCKMODE_ON", DEFAULT_VALUE_BOOLEAN);
        isLocked = PreferenceManager.getBoolean(context,"IS_BLOCKMODE_ON");
        Log.e(TAG, "Intent Action: " + intent.getAction());

        //이벤트 정의
        Log.e(TAG,"onReceive 1: "+isLocked);

        if (ACTION_TOGGLE_BUTTON_CLICKED.equals(intent.getAction())) {
            Log.e(TAG,"toggle 2: ");
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            if (!isLocked) {
                Log.e(TAG,"lock 3: "+ isLocked);
                Intent activityIntent = new Intent(context, Widget_on_Activity.class);  //시작용 acti
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activityIntent);
            } else {
                Log.e(TAG,"unlock 4: "+isLocked);
                Intent activityIntent = new Intent(context, WidgetActivity.class);  //해제용 acti
                activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activityIntent);

            }
            Log.e(TAG,"toggle 5: "+ isLocked);

            updateAppWidget(context, appWidgetManager, appWidgetId, isLocked);
        }

    }



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, isLocked);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, boolean isLocked) {
        Log.e("widget","6: "+isLocked);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.test_widget);
        if (isLocked)   views.setImageViewResource(R.id.toggle_button, R.drawable.icon_lock);
        else            views.setImageViewResource(R.id.toggle_button, R.drawable.icon_unlock);

        // Intent 설정
        Intent intent = new Intent(context, TestWidget.class);
        intent.setAction(ACTION_TOGGLE_BUTTON_CLICKED);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.toggle_button, pendingIntent);        // 토글 버튼 클릭이벤트

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    public void updateWidgetDrawable(Context context, boolean isLockedDra) {
        // 위젯 ID 가져오기
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName widgetComponent = new ComponentName(context, TestWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(widgetComponent);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.test_widget);

        if (isLockedDra)    remoteViews.setImageViewResource(R.id.toggle_button, R.drawable.icon_lock);
        else                remoteViews.setImageViewResource(R.id.toggle_button, R.drawable.icon_unlock);
        isLocked = isLockedDra;
        PreferenceManager.setBoolean(context,"IS_BLOCKMODE_ON", isLocked);
        Log.e(TAG,"onReceive 1: "+isLocked+isLockedDra);

        // 위젯 look 변경~
        for (int appWidgetId : appWidgetIds)
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }


}
