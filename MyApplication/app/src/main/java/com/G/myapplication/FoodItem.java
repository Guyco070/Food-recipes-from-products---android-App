package com.G.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

public class FoodItem implements Serializable {
    private String FoodName;
    private int FoodImage = 0;
    private char Foodtype; // M/D/P
    private String FoodimageUrl;
    private ProxyBitmap Pbitmap;

    public FoodItem(String foodname,int foodimage,char foodtype)
    {
        FoodName = foodname;
        FoodImage = foodimage;
        Foodtype = foodtype;
    }

    public FoodItem(String foodname,String foodimageUrl,char foodtype) throws IOException {
        FoodName = foodname;
        Foodtype = foodtype;
        FoodimageUrl = foodimageUrl;

       StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
       StrictMode.setThreadPolicy(policy);

       URL newurl = new URL(foodimageUrl);
       Pbitmap = new ProxyBitmap(BitmapFactory.decodeStream((InputStream) newurl.getContent()));
    }

    public FoodItem(FoodItem otherFoodItem)
    {
        FoodName = otherFoodItem.getFoodName();
        FoodImage = otherFoodItem.getFoodImage();
        Foodtype = otherFoodItem.getFoodtype();
    }

    public String getFoodName() { return FoodName; }

    public int getFoodImage() { return FoodImage; }

    public char getFoodtype() { return Foodtype; }

    //public Bitmap getmIcon_val() { return mIcon_val; }


    public String getFoodimageUrl() { return FoodimageUrl; }

    public ProxyBitmap getPbitmap() { return Pbitmap; }
}
