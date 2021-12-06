package com.example.weatheralarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.weatheralarm.databinding.ActivityMainBinding;
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
        makeAlert(WeatherKey.Default);
    }

    public void defaultAddButtonTapped2(View view) {
        makeAlert(WeatherKey.Default +"2");
    }

    public void clearAddButtonTapped(View view) {
        makeAlert(WeatherKey.Clear);
    }

    public void clearAddButtonTapped2(View view) {
        makeAlert(WeatherKey.Clear +"2");
    }

    public void cloudyAddButtonTapped(View view) {
        makeAlert(WeatherKey.Cloudy);
    }

    public void cloudyAddButtonTapped2(View view) {
        makeAlert(WeatherKey.Cloudy +"2");
    }

    public void rainyAddButtonTapped(View view) {
        makeAlert(WeatherKey.Rainy);
    }

    public void rainyAddButtonTapped2(View view) {
        makeAlert(WeatherKey.Rainy +"2");
    }

    public void snowAddButtonTapped(View view) {
        makeAlert(WeatherKey.Snow);
    }

    public void snowAddButtonTapped2(View view) {
        makeAlert(WeatherKey.Snow +"2");
    }

    public void thunderAddButtonTapped(View view) {
        makeAlert(WeatherKey.Thunder);
    }

    public void thunderAddButtonTapped2(View view) {
        makeAlert(WeatherKey.Thunder +"2");
    }

    private void makeAlert(String key) {
        final EditText link = new EditText(this);

        AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);

        alert.setTitle("Input Youtube URL");
        alert.setMessage("ex. 'https://youtu.be/uYn7hX-o1zc'");

        alert.setView(makeCustomEditText(link));

        String text = PreferenceManager.getString(this, key);
        if(!text.equals("")) {
            link.setText(text);
        }

        Context context = this;
        alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String urlLink = link.getText().toString();
                Log.d(key + " save", urlLink);
                PreferenceManager.setString(context, key, urlLink);
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
}