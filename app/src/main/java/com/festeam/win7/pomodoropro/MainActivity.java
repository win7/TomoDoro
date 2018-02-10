package com.festeam.win7.pomodoropro;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Choreographer;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Chronometer;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tw_status;
    Chronometer chronometer_pomodoro;
    FloatingActionButton fab_start, fab_reset;

    CountDownTimer timer = null;
    int count_pomodoro = 0;
    int next_time = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tw_status = findViewById(R.id.textView_status);
        tw_status.setText("Waiting...");

        chronometer_pomodoro = (Chronometer) findViewById(R.id.chronometer_pomodoro);
        chronometer_pomodoro.setText(String.format("%02d:%02d", 25, 0));

        fab_start = (FloatingActionButton) findViewById(R.id.fab_start);
        fab_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show(); */
                /*
                * 1 work
                * 2 desc 5
                * 3 work
                * 4 desc 5
                * 5 work
                * 6 desc 5
                * 7 work
                * 8 desc 15
                * */
                count_pomodoro++;

                if (count_pomodoro % 2 == 0){
                    if(count_pomodoro == 8){
                        StartChrometer(15 * 60);
                        count_pomodoro = 0;
                        tw_status.setText("Long Break...");
                    }else{
                        StartChrometer(5 * 60);
                        tw_status.setText("Short Break...");
                    }
                    next_time = 25;
                } else {
                    StartChrometer(25 * 60);
                    tw_status.setText("Working...");
                    if(count_pomodoro == 7){
                        next_time = 15;
                    } else{
                        next_time = 5;
                    }
                }
            }
        });

        fab_reset = (FloatingActionButton) findViewById(R.id.fab_reset);
        fab_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometer_pomodoro.setText(String.format("%02d:%02d", 25, 0));
                tw_status.setText("Waiting...");
                next_time = 25;
                count_pomodoro = 0;
                try {
                    timer.cancel();
                    chronometer_pomodoro.stop();
                }catch (Exception e) {

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("MAIN", "Start");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.e("MAIN", "Restart");
    }

    @Override
    public void onResume() {
        Log.e("MAIN", "Resume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.e("MAIN", "Pause");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.e("MAIN", "Destroy");
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Log.e("MAIN", "BackPressed");
    }

    private void StartChrometer(int second){
        final int[] second_ = {second};
        try {
            timer.cancel();
            chronometer_pomodoro.stop();
        }catch (Exception e) {

        }
        timer = new CountDownTimer(second_[0] * 1000, 1000) {
            long min;
            long seg;
            public void onTick(long millisUntilFinished) {
                min = ((millisUntilFinished / 1000) / 60);
                seg = ((millisUntilFinished / 1000) % 60);
                chronometer_pomodoro.setText(String.format("%02d:%02d", min, seg));
            }
            public void onFinish() {
                chronometer_pomodoro.setText(String.format("%02d:%02d", next_time, 0));
                PlayNotification(1, getApplicationContext());
            }
        };
        timer.start();
    }

    public static void PlayNotification(int option, Context context){
        MediaPlayer player = MediaPlayer.create(context, R.raw.cucu);
        switch (option){
            case 1:
                player = MediaPlayer.create(context, R.raw.beep);
                break;
            case 2:
                player = MediaPlayer.create(context, R.raw.cucu);
                break;
            case 3:
                player = MediaPlayer.create(context, R.raw.cucu);
                break;
        }
        player.start();
    }
}
