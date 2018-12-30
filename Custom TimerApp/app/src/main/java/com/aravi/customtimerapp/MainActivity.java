package com.aravi.customtimerapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // private static final long timein_millis=6000;

    private  long mstartTimeInmillis;

    private boolean isRunning;
    private CountDownTimer countDownTimer;
    private long timeleft_in_millis;
   // private long timeleft_in_millis=timein_millis;
    private long mEndtime;
    EditText editText;
    TextView textView;
    Button mbuttonset,mstartandpausebtn,mResetbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText=findViewById(R.id.setup_time);
        textView=findViewById(R.id.RemainingtmTv);
        mbuttonset=findViewById(R.id.donebutton);
        mstartandpausebtn=findViewById(R.id.Startandpausebutton);
        mResetbtn=findViewById(R.id.Resetbutton);

        mbuttonset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input=editText.getText().toString();

                if (input.length()==0){
                    editText.setError("Please Set The Time");
                    editText.setText("");
                    return;
                }
                long millisInput=Long.parseLong(input)*60000;

                if (millisInput==0){
                    editText.setError("Please Give Any Value Other Than 0");
                    editText.setText("");
                    return;
                }

                setTime(millisInput);
                editText.setText("");
            }
        });

        mstartandpausebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning){
                    pauseTimer();
                }else {
                    startTimer();
                }
            }
        });

        mResetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
       // updateCountDownText();
    }

    private void setTime(long milliseconds){
        mstartTimeInmillis=milliseconds;
        resetTimer();
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        isRunning=false;
        /*mstartandpausebtn.setText("Start");
        mResetbtn.setVisibility(View.VISIBLE);*/
        updateButtons();
    }
    private void startTimer(){
        mEndtime=System.currentTimeMillis()+timeleft_in_millis;

         countDownTimer=new CountDownTimer(timeleft_in_millis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeleft_in_millis=millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                isRunning=false;
               /* mstartandpausebtn.setText("Start");
                mstartandpausebtn.setVisibility(View.INVISIBLE);
                mResetbtn.setVisibility(View.VISIBLE);*/
               updateButtons();

            }
        }.start();
        isRunning=true;
       /* mstartandpausebtn.setText("Pause");
        mResetbtn.setVisibility(View.INVISIBLE);*/
       updateButtons();
    }

    private void updateCountDownText() {
        int hour=(int)(timeleft_in_millis/1000)/3600;
        int min=(int)((timeleft_in_millis/1000)%3600)/60;
        int sec=(int)(timeleft_in_millis/1000)%60;

        String timeleftformat;

        if (hour > 0){
            timeleftformat = String.format(Locale.getDefault(),"%02d:%02d:%02d",hour,min,sec);
        }else {
            timeleftformat = String.format(Locale.getDefault(),"%02d:%02d",min,sec);
        }

        textView.setText(timeleftformat);
    }

    public void updateButtons(){
        if (isRunning){
            //isRunning=false;
            editText.setVisibility(View.INVISIBLE);
            mbuttonset.setVisibility(View.INVISIBLE);
            mstartandpausebtn.setText("Pause");
            mstartandpausebtn.setBackgroundResource(R.drawable.background_btn);
            mResetbtn.setVisibility(View.INVISIBLE);
        }else {
            editText.setVisibility(View.VISIBLE);
            mbuttonset.setVisibility(View.VISIBLE);
            mstartandpausebtn.setText("Start");
            mstartandpausebtn.setBackgroundColor(Color.parseColor("#43BE31"));
            if (timeleft_in_millis < 1000){
                mstartandpausebtn.setVisibility(View.INVISIBLE);
            }else {
                mstartandpausebtn.setVisibility(View.VISIBLE);
            }
            if (timeleft_in_millis < mstartTimeInmillis){
                mResetbtn.setVisibility(View.VISIBLE);
            }else {
                mResetbtn.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void resetTimer() {
        timeleft_in_millis = mstartTimeInmillis;
        updateCountDownText();
        mResetbtn.setVisibility(View.INVISIBLE);
        mstartandpausebtn.setVisibility(View.VISIBLE);
    }

     @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong("millisleft",timeleft_in_millis);
        outState.putBoolean("timerrunning",isRunning);
        outState.putLong("endtime",mEndtime);
    }

   /* @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        timeleft_in_millis=savedInstanceState.getLong("millisleft");
        isRunning=savedInstanceState.getBoolean("timerrunning");

        updateCountDownText();
        updateButtons();

        if (isRunning){
            mEndtime=savedInstanceState.getLong("endtime");
            timeleft_in_millis=mEndtime - System.currentTimeMillis();
            startTimer();
        }

    }*/

   /* public void startmethod(View view) {
        if (isRunning){
            pauseTimer();
        }else {
            startTimer();
        }
    }*/

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences=getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putLong("starttimeinmillis",mstartTimeInmillis);
        editor.putLong("millisleft",timeleft_in_millis);
        editor.putBoolean("timerrunning",isRunning);
        editor.putLong("endtime",mEndtime);

        editor.apply();
        if(countDownTimer!=null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences=getSharedPreferences("prefs",MODE_PRIVATE);

        mstartTimeInmillis=sharedPreferences.getLong("starttimeinmillis",600000);
        timeleft_in_millis=sharedPreferences.getLong("millisleft",mstartTimeInmillis);
        isRunning=sharedPreferences.getBoolean("timerrunning",false);

        updateCountDownText();
        updateButtons();

        if (isRunning){
            mEndtime=sharedPreferences.getLong("endtime",0);
            timeleft_in_millis=mEndtime-System.currentTimeMillis();
            if (timeleft_in_millis < 0){
                timeleft_in_millis=0;
                isRunning=false;
                updateCountDownText();
                updateButtons();
            }else {
                startTimer();
            }
        }

    }
}
