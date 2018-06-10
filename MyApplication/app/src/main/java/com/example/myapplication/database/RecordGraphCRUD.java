package com.example.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/5/7/007.
 */

public class RecordGraphCRUD {
    private RecordGraphDBhelper dbHelper;
    public RecordGraphCRUD(Context con){
        dbHelper=new RecordGraphDBhelper(con);
    }
    /**
     * 插入数据
     * @param rg 封装好的recordgraph对象
     * @return 当前记录的ID
     * wyz
     */
    public int insert(RecordGraph rg) {
        long id = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RecordGraph.KEY_record,rg.record_id);
        values.put(RecordGraph.KEY_date, rg.date);
        //String insertsql="INSERT INTO "+RecordGraph.TABLE+" VALUES (NULL,?,?);";
        id = db.insert(RecordGraph.TABLE, null, values);
        //db.execSQL(insertsql,new String[]{String.valueOf(rg.record_id),String.valueOf(rg.date)});
        db.close();
        return (int) id;
    }

    /**
     * 返回指定记录的所有点击列表
     * @return 对应记录id的所有times列表
     */
    public ArrayList<RecordGraph> getRecordGraphList(int id){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT "+
                RecordGraph.KEY_ID + "," +
                RecordGraph.KEY_date +
                " FROM " + RecordGraph.TABLE +
                " WHERE " +  RecordGraph.KEY_record + " =?";
        ArrayList<RecordGraph> rgList= new ArrayList<>();
        Cursor cursor=db.rawQuery(selectQuery,new String[]{String.valueOf(id)});

        if(cursor.moveToFirst()){
            do{
                RecordGraph rg=new RecordGraph();
                rg.id=cursor.getInt(cursor.getColumnIndex(RecordGraph.KEY_ID));
                rg.record_id=id;
                rg.date=cursor.getInt(cursor.getColumnIndex(RecordGraph.KEY_date));
                rgList.add(rg);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return rgList;
    }

}
