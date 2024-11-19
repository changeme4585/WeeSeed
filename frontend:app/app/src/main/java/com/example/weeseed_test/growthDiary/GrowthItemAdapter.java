package com.example.weeseed_test.growthDiary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weeseed_test.R;
import com.example.weeseed_test.dto.GrowthDiaryDto;

import java.util.List;

public class GrowthItemAdapter extends RecyclerView.Adapter<GrowthItemAdapter.GrowthItemHolder> {

    private List<GrowthDiaryDto> diaryList;
    private GrowthItemClickListener listener;
    private String childName;

    public GrowthItemAdapter (List<GrowthDiaryDto> diaryList, GrowthItemClickListener listener, String childName){
        this.diaryList=diaryList;
        this.listener=listener;
        this.childName=childName;
    }

    @NonNull
    @Override
    public GrowthItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_growthdiary, parent, false);
        return new GrowthItemHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull GrowthItemHolder holder, int position) {
        GrowthDiaryDto dto = diaryList.get(position);
        holder.bind(dto, childName);
    }

    @Override
    public int getItemCount() {
        return diaryList.size();
    }

    ////
    public static class GrowthItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView child, date, maker;
        private GrowthItemClickListener listener;
        GrowthDiaryDto item;
        public GrowthItemHolder(@NonNull View itemView, GrowthItemClickListener listener) {
            super(itemView);
            child = itemView.findViewById(R.id.tv_gr_it_childName);
            date = itemView.findViewById(R.id.tv_gr_it_createDate);
            maker = itemView.findViewById(R.id.tv_gr_it_makerName);

            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        public void bind(GrowthDiaryDto item, String childName){
            this.item = item;
            String formattedDate = item.getCreationTime().replace(":",".");
            child.setText(childName);
            date.setText(formattedDate);
            maker.setText(item.getUserName());
        }

        @Override
        public void onClick(View v) {
            if (listener != null)
                listener.onItemClick(item);
        }
    }

}
