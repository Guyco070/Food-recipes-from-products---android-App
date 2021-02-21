package com.G.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MakeList_ACTV extends AppCompatActivity {
    private List<FoodItem>  foodList_viwer;
    private List<FoodItem>  foodList_viwer_full;
    private List<FoodItem>  foodList_chosen = new ArrayList<>();
    private List<FoodItem>  foodList_DropDownKosher = new ArrayList<>();
    private List<FoodItem>  foodList_not_chosen = new ArrayList<>();
    private List<Recipe> recipeList;
    private List<Recipe>  recipeList_to_show = new ArrayList<>();
    private List<Recipe> recipeFavorites2 = new ArrayList<>();
    private Menu menu;
    private Switch tempswitch;
    private int missnum_int;
    private MyFireBase MFB = new MyFireBase();

    float x1,x2,y1,y2;

    private DrawerLayout fDrawerlayout;

    @Override
    protected void onResume() {
        MakeList_ACTV.super.onResume();
        getAllFavoritesFirebase();
        Refresh_recips_preview_byMenu();
    }

    /*@Override
    protected void onStart() {
        MakeList_ACTV.super.onStart();
    }*/

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_list__actv);
        Intent intent = getIntent();

        foodList_viwer = new ArrayList<>((ArrayList<FoodItem>) intent.getSerializableExtra("foodList_viwer_full"));
        foodList_viwer_full = new ArrayList<>(foodList_viwer);

        recipeList = new ArrayList<>((ArrayList<Recipe>) intent.getSerializableExtra("recipeList"));

        getAllFavoritesFirebase();

        Toolbar toolbar = findViewById(R.id.drwer_tollbar);
        setSupportActionBar(toolbar);

        fDrawerlayout = findViewById(R.id.activity_MakeList_ACTV_Drawer);

        ActionBarDrawerToggle fToggle = new ActionBarDrawerToggle(this,fDrawerlayout,toolbar,R.string.open,R.string.close);// toolbar batton

        fDrawerlayout.addDrawerListener(fToggle);
        fToggle.syncState();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        add_all_recipelist_preview(recipeList);

        final AutoCompleteTextView editText = findViewById(R.id.actv);

        FoodAdapter_ACTV adapter = new FoodAdapter_ACTV(MakeList_ACTV.this,foodList_viwer);
        editText.setAdapter(adapter);

        ((Button) findViewById(R.id.filter_button)).setOnClickListener(new NavigationView.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MakeList_ACTV.this, Timer.class);
                startActivity(i);
            }
        });

        // Filaters
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);   //displays text of header of nav drawer.
        navigationView.setClickable(true);
        navigationView.setOnClickListener(new NavigationView.OnClickListener(){
            @Override
            public void onClick(View view) {
                getCurrentFocus();
            }
        }); // לנסות לצאת ממקלדת...


        View headerview = navigationView.getHeaderView(0);

        menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.filter_base);
        menuItem.setActionView(R.layout.switch_basic_layout);
        //System.out.println(menuItem.getActionView().getId());
        //View actionView = menuItem.getActionView();
        tempswitch = menuItem.getActionView().findViewById(R.id.switch1);
        ((Switch)menuItem.getActionView().findViewById(R.id.switch1)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Refresh_recips_preview_byMenu();
            }
        });

        ///
        MenuItem menuItem2 = menu.findItem(R.id.filter_miss);
        menuItem2.setActionView(R.layout.switch_miss_layout);
        ((Button) menu.findItem(R.id.filter_miss).getActionView().findViewById(R.id.switch1).findViewById(R.id.down)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrement();
            }
        });

        ((Button) menu.findItem(R.id.filter_miss).getActionView().findViewById(R.id.switch1).findViewById(R.id.up)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increment();
            }
        });


        menu.findItem(R.id.filter_calories).setActionView(R.layout.insertlayout_basic);
        ((EditText) menu.findItem(R.id.filter_calories).getActionView().findViewById(R.id.clories_edit_text)).addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable mEdit)
            {
                Refresh_recips_preview_byMenu();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            public void onTextChanged(CharSequence s, int start, int before, int count){ Refresh_recips_preview_byMenu();}
        });

        menu.findItem(R.id.filter_chef).setActionView(R.layout.edit_text_chef);
        ((EditText) menu.findItem(R.id.filter_chef).getActionView().findViewById(R.id.chef_edit_text)).addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable mEdit)
            {
                Refresh_recips_preview_byMenu();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            public void onTextChanged(CharSequence s, int start, int before, int count){ Refresh_recips_preview_byMenu();}
        });

        menu.findItem(R.id.favorites).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i = new Intent(MakeList_ACTV.this, Favorites.class);
                i.putExtra("recipeList",(ArrayList<Recipe>) recipeList);
                i.putExtra("recipeFavorites",(ArrayList<Recipe>) recipeFavorites2);

                startActivity(i);
                return false;
            }
        });

        menu.findItem(R.id.timer).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i = new Intent(MakeList_ACTV.this, Timer.class);
                startActivity(i);
                return false;
            }
        });
        //Filters - end

        final AutoCompleteTextView actv = findViewById(R.id.actv);

        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() { // add chosen food item

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long id)
            {
                final FoodItem clickedItem = (FoodItem) parent.getItemAtPosition(position);
                ((AutoCompleteTextView) findViewById(R.id.actv)).setText("");
                //((AutoCompleteTextView) findViewById(R.id.actv)).showDropDown();

                foodList_chosen.add(clickedItem);
                foodList_viwer_full.remove(clickedItem);

                if(clickedItem.getFoodtype() == 'D') { // Case of a dairy product
                    for (int i = 0; i < foodList_viwer_full.size(); i++) {
                        if (foodList_viwer_full.get(i).getFoodtype() == 'M') {
                            foodList_DropDownKosher.add(foodList_viwer_full.get(i));
                            foodList_viwer_full.remove(foodList_viwer_full.get(i));
                        }
                    }
                    Toast.makeText(MakeList_ACTV.this, "שים לב!\nבחירת מוצר חלבי מורידה את המוצרים הבשריים מהרשימה !" , Toast.LENGTH_LONG).show();
                }

                if(clickedItem.getFoodtype() == 'M') { // Case of a meat product
                    for (int i = 0; i < foodList_viwer_full.size(); i++) {
                        if (foodList_viwer_full.get(i).getFoodtype() == 'D') {
                            foodList_DropDownKosher.add(foodList_viwer_full.get(i));
                            foodList_viwer_full.remove(foodList_viwer_full.get(i));
                        }
                    }
                    Toast.makeText(MakeList_ACTV.this, "שים לב!\nבחירת מוצר בשרי מורידה את המוצרים החלביים מהרשימה !" , Toast.LENGTH_LONG).show();
                }
                foodList_viwer.clear();
                foodList_viwer.addAll(foodList_viwer_full);

                Refresh_recips_preview_byMenu();

                //add choose_butt
                final FoodAdapter_ACTV adapter = new FoodAdapter_ACTV(MakeList_ACTV.this,foodList_viwer);
                editText.setAdapter(adapter);

                FlexboxLayout placeHolder = findViewById(R.id.choosens_flex_layout);
                View choose_but = getLayoutInflater().inflate(R.layout.choose_but,placeHolder);

                CardView cardViewFood = choose_but.findViewById(R.id.image_view_food_cardview);

                ImageView imageViewFood = choose_but.findViewById(R.id.image_view_food_but);
                TextView textViewName = choose_but.findViewById(R.id.text_view_name_but);

                cardViewFood.setId((int)System.currentTimeMillis());
                textViewName.setId((int)System.currentTimeMillis());
                imageViewFood.setId((int)System.currentTimeMillis());
                cardViewFood.setTag(clickedItem.getFoodImage());
                textViewName.setTag(clickedItem.getFoodtype());


                if(clickedItem != null) {
                    if(clickedItem.getFoodImage()!=0)
                        imageViewFood.setImageResource(clickedItem.getFoodImage());
                    else{
                        imageViewFood.setImageBitmap(clickedItem.getPbitmap().getBitmap());
                    }
                    textViewName.setText(clickedItem.getFoodName());
                }// add choose_butt

                Button ExitBut = findViewById(R.id.remove_chosen_but);
                ExitBut.setId((int)System.currentTimeMillis());
                ExitBut.setOnClickListener(new View.OnClickListener() { //remove food item from choosen
                    @Override
                    public void onClick(View view) {
                        ViewGroup parentView1 = (ViewGroup) view.getParent().getParent();
                        ViewGroup parentView2 = (ViewGroup) view.getParent();

                        TextView foodName = (TextView)parentView2.getChildAt(1);
                        CardView foodImageCardview = (CardView) parentView2.getChildAt(0);
                        char foodType =(char) foodName.getTag();
                        System.out.println((parentView2.getChildAt(0)).getTag()); // print chosen to remove
                        /*if(foodList_chosen.contains(new FoodItem((String) foodName.getText(),(int)foodImageCardview.getTag(),foodType)))
                            System.out.println("contains!!!!!!!!!!!!!!!!!!");
                        else
                            System.out.println("noooooooooooooooottttttttttttttttttttttt  contains!!!!!!!!!!!!!!!!!!");*/
                        foodList_chosen.remove(GetFoodIndexByName(foodList_chosen,(String) foodName.getText()));
                        foodList_viwer_full.add(new FoodItem((String) foodName.getText(),(int)foodImageCardview.getTag(),foodType));
                        foodList_viwer.clear();
                        setFoodList_D_or_M();
                        foodList_viwer.addAll(foodList_viwer_full);

                        final FoodAdapter_ACTV adapter = new FoodAdapter_ACTV(MakeList_ACTV.this,foodList_viwer);
                        editText.setAdapter(adapter);
                        Refresh_recips_preview_byMenu();
                        parentView1.removeView(parentView2);
                    }
                });
            }
        });


        //hide when scrolling
        /*ScrollView RPC = findViewById(R.id.recipe_preview_scrollView);
        RPC.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int y = scrollY - oldScrollY;
                if (y > 40) {
                    //scroll up
                    FlexboxLayout placeHolder = MakeList_ACTV.this.findViewById(R.id.choosens_flex_layout);
                    if(placeHolder.getVisibility() != FlexboxLayout.GONE) {
                        placeHolder.setVisibility(FlexboxLayout.GONE);
                        findViewById(R.id.activity_MakeList_ACTV_RL).setVisibility(RelativeLayout.GONE);
                    }
                } else if (y < -40) {
                    //scroll down
                    FlexboxLayout placeHolder = MakeList_ACTV.this.findViewById(R.id.choosens_flex_layout);
                    if(placeHolder.getVisibility() == FlexboxLayout.GONE) {
                        placeHolder.setVisibility(FlexboxLayout.VISIBLE);
                        findViewById(R.id.activity_MakeList_ACTV_RL).setVisibility(RelativeLayout.VISIBLE);
                    }
                } else {

                }

            }
        });*/
    }

    private void setFoodList_D_or_M(){
        boolean isP = true;
        for(FoodItem FI: foodList_chosen) {
            if (FI.getFoodtype() == 'D') {
                for (int i = 0; i < foodList_viwer_full.size(); i++) {
                    if (foodList_viwer_full.get(i).getFoodtype() == 'M') {
                        foodList_DropDownKosher.add(foodList_viwer_full.get(i));
                        foodList_viwer_full.remove(foodList_viwer_full.get(i));
                    }
                }
                break;
            } else if (FI.getFoodtype() == 'M') {
                for (int i = 0; i < foodList_viwer_full.size(); i++) {
                    if (foodList_viwer_full.get(i).getFoodtype() == 'D') {
                        foodList_DropDownKosher.add(foodList_viwer_full.get(i));
                        foodList_viwer_full.remove(foodList_viwer_full.get(i));
                    }
                    break;
                }
            }
        }
        if(isP && !foodList_DropDownKosher.isEmpty()){
            foodList_viwer_full.addAll(foodList_DropDownKosher);
            foodList_DropDownKosher.clear();
        }
    }

    private void increment() {
        TextView missnum_text = menu.findItem(R.id.filter_miss).getActionView().findViewById(R.id.switch1).findViewById(R.id.missnum_textView);
        missnum_int = Integer.parseInt(missnum_text.getText().toString()) + 1;
        missnum_text.setText(String.valueOf(missnum_int));
        Refresh_recips_preview_byMenu();
    }
    //open Timer
    @Override
    public boolean onTouchEvent(MotionEvent touchEvent){
        switch (touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                if(x1 < x2){
                    Intent i = new Intent(MakeList_ACTV.this, Timer.class);
                    startActivity(i);
                }/*else if(x1 > x2){
                    Intent i = new Intent(MakeList_ACTV.this, Timer.class);
                    startActivity(i);
                }*/
                break;
        }
        return false;
    }

    private void decrement() {
        TextView missnum_text = menu.findItem(R.id.filter_miss).getActionView().findViewById(R.id.switch1).findViewById(R.id.missnum_textView);
        if(Integer.parseInt(missnum_text.getText().toString())!=0) {
            missnum_int = Integer.parseInt(missnum_text.getText().toString()) - 1;
            missnum_text.setText(String.valueOf(missnum_int));
            Refresh_recips_preview_byMenu();
        }
    }

    @Override
    public void onBackPressed(){
        if(fDrawerlayout.isDrawerOpen(GravityCompat.START)){
            fDrawerlayout.closeDrawer(GravityCompat.END);
        }else{
            super.onBackPressed();
        }
    }

    // sort by food name
    public static Comparator<FoodItem> FoodNameComparator = new Comparator<FoodItem>() {

        public int compare(FoodItem s1, FoodItem s2) {
            String FoodName1 = s1.getFoodName();
            String FoodName2 = s2.getFoodName();

            //ascending order
            return FoodName2.compareTo(FoodName1);

        }};


    public void add_choose_but (){
        FlexboxLayout placeHolder = findViewById(R.id.choosens_flex_layout);
        getLayoutInflater().inflate(R.layout.choose_but, placeHolder);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void add_recipe_preview (final Recipe recipe){
        final LinearLayout RPC = findViewById(R.id.recipe_preview_scrollView).findViewById(R.id.recipe_preview_container);
        View recip_preview = getLayoutInflater().inflate(R.layout.recipe_preview,RPC);

        RelativeLayout R_layout = (RelativeLayout) recip_preview.findViewById(R.id.recipe_preview);

        R_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MakeList_ACTV.this, PopupRview.class);
                intent.putExtra("recipe",recipe);
                startActivity(intent);
            }
        });
        /*R_layout.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent touchEvent) {
                switch (touchEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        x1 = touchEvent.getX();
                        y1 = touchEvent.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = touchEvent.getX();
                        y2 = touchEvent.getY();
                        if(x1 < x2){
                            Intent i = new Intent(MakeList_ACTV.this, Timer.class);
                            startActivity(i);
                        }/*else if(x1 > x2){
                    Intent i = new Intent(MakeList_ACTV.this, Timer.class);
                    startActivity(i);
                }*/
               /*         break;
                }
                return false;
            }
        });*/

        R_layout.setId((int)System.currentTimeMillis());
        final TextView R_name_details = ((TextView) recip_preview.findViewById(R.id.text_view_recipe_name_details));
        TextView R_spices_details = ((TextView) recip_preview.findViewById(R.id.text_view_name_spices_details));
        TextView R_description_details = ((TextView) recip_preview.findViewById(R.id.text_view_name_description_details));
        final TextView R_chef_details = ((TextView) recip_preview.findViewById(R.id.text_view_name_chef_details));
        ImageView R_image = ((ImageView) recip_preview.findViewById(R.id.image_view_food));
        final RadioButton radioButton_favor = ((RadioButton) recip_preview.findViewById(R.id.radioButtonfavor));

        R_name_details.setText(recipe.getRecipeName());
        R_spices_details.setText(recipe.getSpices());
        R_description_details.setText(recipe.getDescription());
        R_chef_details.setText(recipe.getChef());
        if(recipe.getRecipeImage()!=0)
            R_image.setImageResource(recipe.getRecipeImage());
        else
            R_image.setImageBitmap(recipe.getPbitmap().getBitmap());
        //add underline
        ((TextView) findViewById(R.id.text_view_name_recipe)).setPaintFlags(R_description_details.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        ((TextView) findViewById(R.id.text_view_spices)).setPaintFlags(R_description_details.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        ((TextView) findViewById(R.id.text_view_description)).setPaintFlags(R_description_details.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        ((TextView) findViewById(R.id.text_view_chef)).setPaintFlags(R_description_details.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        //set id
        (findViewById(R.id.text_view_name_recipe)).setId((int)System.currentTimeMillis());
        (findViewById(R.id.text_view_spices)).setId((int)System.currentTimeMillis());
        (findViewById(R.id.text_view_description)).setId((int)System.currentTimeMillis());
        (findViewById(R.id.text_view_chef)).setId((int)System.currentTimeMillis());


        radioButton_favor.setId((int)System.currentTimeMillis());
        R_name_details.setId((int)System.currentTimeMillis());
        R_spices_details.setId((int)System.currentTimeMillis());
        R_description_details.setId((int)System.currentTimeMillis());
        R_chef_details.setId((int)System.currentTimeMillis());
        R_image.setId((int)System.currentTimeMillis());

        if(recipeFavorites2.contains(recipe))
            radioButton_favor.setChecked(true);

        //save favorites
        radioButton_favor.setOnClickListener(new RadioGroup.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> newFavorite = new ArrayList<>();
                if(!recipeFavorites2.contains(recipe))
                {
                    radioButton_favor.setChecked(true);
                    recipeFavorites2.add(recipe);
                }
                else {
                    radioButton_favor.setChecked(false);
                    recipeFavorites2.remove(recipe);
                    MFB.deleteFavoriteFirebase(recipe);
                }
                MFB.updateFavoritesDatabase(recipeFavorites2);
            }
        });
    }

    public void add_all_recipelist_preview (List<Recipe> recipeListToPreview){
        for(int i=0;i<recipeListToPreview.size();i++)
            add_recipe_preview(recipeListToPreview.get(i));
    }

    public List<Recipe> getMatchRecipeList()
    {
        List<FoodItem> foodList_not_choosen = new ArrayList<>();
        foodList_not_choosen = foodList_viwer_full;
        foodList_not_choosen.removeAll(foodList_chosen);
        for (int i = 0; i < recipeList.size(); i++) {
            int flag = 0;
            for (int j = 0; j < foodList_not_choosen.size(); j++) {
                if (recipeList.get(i).getFoodList().contains(foodList_not_choosen.get(j).getFoodName())) {
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                recipeList_to_show.add(recipeList.get(i));
            }
        }
        return recipeList_to_show;
    }

    public List<Recipe> getMatchRecipeListBasic()
    {
        List<Recipe> recipeList_choosen = new ArrayList<>();

        if(recipeList_to_show.isEmpty())
            for (int i = 0; i < recipeList.size(); i++) {
                int flag = 0;
                for (int j = 0; j < foodList_not_chosen.size(); j++) {
                        if (recipeList.get(i).getFoodList_basic().contains(foodList_not_chosen.get(j).getFoodName())) {
                            flag = 1;
                            break;
                        }
                }
                if (flag == 0) {
                    recipeList_choosen.add(recipeList.get(i));
                }
            }
        else{
            for (int i = 0; i < recipeList_to_show.size(); i++) {
                int flag = 0;
                for (int j = 0; j < foodList_not_chosen.size(); j++) {
                    if (recipeList_to_show.get(i).getFoodList_basic().contains(foodList_not_chosen.get(j).getFoodName())) {
                        flag = 1;
                        break;
                    }
                }
                if (flag == 0) {
                    recipeList_choosen.add(recipeList_to_show.get(i));
                }
            }
        }
        recipeList_to_show = recipeList_choosen;
        return recipeList_choosen;
    }

    public List<Recipe> getMatchRecipeListMiss(int missnum)
    {
        String ed_text = ((EditText) menu.findItem(R.id.filter_calories).getActionView().findViewById(R.id.clories_edit_text)).getText().toString().trim();
        if(recipeList_to_show.isEmpty() && (ed_text.isEmpty() || ed_text.length() == 0 || ed_text.equals("") || ed_text == null))
            recipeList_to_show = getMatchRecipeListBasic();
        else {
            List<Recipe> recipeList_to_remove = new ArrayList<>(recipeList);
            recipeList_to_remove.removeAll(getMatchRecipeListBasic());
            recipeList_to_show.removeAll(recipeList_to_remove);
        }
        List<Recipe> recipeList_choosen = new ArrayList<>();

        for (int i = 0; i < recipeList_to_show.size(); i++) {
            int count = 0;
            for (int j = 0; j < foodList_not_chosen.size(); j++) {
                if (recipeList_to_show.get(i).getFoodList().contains(foodList_not_chosen.get(j).getFoodName())) {
                    count++;
                    if(count > missnum)
                        break;
                }
            }
            if (count <= missnum) {
                recipeList_choosen.add(recipeList_to_show.get(i));
            }
        }
        recipeList_to_show = recipeList_choosen;
        return recipeList_choosen;
    }

    public List<Recipe> getMatchRecipeListCalories()
    {
        List<Recipe> recipeList_to_show_temp = new ArrayList<>();
        if (!recipeList_to_show.isEmpty())
            recipeList_to_show_temp = new ArrayList<>(recipeList_to_show);
        else
            recipeList_to_show_temp = new ArrayList<>(recipeList);
        List<Recipe> recipeList_choosen = new ArrayList<>();

        for (int i = 0; i < recipeList_to_show_temp.size(); i++)
            if (recipeList_to_show_temp.get(i).getCalories() <= Float.parseFloat(((EditText) menu.findItem(R.id.filter_calories).getActionView().findViewById(R.id.clories_edit_text)).getText().toString()))
                recipeList_choosen.add(recipeList_to_show_temp.get(i));

        recipeList_to_show = recipeList_choosen;
        return recipeList_choosen;
    }

    public List<Recipe> getMatchRecipeListChef()
    {
        List<Recipe> recipeList_to_show_temp = new ArrayList<>();
        String ed_text = ((EditText) menu.findItem(R.id.filter_calories).getActionView().findViewById(R.id.clories_edit_text)).getText().toString().trim(); //אם ביקבלבשךםרןקד ןכ קצפאט

        if (!recipeList_to_show.isEmpty() || !(ed_text.isEmpty() || ed_text.length() == 0 || ed_text.equals("") || ed_text == null))
            recipeList_to_show_temp = new ArrayList<>(recipeList_to_show);
        else
            recipeList_to_show_temp = new ArrayList<>(recipeList);
        List<Recipe> recipeList_choosen = new ArrayList<>();

        ed_text = ((EditText) menu.findItem(R.id.filter_chef).getActionView().findViewById(R.id.chef_edit_text)).getText().toString();
        for (int i = 0; i < recipeList_to_show_temp.size(); i++)
            if(recipeList_to_show_temp.get(i).getChef().length() >= ed_text.length())
                if (recipeList_to_show_temp.get(i).getChef().substring(0,ed_text.length()).equals(ed_text))
                    recipeList_choosen.add(recipeList_to_show_temp.get(i));

        recipeList_to_show = recipeList_choosen;
        return recipeList_choosen;
    }

    public void Refresh_recips_preview_byMenu()
    {
        int flag = 0;
        recipeList_to_show.clear();
        LinearLayout RPC = findViewById(R.id.recipe_preview_scrollView).findViewById(R.id.recipe_preview_container);
        RPC.removeAllViews();
        String ed_text = ((EditText) menu.findItem(R.id.filter_calories).getActionView().findViewById(R.id.clories_edit_text)).getText().toString().trim();
        if(!(ed_text.isEmpty() || ed_text.length() == 0 || ed_text.equals("") || ed_text == null)) {
            flag=1;
            getMatchRecipeListCalories();
        }

        ed_text = ((EditText) menu.findItem(R.id.filter_chef).getActionView().findViewById(R.id.chef_edit_text)).getText().toString().trim();
        if(!(ed_text.isEmpty() || ed_text.length() == 0 || ed_text.equals("") || ed_text == null)) {
            flag=1;
            getMatchRecipeListChef();
        }

        if(!foodList_chosen.isEmpty()) {
            flag=1;
            foodList_not_chosen = foodList_viwer_full;
            foodList_not_chosen.removeAll(foodList_chosen);
            if(Integer.parseInt(((TextView) menu.findItem(R.id.filter_miss).getActionView().findViewById(R.id.switch1).findViewById(R.id.missnum_textView)).getText().toString()) != 0)
            {
                getMatchRecipeListMiss(missnum_int);
            }
            //by base
            else if((((Switch)menu.findItem(R.id.filter_base).getActionView().findViewById(R.id.switch1))).isChecked())
                getMatchRecipeListBasic();

            else
                add_all_recipelist_preview(getMatchRecipeList());
            }
        if(flag==0)
            add_all_recipelist_preview(recipeList);
        else
            add_all_recipelist_preview(recipeList_to_show);
    }


    public int GetFoodIndexByName(List<FoodItem> foodlist_toSearch,String foodname){
        for(int i=0;i<foodlist_toSearch.size();i++)
            if(foodlist_toSearch.get(i).getFoodName() == foodname)
                return i;
        return -1;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void Refresh_recips_preview()
    {
        LinearLayout RPC = findViewById(R.id.recipe_preview_scrollView).findViewById(R.id.recipe_preview_container);
        RPC.removeAllViews();
        if(!foodList_chosen.isEmpty()) {
            if((tempswitch).isChecked())
                add_all_recipelist_preview(getMatchRecipeListBasic());
            else
                add_all_recipelist_preview(getMatchRecipeList());
        }else
            add_all_recipelist_preview(recipeList);
    }


    Recipe RecipeFinder(String recipeChef,String recipeName) {
        for (int i=0;i<recipeList.size();i++)
            if (recipeList.get(i).getRecipeName().equals(recipeName) && recipeList.get(i).getChef().equals(recipeChef))
                return recipeList.get(i);
        return null;
    }

    private FoodItem FoodFinder(String foodname) {
        for (int i=0;i<foodList_viwer_full.size();i++) {
            if (foodList_viwer_full.get(i).getFoodName().equals(foodname)) {
                return foodList_viwer_full.get(i);
            }
        }
        return null;
    }

    private List<FoodItem> MultiFoodFinder(ArrayList<String> food_name_list){
        List<FoodItem> asked_list = new ArrayList<>();
        for(int i=0;i<food_name_list.size();i++)
        {
            asked_list.add(FoodFinder(food_name_list.get(i)));
        }
        return asked_list;
    }

    private void getAllFavoritesFirebase(){
        Query q = MFB.myRef.child("Favorites").orderByValue();

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipeFavorites2.clear();
                List<List<String>> recipeListFavoritesString = new ArrayList<>();

                for(DataSnapshot dstChef: dataSnapshot.getChildren())
                {
                    for(DataSnapshot dstRecipe: dstChef.getChildren()) {
                        List<String> recipeNameAndChef = new ArrayList<>(); //get(0) = chef name, get(1) = recipe name
                        recipeNameAndChef.add(dstChef.getKey()); //chef name
                        recipeNameAndChef.add(dstRecipe.getKey()); //recipe name
                        recipeListFavoritesString.add(recipeNameAndChef);
                    }
                }

                for(List<String> item: recipeListFavoritesString)
                    recipeFavorites2.add(RecipeFinder(item.get(0),item.get(1)));

                Refresh_recips_preview_byMenu();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
