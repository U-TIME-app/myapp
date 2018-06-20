package nk2018.UTime.nankai.db;

/**
 * Created by Administrator on 2018/4/19 0019.
 */

import android.provider.BaseColumns;
public class TaskContract {
    public static final String DB_NAME = "com.todolist.db1";
    public static final int DB_VERSION = 3;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "tasks";

        public static final String COL_TASK_TITLE = "title";
    }
    public class TaskEntry_sub implements BaseColumns {
        public static final String TABLE = "tasks_sub";

        public static final String COL_TASK_ID_PARENT = "id";
        public static final String COL_TASK_TITLE_sub = "todo";
        public static final String COL_TASK_TITLE_sub_done = "status";
    }
}
