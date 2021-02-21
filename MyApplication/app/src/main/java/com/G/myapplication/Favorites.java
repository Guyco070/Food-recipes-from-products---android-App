package com.G.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Favorites extends AppCompatActivity {
    List<Recipe> recipeList = new ArrayList<>();
    List<List<String>> recipeListFavoritesString= new ArrayList<>();
    List<Recipe> recipeListFavorites = new ArrayList<>();
    MyFireBase MFB = new MyFireBase();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        Intent intent = getIntent();
        recipeList = (ArrayList<Recipe>) intent.getSerializableExtra("recipeList");
        recipeListFavorites = (ArrayList<Recipe>) intent.getSerializableExtra("recipeFavorites");

        getAllFavoritesFirebase();
        /*dataBaseHelper = new DataBaseHelper(getApplicationContext());
        try {
            dataBaseHelper.createDataBase();
            recipeListFavoritesString = dataBaseHelper.getAllRecipesFavorites();
            for(List<String> item: recipeListFavoritesString)
            {
                System.out.println(item.get(0) + "          " + item.get(1));
                recipeListFavorites.add(RecipeFinder(item.get(0),item.get(1)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        add_all_recipelist_preview(recipeListFavorites);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    Recipe RecipeFinder(String recipeChef,String recipeName) {
        for (int i=0;i<recipeList.size();i++)
            if (recipeList.get(i).getRecipeName().equals(recipeName) && recipeList.get(i).getChef().equals(recipeChef))
                return recipeList.get(i);
        return null;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void add_recipe_preview (final Recipe recipe){
        final LinearLayout RPC = findViewById(R.id.favorites_recipe_preview_container);
        View recip_preview = getLayoutInflater().inflate(R.layout.recipe_preview,RPC);
        /*
        RPC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    View viewChild = viewGroup.getChildAt(i);
                    viewChild.setPressed(true);
                }
            }
        });*/



        RelativeLayout R_layout = (RelativeLayout) recip_preview.findViewById(R.id.recipe_preview);

        R_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(Favorites.this, PopupRview.class);
                intent.putExtra("recipe",recipe);
                startActivity(intent);
            }
        });

        final TextView R_name_details = ((TextView) recip_preview.findViewById(R.id.text_view_recipe_name_details));
        TextView R_spices_details = ((TextView) recip_preview.findViewById(R.id.text_view_name_spices_details));
        TextView R_description_details = ((TextView) recip_preview.findViewById(R.id.text_view_name_description_details));
        ImageView R_image = ((ImageView) recip_preview.findViewById(R.id.image_view_food));
        final RadioButton radioButton_favor = ((RadioButton) recip_preview.findViewById(R.id.radioButtonfavor));

        R_name_details.setText(recipe.getRecipeName());
        R_spices_details.setText(recipe.getSpices());
        R_description_details.setText(recipe.getDescription());

        if(recipe.getRecipeImage()!=0)
            R_image.setImageResource(recipe.getRecipeImage());
        else
            R_image.setImageBitmap(recipe.getPbitmap().getBitmap());

        //add underline
        ((TextView) findViewById(R.id.text_view_name_recipe)).setPaintFlags(R_description_details.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        ((TextView) findViewById(R.id.text_view_spices)).setPaintFlags(R_description_details.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        ((TextView) findViewById(R.id.text_view_description)).setPaintFlags(R_description_details.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        //set id
        (findViewById(R.id.text_view_name_recipe)).setId((int)System.currentTimeMillis());
        (findViewById(R.id.text_view_spices)).setId((int)System.currentTimeMillis());
        (findViewById(R.id.text_view_description)).setId((int)System.currentTimeMillis());

        R_layout.setId((int)System.currentTimeMillis());
        radioButton_favor.setId((int)System.currentTimeMillis());
        R_name_details.setId((int)System.currentTimeMillis());
        R_spices_details.setId((int)System.currentTimeMillis());
        R_description_details.setId((int)System.currentTimeMillis());
        R_image.setId((int)System.currentTimeMillis());

        radioButton_favor.setChecked(true);

        //save favorites
        radioButton_favor.setOnClickListener(new RadioGroup.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton_favor.setChecked(false);
                recipeListFavorites.remove(recipe);
                MFB.deleteFavoriteFirebase(recipe);
                ((ViewGroup)v.getParent().getParent().getParent()).removeView(((ViewGroup)v.getParent().getParent()));
            }
        });
    }


    public void add_all_recipelist_preview (List<Recipe> recipeListToPreview){
        for(int i=0;i<recipeListToPreview.size();i++)
            if(recipeListToPreview.get(i)!=null)
                add_recipe_preview(recipeListToPreview.get(i));
    }

    private void getAllFavoritesFirebase(){
        Query q = MFB.myRef.child("Favorites").orderByValue();

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipeListFavorites.clear();

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
                {
                    System.out.println(item.get(0) + "          " + item.get(1));
                    recipeListFavorites.add(RecipeFinder(item.get(0),item.get(1)));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    } //only for this class
}
