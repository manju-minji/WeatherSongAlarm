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

    public void saveButtonTapped(View view) {
        Log.d("Song Setting", "save");
        Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }
}