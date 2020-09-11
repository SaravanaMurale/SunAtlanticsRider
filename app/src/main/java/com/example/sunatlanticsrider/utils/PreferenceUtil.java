package com.example.sunatlanticsrider.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class PreferenceUtil {

    public static final String SHARED_PREF_NAME = "sunatlantic";

    public static final String AUTH_TOKEN = "auth_token";
    public static final String BEARER = "bearer";

    public static final String USER_ID = "user_id";
    public static final String NO_VALUE = "null";

    public static final String POINT_A_LAT = "point_a_lat";
    public static final String POINT_A_LONG = "point_a_long";

    public static final String POINT_B_LAT = "point_b_lat";
    public static final String POINT_B_LONG = "point_b_long";

    public static final String TRACKING_NUM1="tracking_num1";
    public static final String TRACKING_NUM2="tracking_num2";

    public static final String STATUS_ACCEPT1="status_acept1";
    public static final String STATUS_ACCEPT2="status_acept2";

    public static void setValueString(Context context, String key, String value) {

        if (context == null) return;
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();

    }

    public static String getValueString(Context context, String key) {

        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        return preferences.getString(key, NO_VALUE);

    }


    public static void setValueSInt(Context context, String key, int value) {

        if (context == null) return;
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();

    }

    public static int getValueInt(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        return preferences.getInt(key, -1);

    }

    public static void remove(Context contextRemoveRewardID, String key) {

        SharedPreferences removeRewardID = contextRemoveRewardID.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = removeRewardID.edit();
        editor.remove(key);
        editor.commit();


    }

    public static void clear(Context contextRemoveRewardID) {

        SharedPreferences removeRewardID = contextRemoveRewardID.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = removeRewardID.edit();
        editor.clear();
        editor.commit();


    }

}
