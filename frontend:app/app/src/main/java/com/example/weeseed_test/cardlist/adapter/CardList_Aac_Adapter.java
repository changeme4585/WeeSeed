package com.example.weeseed_test.cardlist.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.weeseed_test.cardlist.WebCardItemClickListener;
import com.example.weeseed_test.databinding.ItemCardAacBinding;
import com.example.weeseed_test.dto.Aac_item;

import java.util.List;



public class CardList_Aac_Adapter extends RecyclerView.Adapter<CardList_Aac_Adapter.CardListGlideHolder> {

    private List<Aac_item> aacList;
    private WebCardItemClickListener listener;



    public CardList_Aac_Adapter(List<Aac_item> aacList) {
        this.aacList = aacList;
    }
    public void setOnDataChangedListener(WebCardItemClickListener listener){
        this.listener = listener;
    }

    ////커스텀 메소드////

    @Override
    public int getItemViewType(int position){
        return position;
    }

    public void updateList(List<Aac_item> newlist){
        Log.e("TAG","updateList 진입");
        this.aacList = newlist;
    }
    ///////////////
    @NonNull
    @Override
    public CardListGlideHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCardAacBinding binding= ItemCardAacBinding.inflate(LayoutInflater.from(parent.getContext()), parent,false);
        return new CardListGlideHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CardListGlideHolder holder, int position) {
        Aac_item item = aacList.get(position);
        if (item == null) return;
        Log.e("onBindViewHolder", "카드명: " + item.getCardName() + " 선택 상태: " + item.isSelected()+" 위치: "+position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {return aacList.size();}


    /////////////// inner holder class
    public class CardListGlideHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {

        private ItemCardAacBinding b;
        Aac_item aacItem;

        private Handler holdHandler = new Handler();
        private Runnable holdRunnable;
        private boolean isHoldding = false;


        public CardListGlideHolder(@NonNull ItemCardAacBinding binding) {
            super(binding.getRoot());
            this.b = binding;
            itemView.setOnTouchListener(this);
        }

        public void bind(Aac_item item) {
            if (item == null) return;
            this.aacItem = item;

            // 기존 리스너 제거 후 선택 상태 적용
            b.cbAclSelect.setOnCheckedChangeListener(null);
            b.cbAclSelect.setChecked(aacItem.isSelected()); b.vAclSelect.setVisibility(aacItem.isSelected() ? View.VISIBLE : View.INVISIBLE);

            // 새로운 체크박스 리스너 설정
            b.cbAclSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
                aacItem.setSelected(isChecked);
                b.vAclSelect.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
                if (listener != null) {
                    listener.onItemClick_aacDelete(aacItem, isChecked);
                }
            });

            setupUI(aacItem);

            b.itemAclCardView.setOnClickListener(v -> {

                if (aacItem.getCardType() == 1) {
                    listener.onItemClick_aac(aacItem);
                }
                else {
                    if (aacItem.isCheckBoxVisible())
                        b.cbAclSelect.performClick();
                    else
                        listener.onItemClick_aac(aacItem);
                }
            });
        }

        private void setupUI(Aac_item item){
            // 배경 색상 및 텍스트 설정
            int cardColor;
            cardColor = Color.parseColor("#" + item.getColor());
            if (item.isCheckBoxVisible() && (item.getCardType() == 1)) {
                item.setCheckBoxVisible(false);
            }
            b.itemAclCardView.setCardBackgroundColor(cardColor);
            b.tvAclName.setText(item.getCardName());

            // 카드 타입에 따른 이미지 설정
            if (item.getCardType() == 1) { // 추가 모드
                b.tvAclName.setVisibility(View.GONE);
                b.ivAclImg.setVisibility(View.GONE);
                b.ivAclAdd.setVisibility(View.VISIBLE);
                b.cbAclSelect.setVisibility(View.INVISIBLE);
            }
            else if (item.getCardType() == 2) { // 기본 카드
                b.tvAclName.setVisibility(View.VISIBLE);
                b.ivAclImg.setVisibility(View.VISIBLE);
                b.ivAclAdd.setVisibility(View.GONE);
                Bitmap bitmap = BitmapFactory.decodeByteArray(item.getCardImage_def(), 0, item.getCardImage_def().length);
                Glide.with(itemView.getContext()).load(bitmap).into(b.ivAclImg);
            }
            else { // 일반 카드
                b.tvAclName.setVisibility(View.VISIBLE);
                b.ivAclImg.setVisibility(View.VISIBLE);
                b.ivAclAdd.setVisibility(View.GONE);
                Glide.with(itemView.getContext()).load(item.getImage()).into(b.ivAclImg);
            }

            // 체크박스 표시 여부 설정
            b.cbAclSelect.setVisibility(item.isCheckBoxVisible() ? View.VISIBLE : View.INVISIBLE);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    isHoldding = true;
                    holdRunnable = () -> {
                        if(!(aacItem.isCheckBoxVisible()))
                            if(isHoldding && listener != null){
                                listener.onItemLongClick_aac(aacItem);  ////long click (수정/개별삭제)
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
