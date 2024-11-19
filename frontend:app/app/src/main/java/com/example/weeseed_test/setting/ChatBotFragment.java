package com.example.weeseed_test.setting;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.example.weeseed_test.activity.IntroActivity;
import com.example.weeseed_test.activity.MainActivity;
import com.example.weeseed_test.databinding.FragmentChatBotBinding;
import com.example.weeseed_test.dto.Chat;
import com.example.weeseed_test.util.RetrofitAPI;
import com.example.weeseed_test.util.RetrofitService3;
import com.example.weeseed_test.util.RetrofitService5;
import com.example.weeseed_test.util.Viewmodel;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatBotFragment extends Fragment {

    private FragmentChatBotBinding binding;
    private List<Chat> chatList;
    private ChattingAdapter adapter;
    Viewmodel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = ((MainActivity) requireActivity()).getViewModel();
//        viewModel = ((IntroActivity) requireActivity()).getViewModel();
        binding = FragmentChatBotBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        chatList = new ArrayList<>();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUI();
    }

    private void setupUI(){
        binding.recyclerviewChatting.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new ChattingAdapter(requireContext(),chatList);
        binding.recyclerviewChatting.setAdapter(adapter);

        binding.seBtnBackTitleBar.setOnClickListener(v -> getActivity().onBackPressed());
        binding.btnChatbotInput.setOnClickListener(v -> requestChatModule(new Chat(binding.etChatbotInput.getText().toString(), false)));

        binding.btnChatbotAsk.setOnClickListener(v -> {
//            if(binding.layoutChatTip.isShown())
//                    binding.layoutChatTip.setVisibility(View.GONE);
//            else    binding.layoutChatTip.setVisibility(View.VISIBLE);
            askWithGPT();
        });

        binding.etChatbotInput.setOnEditorActionListener((textView, actionId, keyEvent) ->{
            if(actionId == EditorInfo.IME_ACTION_DONE){
                requestChatModule(new Chat(binding.etChatbotInput.getText().toString(), false));
                return true;
            }
            return false;
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void chatUIUpdate(Chat chat){
        chatList.add(chat);
        adapter.notifyDataSetChanged();
        binding.recyclerviewChatting.scrollToPosition(chatList.size() - 1);
        Log.e("add chat", "["+chat.isChatBot()+"]"+chat.getMessage());
        if (binding.ltLoadingDots.isShown())
            binding.ltLoadingDots.setVisibility(View.GONE);
    }

    private void requestChatModule(Chat chat){
        chatUIUpdate(chat);
        if(binding.layoutChatTip.isShown())
            binding.layoutChatTip.setVisibility(View.GONE);

        switch (chat.getMessage()){
            case "/취약단어":
                askWithGPT();
                break;
            case "/학습방법":           break;
            default:
                chatWithGPT(chat.getMessage());
                break;
        }
        binding.etChatbotInput.setText("");
    }

    private void askWithGPT(){
        String TAG = "questionWithGPT";
        String childCode = viewModel.getSltd_childdto().getChildCode();
        Log.e(TAG, "아동ID: "+childCode);

        binding.ltLoadingDots.setVisibility(View.VISIBLE);

        RetrofitService3 retrofitService = new RetrofitService3(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI = retrofitService.getRetrofit().create(RetrofitAPI.class);


        retrofitAPI.askGptResponse(childCode)  //변경 필요
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()&& response.body() != null) {
                            try {
                                String decodedResponse = new String(response.body().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                                chatUIUpdate(new Chat(decodedResponse, true));
                            }
                            catch (Exception e) {
                                Log.e(TAG, "디코딩 오류: " + response.code());
                                throw new RuntimeException(e);
                            }
                        } else {
                            // 서버 오류 등의 이유로 요청 실패
                            Log.e(TAG, "서버 오류: " + response.code());
                            chatUIUpdate(new Chat("죄송해요 잘 모르겠어요ㅜㅜ", true));
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e(TAG, "서버 오류: " + t.getMessage());
                        chatUIUpdate(new Chat("네트워크 환경을 확인해주세요ㅜㅜ", true));
                    }
                });
    }

    private void chatWithGPT(String prompt){
        String TAG = "chatWithGPT";
        RetrofitService3 retrofitService = new RetrofitService3(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI = retrofitService.getRetrofit().create(RetrofitAPI.class);


        retrofitAPI.getGptResponse(prompt)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()&& response.body() != null) {
                            try {
                                String decodedResponse = new String(response.body().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                                chatUIUpdate(new Chat(decodedResponse, true));
                            }
                            catch (Exception e) {
                                Log.e(TAG, "디코딩 오류: " + response.code());
                                throw new RuntimeException(e);
                            }
                        } else {
                            // 서버 오류 등의 이유로 요청 실패
                            Log.e(TAG, "서버 오류: " + response.code());
                            chatUIUpdate(new Chat("죄송해요 잘 모르겠어요.", true));
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e(TAG, "서버 오류: " + t.getMessage());
                        chatUIUpdate(new Chat("네트워크 환경을 확인해주세요.", true));
                    }
                });
    }


}