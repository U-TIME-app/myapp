package com.UTime.nankai.database;

/**
 * Created by Administrator on 2018/5/2/002.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class RecordDBhelper extends SQLiteOpenHelper{
    public static final String DB_NAME = "utime.db";
    public static final int DB_VERSION = 1;
    public RecordDBhelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_STUDENT="CREATE TABLE "+ Record.TABLE+"("
                +Record.KEY_ID+" INTEGER PRIMARY KEY NOT NULL ,"
                +Record.KEY_name+" TEXT NOT NULL, "
                +Record.KEY_color+" INTEGER NOT NULL, "
                +Record.KEY_times+" INTEGER NOT NULL,"
                +Record.KEY_image+" INTEGER NOT NULL,"
                +Record.KEY_calendar+" INTEGER NOT NULL)";
        db.execSQL(CREATE_TABLE_STUDENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ Record.TABLE);
        //再次创建表
        onCreate(db);
    }
}
