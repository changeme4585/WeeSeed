package com.example.weeseed_test.util;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weeseed_test.activity.MainActivity;
import com.example.weeseed_test.databinding.DialogDatePickerWheelBinding;

public class DatePickerDialog extends Fragment {
    DialogDatePickerWheelBinding binding;
    Viewmodel userviewModel;

    String selectedYMD;
    String showingYMD;
    int sltdyear, sltdmonth, sltdday;

    public interface OnButtonClickListener {
        void onButtonClick();
    }
    private OnButtonClickListener listener;

    public void setListener(OnButtonClickListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogDatePickerWheelBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        userviewModel = ((MainActivity) requireActivity()).getViewModel();

        //사전에 설정한 값이 있다면 이걸로 초기화
        if(userviewModel.getDateForData()!=null) {
            binding.tvSelectedDayDp.setText(showingYMD);
            String[] parts = userviewModel.getDateForData().split(":");

            sltdyear=Integer.parseInt(parts[0]);
            sltdmonth=Integer.parseInt(parts[1]);
            sltdday=Integer.parseInt(parts[2]);
        }
        else {
            sltdyear=2024;
            sltdmonth=5;
            sltdday=18;
        }

        //휠 초기화 및 회전 감지
        binding.datepickerWheelDp.init(sltdyear, sltdmonth, sltdday, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                showingYMD=year+"년 "+(monthOfYear + 1)+"월 "+dayOfMonth+"일";
                selectedYMD = String.format("%d:%02d:%02d", year, monthOfYear + 1, dayOfMonth);
                binding.tvSelectedDayDp.setText(showingYMD);
            }
        });

        //버튼 처리
        binding.btnDpCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "개발 중인 기능입니다", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnDpNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        binding.btnDpYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //situ에 따라 다른 처리
                //situ: addChild
                if (userviewModel.getSituation_datePicker().equals("addChild")) {
                    // 선택한 결과를 저장
                    sltdyear=binding.datepickerWheelDp.getYear();
                    sltdmonth=binding.datepickerWheelDp.getMonth() + 1;
                    sltdday=binding.datepickerWheelDp.getDayOfMonth();

                    selectedYMD=sltdyear+"년 " +sltdmonth+"월 " +sltdday+"일";
                    selectedYMD = String.format("%d:%02d:%02d", sltdyear, sltdmonth, sltdday);

                    userviewModel.setDateForData(selectedYMD);
                    userviewModel.setDateForStr(showingYMD);

                    if(listener!=null){
                        listener.onButtonClick();
                    }

                    getActivity().onBackPressed();
                }
            }
        });

        ///
        return view;
    }

}
