package com.example.weatheralarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.weatheralarm.databinding.ActivitySettingSongBinding;
import android.app.AlertDialog;

public class SettingSongActivity extends AppCompatActivity {

    ActivitySettingSongBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySettingSongBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void defaultAddButtonTapped(View view) {
        makeAlert(WeatherType.Default, 0);
    }
    public void defaultAddButtonTapped2(View view) {
        makeAlert(WeatherType.Default, 1);
    }
    public void defaultAddButtonTapped3(View view) {
        makeAlert(WeatherType.Default, 2);
    }

    public void clearAddButtonTapped(View view) {
        makeAlert(WeatherType.Clear, 0);
    }
    public void clearAddButtonTapped2(View view) {
        makeAlert(WeatherType.Clear, 1);
    }
    public void clearAddButtonTapped3(View view) {
        makeAlert(WeatherType.Clear, 2);
    }

    public void cloudyAddButtonTapped(View view) {
        makeAlert(WeatherType.Cloudy, 0);
    }
    public void cloudyAddButtonTapped2(View view) {
        makeAlert(WeatherType.Cloudy,1);
    }
    public void cloudyAddButtonTapped3(View view) {
        makeAlert(WeatherType.Cloudy,2);
    }

    public void rainyAddButtonTapped(View view) {
        makeAlert(WeatherType.Rainy, 0);
    }
    public void rainyAddButtonTapped2(View view) {
        makeAlert(WeatherType.Rainy, 1);
    }
    public void rainyAddButtonTapped3(View view) {
        makeAlert(WeatherType.Rainy, 2);
    }

    public void snowAddButtonTapped(View view) {
        makeAlert(WeatherType.Snow, 0);
    }
    public void snowAddButtonTapped2(View view) {
        makeAlert(WeatherType.Snow, 1);
    }
    public void snowAddButtonTapped3(View view) {
        makeAlert(WeatherType.Snow, 2);
    }

    public void thunderAddButtonTapped(View view) { makeAlert(WeatherType.Thunder, 0); }
    public void thunderAddButtonTapped2(View view) { makeAlert(WeatherType.Thunder, 1); }
    public void thunderAddButtonTapped3(View view) { makeAlert(WeatherType.Thunder, 2); }

    private void makeAlert(String type, Integer number) {
        final EditText link = new EditText(this);

        AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);

        alert.setTitle("Input Youtube URL");
        alert.setMessage("ex. 'https://youtu.be/uYn7hX-o1zc'");

        alert.setView(makeCustomEditText(link));

        String text = searchDB(type, number);
        if(!text.equals("")) {
            link.setText(text);
        }

        Context context = this;
        alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String urlLink = link.getText().toString();
                Log.d(type + " save", urlLink);
                if (text.equals("") && !urlLink.equals("")) {
                    insertDB(type, urlLink, number);
                } else {
                    updateDB(type, urlLink, number);
                }
            }
        });
        alert.setNegativeButton("no",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alert.show();
    }

    private FrameLayout makeCustomEditText(EditText link) {
        FrameLayout container = new FrameLayout(this);

        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);

        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);

        link.setLayoutParams(params);

        container.addView(link);

        return container;
    }

    public void insertDB(String type, String url, Integer number) {
        VideoURLDBHelper videoURLDBHelper = new VideoURLDBHelper(this);
        SQLiteDatabase sqlDB = videoURLDBHelper.getWritableDatabase();
        sqlDB.execSQL("insert into videoURLTBL values(" + "'" + type + "'" + "," + "'" +url + "'" + "," + "'" + number.toString() +"');");
        sqlDB.close();
    }

    public void updateDB(String type, String url, Integer number) {
        VideoURLDBHelper videoURLDBHelper = new VideoURLDBHelper(this);
        SQLiteDatabase sqlDB = videoURLDBHelper.getWritableDatabase();
        sqlDB.execSQL("update videoURLTBL SET url =" + "'" +url + "'" + " where type =" +  "'" + type + "'" + " and number ="+ "'" + number.toString() +"';");
        sqlDB.close();
    }

    public String searchDB(String type, Integer number) {
        VideoURLDBHelper videoURLDBHelper = new VideoURLDBHelper(this);
        SQLiteDatabase sqlDB = videoURLDBHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("select url from videoURLTBL where type = ? and number = ?;", new String[] {type, number.toString()});

        String url = "";
        while (cursor.moveToNext()) {
            url += cursor.getString(0);
        }
        Log.d("SQL search", url);

        cursor.close();
        sqlDB.close();
        return url;
    }
}