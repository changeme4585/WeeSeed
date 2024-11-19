package com.example.weeseed_test.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.core.app.NotificationCompat;

import com.example.weeseed_test.R;
import com.example.weeseed_test.util.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class MyForegroundService extends Service {

    private static final String CHANNEL_ID = "ForegroundServiceChannel";
    public static final String ACTION_SERVICE_STARTED = "com.example.weeseed_test.ACTION_SERVICE_STARTED";

    private WindowManager windowManager;
    private View popupView;
    private Handler handler = new Handler(Looper.getMainLooper());
    private List<String> blockAppsList;
    String formerApp = "none";
    private boolean isPopupVisible = false;

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        blockAppsList = new ArrayList<>(PreferenceManager.getStringList(getApplicationContext(), "BLOCKED_APPS"));

        // 오버레이 권한 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }


        // 포그라운드 서비스로 전환하기 위해 알림 채널 생성 (API 26 이상에서 필요)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("위시드 경계모드가 켜져 있습니다."); // 추가적인 설명 설정
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);

        }

        // 포그라운드 서비스로 시작
        startForeground(1, getNotification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.post(usageCheckRunnable); // 주기적 실행 시작

        Intent broadcastIntent = new Intent(ACTION_SERVICE_STARTED);
        sendBroadcast(broadcastIntent);
        return START_STICKY;
    }

    private Runnable usageCheckRunnable = new Runnable() {
        @Override
        public void run() {
            checkAppUsage(blockAppsList);
            handler.postDelayed(this, 3000); // 5초
        }
    };

    private void checkAppUsage(List<String> blockAppsList) {
        String TAG = "BACKGROUND";
        UsageStatsManager usm = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        long currentTime = System.currentTimeMillis();
        List<UsageStats> stats = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, currentTime - 1000 * 60, currentTime);

        String sysUI = "com.android.systemui";


        if (stats != null && !stats.isEmpty()) {
            UsageStats recentStats = null;
            for (UsageStats usageStats : stats) {
                if (recentStats == null || usageStats.getLastTimeUsed() > recentStats.getLastTimeUsed()) {
                    recentStats = usageStats;
                }
            }

            String currentApp = recentStats != null ? recentStats.getPackageName() : null;

            Log.w(TAG, "currentApp: " + currentApp);

            // currentApp이 com.android.systemui이면, 직전에 실행된 앱 확인
            if (sysUI.equals(currentApp)) {
                currentApp = formerApp; // 이전 앱을 currentApp으로 설정
                Log.w(TAG, "시스템UI 표시 중. formerApp: " + formerApp);
            } else {
                formerApp = currentApp; // currentApp을 formerApp으로 설정하여 추적
            }

            // 지정된 앱이 blockAppsList에 포함되면 팝업을 표시
            for (String item : blockAppsList) {
                Log.w(TAG, "item: " + item);
                if (item.equals(currentApp)) {
                    Log.e(TAG, "item_show: " + item);
                    showOverlayPopup();  // 오버레이 팝업 표시
                } else {
                    removeOverlayPopup(); // 지정된 앱이 아니면 팝업 제거
                }
            }
        } else {
            Log.d(TAG, "stat 미발견");
        }
    }

    private void showOverlayPopup() {
        if (popupView != null || isPopupVisible) return; // 중복 표시 방지

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.popup_layout, null);

        // '확인' 버튼에 클릭 리스너 설정
        Button confirmButton = popupView.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(v -> {
            removeOverlayPopup(); // 팝업 닫기
        });


        windowManager.addView(popupView, params);
        isPopupVisible = true;

        //
        mediaPlayer = MediaPlayer.create(this,R.raw.block_beep);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    private void removeOverlayPopup() {
        if (popupView != null && isPopupVisible) {
            try {
                windowManager.removeView(popupView);
            } catch (IllegalArgumentException e) {
                e.printStackTrace(); // 팝업이 이미 제거된 경우 예외 처리
            } finally {
                popupView = null;
                isPopupVisible = false; // 팝업 표시 상태 해제

                if(mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }

            }
        }
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(usageCheckRunnable);
        removeOverlayPopup();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification getNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("위시드 경계 모드")
                .setContentText("위시드 경계 모드가 실행 중입니다.")
                .setSmallIcon(R.drawable.icon_lock) // 커스텀 아이콘 설정
                .setPriority(NotificationCompat.PRIORITY_LOW);
        return builder.build();
    }


}
