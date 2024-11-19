package com.example.weeseed_test.growthDiary;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.weeseed_test.R;
import com.example.weeseed_test.activity.MainActivity;
import com.example.weeseed_test.databinding.FragmentGrowDiaryBinding;
import com.example.weeseed_test.dto.ChildDto_forUSE;
import com.example.weeseed_test.dto.GrowthDiaryDto;
import com.example.weeseed_test.util.RetrofitAPI;
import com.example.weeseed_test.util.RetrofitService;
import com.example.weeseed_test.util.Viewmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GrowthDiaryFragment extends Fragment implements GrowthItemClickListener {

    Viewmodel viewmodel;
    private FragmentGrowDiaryBinding bi;
    private RecyclerView recyclerView;

    List<GrowthDiaryDto> diarylist, orderedlist;
    private RecyclerView.LayoutManager layoutManager;

    private boolean showPreview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bi = FragmentGrowDiaryBinding.inflate(inflater, container, false);
        View view = bi.getRoot();
        viewmodel = ((MainActivity) requireActivity()).getViewModel();

        showPreview = false;
        diarylist = new ArrayList<>();
        orderedlist = new ArrayList<>();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupButtons();
        getDiaryList(viewmodel.getUserID(), viewmodel.getSltd_childdto().getChildCode());
    }


    private void setupButtons(){
        recyclerView = bi.recyclerGrSelectList;
        layoutManager =new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        bi.btnBack.setOnClickListener(v -> getActivity().onBackPressed());
        bi.btnGo.setOnClickListener(v -> createNewDiary(viewmodel.getUserID(), viewmodel.getSltd_childdto().getChildCode()));//안되면 어떻게든 코드를 childid로
        bi.btnDialogRight.setOnClickListener(v -> closeDialog());
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        if(bi.dialogForPreCreate.getVisibility() == View.VISIBLE)
                            closeDialog();
                        else{
                            setEnabled(false);
                            requireActivity().onBackPressed();
                        }
                    }
                });

        bi.spinnerGrMaker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                    default:
                        diaryListListView(diarylist);
                    case 1:
                        orderedlist = new ArrayList<>(diarylist);
                        Collections.reverse(orderedlist);
                        diaryListListView(orderedlist);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void createNewDiary(String userid, String childCode) {
        RetrofitService retrofitService = new RetrofitService(viewmodel.getSvaddr());
        RetrofitAPI retrofitAPI = retrofitService.getRetrofit().create(RetrofitAPI.class);
        Log.e("createNewDiary", "1: ");

        try{
            retrofitAPI.createGrowth(userid, childCode)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Log.e("createNewDiary", "3: ");
                            if (response.isSuccessful()) {
                                Log.e("createNewDiary", "1: ");
                                showPreview = true;
                                getDiaryList(viewmodel.getUserID(), viewmodel.getSltd_childdto().getChildCode());
                            }
                            else
                            {
                                Log.e("createNewDiary", "2: ");
                                Toast.makeText(getActivity(), "성장일지 생성에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }

                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("createNewDiary", t.getMessage());
                            Toast.makeText(getActivity(), "오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
        catch(Exception t){
            Log.e("createNewDiary","ERR) "+t.getMessage());
        }
    }
    private void getDiaryList(String userid, String childCode){
        RetrofitService retrofitService = new RetrofitService(viewmodel.getSvaddr());
        RetrofitAPI retrofitAPI = retrofitService.getRetrofit().create(RetrofitAPI.class);
        Log.e("getDiaryList", "1: ");

        try{
            retrofitAPI.getGrowthDiary(userid, childCode)
                    .enqueue(new Callback<List<GrowthDiaryDto>>() {
                        @Override
                        public void onResponse(Call<List<GrowthDiaryDto>> call, Response<List<GrowthDiaryDto>> response) {
                            Log.e("getDiaryList", "3: ");

                            if(response.body().size()==0)
                                Toast.makeText(getActivity(), "생성된 성장일지가 없습니다!", Toast.LENGTH_SHORT).show();
                            else {
                                if(showPreview)
                                    addGrowthElementFrag(false);
                                diarylist = response.body();
                                diaryListListView(diarylist);
                            }
                        }
                        @Override
                        public void onFailure(Call<List<GrowthDiaryDto>> call, Throwable t) {
                            Log.e("getDiaryList", t.getMessage());
                            Toast.makeText(getActivity(), "오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
        catch(Exception t){
            Log.e("getDiaryList","ERR) "+t.getMessage());
        }
    }

    private void addGrowthElementFrag(boolean toViewer){
        // case 1: 뷰어
        // case 2: 성장일지 생성 콜백
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (toViewer) {
            GrowthDiaryViewerFragment fragment = new GrowthDiaryViewerFragment();
            transaction.add(bi.layoutForGrowthViewer.getId(),fragment)
                    .addToBackStack(null);
        } else {
            bi.dialogForPreCreate.setVisibility(View.VISIBLE);
            GrowthDiaryElementFragment fragment = new GrowthDiaryElementFragment();
            transaction.add(bi.layoutForGrowthElement.getId(),fragment);
        }
        transaction.commit();
    }

    private void closeDialog(){
        FragmentManager fragmentManager = getChildFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(bi.layoutForGrowthElement.getId());
        if(fragment != null){
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.remove(fragment);
            transaction.commit();
        }
        bi.dialogForPreCreate.setVisibility(View.GONE);
    }

    private void diaryListListView(List<GrowthDiaryDto> diarylist){
        if(diarylist!= null){
            GrowthItemAdapter adapter = new GrowthItemAdapter(diarylist,this, viewmodel.getSltd_childdto().getName());
            recyclerView.setAdapter(adapter);
        }
        else{
            Log.e("diaryListListView", "생성된 성장일지가 없습니다");
            Toast.makeText(getActivity(), "생성된 성장일지가 없습니다", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(GrowthDiaryDto growthDiaryDto) {
        viewmodel.setSltd_diary(growthDiaryDto);
        addGrowthElementFrag(true);
    }

}