package com.G.myapplication;

import android.graphics.BitmapFactory;
import android.os.StrictMode;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Serializable {

    private String RecipeName;
    private List<String> FoodList = new ArrayList<>();
    private List<String> FoodList_basic = new ArrayList<>();
    private String Spices;
    private String Preparation_Method;
    private String Description;
    private String RecipeTypePerson;// V/T/E
    private String RecipeType; // sandwich...
    private String RecipeTypeKosher;// D/M/P
    private int RecipeImage = 0;
    private String Chef;
    private int Time_at_minutes;
    private float Calories = 0;

    private String RecipeimageUrl;
    private ProxyBitmap Pbitmap;
    //private String FoodList_String;

    public  Recipe(){};//def ctor

    public Recipe(String recipename,int recipeimage,List<String> foodlist,List<String> foodList_basic,String spices,String PM,String description, String recipeTypePerson,String recipeTypeKosher,String recipeType,String chef,int time_at_minutes,float calories)
    {
        RecipeName = recipename;
        Spices = spices;
        Preparation_Method = PM;
        RecipeTypePerson = recipeTypePerson;
        RecipeTypeKosher = recipeTypeKosher;
        FoodList = foodlist;
        FoodList_basic = foodList_basic;
        Description = description;
        RecipeImage = recipeimage;
        Chef = chef;
        Time_at_minutes = time_at_minutes;
        RecipeType = recipeType;
        Calories= calories;
    }

    public Recipe(String recipename,String recipeimageUrl,List<String> foodlist,List<String> foodList_basic,String spices,String PM,String description, String recipeTypePerson,String recipeTypeKosher,String recipeType,String chef,int time_at_minutes,float calories) throws IOException {
        RecipeName = recipename;
        Spices = spices;
        Preparation_Method = PM;
        RecipeTypePerson = recipeTypePerson;
        RecipeTypeKosher = recipeTypeKosher;
        FoodList = foodlist;
        FoodList_basic = foodList_basic;
        Description = description;
        RecipeimageUrl = recipeimageUrl;
        Chef = chef;
        Time_at_minutes = time_at_minutes;
        RecipeType = recipeType;
        Calories= calories;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        URL newurl = new URL(RecipeimageUrl);
        Pbitmap = new ProxyBitmap(BitmapFactory.decodeStream((InputStream) newurl.getContent()));
    }

    public Recipe(Recipe other)//cctor
    {
        RecipeName = other.RecipeName;
        Spices = other.Spices;
        Preparation_Method = other.Preparation_Method;
        RecipeTypePerson = other.RecipeTypePerson;
        RecipeTypeKosher = other.RecipeTypeKosher;
        FoodList = other.FoodList;
        FoodList_basic = other.FoodList_basic;
        Description = other.Description;
        RecipeImage = other.RecipeImage;
        Chef = other.Chef;
        Time_at_minutes = other.Time_at_minutes;
        RecipeType = other.RecipeType;
        Calories= other.Calories;
        Pbitmap = other.Pbitmap;
        RecipeimageUrl = other.RecipeimageUrl;
    }

    public String getRecipeName() { return RecipeName; }

    public List<String> getFoodList() { return FoodList; }

    public String getRecipeTypePerson() { return RecipeTypePerson; }

    public String getRecipeType() { return RecipeType; }

    public String getSpices() { return Spices; }

    public String getDescription() { return Description; }

    public String getPreparation_Method() { return Preparation_Method; }

    public int getRecipeImage() { return RecipeImage; }

    public List<String> getFoodList_basic() { return FoodList_basic; }

    public String getChef() { return Chef; }

    public String getRecipeTypeKosher() { return RecipeTypeKosher; }

    public int getTime_at_minutes() { return Time_at_minutes; }

    public float getCalories() { return Calories; }

    public String getRecipeimageUrl() { return RecipeimageUrl; }

    public ProxyBitmap getPbitmap() { return Pbitmap; }
}
