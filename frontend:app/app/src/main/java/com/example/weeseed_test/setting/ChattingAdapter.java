package com.example.weeseed_test.setting;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weeseed_test.databinding.ItemChattingBinding;
import com.example.weeseed_test.dto.Chat;

import java.util.List;

public class ChattingAdapter extends RecyclerView.Adapter<ChattingAdapter.ChattingHolder> {

    private List<Chat> string;
    private final Context context;

    public ChattingAdapter(Context context, List<Chat> appList){
        this.context = context;
        this.string = appList;
    }
    public void updateList(List<Chat> newList){
        Log.e("TAG","updateList 진입");
        this.string = newList;
    }

    @NonNull
    @Override
    public ChattingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChattingBinding binding = ItemChattingBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ChattingHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChattingHolder holder, int position) {
        Chat chat = string.get(position);
        holder.bind(chat);
    }

    @Override
    public int getItemCount() {
        return string.size();
    }

    //////////////
    public class ChattingHolder extends RecyclerView.ViewHolder{
        ItemChattingBinding binding;

        public ChattingHolder(@NonNull ItemChattingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Chat chat){

            if (chat.isChatBot()){
                binding.layoutChatbot.setVisibility(View.VISIBLE);
                binding.layoutUser.setVisibility(View.GONE);
                binding.tvChatBots.setText(chat.getMessage());
            }
            else{
                binding.layoutChatbot.setVisibility(View.GONE);
                binding.layoutUser.setVisibility(View.VISIBLE);
                binding.tvUsers.setText(chat.getMessage());
            }
        }
    }

}
