package com.example.weatheralarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VideoURLDBHelper extends SQLiteOpenHelper {
    public VideoURLDBHelper(Context context) {
        super(context, "videoURLDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table videoURLTBL (type char(20), url char(50), number char(2), PRIMARY KEY(type, url, number));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists videoURLTBL;");
        onCreate(sqLiteDatabase);
    }
}
