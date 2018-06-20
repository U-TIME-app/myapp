package nk2018.UTime.nankai.db;

/**
 * Created by Administrator on 2018/5/7 0007.
 */

import android.provider.BaseColumns;
public class notifcontract {
    public static final String DB_NAME = "com.notif.db1";
    public static final int DB_VERSION = 1;

    public class notifEntry implements BaseColumns {
        public static final String TABLE = "notif";
        public static final String COL_NOTIF_TITLE = "notiftitle";
        public static final String COL_NOTIF_YEAR="year";
        public static final String COL_NOTIF_MONTH="month";
        public static final String COL_NOTIF_DAY="day";
        public static final String COL_NOTIF_STATUS = "status";

    }
}
