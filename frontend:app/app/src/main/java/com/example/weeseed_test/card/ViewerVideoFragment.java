package com.example.weeseed_test.card;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerControlView;
import androidx.media3.ui.PlayerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.weeseed_test.R;
import com.example.weeseed_test.activity.MainActivity;
import com.example.weeseed_test.databinding.FragmentViewerVideoBinding;
import com.example.weeseed_test.util.RetrofitService3;
import com.example.weeseed_test.util.Viewmodel;
import com.example.weeseed_test.util.RetrofitAPI;
import com.example.weeseed_test.util.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


//비디오 플레이어
public class ViewerVideoFragment extends Fragment {

    Viewmodel viewModel;
    private FragmentViewerVideoBinding binding;
    ///
    ExoPlayer player;
    PlayerView playerView;
    PlayerControlView playerControlView;

    private TextView titleTextView;
    private ImageButton backButton, exo_play, exo_pause;
    private Button btnDeleteVid;

    Boolean isControlShow, isPauseShow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = ((MainActivity) requireActivity()).getViewModel();
        binding = FragmentViewerVideoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        isControlShow = true;
        isPauseShow=true;

        //로그 남기기
        sendCardClicked_Server(viewModel.getSltd_videoCard().getVideoCardId(),"video");

        playerView = binding.pvVvVideoPlayer;
        playerControlView = binding.playerControlView;
        playerControlView.setShowTimeoutMs(0);  //ctrler 자동 숨김 비활성화

        player=new ExoPlayer.Builder(getContext()).build();
        player.setRepeatMode(ExoPlayer.REPEAT_MODE_ONE);
        binding.pvVvVideoPlayer.setPlayer(player);
        playerControlView.setPlayer(player);

        try {
            loadVideoFromUrl(viewModel.getSltd_videoCard().getVideoUrl());
        }
        catch (Exception e){Log.e("ViewerVideoFragment","VID ERR:  "+e.getMessage());}

        //커스텀 컨트롤러
        titleTextView=playerControlView.findViewById(R.id.tv_vv_video_cardName);
        titleTextView.setText(viewModel.getSltd_videoCard().getCardName());
        backButton=playerControlView.findViewById(R.id.ib_btn_back_viewer_video);
        backButton.setOnClickListener(v -> requireActivity().onBackPressed());
        exo_pause = playerControlView.findViewById(R.id.exo_pause);
        exo_pause.setOnClickListener(v -> {
            if (player.isPlaying()) {
                player.pause();
                exo_pause.setVisibility(View.GONE);  // pause 버튼 숨기기
                exo_play.setVisibility(View.VISIBLE);  // play 버튼 보이기
                isPauseShow = false;
            }
        });

        exo_play = playerControlView.findViewById(R.id.exo_play);
        exo_play.setOnClickListener(v -> {
            if (!player.isPlaying()) {
                player.play();
                exo_play.setVisibility(View.GONE);  // play 버튼 숨기기
                exo_pause.setVisibility(View.VISIBLE);  // pause 버튼 보이기
                isPauseShow = true;
            }
        });

        //
        playerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isControlShow){
                    playerControlView.setVisibility(View.INVISIBLE);
                    isControlShow=false;
                }
                else{
                    playerControlView.setVisibility(View.VISIBLE);
                    isControlShow=true;
                }
            }
        });

        btnDeleteVid= playerControlView.findViewById(R.id.btn_temp_delete_vid); //임시 비디오 삭제 버튼
        btnDeleteVid.setOnClickListener(v -> deleteVideoCard(viewModel.getSltd_videoCard().getVideoCardId()));


        return view;
    }

    private void loadVideoFromUrl(String url) {
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(url));
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (player != null) {
            player.release();
            player = null;
        }
        binding = null;
    }


    //////////서버로 click 정보 보내기////////////

    private void sendCardClicked_Server(Long cardId, String cardType){
        Log.e("sendAACClicked_Server", "1: ");

        RetrofitService retrofitService = new RetrofitService(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI = retrofitService.getRetrofit().create(RetrofitAPI.class);
        Log.e("sendAACClicked_Server", "2: ");

        try{
            retrofitAPI.sendClickLog(cardId,cardType)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Log.e("sendAACClicked_Server", "3: 카드ID: "+viewModel.getSltd_videoCard().getVideoCardId().toString());
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("sendAACClicked_ServerERR", t.getMessage());
                            Toast.makeText(getActivity(), "비디오 재생 오류", Toast.LENGTH_SHORT).show();
//                            Toast.makeText(getActivity(), "오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        catch (Exception e){
            Log.e("getAACCardFromServerERR","ERR) "+e.getMessage());
        }
    }


    //////////TEMP 1009 DELETE VID CARD////////

    public void deleteVideoCard(Long cardId) {
        //card ID version
        RetrofitService3 retrofitService=new RetrofitService3(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI=retrofitService.getRetrofit().create(RetrofitAPI.class);
        Log.e("deleteVideoCard","1: ");

        try{
            retrofitAPI.delete_video(cardId)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                Log.e("deleteVideoCard","성공: " +response.code());
                                Toast.makeText(getActivity(), cardId.toString()+"카드 삭제 성공", Toast.LENGTH_SHORT).show();
                                getActivity().onBackPressed();
                            } else {
                                // 서버 오류 등의 이유로 요청 실패
                                Log.e("deleteVideoCard","서버 오류: " +response.code());
                                Toast.makeText(getActivity(), "서버 오류: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("deleteVideoCard", t.getMessage());
                            Toast.makeText(getActivity(), "오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        catch (Exception e){
            Log.e("deleteVideoCard","ERR) "+e.getMessage());
            Toast.makeText(getActivity(), "오류", Toast.LENGTH_SHORT).show();
        }
    }
}