package com.example.weeseed_test.card.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weeseed_test.R;

public class ImageSliderAdapter extends RecyclerView.Adapter <ImageSliderAdapter.MyViewHolder> {
    private Context context;
    private String[] sliderImage;

    public ImageSliderAdapter(Context context, String[] sliderImage){
        this.context=context;
        this.sliderImage=sliderImage;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_slider,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bindSliderImage(sliderImage[position]);
    }

    @Override
    public int getItemCount() {
        return sliderImage.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView mimageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mimageView=itemView.findViewById(R.id.imageSlider);
        }

        public void bindSliderImage(String imageURL){
            Glide.with(context)
                    .load(imageURL)
                    .into(mimageView);
        }
    }

}
