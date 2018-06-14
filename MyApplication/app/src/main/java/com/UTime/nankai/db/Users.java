package com.UTime.nankai.db;

/**
 * Created by Administrator on 2018/4/19 0019.
 */

import android.provider.BaseColumns;

public class Users implements BaseColumns{

        //表名
        public static final String TABLE="users";

        //表的各域名
        public static final String KEY_ID="id";
        public static final String KEY_phone="phone";
        public static final String KEY_IDSQL="idsql";


        //属性
        public int id;
        public String phone;
        public int idsql;
}
