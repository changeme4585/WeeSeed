package com.example.weeseed_test.child.adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weeseed_test.R;
import com.example.weeseed_test.child.ItemClickListener;
import com.example.weeseed_test.dto.ChildDto_forUSE;

import java.util.List;

public class UnlinkChildListAdapter extends RecyclerView.Adapter<UnlinkChildListHolder> {
    private List<ChildDto_forUSE> childList;
    private ItemClickListener itemClickListener;
    private Boolean isGrid;

    public UnlinkChildListAdapter(List<ChildDto_forUSE> childList, ItemClickListener itemClickListener, Boolean isGrid) {
        this.childList = childList;
        this.itemClickListener=itemClickListener;
        this.isGrid=isGrid;
    }

    @NonNull
    @Override
    public UnlinkChildListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //list의 총 길이만큼 제작됨!
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_select_child, parent, false);

        return new UnlinkChildListHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UnlinkChildListHolder holder, int position) {
        ChildDto_forUSE childdto=childList.get(position);
        Log.e("onBindViewHolder","이름: "+childdto.getName());
        holder.bind(childdto);
    }

    @Override
    public int getItemCount() {
        int isize;
        try{
            isize=childList.size();
            Log.e("getItemCount","childList Size: "+isize);
        }
        catch(Exception t){

            Log.e("getItemCountERR","ERR) "+t.getMessage());
        }
        return childList.size(); // childList 크기 반환
    }
}
