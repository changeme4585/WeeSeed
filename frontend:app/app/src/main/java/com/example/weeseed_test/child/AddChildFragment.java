package com.example.weeseed_test.child;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.weeseed_test.R;
import com.example.weeseed_test.activity.MainActivity;
import com.example.weeseed_test.databinding.FragmentAddChildBinding;
import com.example.weeseed_test.dto.ChildDto_ADD;
import com.example.weeseed_test.util.Viewmodel;
import com.example.weeseed_test.util.DatePickerDialog;
import com.example.weeseed_test.util.RetrofitAPI;
import com.example.weeseed_test.util.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddChildFragment extends Fragment implements DatePickerDialog.OnButtonClickListener {
    private FragmentAddChildBinding binding;

    Viewmodel viewModel;


    //사용자 입력 처리//
    String sltd_gender="M";
    String sltd_disaType;
    int sltd_disaGrade;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.setStepAddChild(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = ((MainActivity) requireActivity()).getViewModel();
        binding = FragmentAddChildBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        Log.e("AddChildFragment","5. [ADDCHILD] userID: "+ viewModel.getUserID()+"  세션ID: " +viewModel.getSessionID() + "  유저 타입:  "+viewModel.getUserType());

        //현재 step에 따라 view 설정
        switch (viewModel.getStepAddChild()) {
            case 0: {
                viewModel.setAddingChildDto(new ChildDto_ADD());
                //stepBar
                binding.stepAc1Text.setTextColor(ContextCompat.getColor(requireContext(), R.color.font_black));
                binding.stepAc2Text.setTextColor(ContextCompat.getColor(requireContext(), R.color.font_gray));
                binding.stepAc3Text.setTextColor(ContextCompat.getColor(requireContext(), R.color.font_gray));
                binding.stepAc4Text.setTextColor(ContextCompat.getColor(requireContext(), R.color.font_gray));

                binding.stepAc1Img.setColorFilter(R.color.sys_500);
                binding.stepAc2Img.setColorFilter(R.color.font_gray);
                binding.stepAc3Img.setColorFilter(R.color.font_gray);

                //layout
                binding.layoutAcNameAndGender.setVisibility(View.VISIBLE);
                binding.layoutAcBirthday.setVisibility(View.GONE);
                binding.layoutAcDisabType.setVisibility(View.GONE);
                binding.layoutAcDisabGrade.setVisibility(View.GONE);
                break;
            }
            case 1: {
                //stepBar
                binding.stepAc1Text.setTextColor(ContextCompat.getColor(requireContext(), R.color.font_gray));
                binding.stepAc2Text.setTextColor(ContextCompat.getColor(requireContext(), R.color.font_black));
                binding.stepAc3Text.setTextColor(ContextCompat.getColor(requireContext(), R.color.font_gray));
                binding.stepAc4Text.setTextColor(ContextCompat.getColor(requireContext(), R.color.font_gray));

                binding.stepAc1Img.setColorFilter(R.color.font_gray);
                binding.stepAc2Img.setColorFilter(R.color.sys_500);
                binding.stepAc3Img.setColorFilter(R.color.font_gray);

                //layout
                binding.layoutAcNameAndGender.setVisibility(View.GONE);
                binding.layoutAcBirthday.setVisibility(View.VISIBLE);
                binding.layoutAcDisabType.setVisibility(View.GONE);
                binding.layoutAcDisabGrade.setVisibility(View.GONE);

                //getDate에 data 있을 시 세팅
                if(viewModel.getDateForStr() != null){
                    binding.acTvChildBirthday.setText(viewModel.getDateForStr());
                }
                break;
            }
            case 2: {
                //stepBar
                binding.stepAc1Text.setTextColor(ContextCompat.getColor(requireContext(), R.color.font_gray));
                binding.stepAc2Text.setTextColor(ContextCompat.getColor(requireContext(), R.color.font_gray));
                binding.stepAc3Text.setTextColor(ContextCompat.getColor(requireContext(), R.color.font_black));
                binding.stepAc4Text.setTextColor(ContextCompat.getColor(requireContext(), R.color.font_gray));

                binding.stepAc1Img.setColorFilter(R.color.font_gray);
                binding.stepAc2Img.setColorFilter(R.color.font_gray);
                binding.stepAc3Img.setColorFilter(R.color.sys_500);

                //layout
                binding.layoutAcNameAndGender.setVisibility(View.GONE);
                binding.layoutAcBirthday.setVisibility(View.GONE);
                binding.layoutAcDisabType.setVisibility(View.VISIBLE);
                binding.layoutAcDisabGrade.setVisibility(View.GONE);
                break;
            }
            case 3: {
                //stepBar
                binding.stepAc1Text.setTextColor(ContextCompat.getColor(requireContext(), R.color.font_gray));
                binding.stepAc2Text.setTextColor(ContextCompat.getColor(requireContext(), R.color.font_gray));
                binding.stepAc3Text.setTextColor(ContextCompat.getColor(requireContext(), R.color.font_gray));
                binding.stepAc4Text.setTextColor(ContextCompat.getColor(requireContext(), R.color.font_black));

                binding.stepAc1Img.setColorFilter(R.color.font_gray);
                binding.stepAc2Img.setColorFilter(R.color.font_gray);
                binding.stepAc3Img.setColorFilter(R.color.font_gray);

                //layout
                binding.layoutAcNameAndGender.setVisibility(View.GONE);
                binding.layoutAcBirthday.setVisibility(View.GONE);
                binding.layoutAcDisabType.setVisibility(View.GONE);
                binding.layoutAcDisabGrade.setVisibility(View.VISIBLE);
            }
        }




        //버튼 작동
        binding.lcBtnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (viewModel.getStepAddChild()) {
                    case 0: {
                        //이름/성별에서 go
                        viewModel.setAddingChildDto(new ChildDto_ADD());
                        viewModel.getAddingChildDto().setName(binding.acEtName.getText().toString());
                        viewModel.getAddingChildDto().setGender(sltd_gender);
                        viewModel.getAddingChildDto().setUserId(viewModel.getUserID());

                        viewModel.setStepAddChild(1);
                        //stepBar
                        binding.stepAc1Text.setTextColor(ContextCompat.getColor(requireContext(), R.color.font_gray));
                        binding.stepAc2Text.setTextColor(ContextCompat.getColor(requireContext(), R.color.font_black));
                        binding.stepAc3Text.setTextColor(ContextCompat.getColor(requireContext(), R.color.font_gray));
                        binding.stepAc4Text.setTextColor(ContextCompat.getColor(requireContext(), R.color.font_gray));

                        binding.stepAc1Img.setColorFilter(R.color.font_gray);
                        binding.stepAc2Img.setColorFilter(R.color.sys_500);
                        binding.stepAc3Img.setColorFilter(R.color.font_gray);

                        //layout
                        binding.layoutAcNameAndGender.setVisibility(View.GONE);
                        binding.layoutAcBirthday.setVisibility(View.VISIBLE);
                        binding.layoutAcDisabType.setVisibility(View.GONE);
                        binding.layoutAcDisabGrade.setVisibility(View.GONE);
                        break;
                    }
                    case 1: {
                        //생년월일에서 go
                        viewModel.getAddingChildDto().setBirth(viewModel.getDateForData());
                        viewModel.setStepAddChild(2);

                        //stepBar
                        binding.stepAc1Text.setTextColor(ContextCompat.getColor(requireContext(), R.color.font_gray));
                        binding.stepAc2Text.setTextColor(ContextCompat.getColor(requireContext(), R.color.font_gray));
                        binding.stepAc3Text.setTextColor(ContextCompat.getColor(requireContext(), R.color.font_black));
                        binding.stepAc4Text.setTextColor(ContextCompat.getColor(requireContext(), R.color.font_gray));

                        binding.stepAc1Img.setColorFilter(R.color.font_gray);
                        binding.stepAc2Img.setColorFilter(R.color.font_gray);
                        binding.stepAc3Img.setColorFilter(R.color.sys_500);

                        //layout
                        binding.layoutAcNameAndGender.setVisibility(View.GONE);
                        binding.layoutAcBirthday.setVisibility(View.GONE);
                        binding.layoutAcDisabType.setVisibility(View.VISIBLE);
                        binding.layoutAcDisabGrade.setVisibility(View.GONE);
                        break;
                    }
                    case 2: {
                        //장애유형에서 go
                        viewModel.getAddingChildDto().setDisabilityType(sltd_disaType);
                        viewModel.setStepAddChild(3);
                        //stepBar
                        binding.stepAc1Text.setTextColor(ContextCompat.getColor(requireContext(), R.color.font_gray));
                        binding.stepAc2Text.setTextColor(ContextCompat.getColor(requireContext(), R.color.font_gray));
                        binding.stepAc3Text.setTextColor(ContextCompat.getColor(requireContext(), R.color.font_gray));
                        binding.stepAc4Text.setTextColor(ContextCompat.getColor(requireContext(), R.color.font_black));

                        binding.stepAc1Img.setColorFilter(R.color.font_gray);
                        binding.stepAc2Img.setColorFilter(R.color.font_gray);
                        binding.stepAc3Img.setColorFilter(R.color.font_gray);

                        //layout
                        binding.layoutAcNameAndGender.setVisibility(View.GONE);
                        binding.layoutAcBirthday.setVisibility(View.GONE);
                        binding.layoutAcDisabType.setVisibility(View.GONE);
                        binding.layoutAcDisabGrade.setVisibility(View.VISIBLE);
                        break;
                    }
                    case 3: {
                        //장애등급에서 go
                        viewModel.getAddingChildDto().setGrade(sltd_disaGrade);

                        ChildDto_ADD childDtoForAdd = viewModel.getAddingChildDto();

                        Log.e("AddChildFragment",
                                "5.2. 보호자 명: " + childDtoForAdd.getUserId()
                                        + "  아동 이름: " + childDtoForAdd.getName()
                                        + "  아동 생일:  " + childDtoForAdd.getBirth()
                                        + "  아동 성별: " + childDtoForAdd.getGender()
                                        + "  장애 유형: " + childDtoForAdd.getDisabilityType()
                                        + "  장애 등급:  " + childDtoForAdd.getGrade()
                        );
                        addChildToServer(childDtoForAdd);
                    }
                }
            }
        });


        //성별 radio
        binding.acGenderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rg_btn1){
                    sltd_gender="M";
                    Log.e("acGenderRadioGroup", "[라디오 그룹] 남자");
                }
                else if(checkedId == R.id.rg_btn2){
                    sltd_gender="F";
                    Log.e("acGenderRadioGroup", "[라디오 그룹] 여자");
                }
            }
        });

        //생년월일 tv
        binding.acTvChildBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //datepicker를 add
                viewModel.setSituation_datePicker("addChild");
                DatePickerDialog datePickerDialog = new DatePickerDialog();
                datePickerDialog.setListener(AddChildFragment.this);

                // FragmentTransaction 시작
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.layout_for_datepicker_add_child, datePickerDialog); // replace 메서드로 Fragment를 교체합니다.
                transaction.addToBackStack(null); // 백스택에 추가하여 이전 Fragment로 돌아갈 수 있도록 합니다.
                transaction.commit(); // 변경사항을 적용합니다.
