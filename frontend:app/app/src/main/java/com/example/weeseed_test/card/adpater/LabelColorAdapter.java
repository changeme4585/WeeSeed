package com.example.weeseed_test.card.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weeseed_test.R;
import com.example.weeseed_test.databinding.ItemSpinnerLabelColorBinding;
import com.example.weeseed_test.dto.LabelColor;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LabelColorAdapter extends BaseAdapter {
    private Context context;
    private List<LabelColor> itemList;

    public LabelColorAdapter(Context context){
        this.context=context;

        List<LabelColor> colors = new ArrayList<>();

        colors.add(new LabelColor(R.color.label_black,"검정"));
        colors.add(new LabelColor(R.color.label_red,"빨강"));
        colors.add(new LabelColor(R.color.label_orange,"주황"));
        colors.add(new LabelColor(R.color.label_yellow,"노랑"));
        colors.add(new LabelColor(R.color.label_green,"초록"));
        colors.add(new LabelColor(R.color.label_blue,"파랑"));
        colors.add(new LabelColor(R.color.label_deepblue,"남색"));
        colors.add(new LabelColor(R.color.label_purple,"보라"));

        this.itemList=colors;
    }

    public LabelColorAdapter(Context context, boolean withAll){
        this.context=context;

        List<LabelColor> colors = new ArrayList<>();
        colors.add(new LabelColor(R.color.white,"전체"));
        colors.add(new LabelColor(R.color.label_black,"검정"));
        colors.add(new LabelColor(R.color.label_red,"빨강"));
        colors.add(new LabelColor(R.color.label_orange,"주황"));
        colors.add(new LabelColor(R.color.label_yellow,"노랑"));
        colors.add(new LabelColor(R.color.label_green,"초록"));
        colors.add(new LabelColor(R.color.label_blue,"파랑"));
        colors.add(new LabelColor(R.color.label_deepblue,"남색"));
        colors.add(new LabelColor(R.color.label_purple,"보라"));

        this.itemList=colors;
    }

    public LabelColorAdapter(Context context, int alignVer){
        this.context=context;

        List<LabelColor> colors = new ArrayList<>();
        colors.add(new LabelColor(R.color.white,"기본순"));
        colors.add(new LabelColor(R.color.white,"최신순"));
        colors.add(new LabelColor(R.color.white,"색상순"));
        colors.add(new LabelColor(R.color.white,"제작자"));

        this.itemList=colors;
    }

    @Override
    public int getCount() {
        return itemList != null ? itemList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemSpinnerLabelColorBinding binding;

        if (convertView == null){
            binding = ItemSpinnerLabelColorBinding.inflate(LayoutInflater.from(context), parent,false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }
        else {
            binding= (ItemSpinnerLabelColorBinding) convertView.getTag();
        }

        LabelColor currentItem = itemList.get(position);
        binding.tvNameSpinnerLabelColor.setText(currentItem.getName());
        binding.ivChipSpinnerLabelColor.setImageResource(currentItem.getColor());

//        if (currentItem.getName().equals("ALL"))
//            binding.ivChipSpinnerLabelColor.setImageResource(R.drawable.rainbow_gradient);

        return convertView;
    }
}
