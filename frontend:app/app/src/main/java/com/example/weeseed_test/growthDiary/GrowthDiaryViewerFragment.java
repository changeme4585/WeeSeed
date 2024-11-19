package com.example.weeseed_test.growthDiary;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weeseed_test.R;
import com.example.weeseed_test.activity.MainActivity;
import com.example.weeseed_test.databinding.FragmentGrowDiaryBinding;
import com.example.weeseed_test.databinding.FragmentGrowthDiaryViewerBinding;
import com.example.weeseed_test.util.Viewmodel;

public class GrowthDiaryViewerFragment extends Fragment {
    Viewmodel viewmodel;
    FragmentGrowthDiaryViewerBinding bi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bi = FragmentGrowthDiaryViewerBinding.inflate(inflater, container, false);
        View view = bi.getRoot();
        viewmodel = ((MainActivity) requireActivity()).getViewModel();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bi.btnBack.setOnClickListener(v -> getActivity().onBackPressed());
        addGrowthElementFrag();
    }
    private void addGrowthElementFrag(){
        GrowthDiaryElementFragment fragment = new GrowthDiaryElementFragment();
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(bi.layoutForGrowthElement.getId(),fragment);
        transaction.commit();
    }


}