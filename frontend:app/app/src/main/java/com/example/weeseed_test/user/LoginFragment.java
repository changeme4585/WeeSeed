package com.example.weeseed_test.user;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.weeseed_test.R;
import com.example.weeseed_test.activity.IntroActivity;
import com.example.weeseed_test.activity.MainActivity;
import com.example.weeseed_test.databinding.FragmentLoginBinding;
import com.example.weeseed_test.setting.ChatBotFragment;
import com.example.weeseed_test.util.IsOkDialog;
import com.example.weeseed_test.util.IsOkViewmodel;
import com.example.weeseed_test.util.PreferenceManager;
import com.example.weeseed_test.util.Viewmodel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;

    Viewmodel viewmodel;
    IsOkViewmodel isOkViewModel;
    String sessionId;
    String id, pw;
    Boolean isHidden;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        viewmodel = ((IntroActivity) requireActivity()).getViewModel();
        isOkViewModel = ((IntroActivity) requireActivity()).getIsOkViewModel();


        viewmodel.setSvaddr(binding.hEtSvip.getText().toString(),binding.hEtSvport.getText().toString());
//        viewmodel.setSvaddr(binding.hEtSvip3.getText().toString(),binding.hEtSvport3.getText().toString());

        Log.e("server",viewmodel.getSvaddr());

        viewmodel.setVoice_svaddr(binding.hEtSvip2.getText().toString(),binding.hEtSvport2.getText().toString());

        //서버주소 설정
        isHidden=true;

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.lgId.setText("");
        binding.lgPw.setText("");
        getLoginInfo();
        setupButtons();


    }

    void saveLoginInfo(String username, String password){
        if(binding.checkBoxLgSave.isChecked()){
            PreferenceManager.setString(requireContext(), "MY_ID", username);
            PreferenceManager.setString(requireContext(), "MY_PW", password);
            PreferenceManager.setBoolean(requireContext(), "AUTO_LOGIN", true);
        }
        else{
            PreferenceManager.setString(requireContext(), "MY_ID", "");
            PreferenceManager.setString(requireContext(), "MY_PW", "");
            PreferenceManager.setBoolean(requireContext(), "AUTO_LOGIN", false);
        }
    }

    void getLoginInfo(){
        if(PreferenceManager.getBoolean(requireContext(),"AUTO_LOGIN")){
            binding.checkBoxLgSave.setChecked(true);

            binding.lgId.setText(PreferenceManager.getString(requireContext(),"MY_ID"));
            binding.lgPw.setText(PreferenceManager.getString(requireContext(),"MY_PW"));
        }
    }


    private void setupButtons(){
        binding.ivBabyFace.setOnClickListener(v -> {
            if(isHidden){
                binding.hiddenSvaddr.setVisibility(View.VISIBLE);   //서버 주소 설정버튼
                isHidden = false;
            }
            else {
                binding.hiddenSvaddr.setVisibility(View.GONE);  isHidden=true;
            }
        });
        binding.hBtnSetSvadddr.setOnClickListener(v -> {
            viewmodel.setSvaddr(binding.hEtSvip.getText().toString(),binding.hEtSvport.getText().toString());
            Toast.makeText(getActivity(),"서버 주소 설정 완료",Toast.LENGTH_SHORT).show();
        });
        binding.hBtnSetSvadddr2.setOnClickListener(v -> {
            viewmodel.setVoice_svaddr(binding.hEtSvip2.getText().toString(),binding.hEtSvport2.getText().toString());
            Toast.makeText(getActivity(),"음성ai 서버 주소 설정 완료",Toast.LENGTH_SHORT).show();
        });
        binding.hBtnSetSvadddr3.setOnClickListener(v -> {
            viewmodel.setSvaddr_g(binding.hEtSvip3.getText().toString(),binding.hEtSvport3.getText().toString());
            viewmodel.setSvaddr(binding.hEtSvip3.getText().toString(),binding.hEtSvport3.getText().toString());
//            Toast.makeText(getActivity(),"gpt서버 주소 설정 완료",Toast.LENGTH_SHORT).show();

            ((IntroActivity) requireActivity()).addStackFragment(new ChatBotFragment());

        });


        //login
        binding.btnLogin.setOnClickListener(v -> {
            Log.e("login","clicked ");
            final String username = binding.lgId.getText().toString();
            final String password = binding.lgPw.getText().toString();

            saveLoginInfo(username, password);  //로그인 정보 저장

            //공백 검사
            if (!username.isEmpty() && !password.isEmpty()) {
                binding.ltLgLoadingDots.setVisibility(View.VISIBLE);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    // 네트워크 작업 수행
                    loginToServer(username, password);
                });

                //아이디 비번 길이 검사 (불필요한 요청 막기)
                if(username.length() >= 5 || username.length() <= 15){
                    if(password.length()<10)
                        Toast.makeText(getActivity(),"비밀번호를 확인해주세요",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getActivity(),"아이디를 확인해주세요",Toast.LENGTH_SHORT).show();
            }
            else {
                //미수행 case1: id/pw 미기입
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() { Toast.makeText(getActivity(), "아이디와 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();}
                });
            }
        });
        //회원가입 버튼 클릭 리스너 : frag add
        binding.btnSignin.setOnClickListener(v -> {

            isOkViewModel.setStr_title_isOk("재활사인가요?");
            isOkViewModel.setStr_desc_isOk("장애 아동의 재활을 돕는\n재활사인지 알려주세요");
            isOkViewModel.setStr_left_isOk("보호자");
            isOkViewModel.setStr_right_isOk("재활사");
            isOkViewModel.setSituation_isOk("signIn");

            IsOkDialog isOkDialog = new IsOkDialog();
            // FragmentTransaction 시작
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.layout_for_dialog, isOkDialog); // replace 메서드로 Fragment를 교체합니다.
            transaction.addToBackStack(null); // 백스택에 추가하여 이전 Fragment로 돌아갈 수 있도록 합니다.
            transaction.commit(); // 변경사항을 적용합니다.

        });

        //아이디 찾기 버튼 클릭 리스너 : frag add
        binding.btnFindID.setOnClickListener(v -> ((IntroActivity) requireActivity()).addStackFragment(new FindIdFragment()));

        //비밀번호 변경 버튼 클릭 리스너 : frag add
        binding.btnResetPW.setOnClickListener(v -> ((IntroActivity) requireActivity()).addStackFragment(new ResetPwFragment()));

        //ID입력창의 IME 리스너인데 아직 작동 안됨.. 시간 남으면 처리할 것
        binding.lgId.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_NEXT){
                    binding.lgPw.requestFocus();
                    return true;
                }
                return false;
            }
        });


        //아이디, 비밀번호 입력 textwatcher (로그인 버튼 비활성화 위함)
        binding.lgId.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setLoginButton();
            }
            @Override
            public void afterTextChanged(Editable s) {}

        });
        binding.lgPw.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setLoginButton();
            }
            @Override
            public void afterTextChanged(Editable s) {}

        });

        //imeOptions

        binding.lgId.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                    binding.lgPw.requestFocus(); // 다음 EditText로 포커스 이동
                    return true;
                }
                return false;
            }
        });
        binding.lgPw.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    binding.btnLogin.performClick();    //로그인 수행
                    return true;
                }
                return false;
            }
        });

        ///////////////TEMP 기능 테스트용
        binding.btnTempFrag.setOnClickListener(v -> ((IntroActivity) requireActivity()).addStackFragment(new TempFragment()));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null; // 바인딩 해제
    }


    ///////////나중에 내릴 것들/////////////
    //UI: ID나 비번 미입력시 버튼 비활성화
    private void setLoginButton(){
        id=binding.lgId.getText().toString();
        pw=binding.lgPw.getText().toString();
        if (id.isEmpty()||pw.isEmpty()){
            binding.btnLogin.setEnabled(false);
            binding.btnLogin.setBackgroundColor(requireContext().getResources().getColor(R.color.gray));
            binding.btnLogin.setTextColor(requireContext().getResources().getColor(R.color.font_darkgray));
        }
        else{
            binding.btnLogin.setEnabled(true);
            binding.btnLogin.setBackgroundColor(requireContext().getResources().getColor(R.color.brand_yellow));
            binding.btnLogin.setTextColor(requireContext().getResources().getColor(R.color.font_black));
        }
    }

    //////////////로그인 메소드/////////

    private void loginToServer(String username, String password) {
        try {
            Log.e("login","1: ");
            String baseUrl = viewmodel.getSvaddr();
            URL url = new URL(baseUrl+"/login"); // 서버 URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Log.e("logincon",conn.toString());
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);

            String jsonInputString = "{\"id\": \"" + username + "\", \"password\": \"" + password + "\"}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
                Log.e("login","4.1: ");
            }
            catch (Exception e){Log.e("login","ERR: "+e.getMessage());}
            Log.e("login","5: ");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = in.readLine()) != null) {
                response.append(responseLine.trim());
            }
            in.close();
            int code = conn.getResponseCode();
            Log.e("login 추출","서버코드: "+code);

            // 응답 메시지를 로그로 출력
            String responseMessage = response.toString();
            Log.e("login 추출","서버 응답: "+response.toString());
            if (code == 200) {

                Map<String, List<String>> headerFields = conn.getHeaderFields();
                List<String> cookies = headerFields.get("Set-Cookie");
                if (cookies != null) {
                    for (String cookie : cookies) {
                        if (cookie.startsWith("JSESSIONID")) {
                            String[] parts =cookie.split("[;=]");// 세션 ID 추출
                            sessionId = parts[1];
                            Log.e("login 추출","1userID: "+ username+"  세션ID: " +sessionId + "  유저 타입:  "+responseMessage);  //for 세션 ID 추출
                            viewmodel.setUserID(username);
                            viewmodel.setSessionID(sessionId);
                            viewmodel.setUserType(responseMessage);
                            break;
                        }
                    }
                }
                getActivity().runOnUiThread(() -> {
                    if(binding.ltLgLoadingDots.getVisibility()==View.VISIBLE)
                        binding.ltLgLoadingDots.setVisibility(View.GONE);
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("SESSION_ID", sessionId);
                    intent.putExtra("USER_ID", username);
                    intent.putExtra("USER_TYPE", responseMessage);
                    intent.putExtra("SVADDR", viewmodel.getSvaddr());
                    intent.putExtra("VOICESVADDR", viewmodel.getVoice_svaddr());
                    startActivity(intent);
//                        getActivity().finish();   //어플리케이션 꺼짐 문제로 인해 잠시
                });
            } else {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getActivity(), "로그인 실패", Toast.LENGTH_SHORT).show();
                    if(binding.ltLgLoadingDots.getVisibility()==View.VISIBLE)
                        binding.ltLgLoadingDots.setVisibility(View.GONE);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("loginERR","2ERR: "+e.getMessage());
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getActivity(), "로그인 실패", Toast.LENGTH_SHORT).show();
                if(binding.ltLgLoadingDots.getVisibility()==View.VISIBLE)
                    binding.ltLgLoadingDots.setVisibility(View.GONE);
            });
        }
    }
}