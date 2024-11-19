package com.example.weeseed_test.growthDiary;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.weeseed_test.R;
import com.example.weeseed_test.activity.MainActivity;
import com.example.weeseed_test.databinding.FragmentGrowthDiaryElementBinding;
import com.example.weeseed_test.databinding.FragmentGrowthDiaryViewerBinding;
import com.example.weeseed_test.dto.DailyLearningLogDto;
import com.example.weeseed_test.dto.GrowthDiaryDto;
import com.example.weeseed_test.util.Viewmodel;

public class GrowthDiaryElementFragment extends Fragment {

    Viewmodel viewmodel;
    FragmentGrowthDiaryElementBinding bi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bi = FragmentGrowthDiaryElementBinding.inflate(inflater, container, false);
        View view = bi.getRoot();
        viewmodel = ((MainActivity) requireActivity()).getViewModel();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GrowthDiaryDto item = viewmodel.getSltd_diary();

        String TAG = "GROWTH";
        Log.e(TAG, "선택된 성장일지) "+viewmodel.getSltd_diary().getCreationTime()
                +"[name] "+viewmodel.getSltd_diary().getUserName()
                +"[img] "+viewmodel.getSltd_diary().getImageCardNum()
                +"[vid]" +viewmodel.getSltd_diary().getVideoCardNum()
                +"[most pic] "+viewmodel.getSltd_diary().getDailyLearningLogDtoList().get(0).getImage()
                +"[most name] "+viewmodel.getSltd_diary().getDailyLearningLogDtoList().get(0).getCardName()
                +"[most color] "+viewmodel.getSltd_diary().getDailyLearningLogDtoList().get(0).getColor()
        );

        //
        try {
            String formattedDate = item.getCreationTime().replace(":",".");

            bi.tvGrEleChildName.setText(viewmodel.getSltd_childdto().getName());
            bi.tvGrEleCreateDate.setText(formattedDate);
            bi.tvGrEleMakerName.setText(item.getUserName());
            bi.tvGrEleCntALL.setText(String.valueOf(item.getImageCardNum()+item.getVideoCardNum()));
            bi.tvGrEleCntAAC.setText(String.valueOf(item.getImageCardNum()));
            bi.tvGrEleCntVID.setText(String.valueOf(item.getVideoCardNum()));

            if(!item.getDailyLearningLogDtoList().isEmpty()) {
                DailyLearningLogDto most = item.getDailyLearningLogDtoList().get(0);
                bi.tvGrEleMostSelectedCardName.setText(most.getCardName());
                bi.tvAacName.setText(most.getCardName());
                int cardColor = Color.parseColor("#"+most.getColor());
                bi.itemAac.setBackgroundColor(cardColor);
                bi.itemAac.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(most.getImage())
                        .into(bi.ivAacImg);
            }
            else
                bi.tvGrEleMostSelectedCardName.setText("아직 없어요");
        }
        catch (Exception e){
            Log.e(TAG,"ERR: "+e.getMessage());
        }
    }
}