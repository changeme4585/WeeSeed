package com.example.weeseed_test.user;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.weeseed_test.Service.MyForegroundService;
import com.example.weeseed_test.Service.WidgetLockFragment;
import com.example.weeseed_test.activity.IntroActivity;
import com.example.weeseed_test.activity.MainActivity;
import com.example.weeseed_test.databinding.FragmentTempBinding;
import com.example.weeseed_test.dto.AppInfo;
import com.example.weeseed_test.setting.AppItemClickListener;
import com.example.weeseed_test.setting.BlockAppsAdapter;
import com.example.weeseed_test.util.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class TempFragment extends Fragment implements AppItemClickListener {

    private FragmentTempBinding binding;

    //block apps test
    private List<String> blockAppsList;
    private List<AppInfo> installedAppsList;
    private BlockAppsAdapter adapter;
    private static final int PERMISSION_REQUEST_CODE = 777;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTempBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        blockAppsList = new ArrayList<>();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUI();
    }

    @Override
    public void onStart() {
        super.onStart();
        setupContents();
    }

    private void setupContents(){
        installedAppsList = getInstalledApps();

        blockAppsList = new ArrayList<>(PreferenceManager.getStringList(requireContext(), "BLOCKED_APPS"));
        setAppsBlocked(installedAppsList, blockAppsList);
        binding.switchBaBlock.setChecked(PreferenceManager.getBoolean(requireContext(),"IS_BLOCKMODE_ON"));

        adapter = new BlockAppsAdapter(requireContext(), installedAppsList);
        adapter.setOnDataChangedListener(this);
        binding.recyclerviewBlockApps.setAdapter(adapter);

    }

    private void setupUI(){
        //get & init app list

        binding.recyclerviewBlockApps.setLayoutManager(new LinearLayoutManager(requireContext()));


        //
        binding.btnBack.setOnClickListener(v -> getActivity().onBackPressed());
        binding.btnBaGo.setOnClickListener(v -> saveBlockList());
        binding.btnBaInit.setOnClickListener(v -> clearBlockList());

        binding.switchBaBlock.setOnCheckedChangeListener((buttonView, isChecked) -> switchButtonEvent(isChecked));
    }
    //////
    private void switchButtonEvent(boolean isChecked){
        PreferenceManager.setBoolean(requireContext(), "IS_BLOCKMODE_ON", isChecked);
        Intent serviceIntent = new Intent(getContext(), MyForegroundService.class);

        if (isChecked) {
            // 권한 확인
            if (checkPermissions()) startMyService();
            else {
                //권한 요청
                if (!checkUsageStatsPermission()) requestUsageStatsPermission();
                if (!Settings.canDrawOverlays(getContext())) requestOverlayPermission();
            }
        } else {
            // Switch가 off일 경우 서비스 중지
            getActivity().stopService(serviceIntent);

            if (getActivity() instanceof IntroActivity)
                ((IntroActivity)getActivity()).addStackFragment(new WidgetLockFragment());
            else if (getActivity() instanceof MainActivity)
                ((MainActivity)getActivity()).addStackFragment(new WidgetLockFragment());

        }
    }


    private void startMyService() {
        Intent serviceIntent = new Intent(getContext(), MyForegroundService.class);
        getActivity().startService(serviceIntent);  // 또는 getContext().startService(serviceIntent);
    }

    private boolean checkPermissions() {
        return checkUsageStatsPermission() && Settings.canDrawOverlays(getContext());
    }

    // PACKAGE_USAGE_STATS 권한 확인
    private boolean checkUsageStatsPermission() {
        // 실제로 사용자가 권한을 허용했는지 확인
        UsageStatsManager usm = (UsageStatsManager) getActivity().getSystemService(Context.USAGE_STATS_SERVICE);
        long currentTime = System.currentTimeMillis();
        List<UsageStats> stats = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, currentTime - 1000 * 60 * 60, currentTime);

        return stats != null && !stats.isEmpty();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startMyService();
            } else {
                Toast.makeText(getContext(), "권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    ///////권한요청/////////
    private void requestUsageStatsPermission() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivityForResult(intent, PERMISSION_REQUEST_CODE);
    }


    private void requestOverlayPermission() {
        if (!Settings.canDrawOverlays(getContext())) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            startActivityForResult(intent, PERMISSION_REQUEST_CODE);
        }
    }


    /////////
    private void saveBlockList(){
        blockAppsList.clear();
        for (AppInfo appInfo : installedAppsList){
            if (appInfo.isBlocked())
                blockAppsList.add(appInfo.getPackageName());
        }
        PreferenceManager.setStringList(requireContext(), "BLOCKED_APPS", blockAppsList);
        Toast.makeText(getActivity(),"저장되었습니다.", Toast.LENGTH_SHORT).show();
        switchButtonEvent(binding.switchBaBlock.isChecked());
    }

    private void clearBlockList(){
        Log.e("TAG","초기화 클릭 ");
        blockAppsList.clear();
        for (AppInfo appInfo : installedAppsList) {
            appInfo.setBlocked(false);
        }
        adapterUpdateModule();
    }
    private List<AppInfo> getInstalledApps(){
        List<AppInfo> appList = new ArrayList<>();
        PackageManager pm = getActivity().getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);

        //sys app 제외하기
        for(PackageInfo packageInfo : packages){
            if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)== 0){
                String appName = packageInfo.applicationInfo.loadLabel(pm).toString();
                String packageName = packageInfo.packageName;
                Drawable appIcon = packageInfo.applicationInfo.loadIcon(pm);
                appList.add(new AppInfo(appName,packageName,appIcon,false));
            }
        }
        return appList;
    }

    private List<AppInfo> setAppsBlocked(List<AppInfo> installedAppsList, List<String> blockAppsList){
        Log.e("TAG",String.join(" ",blockAppsList));
        for(AppInfo appInfo : installedAppsList){
            if(blockAppsList.contains(appInfo.getPackageName()))
                appInfo.setBlocked(true);
        }
        return installedAppsList;
    }

    @Override
    public void onItemSwitchClick(String packageName, boolean ischecked) {
        //itemclick 시 원본 데이터에 반영
        for (AppInfo appInfo : installedAppsList) {
            if (appInfo.getPackageName().equals(packageName)) {
                appInfo.setBlocked(ischecked);
                adapter.updateList(installedAppsList);  //item click 시, 원본데이터에 반영은 이정도로만.
            }
        }
    }

    private void adapterUpdateModule(){
        //외부버튼 클릭에 의해 어뎁터 내용을 수정해야 할때
        adapter.updateList(installedAppsList);  //뉴 리스트를 전달
        adapter.notifyDataSetChanged();         //데이터셋 바뀌었어!

    }

}