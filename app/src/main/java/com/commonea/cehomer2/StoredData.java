package com.commonea.cehomer2;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Set;

/**
 * Created by Goutham on 20-02-2015.
 */
public class StoredData {
    private static String userCredentials = "login_data" ;
    private static String p_Username = "pUsername";
    private static String p_Password = "pPassword";
    String post_username;
    String post_password;
    private SharedPreferences shared_preferences;

    public StoredData() {
    }

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(userCredentials, Context.MODE_PRIVATE);
    }

    public static String getUsername(Context context) {
        Log.d("getUsername", getPrefs(context).getString(p_Username, "default_username"));
        return getPrefs(context).getString(p_Username, "default_username");
    }
    public static String getPassword(Context context) {
        Log.d("getPassword", getPrefs(context).getString(p_Password, "default_password"));
        return getPrefs(context).getString(p_Password, "default_password");
    }
    public static void setUsername(Context context, String input) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(p_Username, input);
        editor.commit();
    }
    public static void setPassword(Context context, String input) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(p_Password, input);
        editor.commit();
    }
}
