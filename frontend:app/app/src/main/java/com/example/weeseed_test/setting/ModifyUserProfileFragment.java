package com.example.weeseed_test.setting;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.weeseed_test.activity.MainActivity;
import com.example.weeseed_test.databinding.FragmentModifyUserProfileBinding;
import com.example.weeseed_test.dto.NokDto;
import com.example.weeseed_test.dto.PathologistDto;
import com.example.weeseed_test.util.Viewmodel;
import com.example.weeseed_test.util.RetrofitAPI;
import com.example.weeseed_test.util.RetrofitService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ModifyUserProfileFragment extends Fragment {

    FragmentModifyUserProfileBinding binding;
    Viewmodel viewModel;
    boolean isEditmode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = ((MainActivity) requireActivity()).getViewModel();
        binding = FragmentModifyUserProfileBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        isEditmode = false;

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUI();
    }

    ///////

    private void setUI(){

        if(viewModel.getUserType().equals("Nok")) {
            binding.layoutInputOrg.setVisibility(View.INVISIBLE);
            binding.etId.setText(viewModel.getUserNok().getNokId());
            binding.etEmail.setText(viewModel.getUserNok().getEmail());
            binding.etName.setText(viewModel.getUserNok().getName());
        }
        else if(viewModel.getUserType().equals("Path")) {
            binding.etId.setText(viewModel.getUserPatho().getPathologistId());
            binding.etEmail.setText(viewModel.getUserPatho().getEmail());
            binding.etName.setText(viewModel.getUserPatho().getName());
            binding.etOrg.setText(viewModel.getUserPatho().getOrganizationName());
        }

        //buttons
        binding.muBtnGo.setOnClickListener(v -> {
            if(isEditmode)
                updateUserInfoModule();
            else
                setEditTestsAbility(true);
        });
        binding.etId.setOnClickListener(v -> {
            if(isEditmode)Toast.makeText(getActivity(), "아이디는 수정할 수 없습니다.", Toast.LENGTH_SHORT).show();
        });
        binding.btnBack.setOnClickListener(v -> getActivity().onBackPressed());

    }

    private void setEditTestsAbility(boolean stat){
        binding.etName.setEnabled(stat);
        binding.etEmail.setEnabled(stat);
        binding.etOrg.setEnabled(stat);
        if (stat) {
            binding.muBtnGo.setText("확인");
            binding.muTvDesc.setText("수정할 정보를 입력해주세요.");
        }
        else {
            binding.muBtnGo.setText("수정하기");
            binding.muTvDesc.setText("  ");
        }
        isEditmode = stat;
    }

    private void updateUserInfoModule (){

        if(viewModel.getUserType().equals("Nok")) {
            NokDto newinfo = viewModel.getUserNok();

            newinfo.setName(binding.etName.getText().toString());
            newinfo.setEmail(binding.etEmail.getText().toString());


            if(checkFormFindId(newinfo.getName(),newinfo.getEmail(),"NONE",false))
                updateNokInfo(newinfo);
        }
        else if(viewModel.getUserType().equals("Path")) {
            PathologistDto newinfo = viewModel.getUserPatho();

            newinfo.setName(binding.etName.getText().toString());
            newinfo.setEmail(binding.etEmail.getText().toString());
            newinfo.setOrganizationName(binding.etOrg.getText().toString());

            if(checkFormFindId(newinfo.getName(),newinfo.getEmail(),newinfo.getOrganizationName(),true))
                updatePathInfo(newinfo);
        }
    }
    private void updateNokInfo(NokDto nokDto) {
        String TAG = "updateNokInfo";

        RetrofitService retrofitService = new RetrofitService(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI = retrofitService.getRetrofit().create(RetrofitAPI.class);

        retrofitAPI.updateNok(nokDto)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.e(TAG, "1: ");
                            Toast.makeText(getActivity(), "프로필 수정 성공!", Toast.LENGTH_SHORT).show();
                            setEditTestsAbility(false);
                        }
                        else {
                            Log.e(TAG, "2: ");
                            Toast.makeText(getActivity(), "프로필 수정 실패", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        t.printStackTrace();
                        // 실패 처리
                        Log.e(TAG, "ERR!!: 3. "+t.getMessage());
                        Toast.makeText(getActivity(), "에러!! : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updatePathInfo(PathologistDto pathologistDto) {
        String TAG = "updatePathInfo";

        RetrofitService retrofitService = new RetrofitService(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI = retrofitService.getRetrofit().create(RetrofitAPI.class);

        retrofitAPI.updatePath(pathologistDto)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.e(TAG, "1: ");
                            Toast.makeText(getActivity(), "프로필 수정 성공!", Toast.LENGTH_SHORT).show();
                            setEditTestsAbility(false);
                        }
                        else {
                            Log.e(TAG, "2: ");
                            Toast.makeText(getActivity(), "프로필 수정 실패", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        t.printStackTrace();
                        // 실패 처리
                        Log.e(TAG, "ERR!!: 3. "+t.getMessage());
                        Toast.makeText(getActivity(), "에러!! : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean checkFormFindId(String name, String email, String org, boolean isPatho){
        if (name.isEmpty()) {
            binding.descEtName.setText("이름을 입력해주세요");
            return false;
        }

        if (email.isEmpty()) {
            binding.descEtEmail.setText("이메일을 입력해주세요");
            return false;
        }
        if (!isValidEmail(email)) {
            binding.descEtEmail.setText("적절한 이메일을 입력해주세요");
            return false;
        }

        if(isPatho && org.isEmpty()) {
            binding.descOrg.setText("소속 기관명을 입력해주세요");
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}