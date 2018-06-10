package com.example.myapplication.db;

/**
 * Created by Administrator on 2018/4/19 0019.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class TaskDbHelper extends SQLiteOpenHelper {
    public TaskDbHelper(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TaskContract.TaskEntry.TABLE + " ( " +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL);";

        String createTable2 = "CREATE TABLE " + TaskContract.TaskEntry_sub.TABLE + " ( " +
                TaskContract.TaskEntry_sub._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry_sub.COL_TASK_ID_PARENT + " TEXT NOT NULL, " +
                TaskContract.TaskEntry_sub.COL_TASK_TITLE_sub + " TEXT NOT NULL, " +
                TaskContract.TaskEntry_sub.COL_TASK_TITLE_sub_done + " TEXT NOT NULL);";
        db.execSQL(createTable);
        db.execSQL(createTable2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry_sub.TABLE);
        onCreate(db);
    }
}
