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

//Select Child recy-view에서 쓸 홀더!
public class ChildListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView name, birth, gender, disaType, disaGrade,code;
    ImageView profImg, iv_sc_gender;
    ItemClickListener itemClickListener;
    ChildDto_forUSE childDto;


    public ChildListHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
        super(itemView);
        profImg = itemView.findViewById(R.id.iv_sc_it_child_profImg);
        name = itemView.findViewById(R.id.tv_sc_it_childName);
        birth = itemView.findViewById(R.id.tv_sc_it_childBirth);
        gender = itemView.findViewById(R.id.tv_sc_it_childGender);
        disaType = itemView.findViewById(R.id.tv_sc_it_childDisaType);
        disaGrade = itemView.findViewById(R.id.tv_sc_it_childDisaGrade);
        code=itemView.findViewById(R.id.tv_sc_it_childCode);
        iv_sc_gender=itemView.findViewById(R.id.iv_sc_gender);

        this.itemClickListener = itemClickListener;
        itemView.setOnClickListener(this);
    }

    public void bind(ChildDto_forUSE childDto) {
        this.childDto = childDto;
        name.setText(childDto.getName().toString());
        gender.setText(childDto.getGender().toString());
        disaType.setText(childDto.getDisabilityType().toString());
        disaGrade.setText(String.valueOf(childDto.getGrade()));
        code.setText(String.valueOf(childDto.getChildCode()));

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
                    profImg.setImageResource(R.drawable.prof_child9);
                    break;
                case 1:
                    profImg.setImageResource(R.drawable.prof_child1);
                    break;
                case 2:
                    profImg.setImageResource(R.drawable.prof_child2);
                    break;
                case 3:
                    profImg.setImageResource(R.drawable.prof_child3);
                    break;
                case 4:
                    profImg.setImageResource(R.drawable.prof_child4);
                    break;
                case 5:
                    profImg.setImageResource(R.drawable.prof_child5);
                    break;
                case 6:
                    profImg.setImageResource(R.drawable.prof_child6);
                    break;
                case 7:
                    profImg.setImageResource(R.drawable.prof_child7);
                    break;
                case 8:
                    profImg.setImageResource(R.drawable.prof_child8);
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