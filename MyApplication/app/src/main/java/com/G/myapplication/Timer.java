package com.G.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

public class Timer extends AppCompatActivity {
    private FloatingActionButton PlayButton;
    private FloatingActionButton PauseButton;
    private FloatingActionButton StopButton;
    private Chronometer Chronometer_timer;
    private EditText Minutes_et;
    private RadioButton hours_choose;
    private RadioButton minutes_choose;
    private RadioButton seconds_choose;
    private RadioGroup radioGroup;
    private int time_changer = 60000;

    private boolean isChronometerRunning = false;

    private Ringtone ringtone;
    private long lastPause;

    float x1,y1,x2,y2;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        TextView exp = new TextView(this);
        exp.setText("על מנת לעבור מסטופר לטיימר הכנס זמן רצוי.");
        exp.setGravity(View.TEXT_ALIGNMENT_CENTER);
        exp.setPadding(20,20,20,20);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            exp.setBackground(getDrawable(R.drawable.choose_but_border));
        }
        Toast toast = new Toast(this);
        toast.setView(exp);
        toast.show();
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        PlayButton = findViewById(R.id.play_but);
        PauseButton = findViewById(R.id.pause_but);
        StopButton = findViewById(R.id.stop_but);
        Chronometer_timer = findViewById(R.id.chronometer);
        Minutes_et = findViewById(R.id.minutes_editText);

        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.hours_choose:
                        Minutes_et.setHint("  שעות  ");
                        time_changer = 3600000;
                        break;
                    case R.id.minutes_choose:
                        Minutes_et.setHint("  דקות  ");
                        time_changer = 60000;
                        break;
                    case R.id.seconds_choose:
                        Minutes_et.setHint("  שניות  ");
                        time_changer = 1000;
                        break;
                }
            }
        });

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Minutes_et.addTextChangedListener(new TextWatcher()
        {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void afterTextChanged(Editable mEdit) {
                if(!isChronometerRunning)
                    if (!(Minutes_et.length() == 0 || Minutes_et.equals("") || Minutes_et == null)) {
                        Chronometer_timer.setCountDown(true);
                        Long cur_time_to_set = Long.parseLong(Minutes_et.getText().toString());
                        if (cur_time_to_set > 0)
                            Chronometer_timer.setBase(SystemClock.elapsedRealtime() + (cur_time_to_set * time_changer));
                        else {
                            Chronometer_timer.setBase(SystemClock.elapsedRealtime());
                            Toast.makeText(Timer.this, "הכנס מספר חיובי", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Chronometer_timer.setBase(SystemClock.elapsedRealtime());
                        Chronometer_timer.setCountDown(false);
                    }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            public void onTextChanged(CharSequence s, int start, int before, int count){  }
        });

        PlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Minutes_et.length() == 0 || Minutes_et.equals("") || Minutes_et == null)
                    if(lastPause != 0){
                        Chronometer_timer.setBase(Chronometer_timer.getBase() + SystemClock.elapsedRealtime() - lastPause);
                    }else{
                        Chronometer_timer.setBase(SystemClock.elapsedRealtime());
                    }
                else {
                    Long cur_time_to_set = Long.parseLong(Minutes_et.getText().toString());
                    if (cur_time_to_set > 0)
                        Chronometer_timer.setBase(SystemClock.elapsedRealtime() + (cur_time_to_set * time_changer));
                }
                Chronometer_timer.start();
                isChronometerRunning = true;
                PlayButton.setEnabled(false);
                PauseButton.setEnabled(true);
            }
        });

        PauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastPause = SystemClock.elapsedRealtime();
                Chronometer_timer.stop();
                isChronometerRunning = false;
                PauseButton.setEnabled(false);
                PlayButton.setEnabled(true);
                if(ringtone.isPlaying())
                    ringtone.stop();
            }
        });

        StopButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Chronometer_timer.stop();
                isChronometerRunning = false;
                if(ringtone.isPlaying())
                    ringtone.stop();
                if(Minutes_et.length() == 0 || Minutes_et.equals("") || Minutes_et == null) {
                    Chronometer_timer.setBase(SystemClock.elapsedRealtime());
                    Chronometer_timer.setCountDown(false);
                }
                else {
                    Long cur_time_to_set = Long.parseLong(Minutes_et.getText().toString());
                    if (cur_time_to_set > 0)
                        Chronometer_timer.setBase(SystemClock.elapsedRealtime() + (cur_time_to_set * time_changer));
                }
                lastPause = 0;
                PlayButton.setEnabled(true);
                PauseButton.setEnabled(false);
            }
        });

        Chronometer_timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                // TODO Auto-generated method stub
                long myElapsedMillis = SystemClock.elapsedRealtime() - Chronometer_timer.getBase();
                if(((SystemClock.elapsedRealtime() - Chronometer_timer.getBase()) % 10) > 0)
                    myElapsedMillis = (SystemClock.elapsedRealtime() - Chronometer_timer.getBase()) - time_changer;

                System.out.println(myElapsedMillis);
                if(myElapsedMillis*-1<=0) {
                    if (Chronometer_timer.isCountDown() && isChronometerRunning) {
                        Chronometer_timer.stop();
                        isChronometerRunning = false;

                        try {
                            ringtone.play();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }}
       );

        /*Stoper
        PlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastPause != 0){
                    Chronometer_timer.setBase(Chronometer_timer.getBase() + SystemClock.elapsedRealtime() - lastPause);
                }else{
                    Chronometer_timer.setBase(SystemClock.elapsedRealtime());
                }

                Chronometer_timer.start();
                PlayButton.setEnabled(false);
                PauseButton.setEnabled(true);
            }
        });

        PauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastPause = SystemClock.elapsedRealtime();
                Chronometer_timer.stop();
                PauseButton.setEnabled(false);
                PlayButton.setEnabled(true);
            }
        });

        StopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chronometer_timer.stop();
                Chronometer_timer.setBase(SystemClock.elapsedRealtime());
                lastPause = 0;
                PlayButton.setEnabled(true);
                PauseButton.setEnabled(false);
            }
        });
         */

    }
    /*
    public boolean onTouchEvent(MotionEvent touchEvent) {
        switch (touchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                if (x1 < x2) {
                    Intent i = new Intent(Timer.this, MakeList_ACTV.class);
                    startActivity(i);
                }/*else if(x1 > x2){
                    Intent i = new Intent(MakeList_ACTV.this, Timer.class);
                    startActivity(i);
                }*/
       /*         break;
        }
        return false;
    }*/
}
