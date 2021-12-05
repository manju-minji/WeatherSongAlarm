package com.example.weatheralarm;

import android.content.Context;

import android.content.SharedPreferences;

public class PreferenceManager {

    public static final String PREFERENCES_NAME = "com.example.weatheralarm";

    private static final String DEFAULT_VALUE_STRING = "";

    private static SharedPreferences getPreferences(Context context) {

        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

    }

    public static void setString(Context context, String key, String value) {

        SharedPreferences prefs = getPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(key, value);

        editor.commit();
    }

    public static String getString(Context context, String key) {

        SharedPreferences prefs = getPreferences(context);

        String value = prefs.getString(key, DEFAULT_VALUE_STRING);

        return value;
    }

    /**

     * 키 값 삭제

     * @param context

     * @param key

     */

    public static void removeKey(Context context, String key) {

        SharedPreferences prefs = getPreferences(context);

        SharedPreferences.Editor edit = prefs.edit();

        edit.remove(key);

        edit.commit();
    }

    /**

     * 모든 저장 데이터 삭제

     * @param context

     */

    public static void clear(Context context) {

        SharedPreferences prefs = getPreferences(context);

        SharedPreferences.Editor edit = prefs.edit();

        edit.clear();

        edit.commit();
    }
}
