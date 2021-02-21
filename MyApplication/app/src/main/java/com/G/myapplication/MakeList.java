package com.G.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MakeList extends AppCompatActivity {

    private ArrayList<FoodItem> foodList;
    private FoodAdapter FAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_list);
        initList();

        Spinner spinnerFood = findViewById(R.id.food_spin);

        FAdapter = new FoodAdapter(this,foodList);
        spinnerFood.setAdapter(FAdapter);

        spinnerFood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FoodItem clickedItem = (FoodItem) parent.getItemAtPosition(position);
                String clickedFoodName = clickedItem.getFoodName();
                Toast.makeText(MakeList.this, clickedFoodName+" נבחר" , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public static Comparator<FoodItem> FoodNameComparator = new Comparator<FoodItem>() {

        public int compare(FoodItem s1, FoodItem s2) {
            String FoodName1 = s1.getFoodName();
            String FoodName2 = s2.getFoodName();

            //ascending order
            return FoodName1.compareTo(FoodName2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};

    private void initList(){
        foodList = new ArrayList<>();
        foodList.add(new FoodItem("בננה",R.drawable.banana,'P'));
        foodList.add(new FoodItem("חציל",R.drawable.aggplant,'P'));
        foodList.add(new FoodItem("בשר טחון",R.drawable.mince,'M'));
        foodList.add(new FoodItem("גבינה צהובה",R.drawable.yellowchees,'D'));
        foodList.add(new FoodItem("חסה",R.drawable.lettuce,'P'));
        Collections.sort(foodList,MakeList_ACTV.FoodNameComparator);
    }
}
