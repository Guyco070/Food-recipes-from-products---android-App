package com.G.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FoodAdapter_ACTV extends ArrayAdapter<FoodItem> {

    private List<FoodItem> foodListFull;

    public FoodAdapter_ACTV(@NonNull Context context, @NonNull List<FoodItem> foodList) {
        super(context, 0, foodList);
        foodListFull = new ArrayList<>(foodList);
    }

    public Filter getFilter() {
        return foodFilter;
    }
    @NonNull
    @Override
    public View getView(int position,@Nullable View convertView,@Nullable ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.food_autocomplete_row,parent,false
            );
        }

        TextView textViewName = convertView.findViewById(R.id.text_view_name);

        ImageView imageViewFood = convertView.findViewById(R.id.image_view_food);

        FoodItem foodItem = getItem(position);

        if(foodItem != null) {
            if(foodItem.getFoodImage() != 0)
                imageViewFood.setImageResource(foodItem.getFoodImage());
            else
                imageViewFood.setImageBitmap(foodItem.getPbitmap().getBitmap());

            textViewName.setText(foodItem.getFoodName());
        }

        return convertView;
    }

    private Filter foodFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<FoodItem> suggestions = new ArrayList<>();
            if(constraint == null || constraint.length() == 0) {
                suggestions.addAll(foodListFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (FoodItem item : foodListFull) {
                    //if(item.getFoodName().toLowerCase().contains(filterPattern)) {
                    if(item.getFoodName().length() >= filterPattern.length())
                        if(item.getFoodName().toLowerCase().substring(0,filterPattern.length()).equals(filterPattern)) {
                            suggestions.add(item);
                        }
                }
            }
            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((FoodItem) resultValue).getFoodName();
        }
    };
}
