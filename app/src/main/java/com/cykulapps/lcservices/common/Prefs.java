package com.cykulapps.lcservices.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;

import java.util.Map;
import java.util.Set;


public class Prefs {


    public static final String LOGGED_IN = "login";
    public static final String LOGGED_IN_VENDEOR = "login_vendor";
    public static final String COUNTRY = "country";
    /**
     * Initialize the Prefs helper class to keep a reference to the
     * SharedPreference for this application the SharedPreference will use the
     * package name of the application as the Key.
     * @param context
     * the Application context.
     */

    private static SharedPreferences mPrefs;
    private static final String key = "LoginPref";

    public static void initPrefs(Context context) {
        if (mPrefs == null) {
            mPrefs = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        }
    }

    public static void clearallPrefs(Context ctx) {

        	getPreferences().edit().clear().apply();

        // / App specific


        // =========

    }

    /**
     * Returns an instance of the shared preference for this app.
     * @return an Instance of the SharedPreference
     */
    public static SharedPreferences getPreferences() {
        if (mPrefs != null) {
            return mPrefs;
        }
        throw new RuntimeException(
                "Prefs class not correctly instantiated please call Prefs.iniPrefs(context) in the Application class onCreate.");
    }

    /**
     * @return Returns a map containing a list of pairs key/value representing
     * the preferences.
     * @see SharedPreferences#getAll()
     */
    public static Map<String, ?> getAll() {
        return getPreferences().getAll();
    }

    /**
     * @param key The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue. Throws
     * ClassCastException if there is a preference with this name that
     * is not an int.
     * @see SharedPreferences#getInt(String, int)
     */
    public static int getInt(final String key, final int defValue) {
        return getPreferences().getInt(key, defValue);
    }

    /**
     * @param key The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue. Throws
     * ClassCastException if there is a preference with this name that
     * is not a boolean.
     * @see SharedPreferences#getBoolean(String, boolean)
     */
    public static boolean getBoolean(final String key, final boolean defValue) {
        return getPreferences().getBoolean(key, defValue);
    }

    /**
     * @param key The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue. Throws
     * ClassCastException if there is a preference with this name that
     * is not a long.
     * @see SharedPreferences#getLong(String, long)
     */
    public static long getLong(final String key, final long defValue) {
        return getPreferences().getLong(key, defValue);
    }

    /**
     * @param key The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue. Throws
     * ClassCastException if there is a preference with this name that
     * is not a float.
     * @see SharedPreferences#getFloat(String, float)
     */
    public static float getFloat(final String key, final float defValue) {
        return getPreferences().getFloat(key, defValue);
    }

    /**
     * @param key The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue. Throws
     * ClassCastException if there is a preference with this name that
     * is not a String.
     * @see SharedPreferences#getString(String, String)
     */
    public static String getString(final String key, final String defValue) {
        return getPreferences().getString(key, defValue);
    }

    /**
     * @param key The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference values if they exist, or defValues. Throws
     * ClassCastException if there is a preference with this name that
     * is not a Set.
     * @see SharedPreferences#getStringSet(String,
     * Set)
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Set<String> getStringSet(final String key,
                                           final Set<String> defValue) {
        SharedPreferences prefs = getPreferences();
        return prefs.getStringSet(key, defValue);
    }

    /**
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.
     * @see Editor#putLong(String, long)
     */
    public static void putLong(final String key, final long value) {
        final Editor editor = getPreferences().edit();
        editor.putLong(key, value);
        editor.apply();
    }

    /**
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.
     * @see Editor#putInt(String, int)
     */
    public static void putInt(final String key, final int value) {
        final Editor editor = getPreferences().edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.
     * @see Editor#putFloat(String, float)
     */
    public static void putFloat(final String key, final float value) {
        final Editor editor = getPreferences().edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    /**
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.
     * @see Editor#putBoolean(String, boolean)
     */
    public static void putBoolean(final String key, final boolean value) {
        final Editor editor = getPreferences().edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.
     * @see Editor#putString(String, String)
     */
    public static void putString(final String key, final String value) {
        final Editor editor = getPreferences().edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * @param key The name of the preference to modify.
     * @param value The new value for the preference.
     * @see Editor#putStringSet(String,
     * Set)
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void putStringSet(final String key, final Set<String> value) {
        final Editor editor = getPreferences().edit();
        editor.putStringSet(key, value);
        editor.apply();
    }

    /**
     * @param key The name of the preference to remove.
     * @see Editor#remove(String)
     */
    public static void remove(final String key) {
        SharedPreferences prefs = getPreferences();
        final Editor editor = prefs.edit();
        if (prefs.contains(key + "#LENGTH")) {
            // Workaround for pre-HC's lack of StringSets
            int stringSetLength = prefs.getInt(key + "#LENGTH", -1);
            if (stringSetLength >= 0) {
                editor.remove(key + "#LENGTH");
                for (int i = 0; i < stringSetLength; i++) {
                    editor.remove(key + "[" + i + "]");
                }
            }
        }
        editor.remove(key);

        editor.apply();
    }

    /**
     * @param key The name of the preference to check.
     * @see SharedPreferences#contains(String)
     */
    public static boolean contains(final String key) {
        return getPreferences().contains(key);
    }

    /**
     * Clear session details
     */
    public static void logoutUser(Context context) {
        getPreferences().edit().clear().apply();
       /* Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Staring Login Activity
        context.startActivity(i);*/


    }


    /**
     * Clear session details
     */
    public static void clearPref(Context context) {
        getPreferences().edit().clear().apply();
    }
}