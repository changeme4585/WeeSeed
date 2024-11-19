package com.example.weeseed_test.dto;

import android.graphics.drawable.Drawable;

public class AppInfo {
    private String name;
    private String packageName;
    private Drawable icon;
    boolean isBlocked;

    public AppInfo(String name, String packageName, Drawable icon, boolean isBlocked) {
        this.name = name;
        this.packageName = packageName;
        this.icon = icon;
        this.isBlocked = isBlocked;
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public boolean isBlocked() {
        return isBlocked;
    }


    //////


    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
