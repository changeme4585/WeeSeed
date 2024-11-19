package com.example.weeseed_test.child;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.weeseed_test.activity.MainActivity;
import com.example.weeseed_test.databinding.FragmentLinkChildBinding;
import com.example.weeseed_test.util.RetrofitService3;
import com.example.weeseed_test.util.Viewmodel;
import com.example.weeseed_test.util.RetrofitAPI;
import com.example.weeseed_test.util.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LinkChildFragment extends Fragment {
    FragmentLinkChildBinding binding;
    Viewmodel viewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = ((MainActivity) requireActivity()).getViewModel();
        binding = FragmentLinkChildBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        binding.lcBtnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //아동 연동
                String childCode = binding.lcEtChildCode.getText().toString();
                if(!childCode.isEmpty())
                    linkChildToServer(childCode, viewModel.getUserID());
                else
                    Toast.makeText(getActivity(), "아동코드를 입력해주세요", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    private void linkChildToServer(String childCode, String userId) {
        Log.e("addChildToServer", "1: "+childCode+"  "+userId + " "+viewModel.getSvaddr());

        RetrofitService3 retrofitService = new RetrofitService3(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI = retrofitService.getRetrofit().create(RetrofitAPI.class);
        Log.e("addChildToServer", "2: ");


        retrofitAPI.linkChild(childCode, userId)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            Log.e("linkChildToServer", "log10: ");
                            String result = response.body();
                            Log.e("linkChildToServer", "log11.1: "+response.body());
                            Log.e("linkChildToServer", "log11.2: "+result);
                            if (result != null && result.equals("ok")) {
                                // addChild 성공
                                Log.e("linkChildToServer", "성공: ");
                                getActivity().onBackPressed();
                            } else {
                                // addChild 실패
                                Toast.makeText(getActivity(), "linkChildToServer 실패", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // 서버 오류 등의 이유로 요청 실패
                            Log.e("linkChildToServerERR", "서버 오류: " + response.code());
                            Toast.makeText(getActivity(), "서버 오류: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        // 통신 실패 : 뭔가 여기서 자꾸 Use JsonReader.setLenient(true) to accept malformed JSON at line 1 column 1 path $
                        //에러가 걸리는데 서버에는 정상적으로 들어옴
                        Log.e("linkChildToServerERR2", t.getMessage());
                        Toast.makeText(getActivity(), "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    ////////

}