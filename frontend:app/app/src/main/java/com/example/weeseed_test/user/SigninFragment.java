package com.example.weeseed_test.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.weeseed_test.R;
import com.example.weeseed_test.activity.IntroActivity;
import com.example.weeseed_test.databinding.FragmentSigninBinding;
import com.example.weeseed_test.dto.NokDto;
import com.example.weeseed_test.dto.PathologistDto;
import com.example.weeseed_test.user.worker.CheckIdResultListener;
import com.example.weeseed_test.util.RetrofitAPI;
import com.example.weeseed_test.util.RetrofitService;
import com.example.weeseed_test.util.RetrofitService3;
import com.example.weeseed_test.util.Viewmodel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SigninFragment extends Fragment implements CheckIdResultListener {

    Viewmodel viewModel;
    private FragmentSigninBinding binding;
    int isPatho = 1;
    int signinStep = 1;

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSigninBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        ((IntroActivity) requireActivity()).setStatusBarColor(R.color.app_bar);

        viewModel = ((IntroActivity) requireActivity()).getViewModel();


        //step1) isOK dialogFrag에서 재활사 여부 체크 + ID입력
        showCustomDialog();


        //일단 임시로 사용할 full버전
        setupButtons();

        return view;
    }

    private void setupButtons(){
        binding.btnGo.setOnClickListener(v -> {

            Boolean isSuitable = true;
            //공백 테스트
            //아이디 공백
            if(binding.etId.getText().toString().isEmpty()
                    || binding.etId.getText().toString().length() < 5
                    || binding.etId.getText().toString().length() > 15) {
                binding.destEtId.setText("5~15자의 아이디를 입력해주세요");
                isSuitable=false;
            }

            //step2.1: pw/pwcheck 공백/ 비번 길이
            if (binding.etPw.getText().toString().isEmpty()
                    || binding.etPwcheck.getText().toString().isEmpty()
                    || binding.etPw.getText().toString().length() < 10) {
                binding.descEtPw.setText("10자 이상의 비밀번호를 입력해주세요");

            }
            //step2.2: pw/pwcheck 동일 여부
            else if (!(binding.etPw.getText().toString().equals(binding.etPwcheck.getText().toString()))) {
                binding.descEtPw.setText("입력된 비밀번호가 동일하지 않습니다");
                isSuitable=false;
            }

            //step3 이메일 공백/유효성
            if (binding.etEmail.getText().toString().isEmpty()) {
                binding.descEtEmail.setText("이메일을 입력해주세요");
                isSuitable=false;
            }
            else if (!(isValidEmail(binding.etEmail.getText().toString()))) {
                binding.descEtEmail.setText("적절한 이메일을 입력해주세요");
                isSuitable=false;
            }

            //step4 이름 공백
            if (binding.etName.getText().toString().isEmpty()) {
                binding.descEtName.setText("이름을 입력해주세요");
                isSuitable=false;
            }

            //step5 소속기관 공백
            if (binding.etName.getText().toString().isEmpty()) {
                if(isPatho==1)
                    binding.etName.setText("NOK");
                else {
                    binding.descOrg.setText("소속기관을 입력해주세요");
                    isSuitable = false;
                }
            }

            //////////////

            if(isSuitable){
                if(isPatho==1){
                    //보호자 로그인
                    //id, pw, email, name, org
                    NokDto signInNokdto = new NokDto();

                    signInNokdto.setNokId(binding.etId.getText().toString());
                    signInNokdto.setPassword(binding.etPw.getText().toString());
                    signInNokdto.setEmail(binding.etEmail.getText().toString());
                    signInNokdto.setName(binding.etName.getText().toString());

                    //retrofit version
                    nokSignInToServer(signInNokdto);
                }
                else{
                    //재활사 로그인
                    //isPatho, id, pw, email, name, org
                    PathologistDto pathologistDto = new PathologistDto();

                    pathologistDto.setPathologistId(binding.etId.getText().toString());
                    pathologistDto.setPassword(binding.etPw.getText().toString());
                    pathologistDto.setEmail(binding.etEmail.getText().toString());
                    pathologistDto.setName(binding.etName.getText().toString());
                    pathologistDto.setOrganizationName(binding.etOrg.getText().toString());

                    //retrofit version
                    pathSignInToServer(pathologistDto);
                }
            }
            else
                Toast.makeText(getActivity(), "양식을 확인해주세요", Toast.LENGTH_SHORT).show();


        });
        binding.btnBackTitleBarSignin.setOnClickListener(v -> getActivity().onBackPressed());

    }


    private void showCustomDialog() {
        View v = getLayoutInflater().inflate(R.layout.dialog_is_ok, null);

        Bundle bundle = getArguments();
        if(bundle!=null){
            isPatho= bundle.getInt("isOKChoice");
        }

        if (isPatho==1){
            Toast.makeText(getActivity(), "보호자 선택", Toast.LENGTH_SHORT).show();
        }
        else if(isPatho==2){
            Toast.makeText(getActivity(), "재활사 선택", Toast.LENGTH_SHORT).show();
            binding.layoutInputOrg.setVisibility(View.VISIBLE);
        }
    }

    public boolean isValidEmail(String email) {
        final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }




    //by retrofit2
    private void nokSignInToServer(NokDto signInNokdto){
        binding.ltSiLoadingDots.setVisibility(View.VISIBLE);
        RetrofitService retrofitService=new RetrofitService(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI=retrofitService.getRetrofit().create(RetrofitAPI.class);


        try {
            retrofitAPI.nokSignIn(signInNokdto)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            binding.ltSiLoadingDots.setVisibility(View.GONE);
                            if (response.isSuccessful()) {
                                String result = response.body();
                                if (result != null && result.equals("yes")) {
                                    // nokSignIn 성공
                                    Toast.makeText(getActivity(), "보호자 회원등록 성공", Toast.LENGTH_SHORT).show();
                                    getActivity().onBackPressed();
                                    Log.e("login 추출","서버코드: "+response.code());
                                } else {
                                    // nokSignIn 실패
                                    Toast.makeText(getActivity(), "보호자 회원등록 실패", Toast.LENGTH_SHORT).show();
                                    Log.e("login 추출","서버코드: "+response.code());
                                }
                            } else {
                                // 서버 오류 등의 이유로 요청 실패
                                Log.e("signInERR","서버코드: " +response.code());
                                Toast.makeText(getActivity(), "서버 오류: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            binding.ltSiLoadingDots.setVisibility(View.GONE);
                            // 통신 실패 : 뭔가 여기서 자꾸 Use JsonReader.setLenient(true) to accept malformed JSON at line 1 column 1 path $
                            //에러가 걸리는데 서버에는 정상적으로 들어옴
                            Log.e("signInERR",t.getMessage());
                            if(t.getMessage().equals("JSON document was not fully consumed.")){
                                Toast.makeText(getActivity(), "회원가입 완료!", Toast.LENGTH_SHORT).show();
                                getActivity().onBackPressed();
                            }
                            else
                                Toast.makeText(getActivity(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
        }
        catch (Exception e){}
    }

    //by retrofit2
    private void pathSignInToServer(PathologistDto pathologistDto){

        RetrofitService retrofitService=new RetrofitService(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI=retrofitService.getRetrofit().create(RetrofitAPI.class);


        try {
            retrofitAPI.pathSignIn(pathologistDto)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                String result = response.body();
                                if (result != null && result.equals("yes")) {
                                    // nokSignIn 성공
                                    Toast.makeText(getActivity(), "재활사 회원등록 성공", Toast.LENGTH_SHORT).show();
                                    getActivity().onBackPressed();
                                } else {
                                    Toast.makeText(getActivity(), "재활사 회원등록 실패", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // 서버 오류 등의 이유로 요청 실패
                                Toast.makeText(getActivity(), "서버 오류: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            // 통신 실패
                            Log.e("signInERR2",t.getMessage());
                            if(t.getMessage().equals("JSON document was not fully consumed.")){
                                Toast.makeText(getActivity(), "회원가입 완료!", Toast.LENGTH_SHORT).show();
                                getActivity().onBackPressed();
                            }
                            else
                                Toast.makeText(getActivity(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        catch (Exception e){

        }
    }


    @Override
    public void oncheckIdSuccess() {
        Toast.makeText(getActivity(),"사용 가능한 ID입니다.",Toast.LENGTH_SHORT).show();
        //레이아웃 업데이트 step++
        binding.layoutInputId.setVisibility(View.GONE);
        binding.layoutInputPw.setVisibility(View.VISIBLE);
        //changeStepColor(step_signIn_1_text,step_signIn_1_img,step_signIn_2_text,step_signIn_2_img);

        signinStep++;
    }

    @Override
    public void oncheckIdFailure(String msg) {
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void oncheckIdError(String msg) {
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }
}