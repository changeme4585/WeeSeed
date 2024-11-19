package com.example.weeseed_test.Service;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import com.example.weeseed_test.R;
import com.example.weeseed_test.util.PreferenceManager;

public class WidgetActivity extends AppCompatActivity implements WidgetLockFragment.OnWidgetButtonClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_for_widget,new WidgetLockFragment())
                .commit();
    }

    @Override
    public void onButtonClick() {
        //무조건 unlock임
        Intent intent = new Intent("com.example.weeseed_test.ACTION_UNLOCK_WIDGET");    // Broadcast 송신
        sendBroadcast(intent);
        Log.e("WIDacti","액션 송신 listen");
    }
}
