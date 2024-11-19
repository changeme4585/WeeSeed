package com.example.weeseed_test.card;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.weeseed_test.R;
import com.example.weeseed_test.databinding.FragmentStudyResultBinding;


/*
* 용도: ViewerAAC에서 학습결과 알려줄때
* */

public class StudyResultFragment extends Fragment {

    private FragmentStudyResultBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStudyResultBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        int res_mode;
        if(getArguments()!=null)
            res_mode = getArguments().getInt("res_mode",0);
        else res_mode=0;

        //
        if(res_mode==1)         mode1();
        else if (res_mode==2)   mode2();
        else if (res_mode==3)   mode3();
        else                    modeERR();



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnResBack.setOnClickListener(v -> getActivity().onBackPressed());
    }




    @Override
    public void onDestroyView() {

        super.onDestroyView();
//        binding=null;
    }

    private void mode1(){
        binding.tvResTitle.setText("완벽해요!");
        binding.tvResDesc.setText("완벽해요!\n모든 것을 정말 잘 해냈어요!");
        binding.ivResBear.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.bear_perfect));
    }
    private void mode2(){
        binding.tvResTitle.setText("최고예요!");
        binding.tvResDesc.setText("우와, 정말 최고예요!\n너무 잘했어요!");
        binding.ivResBear.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.bear_great));
    }
    private void mode3(){
        binding.tvResTitle.setText("잘했어요!");
        binding.tvResDesc.setText("잘했어요!\n정말 멋진 일을 했어요!");
        binding.ivResBear.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.bear_good));
    }
    private void modeERR(){
        binding.tvResTitle.setText("오류 발생!");
        binding.tvResDesc.setText("오류가 발생했습니다.\n다시 시도해 주세요.");
        binding.ivResBear.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.chara_bear_error));
    }


}