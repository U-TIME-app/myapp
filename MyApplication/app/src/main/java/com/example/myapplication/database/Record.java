package com.example.myapplication.database;

/**
 * Created by Administrator on 2018/5/2/002.
 */
import android.provider.BaseColumns;
public class Record implements BaseColumns{
    //表名
    public static final String TABLE="record";

    //表的各域名
    public static final String KEY_ID="id";
    //事件ID
    public static final String KEY_name="name";
    //事件名称
    public static final String KEY_times="times";
    //事件次数
    public static final String KEY_color="color";
    //事件颜色
    public static final String KEY_image="image";
    //图片id
    public static final String KEY_calendar="calendar";
    //calendar  id
    public int id;
    public String name;
    public int times;
    public int color ;
    public int image ;
    public int calendar;
}
