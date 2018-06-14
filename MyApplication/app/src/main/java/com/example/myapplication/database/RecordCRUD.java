package com.example.myapplication.database;

/**
 * Created by Administrator on 2018/5/2/002.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RecordCRUD {
    private RecordDBhelper dbHelper;
    public RecordCRUD(Context con){
        dbHelper=new RecordDBhelper(con);
    }

    /**
     * 插入数据,检测属于同一用户的同名事件是否存在
     * 存在返回-1
     * 不存在返回插入数据的id
     * @param record 封装好的record对象
     * @return 当前记录的ID
     * wyz
     */
    public int insert(Record record) {
        long id = 0;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Record.KEY_color, record.color);
        values.put(Record.KEY_name, record.name);
        values.put(Record.KEY_times, record.times);
        values.put(Record.KEY_image, record.image);
        values.put(Record.KEY_calendar, record.calendar);
        id = db.insert(Record.TABLE, null, values);
        db.close();
        return (int) id;
    }

    public int insertall(Record record) {
        long id = 0;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Record.KEY_ID,record.id);
        values.put(Record.KEY_color, record.color);
        values.put(Record.KEY_name, record.name);
        values.put(Record.KEY_times, record.times);
        values.put(Record.KEY_image, record.image);
        values.put(Record.KEY_calendar, record.calendar);
        id = db.insert(Record.TABLE, null, values);
        db.close();
        return (int) id;
    }
    /**
    *通过记录ID删除制定记录元素
    * @param id 记录的ID
    */
    public void delete(int id){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete(Record.TABLE,Record.KEY_ID+"=?", new String[]{String.valueOf(id)});
        db.close();
    }


    /**
     * 更新数据库数据，通过getByUserName函数唯一确定确认修改的record的ID，对应记录ID未知的情况
     * 可更改的项目包括次数，颜色，事件名称
     * @param id 传进的参数是已经更改好对象
     * wyz
     */
    public void update(int id){
         SQLiteDatabase db=dbHelper.getWritableDatabase();
         String selectQuery="SELECT "+
                 Record.KEY_times + "," +
                 Record.KEY_name +"," +
                 Record.KEY_color+
                 " FROM " + Record.TABLE
                 + " WHERE " +
                 Record.KEY_ID + "=?";

         Record newr=new Record();
         newr.id=id;
         Cursor cursor=db.rawQuery(selectQuery,new String[]{String.valueOf(id)});
         if(cursor.moveToFirst()){
             do{
                 newr.times=cursor.getInt(cursor.getColumnIndex(Record.KEY_times));
                 newr.name=cursor.getString(cursor.getColumnIndex(Record.KEY_name));
                 newr.color =cursor.getInt(cursor.getColumnIndex(Record.KEY_color));
             }while(cursor.moveToNext());
         }
         cursor.close();
        if(newr!=null){
            String updatesql="UPDATE "+ Record.TABLE+" SET "+Record.KEY_times+"=?  WHERE "+Record.KEY_ID+"=? ;";
            int k=(newr.times)+1;
            db.execSQL(updatesql,new Object[]{ k,id});
            db.close();
        }
    }

    /**
     * 通过用户名和事件名称唯一确定一条记录，获取记录信息
     * @param name 记录名称
     * @return 一条record对象或null
     */
    public Record getByName(String name){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT "+
                Record.KEY_times + "," +
                Record.KEY_ID +"," +
                Record.KEY_image +"," +
                Record.KEY_calendar +"," +
                Record.KEY_color+
                " FROM " + Record.TABLE
                + " WHERE " +
                Record.KEY_name + "=?";

        Record record=new Record();
        record.name=name;
        Cursor cursor=db.rawQuery(selectQuery,new String[]{String.valueOf(name)});
        if(cursor.moveToFirst()){
            do{
                record.times=cursor.getInt(cursor.getColumnIndex(Record.KEY_times));
                record.id=cursor.getInt(cursor.getColumnIndex(Record.KEY_ID));
                record.color =cursor.getInt(cursor.getColumnIndex(Record.KEY_color));
                record.image =cursor.getInt(cursor.getColumnIndex(Record.KEY_image));
                record.calendar =cursor.getInt(cursor.getColumnIndex(Record.KEY_calendar));
                
            }while(cursor.moveToNext());
        }
        else{
            return null;
        }
        cursor.close();
        db.close();
        return record;
    }
    /**
     * 通过事件ID唯一确定一条记录，获取记录信息
     * @param id 记录名称
     * @return 一条record对象或null
     */
    public Record getByID(int id){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT "+
                Record.KEY_times + "," +
                Record.KEY_ID +"," +
                Record.KEY_image +"," +
                Record.KEY_calendar +"," +
                Record.KEY_color+
                " FROM " + Record.TABLE
                + " WHERE " +
                Record.KEY_name + "=?";

        Record record=new Record();
        record.id=id;
        Cursor cursor=db.rawQuery(selectQuery,new String[]{String.valueOf(id)});
        if(cursor.moveToFirst()){
            do{
                record.times=cursor.getInt(cursor.getColumnIndex(Record.KEY_times));
                record.id=cursor.getInt(cursor.getColumnIndex(Record.KEY_ID));
                record.color =cursor.getInt(cursor.getColumnIndex(Record.KEY_color));
                record.image =cursor.getInt(cursor.getColumnIndex(Record.KEY_image));
                record.calendar =cursor.getInt(cursor.getColumnIndex(Record.KEY_calendar));
            }while(cursor.moveToNext());
        }
        else{
            return null;
        }
        cursor.close();
        db.close();
        return record;
    }
    /**
     * 返回指定用户的所有事件列表
     * @return 事件列表
     */
    public ArrayList<Record> getRecordList(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT "+
                Record.KEY_name + "," +
                Record.KEY_times + "," +
                Record.KEY_ID +"," +
                Record.KEY_image +"," +
                Record.KEY_calendar +"," +
                Record.KEY_color+
                " FROM " + Record.TABLE;
        ArrayList<Record> RecordList= new ArrayList<>();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                Record record=new Record();
                record.name=cursor.getString(cursor.getColumnIndex(Record.KEY_name));
                record.times=cursor.getInt(cursor.getColumnIndex(Record.KEY_times));
                record.id=cursor.getInt(cursor.getColumnIndex(Record.KEY_ID));
                record.color =cursor.getInt(cursor.getColumnIndex(Record.KEY_color));
                record.image =cursor.getInt(cursor.getColumnIndex(Record.KEY_image));
                record.calendar =cursor.getInt(cursor.getColumnIndex(Record.KEY_calendar));
                RecordList.add(record);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return RecordList;
    }

    /**
     * 添加备份
     * @return
     */
    public JSONArray RecoveryJson(){
        JSONArray array=new JSONArray();
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT "+
                Record.KEY_ID + "," +
                Record.KEY_name + "," +
                Record.KEY_times + "," +
                Record.KEY_image +"," +
                Record.KEY_calendar +"," +
                Record.KEY_color+
                " FROM " + Record.TABLE;
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do{
                try{
                    JSONObject record=new JSONObject();
                    record.put("id",cursor.getString(cursor.getColumnIndex(Record.KEY_ID)));
                    record.put("name",cursor.getString(cursor.getColumnIndex(Record.KEY_name)));
                    record.put("times", cursor.getInt(cursor.getColumnIndex(Record.KEY_times)));
                    record.put("color",cursor.getInt(cursor.getColumnIndex(Record.KEY_color)));
                    record.put("image", cursor.getInt(cursor.getColumnIndex(Record.KEY_image)));
                    record.put("calendar",cursor.getInt(cursor.getColumnIndex(Record.KEY_calendar)));
                    array.put(record);
                }
                catch (JSONException e){

                }
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return array;
    }

    public boolean deleteall(){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete(Record.TABLE,null,
                null);
        db.close();
        return true;
    }
}
