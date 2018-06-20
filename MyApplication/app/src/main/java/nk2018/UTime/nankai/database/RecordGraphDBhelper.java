package nk2018.UTime.nankai.database;

/**
 * Created by Administrator on 2018/5/2/002.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class RecordGraphDBhelper extends SQLiteOpenHelper{
    public static final String DB_NAME = "urecord.db";
    public static final int DB_VERSION = 1;
    public RecordGraphDBhelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_STUDENT="CREATE TABLE "+ RecordGraph.TABLE+"("
                +RecordGraph.KEY_ID+" INTEGER PRIMARY KEY NOT NULL ,"
                +RecordGraph.KEY_record+" INTEGER NOT NULL, "
                +RecordGraph.KEY_date+" INTEGER NOT NULL)";
        db.execSQL(CREATE_TABLE_STUDENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ RecordGraph.TABLE);
        //再次创建表
        onCreate(db);
    }
}
