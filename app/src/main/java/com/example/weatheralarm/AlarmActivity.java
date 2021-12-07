package com.example.weatheralarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import java.util.Random;

import com.example.weatheralarm.databinding.ActivityAlarmBinding;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class AlarmActivity extends YouTubeBaseActivity {
    Calendar calendar;
    SwipeButton swipeButton;
    TextView timeText;
    boolean flag = true;

    private ActivityAlarmBinding binding;

    private static String API_KEY = "AIzaSyDqRAsWwct5ZQprzKX6WVmEMJL9dJTQIxg";

    YouTubePlayer player;

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

        initPlayer();

        swipeButton = binding.swipeBtn;
        timeText = binding.timeTextView;

        setTimeText();

       new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String URL = "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=날씨";

                    Document doc = Jsoup.connect(URL).get();    //URL 웹사이트에 있는 html 코드를 다 끌어오기
                    Elements weatherStatus = doc.select(".temperature_info span:nth-child(2)");
                    Boolean isEmpty = weatherStatus.isEmpty(); //빼온 값 null체크
                    Log.d("Tag", "isNull? : " + isEmpty); //로그캣 출력
                    if (isEmpty == false) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String weather = weatherStatus.get(0).text();
                                weather = "아무것도 아님";
                                binding.weatherTextView.setText("현재 날씨는 " + weather +"입니다.");
                                startVideo(weather);
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
                player.release();
                flag=false;
                finish();
            }
        }); // Swipe Button 밀어서 해제
    }

    public void setTimeText() {
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
    }

    public void initPlayer() {
        binding.youtubePlayerView.initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                player = youTubePlayer;
                player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                    @Override
                    public void onLoading() {

                    }

                    @Override
                    public void onLoaded(String s) {
                        player.play();
                    }

                    @Override
                    public void onAdStarted() {

                    }

                    @Override
                    public void onVideoStarted() {

                    }

                    @Override
                    public void onVideoEnded() {

                    }

                    @Override
                    public void onError(YouTubePlayer.ErrorReason errorReason) {

                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }

    public void startVideo(String weather) {
        if (player != null) {
            if (player.isPlaying()){
                player.pause();
            } else {
                Context context = this;
                final String[] videoURL = {""};

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (weather.contains("맑음")) {
                                videoURL[0] = getVideoURL(WeatherKey.Clear);
                            } else if (weather.contains("흐림") || weather.contains("구름")) {
                                videoURL[0] = getVideoURL(WeatherKey.Cloudy);
                            } else if (weather.contains("눈")) {
                                videoURL[0] = getVideoURL(WeatherKey.Snow);
                            } else if (weather.contains("비") || weather.contains("소나기")) {
                                videoURL[0] = getVideoURL(WeatherKey.Rainy);
                            } else if (weather.contains("번개") || weather.contains("뇌우")) {
                                videoURL[0] = getVideoURL(WeatherKey.Thunder);
                            } else {
                                videoURL[0] = getVideoURL(WeatherKey.Default);
                            }
                            Log.d("videoID", videoURL[0]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();

                try {
                    thread.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String videoID = getVideoID(videoURL[0]);
                if (videoID.equals("")) {
                    videoID = getVideoID(PreferenceManager.getString(context, WeatherKey.Default));
                }
                player.cueVideo(videoID);
            }
        }
    }

    private String getVideoURL(String type) {
        VideoURLDBHelper videoURLDBHelper = new VideoURLDBHelper(this);
        SQLiteDatabase sqlDB = videoURLDBHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("select url from videoURLTBL where type = ?;", new String[] {type});

        Integer urlCount = cursor.getCount();
        String url[] = new String[urlCount];
        int i = 0;

        if (urlCount > 0 ) {
            while (cursor.moveToNext()) {
                url[i] = cursor.getString(0);
                i++;
            }
        }

        Random random = new Random();
        Integer randomIndex = random.nextInt(urlCount);

        Log.d("SQL search", url[randomIndex]);

        cursor.close();
        sqlDB.close();
        return url[randomIndex];
    }

    private String getVideoID(String url) {
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