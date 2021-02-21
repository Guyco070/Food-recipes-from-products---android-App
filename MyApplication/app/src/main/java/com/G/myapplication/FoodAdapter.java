package com.G.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class FoodAdapter extends ArrayAdapter<FoodItem> {
    public FoodAdapter(Context context, ArrayList<FoodItem> FoodList) {
        super(context,0,FoodList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    private View initView(int position,View convertView,ViewGroup parent){
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.food_spin,parent,false
            );
        }
        ImageView imageViewFood = convertView.findViewById(R.id.image_view_food);
        TextView textViewName = convertView.findViewById(R.id.text_view_name);

        FoodItem currentItem = getItem(position);

        if(currentItem != null) {
            imageViewFood.setImageResource(currentItem.getFoodImage());
            textViewName.setText(currentItem.getFoodName());
        }
        return convertView;
    }
}
