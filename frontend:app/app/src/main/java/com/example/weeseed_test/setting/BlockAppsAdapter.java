package com.example.weeseed_test.setting;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weeseed_test.databinding.ItemBlockAppBinding;
import com.example.weeseed_test.dto.AppInfo;

import java.util.List;

public class BlockAppsAdapter extends RecyclerView.Adapter<BlockAppsAdapter.BlockAppsHolder> {

    private List<AppInfo> appList;
    private final Context context;
    private AppItemClickListener listener;

    public BlockAppsAdapter(Context context, List<AppInfo> appList){
        this.context = context;
        this.appList = appList;
    }
    public void setOnDataChangedListener(AppItemClickListener listener){
        this.listener = listener;
    }

    public void updateList(List<AppInfo> newList){
        Log.e("TAG","updateList 진입");
        this.appList = newList;
    }

    @NonNull
    @Override
    public BlockAppsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBlockAppBinding binding = ItemBlockAppBinding.inflate(LayoutInflater.from(context), parent, false);
        return new BlockAppsHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BlockAppsHolder holder, int position) {
        AppInfo appInfo = appList.get(position);
        holder.bind(appInfo);
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    //////////////
    public class BlockAppsHolder extends RecyclerView.ViewHolder{
        ItemBlockAppBinding binding;

        public BlockAppsHolder(@NonNull ItemBlockAppBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(AppInfo appInfo){
            binding.ivAppIcon.setImageDrawable(appInfo.getIcon());
            binding.tvAppName.setText(appInfo.getName());

            binding.switchAppIsblock.setOnCheckedChangeListener(null); // 이전 리스너를 초기화
            binding.switchAppIsblock.setChecked(appInfo.isBlocked());

            binding.switchAppIsblock.setOnCheckedChangeListener((buttonView, isChecked) -> {
                appInfo.setBlocked(isChecked);
                if (listener != null) {
                    listener.onItemSwitchClick(appInfo.getPackageName(), isChecked);
                }
            });
        }
    }

}
