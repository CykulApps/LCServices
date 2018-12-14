package com.cykulapps.lcservices;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class PrefController
{
    final public static String mobileNumber = "mobileNumber";
    final public static String yes = "yes";
    final public static String about = "about";
    public static String riderID="riderID";

    public static void savePrefs(String key, String value,Context context){
        SharedPreferences.Editor editor  =context.getSharedPreferences("cp", MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static void savePrefs(String key, int value,Context context){
        SharedPreferences.Editor editor  =context.getSharedPreferences("cp", MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    //get prefs
    public static  String loadPreferences(String key, String value,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("cp", MODE_PRIVATE);
        String data = sharedPreferences.getString(key, value);
        return data;
    }

    public static  int loadPreferences(String key, int value,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("cp", MODE_PRIVATE);
        int data = sharedPreferences.getInt(key, value);
        return data;
    }

    public static void saveRecordData(String key, String value,Context context){
        SharedPreferences.Editor editor  =context.getSharedPreferences("mapsData", MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String loadRecordData(String key, String value,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("mapsData", MODE_PRIVATE);
        String data = sharedPreferences.getString(key, value);
        return data;
    }

    public static void removePreferance(String key,Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("cp", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();


    }

}

