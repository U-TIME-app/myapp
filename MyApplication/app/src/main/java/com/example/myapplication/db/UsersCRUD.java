package com.example.myapplication.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UsersCRUD {
    private UsersDbHelper dbHelper;
    public UsersCRUD(Context con){
        dbHelper=new UsersDbHelper(con);
    }
    public int insert(Users user){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Users.KEY_phone,user.phone);
        values.put(Users.KEY_IDSQL,user.idsql);
        //
        long id=db.insert(Users.TABLE,null,values);
        db.close();
        return (int)id;
    }
    public void update(Users user){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Users.KEY_phone,user.phone);
        values.put(Users.KEY_IDSQL,user.idsql);
        //
        db.update(Users.TABLE,values,Users.KEY_ID+"=?",new String[] { String.valueOf(user.id) });
        db.close();
    }

    public int getsum(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String sql = "SELECT COUNT(*) FROM "+Users.TABLE;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        cursor.close();
        return (int)count;
    }
    public Users getUser(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT "+
                Users.KEY_ID+ "," +
                Users.KEY_IDSQL+ " ," +
                Users.KEY_phone+ "  " +
                " FROM " + Users.TABLE;
        int iCount=0;
        Users user=new Users();
        Cursor cursor=db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
                user.id=cursor.getInt(cursor.getColumnIndex(Users.KEY_ID));
                user.phone=cursor.getString(cursor.getColumnIndex(Users.KEY_phone));
                user.idsql=cursor.getInt(cursor.getColumnIndex(Users.KEY_IDSQL));
        }
        else{
            return null;
        }
        cursor.close();
        db.close();
        return user;
    }
}

