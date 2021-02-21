package com.G.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<FoodItem> foodList_viwer;
    private MyFireBase MFB = new MyFireBase();
    private List<FoodItem>  foodList_viwer_full;
    private List<Recipe> recipeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView titleTextView = findViewById(R.id.title_Text_view);
        titleTextView.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,android.R.anim.slide_in_left));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CardView cardViewPancake = findViewById(R.id.pancake_cardview);
                cardViewPancake.setVisibility(View.VISIBLE);
                cardViewPancake.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,android.R.anim.slide_in_left));
            }
        }, 500);


        fillFoodList();
        fillRecipeList();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Collections.sort(foodList_viwer,MakeList_ACTV.FoodNameComparator);
                foodList_viwer_full = new ArrayList<>(foodList_viwer);
                open_Make_List_ACTV_activity();
            }
        }, 2500);
    }

    public void open_Make_List_activity() {
        Intent intent = new Intent(this, MakeList.class);
        startActivity(intent);
    }

    public void open_Make_List_ACTV_activity() {
        for(FoodItem item:foodList_viwer_full)
            System.out.println(item.getFoodName());

        Intent intent = new Intent(MainActivity.this, MakeList_ACTV.class);
        intent.putExtra("foodList_viwer_full",(ArrayList<FoodItem>) foodList_viwer_full);
        intent.putExtra("recipeList",(ArrayList<Recipe>) recipeList);
        try {
            startActivity(intent);
        }catch (Exception e){
            System.out.println(e);
        }

    }


    private void fillFoodList() {
        foodList_viwer = new ArrayList<>();
        foodList_viwer.add(new FoodItem("בננה", R.drawable.banana, 'P'));
        foodList_viwer.add(new FoodItem("חציל", R.drawable.aggplant, 'P'));
        foodList_viwer.add(new FoodItem("בשר טחון", R.drawable.mince, 'M'));
        foodList_viwer.add(new FoodItem("גבינה צהובה", R.drawable.yellowchees, 'D'));
        foodList_viwer.add(new FoodItem("חסה", R.drawable.lettuce, 'P'));
        foodList_viwer.add(new FoodItem("עגבניה", R.drawable.tomato, 'P'));
        foodList_viwer.add(new FoodItem("ביצה", R.drawable.egg, 'P'));
        foodList_viwer.add(new FoodItem("לחם", R.drawable.bread, 'P'));
        foodList_viwer.add(new FoodItem("פסטה", R.drawable.pasta, 'P'));


        // manual insert of food items to firebase database
        /*MFB.myRef.child("FoodItem").child("יוגורט").child("foodName").setValue("יוגורט");
        MFB.myRef.child("FoodItem").child("יוגורט").child("foodType").setValue("D");

        MFB.myRef.child("FoodItem").child("גבינה לבנה").child("foodName").setValue("גבינה לבנה");
        MFB.myRef.child("FoodItem").child("גבינה לבנה").child("foodType").setValue("D");

        MFB.myRef.child("FoodItem").child("סטייק אנטריקוט").child("foodName").setValue("סטייק אנטריקוט");
        MFB.myRef.child("FoodItem").child("סטייק אנטריקוט").child("foodType").setValue("M");

        MFB.myRef.child("FoodItem").child("פטרוזיליה").child("foodName").setValue("פטרוזיליה");
        MFB.myRef.child("FoodItem").child("פטרוזיליה").child("foodType").setValue("P");


        MFB.myRef.child("FoodItem").child("עוף").child("foodName").setValue("עוף");
        MFB.myRef.child("FoodItem").child("עוף").child("foodType").setValue("M");

        MFB.myRef.child("FoodItem").child("פירורי לחם").child("foodName").setValue("פירורי לחם");
        MFB.myRef.child("FoodItem").child("פירורי לחם").child("foodType").setValue("P");

        MFB.myRef.child("FoodItem").child("שמנת מתוקה").child("foodName").setValue("שמנת מתוקה");
        MFB.myRef.child("FoodItem").child("שמנת מתוקה").child("foodType").setValue("D");*///add food to firebase


        Query q = MFB.myRef.child("FoodItem").orderByValue();

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String [] lo = {"בננה","חציל","בשר טחון","גבינה צהובה","חסה","עגבניה","ביצה","לחם","פסטה"};//temp

                for (DataSnapshot dstIndex : dataSnapshot.getChildren()) {
                    String food_name = dstIndex.child("foodName").getValue(String.class);
                    System.out.println(food_name);
                    if(dstIndex.child("foodImage").exists() && !(dstIndex.child("foodImage").getValue(String.class)).equals("")) {
                        try {
                            foodList_viwer.add(new FoodItem(dstIndex.child("foodName").getValue(String.class),dstIndex.child("foodImage").getValue(String.class), dstIndex.child("foodType").getValue(String.class).charAt(0)));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        boolean temp = true;
                        for(int i=0;i<9;i++)
                            if(food_name.equals(lo[i])){
                                temp = false;
                                break;
                            }
                            if(temp)
                                foodList_viwer.add(new FoodItem(dstIndex.child("foodName").getValue(String.class), R.drawable.white, dstIndex.child("foodType").getValue(String.class).charAt(0)));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void fillRecipeList(){
        recipeList = new ArrayList<>();
        final List<String>  foodList_recipe = new ArrayList<>();
        final List<String>  foodList_basic_recipe = new ArrayList<>();
        String spices_recipe;
        String PM;
        String description;
        String recipetypePerson;
        String recipetypeKosher;
        String cheff;
        String recipetype;

        foodList_recipe.add("חסה");
        foodList_recipe.add("ביצה");
        foodList_recipe.add("גבינה צהובה");

        foodList_basic_recipe.add("ביצה");

        spices_recipe="פלפל שחור,מלח,פפריקה.";

        PM = "לשקשק את ביצה בצלחת להוסיף כוסברה לשים במחבת";
        description = "ביצה מטוגנת עם עלים ירוקים וגבינה צהובה.";
        recipetypePerson = "V";
        recipetypeKosher = "D";
        cheff = "גיא כהן";
        recipetype = "מנה עיקרית";



        // manual insert of recipe to firebase database - only for example
        /*MFB.myRef.child("Recipes").child("פסטה אלפרדו").child("recipeName").setValue("פסטה אלפרדו");
        MFB.myRef.child("Recipes").child("פסטה אלפרדו").child("spices").setValue("מלח, פלפל לבן.");
        MFB.myRef.child("Recipes").child("פסטה אלפרדו").child("PM").setValue("הכנת הפסטה:\n" +
                "1. מרתיחים סיר מים יחד עם 2 כפות שמן. כשהמים מבעבעים מוסיפים את הפסטה למשך כ - 15-10 דקות. \n" +
                "2. כאשר הפסטה מוכנה מסננים ומקררים את הפסטה עם מים קרים.\n" +
                "\n" +
                "הכנת הרוטב:\n" +
                "1. חותכים בצל דק וקטן , חותכים את הפטריות .\n" +
                "2. שמים את הבצל בתוך סיר יחד עם חמאה ומטגנים עד שמזהיב. מוסיפים את הפטריות ומטגנים ביחד עם עוד מעט חמאה עד שהן מתרככות.\n" +
                "3. מוסיפים את השמנת לבישול וכשהיא מתחילה לרתוח מוסיפים מלח, אבקת מרק פטריות ופלפל לבן לפי הטעם .אם נוצרת סמיכות רבה מדי מוסיפים חלב כדי למנוע את הסמיכות וכדי להגדיל את הכמות. \n" +
                "4. מביאים לרתיחה ומורידים מהגז.\n" +
                "5. מניחים פסטה בצלחת, מעל שמים את הרוטב ומגישים חם.");
        MFB.myRef.child("Recipes").child("פסטה אלפרדו").child("description").setValue("פסטה ברוטב אלפרדו עם שמנת ופטריות שתוכלו להכין בקלות ובמהירות לארוחת ערב - מנה שתשאיר לכם ולכל הסועדים טעם של עוד");
        MFB.myRef.child("Recipes").child("פסטה אלפרדו").child("recipeTypePerson").setValue("V");
        MFB.myRef.child("Recipes").child("פסטה אלפרדו").child("recipeTypeKosher").setValue("D");
        MFB.myRef.child("Recipes").child("פסטה אלפרדו").child("recipeType").setValue("ארוחה בצלחת");
        MFB.myRef.child("Recipes").child("פסטה אלפרדו").child("cheff").setValue("עורך השף הלבן");


        MFB.myRef.child("Recipes").child("פסטה אלפרדו").child("foodList").child("שמנת לבישול").setValue("");
        MFB.myRef.child("Recipes").child("פסטה אלפרדו").child("foodList").child("פטריות טריות").setValue("");
        MFB.myRef.child("Recipes").child("פסטה אלפרדו").child("foodList").child("אבקת מרק פטריות").setValue("");
        MFB.myRef.child("Recipes").child("פסטה אלפרדו").child("foodList").child("חלב").setValue("");
        MFB.myRef.child("Recipes").child("פסטה אלפרדו").child("foodList").child("בצל").setValue("");
        MFB.myRef.child("Recipes").child("פסטה אלפרדו").child("foodList").child("חמאה").setValue("");
        MFB.myRef.child("Recipes").child("פסטה אלפרדו").child("foodList").child("פסטה").setValue("");
        MFB.myRef.child("Recipes").child("פסטה אלפרדו").child("foodList").child("שמן קנולה").setValue("");

        MFB.myRef.child("Recipes").child("פסטה אלפרדו").child("foodListBasic").child("פסטה").setValue("");
        MFB.myRef.child("Recipes").child("פסטה אלפרדו").child("foodListBasic").child("שמנת לבישול").setValue("");
        MFB.myRef.child("Recipes").child("פסטה אלפרדו").child("foodListBasic").child("פטריות טריות").setValue("");
        MFB.myRef.child("Recipes").child("פסטה אלפרדו").child("foodListBasic").child("חלב").setValue("");*/


        Query q = MFB.myRef.child("Recipes").orderByValue();

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dstIndex: dataSnapshot.getChildren()){
                        String spices_recipe = dstIndex.child("spices").getValue(String.class);
                        String PM = dstIndex.child("PM").getValue(String.class);
                        String description = dstIndex.child("description").getValue(String.class);
                        String recipetypePerson = dstIndex.child("recipeTypePerson").getValue(String.class);
                        String recipetypeKosher = dstIndex.child("recipeTypeKosher").getValue(String.class);
                        String recipetype = dstIndex.child("recipeType").getValue(String.class);
                        String cheff = dstIndex.child("cheff").getValue(String.class);
                        String recipeImage = dstIndex.child("recipeImage").getValue(String.class);
                    try {
                        recipeList.add(new Recipe(dstIndex.child("recipeName").getValue(String.class),R.drawable.white/*recipeImage*/,getAllFoodList(dstIndex),getAllFoodListBasic(dstIndex),spices_recipe,PM,description,recipetypePerson,recipetypeKosher,recipetype,cheff,5,150));
                        System.out.println(recipeImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        recipeList.add(new Recipe("חביתה",R.drawable.omelet,foodList_recipe,foodList_basic_recipe,spices_recipe,PM,description,recipetypePerson,recipetypeKosher,recipetype,cheff,5,150));

        List<String> foodList_recipe2 = new ArrayList<>();
        List<String>  foodList_basic_recipe2 = new ArrayList<>();

        foodList_recipe2.add("חסה");
        foodList_recipe2.add("בשר טחון");
        foodList_recipe2.add("לחם");
        foodList_recipe2.add("עגבניה");

        foodList_basic_recipe2.add("בשר טחון");
        foodList_basic_recipe2.add("לחם");


        spices_recipe = "פלפל שחור, מלח.";

        PM = "להכין קציצה מההמבורגר...";
        description = "המבורגר ביתי בלחמניה עם חסה ועגבניה.";
        recipetypePerson = "E";
        recipetypeKosher = "M";
        cheff = "חיים כהן";
        recipetype = "סנדוויץ'";


        // manual insert of recipes (no data base)
        recipeList.add(new Recipe("המבורגר ביתי",R.drawable.amburger,foodList_recipe2,foodList_basic_recipe2,spices_recipe,PM,description,recipetypePerson,recipetypeKosher,recipetype,cheff,80,650));
        recipeList.add(new Recipe("המבורגר",R.drawable.amburger,foodList_recipe2,foodList_basic_recipe2,spices_recipe,PM,description,recipetypePerson,recipetypeKosher,recipetype,cheff,80,650));
        recipeList.add(new Recipe("המבורגר ביתי",R.drawable.amburger,foodList_recipe2,foodList_basic_recipe2,spices_recipe,PM,description,recipetypePerson,recipetypeKosher,recipetype,cheff,80,650));
        recipeList.add(new Recipe("המבורגר ביתי",R.drawable.amburger,foodList_recipe2,foodList_basic_recipe2,spices_recipe,PM,description,recipetypePerson,recipetypeKosher,recipetype,cheff,80,650));
        recipeList.add(new Recipe("המבורגר ביתי",R.drawable.amburger,foodList_recipe2,foodList_basic_recipe2,spices_recipe,PM,description,recipetypePerson,recipetypeKosher,recipetype,cheff,80,650));
        recipeList.add(new Recipe("המבורגר ביתי",R.drawable.amburger,foodList_recipe2,foodList_basic_recipe2,spices_recipe,PM,description,recipetypePerson,recipetypeKosher,recipetype,cheff,80,650));
        recipeList.add(new Recipe("המבורגר ביתי",R.drawable.amburger,foodList_recipe2,foodList_basic_recipe2,spices_recipe,PM,description,recipetypePerson,recipetypeKosher,recipetype,cheff,80,650));
        recipeList.add(new Recipe("המבורגר ביתי",R.drawable.amburger,foodList_recipe2,foodList_basic_recipe2,spices_recipe,PM,description,recipetypePerson,recipetypeKosher,recipetype,cheff,80,650));
        recipeList.add(new Recipe("המבורגר ביתי",R.drawable.amburger,foodList_recipe2,foodList_basic_recipe2,spices_recipe,PM,description,recipetypePerson,recipetypeKosher,recipetype,cheff,80,650));
        recipeList.add(new Recipe("המבורגר ביתי",R.drawable.amburger,foodList_recipe2,foodList_basic_recipe2,spices_recipe,PM,description,recipetypePerson,recipetypeKosher,recipetype,cheff,80,650));
        recipeList.add(new Recipe("המבורגר ביתי",R.drawable.amburger,foodList_recipe2,foodList_basic_recipe2,spices_recipe,PM,description,recipetypePerson,recipetypeKosher,recipetype,cheff,80,650));
        recipeList.add(new Recipe("המבורגר ביתי",R.drawable.amburger,foodList_recipe2,foodList_basic_recipe2,spices_recipe,PM,description,recipetypePerson,recipetypeKosher,recipetype,cheff,80,650));
        recipeList.add(new Recipe("המבורגר ביתי",R.drawable.amburger,foodList_recipe2,foodList_basic_recipe2,spices_recipe,PM,description,recipetypePerson,recipetypeKosher,recipetype,cheff,80,650));
        recipeList.add(new Recipe("המבורגר ביתי",R.drawable.amburger,foodList_recipe2,foodList_basic_recipe2,spices_recipe,PM,description,recipetypePerson,recipetypeKosher,recipetype,cheff,80,650));
        recipeList.add(new Recipe("המבורגר ביתי",R.drawable.amburger,foodList_recipe2,foodList_basic_recipe2,spices_recipe,PM,description,recipetypePerson,recipetypeKosher,recipetype,cheff,80,650));
        recipeList.add(new Recipe("המבורגר ביתי",R.drawable.amburger,foodList_recipe2,foodList_basic_recipe2,spices_recipe,PM,description,recipetypePerson,recipetypeKosher,recipetype,cheff,80,650));
        recipeList.add(new Recipe("המבורגר ביתי",R.drawable.amburger,foodList_recipe2,foodList_basic_recipe2,spices_recipe,PM,description,recipetypePerson,recipetypeKosher,recipetype,cheff,80,650));

        Collections.sort(foodList_viwer,MakeList_ACTV.FoodNameComparator);
        foodList_viwer_full = new ArrayList<>(foodList_viwer);
    }

    private ArrayList<String> getAllFoodList(DataSnapshot dataSnapshot)
    {
        List<String> curFoodListString = new ArrayList<>();
        for(DataSnapshot foodItem: dataSnapshot.child("foodList").getChildren())
            curFoodListString.add(foodItem.getKey());

        return (ArrayList<String>) curFoodListString;
    }

    private ArrayList<String> getAllFoodListBasic(DataSnapshot dataSnapshot)
    {
        List<String> curFoodListString = new ArrayList<>();
        for(DataSnapshot foodItem: dataSnapshot.child("foodListBasic").getChildren())
            curFoodListString.add(foodItem.getKey());

        return (ArrayList<String>) curFoodListString;
    }
}
