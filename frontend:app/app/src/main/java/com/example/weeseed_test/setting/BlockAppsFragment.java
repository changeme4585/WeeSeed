package com.example.weeseed_test.setting;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.weeseed_test.activity.MainActivity;
import com.example.weeseed_test.databinding.FragmentBlockAppsBinding;
import com.example.weeseed_test.dto.AppInfo;
import com.example.weeseed_test.util.PreferenceManager;
import com.example.weeseed_test.util.Viewmodel;

import java.util.ArrayList;
import java.util.List;

public class BlockAppsFragment extends Fragment implements AppItemClickListener {

    /*
    * todo: sharedpreferences에 저장 & 불러오기    :: preferenceManager 클래스 사용
    *  어플리케이션 목록 불러오기                    :: 해결완
    *  recyclerview 서버테스트 돌리기               :: 완
    *  어플리케이션이 백그라운드에 있을 때 타 어플 사용 차단   :: 이거 공부 필요
    * */


    private FragmentBlockAppsBinding binding;
    private List<String> blockAppsList;
    private List<AppInfo> installedAppsList;
    private BlockAppsAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBlockAppsBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupUI();
    }

    private void setupUI(){
        //get & init app list

        binding.recyclerviewBlockApps.setLayoutManager(new LinearLayoutManager(requireContext()));

        installedAppsList = getInstalledApps();

        blockAppsList = new ArrayList<>(PreferenceManager.getStringList(requireContext(), "BLOCKED_APPS"));
        setAppsBlocked(installedAppsList, blockAppsList);

        adapter = new BlockAppsAdapter(requireContext(), installedAppsList);
        adapter.setOnDataChangedListener(this);
        binding.recyclerviewBlockApps.setAdapter(adapter);

        binding.btnBack.setOnClickListener(v -> getActivity().onBackPressed());
        binding.btnBaGo.setOnClickListener(v -> saveBlockList());
        binding.btnBaInit.setOnClickListener(v -> clearBlockList());
    }
    private void saveBlockList(){
        blockAppsList.clear();
        for (AppInfo appInfo : installedAppsList){
            if (appInfo.isBlocked())
                blockAppsList.add(appInfo.getPackageName());
        }
        PreferenceManager.setStringList(requireContext(), "BLOCKED_APPS", blockAppsList);
        Toast.makeText(getActivity(),"저장되었습니다.", Toast.LENGTH_SHORT).show();
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
                adapter.updateList(installedAppsList);  //원본데이터에 반영은 이정도로만.
            }
        }
    }

    private void adapterUpdateModule(){
        //외부버튼 클릭에 의해 어뎁터 내용을 수정해야 할때
        adapter.updateList(installedAppsList);  //뉴 리스트를 전달
        adapter.notifyDataSetChanged();         //데이터셋 바뀌었어! UI 업데이트해주라!

    }

}