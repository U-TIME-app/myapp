package nk2018.UTime.nankai.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2018/5/7 0007.
 */

public class notifdbhelper extends SQLiteOpenHelper {
    public notifdbhelper(Context context) {
        super(context, notifcontract.DB_NAME, null, notifcontract.DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + notifcontract.notifEntry.TABLE + " ( " +
                notifcontract.notifEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                notifcontract.notifEntry.COL_NOTIF_TITLE + " TEXT NOT NULL," +
                notifcontract.notifEntry.COL_NOTIF_YEAR + " INTEGER, " +
                notifcontract.notifEntry.COL_NOTIF_MONTH + " INTEGER, " +
                notifcontract.notifEntry.COL_NOTIF_DAY + " INTEGER,"+
                notifcontract.notifEntry.COL_NOTIF_STATUS + " TEXT);";
        db.execSQL(createTable);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + notifcontract.notifEntry.TABLE);
        onCreate(db);
    }
}
