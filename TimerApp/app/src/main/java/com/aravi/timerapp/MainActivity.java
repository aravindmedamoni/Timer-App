package com.aravi.timerapp;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    TextView mnumber,mcnfrmmsg;
    MediaPlayer m1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.btnset);
        editText=findViewById(R.id.tmrsetter);
        mnumber=findViewById(R.id.number);
        mcnfrmmsg=findViewById(R.id.cnfrmmsg);

        m1=MediaPlayer.create(this,R.raw.fv);

    }

    public void setTime(View view) {
        String s=editText.getText().toString();
        if (s.length()==0){
            editText.setError("please Set the timer");
        }else {
            int n=Integer.valueOf(s);
            editText.setText(null);
            button.setVisibility(View.INVISIBLE);
            new CountDownTimer(n * 1000, 1000) {

                public void onTick(long millisecondsUntilDone) {
                    mnumber.setText("Time: " + String.valueOf(millisecondsUntilDone / 1000));
                }

                @Override
                public void onFinish() {
                    mcnfrmmsg.setText("Done");
                    button.setVisibility(View.VISIBLE);
                    m1.start();

                }
            }.start();
        }

        }
}
