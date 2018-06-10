package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.db.Users;
import com.example.myapplication.db.UsersCRUD;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "wyz";
    EditText mEditTextPhoneNumber;
    EditText mEditTextCode;
    Button mButtonGetCode;
    Button mButtonLogin;

    EventHandler eventHandler;
    String strPhoneNumber;
    UsersCRUD ucrud=new UsersCRUD(this);

    int idsql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditTextPhoneNumber = (EditText) findViewById(R.id.phone_number);
        mEditTextCode = (EditText) findViewById(R.id.verification_code);
        mButtonGetCode = (Button) findViewById(R.id.button_send_verification_code);
        mButtonLogin = (Button) findViewById(R.id.button_login);

        mButtonGetCode.setOnClickListener(this);
        mButtonLogin.setOnClickListener(this);

        SMSSDK.initSDK(this, "25f0f5d8a60b0", "e4f7d9001e6058fe5dda83dc73af398c");
        eventHandler = new EventHandler() {

            /**
             * 在操作之后被触发
             *
             * @param event  参数1
             * @param result 参数2 SMSSDK.RESULT_COMPLETE表示操作成功，为SMSSDK.RESULT_ERROR表示操作失败
             * @param data   事件操作的结果
             */

            @Override
            public void afterEvent(int event, int result, Object data) {
                Message message = myHandler.obtainMessage(0x00);
                message.arg1 = event;
                message.arg2 = result;
                message.obj = data;
                myHandler.sendMessage(message);
            }
        };

        SMSSDK.registerEventHandler(eventHandler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_login) {
//            final Users nuser =new Users();
//            nuser.phone=mEditTextPhoneNumber.getText().toString();
//            idsql=0;
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    HttpURLConnection connection=null;
//                    try{
//                        URL url=new URL("http://39.107.253.131:3389/sign");
//                        connection=(HttpURLConnection)url.openConnection();
//                        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
///* optional request header */
//                        connection.setRequestProperty("Accept", "application/json");
//                        connection.setRequestMethod("POST");
//                        connection.setDoOutput(true);
//                        connection.setDoInput(true);
//                        connection.setConnectTimeout(8000);
//                        connection.setReadTimeout(8000);
//                        OutputStreamWriter out=new OutputStreamWriter (connection.getOutputStream());
//                        JSONObject object=new JSONObject();
//                        object.put("phone",mEditTextPhoneNumber.getText().toString());
//                        out.append(object.toString());
//                        out.flush();
//                        out.close();
//                        BufferedReader reader = new BufferedReader(new InputStreamReader(
//                                connection.getInputStream()));
//                        String lines;
//                        StringBuffer sb = new StringBuffer("");
//                        while ((lines = reader.readLine()) != null) {
//                            lines = new String(lines.getBytes(), "utf-8");
//                            sb.append(lines);
//                        }
//                        JSONObject json=new JSONObject(String.valueOf(sb));
//                        try{
//                            idsql=json.getInt("userid");
//                        }
//                        catch(Exception e){
//                            idsql=json.getInt("insertId");
//                        }
//                        nuser.idsql=idsql;
//                        reader.close();
//                        ucrud.insert(nuser);
//                        Intent intent = new Intent(LoginActivity.this, SecondActivity.class);
//                        startActivity(intent);
//                    }catch(Exception e){
//                        e.printStackTrace();
//                    }finally {
//                        if(connection!=null){
//                            connection.disconnect();
//                        }
//                    }
//                }
//            }).start();

            String strCode = mEditTextCode.getText().toString();
            if (null != strCode && strCode.length() == 4) {
                Log.d(TAG, mEditTextCode.getText().toString());
                SMSSDK.submitVerificationCode("86", strPhoneNumber, mEditTextCode.getText().toString());
            } else {
                Toast.makeText(this, "密码长度不正确", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.button_send_verification_code) {
            strPhoneNumber = mEditTextPhoneNumber.getText().toString();
            if (null == strPhoneNumber || "".equals(strPhoneNumber) || strPhoneNumber.length() != 11) {
                Toast.makeText(this, "电话号码输入有误", Toast.LENGTH_SHORT).show();
                return;
            }
            SMSSDK.getVerificationCode("86", strPhoneNumber);
            mButtonGetCode.setClickable(false);
            //开启线程去更新button的text
            new Thread() {
                @Override
                public void run() {
                    int totalTime = 60;
                    for (int i = 0; i < totalTime; i++) {
                        Message message = myHandler.obtainMessage(0x01);
                        message.arg1 = totalTime - i;
                        myHandler.sendMessage(message);
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    myHandler.sendEmptyMessage(0x02);
                }
            }.start();
        }
    }

    @SuppressLint("HandlerLeak")
    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    Log.e(TAG, "result : " + result + ", event: " + event + ", data : " + data);
                    if (result == SMSSDK.RESULT_COMPLETE) { //回调  当返回的结果是complete
                        if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) { //获取验证码
                            Toast.makeText(LoginActivity.this, "发送验证码成功", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "get verification code successful.");
                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) { //提交验证码
                            Log.d(TAG, "submit code successful");
                            Toast.makeText(LoginActivity.this, "提交验证码成功", Toast.LENGTH_SHORT).show();

                            final Users nuser =new Users();
                            nuser.phone=mEditTextPhoneNumber.getText().toString();
                            idsql=-1;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    HttpURLConnection connection=null;
                                    try{
                                        URL url=new URL("http://39.107.253.131:3389/sign");
                                        connection=(HttpURLConnection)url.openConnection();
                                        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

/* optional request header */
                                        connection.setRequestProperty("Accept", "application/json");
                                        connection.setRequestMethod("POST");
                                        connection.setDoOutput(true);
                                        connection.setDoInput(true);
                                        connection.setConnectTimeout(8000);
                                        connection.setReadTimeout(8000);
                                        OutputStreamWriter out=new OutputStreamWriter (connection.getOutputStream());
                                        JSONObject object=new JSONObject();
                                        object.put("phone",mEditTextPhoneNumber.getText().toString());
                                        out.append(object.toString());
                                        out.flush();
                                        out.close();
                                        BufferedReader reader = new BufferedReader(new InputStreamReader(
                                                connection.getInputStream()));
                                        String lines;
                                        StringBuffer sb = new StringBuffer("");
                                        while ((lines = reader.readLine()) != null) {
                                            lines = new String(lines.getBytes(), "utf-8");
                                            sb.append(lines);
                                        }
                                        JSONObject json=new JSONObject(String.valueOf(sb));
                                          try{
                                              idsql=json.getInt("userid");
                                           }
                                          catch(Exception e){
                                              idsql=json.getInt("insertId");
                                           }
                                        reader.close();

                                    }catch(Exception e){

                                        e.printStackTrace();
                                    }finally {
                                        if(connection!=null){
                                            connection.disconnect();
                                        }
                                        nuser.idsql=idsql;
                                        ucrud.insert(nuser);
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            }).start();


                        } else {
                            Log.d(TAG, data.toString());
                        }
                    } else { //进行操作出错，通过下面的信息区分析错误原因
                        try {
                            Throwable throwable = (Throwable) data;
                            throwable.printStackTrace();
                            JSONObject object = new JSONObject(throwable.getMessage());
                            String des = object.optString("detail");//错误描述
                            int status = object.optInt("status");//错误代码
                            //错误代码：  http://wiki.mob.com/android-api-%E9%94%99%E8%AF%AF%E7%A0%81%E5%8F%82%E8%80%83/
                            Log.e(TAG, "status: " + status + ", detail: " + des);
                            if (status > 0 && !TextUtils.isEmpty(des)) {
                                Toast.makeText(LoginActivity.this, des, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 0x01:
                    mButtonGetCode.setText("重新发送(" + msg.arg1 + ")");
                    break;
                case 0x02:
                    mButtonGetCode.setText("获取验证码");
                    mButtonGetCode.setClickable(true);
                    break;
            }
        }
  };
}