//                ((MainActivity) requireActivity()).addStackFragment(new DatePickerDialog());
            }
        });

        //장애유형 spinner

        binding.spinnerAcDisabType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 선택된 항목 처리
                String disaType = parent.getItemAtPosition(position).toString();
                Log.e("spinnerCrAacLabel", "[선택유형]" + disaType);
                switch (disaType) {
                    case "장애 유형":
                    case "지적장애":
                        sltd_disaType = "지적장애";
                        break;
                    case "자폐성장애":
                        sltd_disaType = "자폐성장애";
                        break;
                    case "뇌병변장애":
                        sltd_disaType = "뇌병변장애";
                        break;
                    case "발음장애":
                        sltd_disaType = "발음장애";
                        break;
                    case "행동장애":
                        sltd_disaType = "행동장애";
                        break;
                    case "중도중복장애":
                        sltd_disaType = "중도중복장애";
                        break;
                    case "기타":
                        sltd_disaType = "기타";
                        break;
                    default:
                        sltd_disaType = "안됨~~";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무 것도 선택되지 않은 경우 처리
            }
        });
        //장애등급 spinner
        binding.spinnerAcDisabGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 선택된 항목 처리
                String disaGrade = parent.getItemAtPosition(position).toString();
                Log.e("spinnerCrAacLabel", "[선택등급]" + disaGrade);
                switch (disaGrade) {
                    case "장애 등급":
                    case "1":
                        sltd_disaGrade = 1;
                        break;
                    case "2":
                        sltd_disaGrade = 2;
                        break;
                    case "3":
                        sltd_disaGrade = 3;
                        break;
                    case "4":
                        sltd_disaGrade = 4;
                        break;
                    case "5":
                        sltd_disaGrade = 5;
                        break;
                    case "6":
                        sltd_disaGrade = 6;
                        break;
                    default:
                        sltd_disaGrade = 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무 것도 선택되지 않은 경우 처리
            }
        });

        binding.acBtnBackTitleBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });




        return view;
    }


    //////////ADD CHILD////////

    private void addChildToServer(ChildDto_ADD childdto) {
        binding.ltAcLoadingDots.setVisibility(View.VISIBLE);
        Log.e("addChildToServer", "1: ");

        RetrofitService retrofitService = new RetrofitService(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI = retrofitService.getRetrofit().create(RetrofitAPI.class);
        Log.e("addChildToServer", "2: ");


        retrofitAPI.registchild(childdto)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            String result = response.body();
                            if (result != null && result.equals("ok")) {
                                if(binding.ltAcLoadingDots.getVisibility()==View.VISIBLE)
                                    binding.ltAcLoadingDots.setVisibility(View.GONE);
                                // addChild 성공
                                Log.e("addChildToServer", "성공: ");
                                Toast.makeText(getActivity(), childdto.getName()+" 아동의 프로필이 생성되었습니다", Toast.LENGTH_SHORT).show();
                                getActivity().onBackPressed();
                            } else {
                                // addChild 실패
                                Toast.makeText(getActivity(), "addChildToServer 실패", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if(binding.ltAcLoadingDots.getVisibility()==View.VISIBLE)
                                binding.ltAcLoadingDots.setVisibility(View.GONE);
                            // 서버 오류 등의 이유로 요청 실패
                            Log.e("addChildToServerERR", "서버 오류: " + response.code());
                            Toast.makeText(getActivity(), "서버 오류: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        // 통신 실패 : 뭔가 여기서 자꾸 Use JsonReader.setLenient(true) to accept malformed JSON at line 1 column 1 path $
                        //에러가 걸리는데 서버에는 정상적으로 들어옴
                        Log.e("signInERR", t.getMessage());
                        Toast.makeText(getActivity(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onButtonClick() {
        if(viewModel.getDateForStr() != null)
            binding.acTvChildBirthday.setText(viewModel.getDateForStr());
    }
}




