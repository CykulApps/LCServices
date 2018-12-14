package com.cykulapps.lcservices.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by LSM on 25-Nov-17.
 */

public class PrefController
{
    public static void savePrefs(String key, String value,Context context){
        SharedPreferences.Editor editor  =context.getSharedPreferences("lcservice", MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static void savePrefs(String key, int value,Context context){
        SharedPreferences.Editor editor  =context.getSharedPreferences("lcservice", MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    //get prefs
    public static  String loadPreferences(String key, String value,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("lcservice", MODE_PRIVATE);
        String data = sharedPreferences.getString(key, value);
        return data;
    }
    public static  int loadPreferences(String key, int value,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("lcservice", MODE_PRIVATE);
        int data = sharedPreferences.getInt(key, value);
        return data;
    }
    public static void removePreferance(String key,Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("lcservice", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();


    }

}
