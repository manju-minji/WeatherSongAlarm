package com.example.weatheralarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;

import java.util.Calendar;

import com.example.weatheralarm.databinding.ActivityAlarmBinding;

public class AlarmActivity extends AppCompatActivity {
    Calendar calendar;
    SwipeButton swipeButton;
    TextView timeText;
    MediaPlayer mediaPlayer;
    boolean flag = true;
    private PowerManager.WakeLock wl;

    private ActivityAlarmBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("AlarmActivity", "start");

        // 잠금 화면 위로 activity 띄워줌
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        binding = ActivityAlarmBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        calendar = Calendar.getInstance();
        swipeButton = binding.swipeBtn;
        timeText = binding.time;

        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.ouu);   // 소리를 재생할 MediaPlayer
        mediaPlayer.setLooping(true);   // 무한반복
        mediaPlayer.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (flag == true) {
                    try {
                        calendar = Calendar.getInstance();
                        if (calendar.get(Calendar.HOUR_OF_DAY) > 0 && calendar.get(Calendar.HOUR_OF_DAY) < 12) {
                            timeText.setText("AM " + calendar.get(Calendar.HOUR_OF_DAY) + "시 " + calendar.get(Calendar.MINUTE) + "분 " + calendar.get(Calendar.SECOND) + "초");
                        } else if (calendar.get(Calendar.HOUR_OF_DAY) == 12) {
                            timeText.setText("PM " + calendar.get(Calendar.HOUR_OF_DAY) + "시 " + calendar.get(Calendar.MINUTE) + "분 " + calendar.get(Calendar.SECOND) + "초");
                        } else if (calendar.get(Calendar.HOUR_OF_DAY) > 12 && calendar.get(Calendar.HOUR_OF_DAY) < 24) {
                            timeText.setText("PM " + (calendar.get(Calendar.HOUR_OF_DAY) - 12) + "시 " + calendar.get(Calendar.MINUTE) + "분 " + calendar.get(Calendar.SECOND) + "초");
                        } else if (calendar.get(Calendar.HOUR_OF_DAY) == 0) {
                            timeText.setText("AM 0시 " + calendar.get(Calendar.MINUTE) + "분 " + calendar.get(Calendar.SECOND) + "초");
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {}
                }
            }
        }).start(); // 실시간으로 시계 출력

        swipeButton.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                mediaPlayer.stop();
                flag=false;
                finish();
            }
        }); // Swipe Button 밀어서 해제
    }
}