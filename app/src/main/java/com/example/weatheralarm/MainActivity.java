package com.example.weatheralarm;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.weatheralarm.databinding.ActivityMainBinding;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    Button alarmButton;
    private TimePicker timePicker;
    Calendar alarmCalendar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        TedPermission.create().setPermissionListener(permissionListener)
                .setDeniedMessage("Permissions are Denied")
                .setPermissions(Manifest.permission.WAKE_LOCK, Manifest.permission.DISABLE_KEYGUARD, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.INTERNET)
                .check();

        timePicker = binding.timePicker;

        String previousSet = PreferenceManager.getString(this, "Time");
        if(previousSet != "") {
            binding.settedAlarmTimeTextView.setText("설정된 알람: " + previousSet);
        } else {
            binding.settedAlarmTimeTextView.setText("이전에 설정된 알람이 없습니다.");
        }

        String defaultVideoID = PreferenceManager.getString(this, WeatherKey.Default);
        if ( defaultVideoID.equals("")) {
            PreferenceManager.setString(this, WeatherKey.Default, "https://www.youtube.com/watch?v=KnJuK9TBzlE");
        }
    }

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    public void setAlarm(View view) {
        int hour, hour_24, minute;
        String am_pm;
        if (Build.VERSION.SDK_INT >= 23 ){
            hour_24 = timePicker.getHour();
            minute = timePicker.getMinute();
        }
        else{
            hour_24 = timePicker.getCurrentHour();
            minute = timePicker.getCurrentMinute();
        }
        if(hour_24 > 12) {
            am_pm = "PM";
            hour = hour_24 - 12;
        }
        else
        {
            hour = hour_24;
            am_pm="AM";
        }

        alarmCalendar = Calendar.getInstance();
        alarmCalendar.setTimeInMillis(System.currentTimeMillis());
        alarmCalendar.set(Calendar.HOUR_OF_DAY, hour_24);
        alarmCalendar.set(Calendar.MINUTE, minute);
        alarmCalendar.set(Calendar.SECOND, 0);
        // TimePickerDialog 에서 설정한 시간을 알람 시간으로 설정

        if (alarmCalendar.before(Calendar.getInstance())) alarmCalendar.add(Calendar.DATE, 1);
        // 알람 시간이 현재시간보다 빠를 때 하루 뒤로 맞춤
        Intent alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmIntent.setAction(AlarmReceiver.ACTION_RESTART_SERVICE);
        PendingIntent alarmCallPendingIntent
                = PendingIntent.getBroadcast
                (MainActivity.this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            alarmManager.setExactAndAllowWhileIdle
                    (AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), alarmCallPendingIntent);
        else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            alarmManager.setExact
                    (AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), alarmCallPendingIntent);

        String alarmText = hour_24 + "시 " + minute + "분";
        Toast.makeText(getApplicationContext(), alarmText +"에 알람이 설정되었습니다!", Toast.LENGTH_SHORT).show();
        PreferenceManager.setString(this, "Time", alarmText);
        binding.settedAlarmTimeTextView.setText("설정된 알람: " + alarmText);
    } // 알람 설정

    public void openSetSongPage(View view) {
        Intent intent = new Intent(this, SettingSongActivity.class);
        startActivity(intent);
    }
}