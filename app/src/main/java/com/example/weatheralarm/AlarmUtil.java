package com.example.weatheralarm;

import android.util.Log;

public class AlarmUtil {
    public static String getVideoID(String url) {
        if (url.contains("?v=")) {
            String id = url.substring(url.indexOf("?v=")+3);
            Log.d("ReturnVideoID", id);
            return id;
        } else if (url.contains("youtu.be/")) {
            String id = url.substring(url.indexOf("youtu.be/")+9);
            Log.d("ReturnVideoID", id);
            return id;
        } else {
            return "";
        }
    }
}
