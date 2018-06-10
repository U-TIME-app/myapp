package com.example.myapplication.db;

/**
 * Created by Administrator on 2018/4/19 0019.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UsersDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "users.db";
    public static final int DB_VERSION = 1;
    public UsersDbHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_STUDENT="CREATE TABLE "+ Users.TABLE+"("
                +Users.KEY_ID+" INTEGER PRIMARY KEY NOT NULL ,"
                +Users.KEY_IDSQL+" INTEGER  NOT NULL ,"
                +Users.KEY_phone+" TEXT NOT NULL)";
        db.execSQL(CREATE_TABLE_STUDENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ Users.TABLE);
        //再次创建表
        onCreate(db);
    }
}
