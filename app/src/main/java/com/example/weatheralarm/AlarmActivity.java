package com.example.weatheralarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;

import java.io.IOException;
import java.util.Calendar;

import com.example.weatheralarm.databinding.ActivityAlarmBinding;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class AlarmActivity extends AppCompatActivity {
    Calendar calendar;
    SwipeButton swipeButton;
    TextView timeText;
    MediaPlayer mediaPlayer;
    boolean flag = true;
    private PowerManager.WakeLock wl;

    Bundle bundle = new Bundle();

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
        timeText = binding.timeTextView;

        calendar = Calendar.getInstance();
        if (calendar.get(Calendar.HOUR_OF_DAY) > 0 && calendar.get(Calendar.HOUR_OF_DAY) < 12) {
            timeText.setText("AM " + calendar.get(Calendar.HOUR_OF_DAY) + "시 " + calendar.get(Calendar.MINUTE) + "분 ");
        } else if (calendar.get(Calendar.HOUR_OF_DAY) == 12) {
            timeText.setText("PM " + calendar.get(Calendar.HOUR_OF_DAY) + "시 " + calendar.get(Calendar.MINUTE) + "분 ");
        } else if (calendar.get(Calendar.HOUR_OF_DAY) > 12 && calendar.get(Calendar.HOUR_OF_DAY) < 24) {
            timeText.setText("PM " + (calendar.get(Calendar.HOUR_OF_DAY) - 12) + "시 " + calendar.get(Calendar.MINUTE) + "분 ");
        } else if (calendar.get(Calendar.HOUR_OF_DAY) == 0) {
            timeText.setText("AM 0시 " + calendar.get(Calendar.MINUTE) + "분");
        }
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.ouu);   // 소리를 재생할 MediaPlayer
        mediaPlayer.setLooping(true);   // 무한반복
        mediaPlayer.start();

       /* new Thread(new Runnable() {
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
        }).start(); // 실시간으로 시계 출력*/

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String URL = "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=%EC%84%9C%EC%9A%B8%EB%82%A0%EC%94%A8";

                    Document doc = Jsoup.connect(URL).get();    //URL 웹사이트에 있는 html 코드를 다 끌어오기
                    System.out.println(doc);
                    Elements weatherStatus = doc.select(".temperature_info span:nth-child(2)");
                    Boolean isEmpty = weatherStatus.isEmpty(); //빼온 값 null체크
                    Log.d("Tag", "isNull? : " + isEmpty); //로그캣 출력
                    if (isEmpty == false) { //null값이 아니면 크롤링 실행
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String weather = weatherStatus.get(0).text();
                                binding.weatherTextView.setText(weather);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

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