package com.brandonjenniges.cats;

import android.content.Context;

public class Cat {

    public static final String EXTRA_KEY = "cat_key";

    private String name;
    private String imageName;

    public int getImageResourceId(Context context) {
        return context.getResources().getIdentifier(this.imageName, "drawable", context.getPackageName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
