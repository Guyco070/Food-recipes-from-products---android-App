package com.G.myapplication;

import android.app.Application;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MyFireBase extends Application {
    public FirebaseDatabase database;
    public DatabaseReference myRef;

    MyFireBase(){
        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        //
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public void deleteFavoriteFirebase(Recipe recipe){
        myRef.child("Favorites").child(recipe.getChef()).removeValue();
    }

    public void updateFavoritesDatabase(List<Recipe> recipeFavorites){
        for(int i=0;i<recipeFavorites.size();i++)
        {
            myRef.child("Favorites").child(recipeFavorites.get(i).getChef()).child(recipeFavorites.get(i).getRecipeName()).setValue("");
        }
    }
}
