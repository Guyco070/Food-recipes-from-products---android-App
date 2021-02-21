package com.G.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class PopupRview extends Activity {

    void PopupRview(){
        Intent intent = getIntent();
        Recipe recipe = (Recipe) intent.getSerializableExtra("recipe");

        TextView title = (TextView) findViewById(R.id.text_view_name_recipe);

        TextView chef_name_details = (TextView) findViewById(R.id.textView_chef_details);//chef_name
        TextView chef_name_title = (TextView) findViewById(R.id.textView_chef);//chef_name_title

        TextView recipetype_details = (TextView) findViewById(R.id.textView_recipetype_details);//recipetype_details
        TextView recipetype_title = (TextView) findViewById(R.id.textView_recipetype);//recipetype_title
        TextView recipetype_kosher_details = (TextView) findViewById(R.id.textView_recipetype_kosher_details);//recipetype_kosher_details
        TextView recipetype_kosher_title = (TextView) findViewById(R.id.textView_recipetype_kosher);//recipetype_kosher_title
        TextView time_details = (TextView) findViewById(R.id.textView_time_details);//time_details
        TextView time_title = (TextView) findViewById(R.id.textView_time);//time_title
        TextView calories_details = (TextView) findViewById(R.id.textView_calories_details);//calories_details
        TextView calories_title = (TextView) findViewById(R.id.textView_calories);//calories_title

        TextView RecipeTypePerson_details = (TextView) findViewById(R.id.textView_RecipeTypePerson);//RecipeTypePerson_details

        TextView Description_details = (TextView) findViewById(R.id.textView_Description_details);//Description_details
        TextView Description_title = (TextView) findViewById(R.id.textView_Description);//Description_title
        TextView Spices_details = (TextView) findViewById(R.id.textView_Spices_details);//Spices_details
        TextView Spices_title = (TextView) findViewById(R.id.textView_Spices);//Spices_title
        TextView FoodList_details = (TextView) findViewById(R.id.textView_FoodList_details);//FoodList_details
        TextView FoodList_title = (TextView) findViewById(R.id.textView_FoodList);//FoodList_title
        TextView Preparation_Method_details = (TextView) findViewById(R.id.textView_Preparation_Method_details);//Preparation_Method_details
        TextView Preparation_Method_title = (TextView) findViewById(R.id.textView_Preparation_Method);//Preparation_Method_title

        ImageView recipe_image = (ImageView) findViewById(R.id.image_view_recipe);

        //set details
        title.setText(recipe.getRecipeName());
        chef_name_details.setText(recipe.getChef());
        recipetype_details.setText(recipe.getRecipeType());
        time_details.setText(recipe.getTime_at_minutes() + " דקות");
        Description_details.setText(recipe.getDescription() + "\n");
        Spices_details.setText(recipe.getSpices()+ "\n");
        if(recipe.getRecipeImage() != 0)
            recipe_image.setImageResource(recipe.getRecipeImage());
        else
            recipe_image.setImageBitmap(recipe.getPbitmap().getBitmap());
        Preparation_Method_details.setText(recipe.getPreparation_Method() + "\n");

        for(String item:recipe.getFoodList()) {
            if(recipe.getFoodList_basic().contains(item)) {
                String temp_text = (String) FoodList_details.getText().toString();
                FoodList_details.setText(item + "*\n" + temp_text);
            }
            else
                FoodList_details.append(item + "\n");
        }

        if(recipe.getCalories() == 0)
            calories_details.setText("-");
        else
            calories_details.setText(""+((int)recipe.getCalories()));

        if(recipe.getRecipeTypeKosher().equals("M"))
            recipetype_kosher_details.setText("בשרית");
        else if(recipe.getRecipeTypeKosher().equals("P"))
            recipetype_kosher_details.setText("פרווה");
        else if(recipe.getRecipeTypeKosher().equals("D"))
            recipetype_kosher_details.setText("חלבית");

        if(recipe.getRecipeTypePerson().equals("V")){
            RecipeTypePerson_details.setText("מנה צמחונית\n");
            RecipeTypePerson_details.setVisibility(View.VISIBLE);
        }
        else if(recipe.getRecipeTypePerson().equals("T")) {
            RecipeTypePerson_details.setText("מנה טבעונית\n");
            RecipeTypePerson_details.setVisibility(View.VISIBLE);
        }

        //add underline
        title.setPaintFlags(title.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG); //title
        chef_name_title.setPaintFlags(chef_name_title.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        recipetype_title.setPaintFlags(chef_name_title.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        Description_title.setPaintFlags(chef_name_title.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        recipetype_kosher_title.setPaintFlags(chef_name_title.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        time_title.setPaintFlags(chef_name_title.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        calories_title.setPaintFlags(chef_name_title.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        Spices_title.setPaintFlags(chef_name_title.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        FoodList_title.setPaintFlags(chef_name_title.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        Preparation_Method_title.setPaintFlags(chef_name_title.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        //set text color white for all
        TextView[] allText = {title,chef_name_details,chef_name_title,recipetype_details,recipetype_title,recipetype_kosher_details,recipetype_kosher_title,time_details
                ,time_title,calories_details,calories_title,RecipeTypePerson_details,Description_details,Description_title,Spices_details,Spices_title,FoodList_details
                ,FoodList_title,Preparation_Method_details,Preparation_Method_title};
        for(TextView item:allText){
            item.setTextColor(Color.WHITE);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_rview);

        DisplayMetrics dn = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dn);

        int width = dn.widthPixels;
        int height = dn.heightPixels;

        getWindow().setLayout((int) (width*.9),(int) (height*.8));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 20;

        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        PopupRview();
    }

}
