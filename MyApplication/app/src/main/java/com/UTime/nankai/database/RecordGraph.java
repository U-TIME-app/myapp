package com.UTime.nankai.database;

/**
 * Created by Administrator on 2018/5/2/002.
 */
import android.provider.BaseColumns;
public class RecordGraph implements BaseColumns{
    //表名
    public static final String TABLE="record_graph";

    //表的各域名
    public static final String KEY_ID="id";
    //记录ID
    public static final String KEY_record="recordID";
    //所属record的id
    public static final String KEY_date="date";
    //点击具体日期和时间


    //属性
    public int id;
    public int record_id;
    public long date ;
}
