package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dongjie on 2018/4/18.
 */

public class catcount extends AppCompatActivity {
    private  Timer timer1,timer2;
    private int time;
    CountDownTimer cdt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cat);
        Bundle bundle = this.getIntent().getExtras();
        time=bundle.getInt("time");
        final circlebar mCircle = (circlebar) findViewById(R.id.circleProgressbar);
        mCircle.setminpro(time);
        timer2 = new Timer();
        timer2.schedule(new TimerTask() {

            @Override
            public void run() {
                mCircle.updateball();
            }
        }, 0, 2);
        cdt = new CountDownTimer(time*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mCircle.update();
            }
            @Override
            public void onFinish() {
                timer2.cancel();
                timer2=null;
                showstopDialog();
            }
        };
        cdt.start();
        //mCircle.setminpro(time);
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }
    private void showExitDialog(){
        new AlertDialog.Builder(this)
                .setTitle("确定框")
                .setMessage("确定放弃吗")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        startActivity(new Intent(catcount.this, MainActivity.class));
                        timer2.cancel();
                        timer2=null;
                        cdt.cancel();
                        cdt=null;
                    }
                })
                .setNegativeButton("否", null)
                .show();
    }
    private void showstopDialog(){
        new AlertDialog.Builder(this)
                .setTitle("确定框")
                .setMessage("定时结束")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        startActivity(new Intent(catcount.this, MainActivity.class));
                    }
                })
                .show();
    }

    public void cou_abo(View e){
        showExitDialog();
    }
}
