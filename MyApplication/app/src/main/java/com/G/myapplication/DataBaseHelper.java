package com.G.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static String DATABASE_PATH = "";
    private static String DATABASE_NAME = "recipe.db";
    private SQLiteDatabase mDataBase;
    private Context mContext=null;

    /*private static final String TABLE_NAME = "recipe_table";
    private static final String Recipe_name_col = "Recipe_name";
    private static final String Recipe_image_col = "Recipe_image";
    private static final String Food_list_col = "Food_list";
    private static final String Food_list_basic_col = "Food_list_basic";
    private static final String Spices_col = "Spices";
    private static final String PM_col = "PM";
    private static final String Description_col = "Description";
    private static final String RT_person_col = "RT_persone";
    private static final String RT_kosher_col = "RT_kosher";
    private static final String RT_col = "RT";
    private static final String Chef_col = "Chef";
    private static final String Time_col = "Time";
    private static final String Calories_col = "Calories";*/

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        if(Build.VERSION.SDK_INT >= 17)
            DATABASE_PATH = context.getApplicationInfo().dataDir + "/databases/";
        else
            DATABASE_PATH = "/data/data/"+context.getPackageName() + "/databases/";

        mContext = context;
    }
    @Override
    public synchronized void close(){
        if(mDataBase != null)
            mDataBase.close();
        super.close();
    }

    private boolean checkDataBase(){
        SQLiteDatabase tempDB = null;
        try {
            String path = DATABASE_PATH + DATABASE_NAME;
            tempDB = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(tempDB!=null)
            tempDB.close();
        return tempDB!=null?true:false;
    }

    public void copyDataBase(){
        try{
            InputStream MyInput = mContext.getAssets().open(DATABASE_NAME);
            String outputFileName = DATABASE_PATH + DATABASE_NAME;
            OutputStream MyOutput = new FileOutputStream(outputFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length=MyInput.read(buffer))>0){
                MyOutput.write(buffer,0,length);
            }
            MyOutput.flush();
            MyOutput.close();
            MyInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openDataBase(){
        String path = DATABASE_PATH + DATABASE_NAME;
        mDataBase = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE);
    }

    public void createDataBase(){
        boolean isDBExist = checkDataBase();
        if(isDBExist){
        }else{
            this.getReadableDatabase();
            try{
                copyDataBase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<List<String>> getAllRecipesFavorites(){
        List<List<String>> recipeL_all = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        try{
            System.out.println(db.getPath());
            cursor = db.rawQuery("SELECT * FROM Favorites",null);
            if(cursor == null) return null;

            do{
                List<String> recipe_det = new ArrayList<>();
                recipe_det.add(cursor.getString(cursor.getColumnIndex("Rname")));
                recipe_det.add(cursor.getString(cursor.getColumnIndex("chef")));

                recipeL_all.add(recipe_det);
            }while(cursor.moveToNext());
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();

        return recipeL_all;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("create table " + TABLE_NAME + " (Recipe_name TEXT PRIMARY KEY AUTOINCREMENT,) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
