package com.example.weeseed_test.dto;

import android.graphics.Color;

public class LabelColor {
    int color;
    String name;


    public LabelColor(int color,String name){
        this.color=color;
        this.name=name;
    }
    public void setColorByCode(String colorCodeStr) {
        this.color = Color.parseColor(colorCodeStr);
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setColor(int color) {
        this.color = color;
    }


    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

}
