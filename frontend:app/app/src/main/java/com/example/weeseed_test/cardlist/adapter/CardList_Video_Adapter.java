package com.example.weeseed_test.cardlist.adapter;

import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weeseed_test.cardlist.VideoCardItemClickListener;
import com.example.weeseed_test.databinding.ItemCardVideoBinding;
import com.example.weeseed_test.dto.Video_item;

import java.util.List;

public class CardList_Video_Adapter extends RecyclerView.Adapter<CardList_Video_Adapter.CardList_Video_Holder> {


    private List<Video_item> videoList;
    private VideoCardItemClickListener listener;


    public CardList_Video_Adapter(List<Video_item> videoList){
        this.videoList=videoList;
    }

    public void setOnDataChangedListener(VideoCardItemClickListener listener){
        this.listener = listener;
    }

    ////커스텀 메소드////

    @Override
    public int getItemViewType(int position){
        return position;
    }

    public void updateList(List<Video_item> newlist){
        Log.e("TAG","updateList 진입");
        this.videoList = newlist;
    }

    ///

    @NonNull
    @Override
    public CardList_Video_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCardVideoBinding binding= ItemCardVideoBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new CardList_Video_Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CardList_Video_Holder holder, int position) {
        Video_item item = videoList.get(position);
        Log.e("onBindViewHolder[VID]","[vid]카드명: "+item.getCardName()+" 아이디: "+item.getVideoCardId());

        holder.bind(item);
    }
    @Override
    public int getItemCount() {
        return videoList.size(); // videoList 크기 반환
    }

    /////////////// inner holder class
    public class CardList_Video_Holder extends RecyclerView.ViewHolder implements View.OnTouchListener {

        private ItemCardVideoBinding b;
        Video_item videoItem;

        private Handler holdHandler = new Handler();
        private Runnable holdRunnable;
        private boolean isHoldding = false;

        public CardList_Video_Holder(@NonNull ItemCardVideoBinding binding) {
            super(binding.getRoot());
            this.b=binding;
            itemView.setOnTouchListener(this);
        }

        public void bind(Video_item item){
            this.videoItem =item;
            setupUI(videoItem);

            // 기존 리스너 제거 후 선택 상태 적용
            b.cbVclSelect.setOnCheckedChangeListener(null);
            b.cbVclSelect.setChecked(videoItem.isSelected()); b.vVclSelect.setVisibility(videoItem.isSelected() ? View.VISIBLE : View.INVISIBLE);

            // 새로운 체크박스 리스너 설정
            b.cbVclSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
                videoItem.setSelected(isChecked);
                b.vVclSelect.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
                if (listener != null) {
                    listener.onItemClick_vidDelete(videoItem, isChecked);
                }
            });


            b.itemVclCardViewWeb.setOnClickListener(v -> {
                if (item.getCardType() == 1) { // 추가 모드
                    listener.onItemClick_vid(videoItem);
                }
                else {
                    if (videoItem.isCheckBoxVisible())
                        b.cbVclSelect.performClick();
                    else
                        listener.onItemClick_vid(videoItem);
                }
            });
        }

        private void setupUI(Video_item item){
            int cardColor;
            cardColor= Color.parseColor("#"+item.getColor());
            b.itemVclCardViewWeb.setCardBackgroundColor(cardColor);

            Log.e("정보",item.getCardName()+"  "+item.getChildId()+"  "+item.getUserId());

            if (item.isCheckBoxVisible() && item.getCardType()==1) {    //case: delete mode
                item.setCheckBoxVisible(false);
            }


            b.cbVclSelect.setVisibility(item.isCheckBoxVisible() ? View.VISIBLE : View.INVISIBLE);  //checkbox 표시 여부
            try {
                ///cardType에 따라 다르게 이미지 처리///
                if (item.getCardType()==1) {
                    b.tvVclName.setVisibility(View.GONE);
                    b.ivVclImg.setVisibility(View.GONE);
                    b.ivVclAdd.setVisibility(View.VISIBLE);
                    b.cbVclSelect.setVisibility(View.INVISIBLE);
                }  else {
                    b.tvVclName.setVisibility(View.VISIBLE);
                    b.ivVclImg.setVisibility(View.VISIBLE);
                    b.ivVclAdd.setVisibility(View.GONE);

                    Glide.with(itemView.getContext())
                            .load(item.getThumbnailUrl())
                            .into(b.ivVclImg);
                }
            }
            catch (Exception e){Log.e("썸네일 로딩","[오류] "+e.getMessage());}
            b.tvVclName.setText(item.getCardName());
        }
        ///

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    isHoldding = true;
                    holdRunnable = () -> {
                        if(!(videoItem.isCheckBoxVisible()))
                            if(isHoldding && listener != null){
                                listener.onItemLongClick_vid(videoItem);  ////long click (수정/개별삭제)
                            }
                    };
                    holdHandler.postDelayed(holdRunnable,1000);
                    return false;


                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    isHoldding = false;
                    holdHandler.removeCallbacks(holdRunnable);
                    return false;
            }
            return false;
        }

    }
}
