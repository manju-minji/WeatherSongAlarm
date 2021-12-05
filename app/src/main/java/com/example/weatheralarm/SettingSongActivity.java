package com.example.weatheralarm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.weatheralarm.databinding.ActivityMainBinding;
import com.example.weatheralarm.databinding.ActivitySettingSongBinding;

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

        textViewInit();
    }

    public void textViewInit() {
        binding.defaultURLTextView.setText(PreferenceManager.getString(this, WeatherKey.Default));
        binding.clearURLTextView.setText(PreferenceManager.getString(this, WeatherKey.Clear));
        binding.cloudyURLTextView.setText(PreferenceManager.getString(this, WeatherKey.Cloudy));
        binding.rainyURLTextView.setText(PreferenceManager.getString(this, WeatherKey.Rainy));
        binding.snowURLTextView.setText(PreferenceManager.getString(this, WeatherKey.Snow));
        binding.thunderURLTextView.setText(PreferenceManager.getString(this, WeatherKey.Thunder));
    }

    public void saveButtonTapped(View view) {
        Log.d("Song Setting", "save");

        String defaultVideoURL = binding.defaultURLTextView.getText().toString();
        String clearVideoURL = binding.clearURLTextView.getText().toString();
        String cloudyVideoURL  = binding.cloudyURLTextView.getText().toString();
        String rainyVideoURL = binding.rainyURLTextView.getText().toString();
        String snowVideoURL = binding.snowURLTextView.getText().toString();
        String thunderVideoURL = binding.thunderURLTextView.getText().toString();

        Log.d("VideoURL", defaultVideoURL);

        if ( cloudyVideoURL != PreferenceManager.getString(this, WeatherKey.Default)) {
            PreferenceManager.setString(this, WeatherKey.Default, AlarmUtil.getVideoID(defaultVideoURL));
        }

        if  (clearVideoURL != null || clearVideoURL != "") {
            Log.d("VideoURL", clearVideoURL);
            if ( cloudyVideoURL != PreferenceManager.getString(this, WeatherKey.Clear)) {
                PreferenceManager.setString(this, WeatherKey.Clear, AlarmUtil.getVideoID(clearVideoURL));
            }
        } else {
            PreferenceManager.setString(this, WeatherKey.Clear, "");
        }
        if  (cloudyVideoURL != null || cloudyVideoURL != "") {
            Log.d("VideoURL", cloudyVideoURL);
            if ( cloudyVideoURL != PreferenceManager.getString(this, WeatherKey.Cloudy)) {
                PreferenceManager.setString(this, WeatherKey.Cloudy, AlarmUtil.getVideoID(cloudyVideoURL));
            }
        } else {
            PreferenceManager.setString(this, WeatherKey.Cloudy, "");
        }
        if  (rainyVideoURL != null || rainyVideoURL != "") {
            Log.d("VideoURL", rainyVideoURL);
            if ( cloudyVideoURL != PreferenceManager.getString(this, WeatherKey.Rainy)) {
                PreferenceManager.setString(this, WeatherKey.Rainy, AlarmUtil.getVideoID(rainyVideoURL));
            }
        } else {
            PreferenceManager.setString(this, WeatherKey.Rainy, "");
        }
        if  (snowVideoURL != null || snowVideoURL != "") {
            Log.d("VideoURL", snowVideoURL);
            if ( cloudyVideoURL != PreferenceManager.getString(this, WeatherKey.Snow)) {
                PreferenceManager.setString(this, WeatherKey.Snow, AlarmUtil.getVideoID(snowVideoURL));
            }
        } else {
            PreferenceManager.setString(this, WeatherKey.Snow, "");
        }
        if  (thunderVideoURL != null || thunderVideoURL != "") {
            Log.d("VideoURL", thunderVideoURL);
            if ( cloudyVideoURL != PreferenceManager.getString(this, WeatherKey.Thunder)) {
                PreferenceManager.setString(this, WeatherKey.Thunder, AlarmUtil.getVideoID(thunderVideoURL));
            }
        } else {
            PreferenceManager.setString(this, WeatherKey.Thunder, "");
        }
        Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }
}