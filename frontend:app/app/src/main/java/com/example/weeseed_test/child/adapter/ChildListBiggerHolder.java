package com.example.weeseed_test.child.adapter;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weeseed_test.R;
import com.example.weeseed_test.child.ItemClickListener;
import com.example.weeseed_test.dto.ChildDto_forUSE;

public class ChildListBiggerHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{
    TextView name, birth, disaType, disaGrade;
    ImageView profImg, iv_sc_gender;
    ItemClickListener itemClickListener;
    ChildDto_forUSE childDto;


    public ChildListBiggerHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
        super(itemView);
        profImg = itemView.findViewById(R.id.iv_sc_it_child_profImg_bigger);
        name = itemView.findViewById(R.id.tv_sc_it_childName_bigger);
        birth = itemView.findViewById(R.id.tv_sc_it_childBirth_bigger);
        disaType = itemView.findViewById(R.id.tv_sc_it_childDisaType_bigger);
        disaGrade = itemView.findViewById(R.id.tv_sc_it_childDisaGrade_bigger);
        iv_sc_gender=itemView.findViewById(R.id.iv_sc_gender_bigger);

        this.itemClickListener = itemClickListener;
        itemView.setOnClickListener(this);
    }

    public void bind(ChildDto_forUSE childDto) {
        this.childDto = childDto;
        name.setText(childDto.getName().toString());
        disaType.setText(childDto.getDisabilityType().toString());
        disaGrade.setText(String.valueOf(childDto.getGrade()));

        ////////


        if(childDto.getGender().toString().equals("M"))
            iv_sc_gender.setImageResource(R.drawable.child_gender_m);

        //YYYY:MM:DD
        String origBirth =childDto.getBirth().toString();
        String[] parts = origBirth.split(":");
        Log.e("childList","BIRTH: "+childDto.getBirth().toString());
        if (parts.length >= 3) {
            String year = parts[0];
            String month = parts[1];
            String day = parts[2];

            birth.setText(year + "." + month + "." + day);

            int profVal = Integer.parseInt(day);


            switch (profVal / 9) {
                case 0:
                    profImg.setImageResource(R.drawable.prof_child_big_9);
                    break;
                case 1:
                    profImg.setImageResource(R.drawable.prof_child_big_1);
                    break;
                case 2:
                    profImg.setImageResource(R.drawable.prof_child_big_2);
                    break;
                case 3:
                    profImg.setImageResource(R.drawable.prof_child_big_3);
                    break;
                case 4:
                    profImg.setImageResource(R.drawable.prof_child_big_4);
                    break;
                case 5:
                    profImg.setImageResource(R.drawable.prof_child_big_5);
                    break;
                case 6:
                    profImg.setImageResource(R.drawable.prof_child_big_6);
                    break;
                case 7:
                    profImg.setImageResource(R.drawable.prof_child_big_7);
                    break;
                case 8:
                    profImg.setImageResource(R.drawable.prof_child_big_8);
            }

        }



    }

    @Override
    public void onClick(View v) {
        if (itemClickListener != null) {
            itemClickListener.onItemClick(childDto);
        }
    }
}