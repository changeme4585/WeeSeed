package com.example.weeseed_test.child.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.weeseed_test.R;

import java.util.List;

public class SpinnerChildInfoAdapter extends BaseAdapter {

    private Context context;
    private List<String> itemList;

    public SpinnerChildInfoAdapter(Context context, List<String> itemList){
        this.context=context;
        this.itemList=itemList;
    }

    @Override
    public int getCount() {
        if(itemList!=null)
            return itemList.size();
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View rootview = LayoutInflater.from(context)
                .inflate(R.layout.item_spinner_info, viewGroup, false);
        TextView tv=rootview.findViewById(R.id.tv_spinner_for_info);

        tv.setText(itemList.get(position));

        return rootview;
    }
}
