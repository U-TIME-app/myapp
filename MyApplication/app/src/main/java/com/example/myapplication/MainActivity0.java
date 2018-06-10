package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.db.UsersCRUD;

public class MainActivity0 extends AppCompatActivity {

    UsersCRUD ucrud=new UsersCRUD(this);
    int user;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main0);

        Integer time = 2000;    //设置等待时间，单位为毫秒
        user=ucrud.getsum();
        Handler handler = new Handler();
        //当计时结束时，跳转至主界面
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(user==0){
                    Intent intent = new Intent(MainActivity0.this,LoginActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(MainActivity0.this,MainActivity.class);
                    startActivity(intent);
                }
                MainActivity0.this.finish();
            }
        }, time);
    }
}
