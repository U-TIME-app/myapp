package com.UTime.nankai;

/**
 * Created by Administrator on 2018/4/22 0022.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.UTime.nankai.db.TaskContract;
import com.UTime.nankai.db.TaskDbHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class nextpage extends Activity {
    private TaskDbHelper mHelper;
    private ListView mTaskListView;
    private ListView mTaskListView1;
    private ArrayAdapter<String> mAdapter_sub;
    private Button  back;
    private Button  add_task;
    private TextView todotext,donetext;

    protected void onCreate(Bundle savedInstanceState) {

        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nextpage);

        mHelper = new TaskDbHelper(this);
        mTaskListView = (ListView) findViewById(R.id.list_todo_sub);
        mTaskListView1 = (ListView) findViewById(R.id.list_todo_sub_done);
        todotext=(TextView)findViewById(R.id.todotext);
        donetext=(TextView)findViewById(R.id.donetext);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String id = intent.getStringExtra("id");
        TextView titleTextView = (TextView) findViewById(R.id.page_sub_title);
        titleTextView.setText(title);

        updateUI_sub();

        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        add_task = (Button) findViewById(R.id.add_task_sub);
        add_task.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                addTask();

            }
        });
    }

    public boolean addTask() {
        Intent intent = getIntent();
        final String id = intent.getStringExtra("id");
        final String title = intent.getStringExtra("title");
        final EditText taskEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("在" + title + "分类中添加任务")
                        .setMessage("你想做什么?")
                        .setView(taskEditText)
                        .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(TaskContract.TaskEntry_sub.COL_TASK_ID_PARENT, id);
                                values.put(TaskContract.TaskEntry_sub.COL_TASK_TITLE_sub, task);
                                values.put(TaskContract.TaskEntry_sub.COL_TASK_TITLE_sub_done, "unchecked");
                                db.insertWithOnConflict(TaskContract.TaskEntry_sub.TABLE,
                                        null,
                                        values,
                                        SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                                updateUI_sub();
                              }
                        })
                        .setNegativeButton("取消", null)
                        .create();
                dialog.show();
                return true;

    }
    public void done_sub(View view){
        View parent = (View) view.getParent().getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title_sub);
        CheckBox done = (CheckBox) view.findViewById(R.id.done_sub);
        if(done.isChecked()){
            taskTextView.setText(taskTextView.getText());
            taskTextView.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
            ContentValues values = new ContentValues();
            Intent intent = getIntent();
            final String id = intent.getStringExtra("id");
            SQLiteDatabase db = mHelper.getWritableDatabase();
            values.put(TaskContract.TaskEntry_sub.COL_TASK_TITLE_sub_done, "checked");
            TextView id_sub_TextView = (TextView) parent.findViewById(R.id.task_id_sub);
            String id_sub = String.valueOf(id_sub_TextView.getText());
            db.updateWithOnConflict(TaskContract.TaskEntry_sub.TABLE,
                    values,
                    TaskContract.TaskEntry_sub.COL_TASK_ID_PARENT + " = ? and "+TaskContract.TaskEntry_sub._ID + "= ? ",
                    new String[]{id,id_sub},
                    SQLiteDatabase.CONFLICT_REPLACE
            );
            db.close();
            updateUI_sub();
        }
        else{
            taskTextView.setText(taskTextView.getText());
            taskTextView.getPaint().setFlags(0);
            ContentValues values = new ContentValues();
            TextView idTextView = (TextView) parent.findViewById(R.id.task_id_sub);
            final String id = String.valueOf(idTextView.getText());
            SQLiteDatabase db = mHelper.getWritableDatabase();
            values.put(TaskContract.TaskEntry_sub.COL_TASK_TITLE_sub_done, "unchecked");
            db.updateWithOnConflict(TaskContract.TaskEntry_sub.TABLE,
                    values,
                    TaskContract.TaskEntry_sub._ID + " = ?",
                    new String[]{id},
                    SQLiteDatabase.CONFLICT_REPLACE
            );
            db.close();
            updateUI_sub();
        }
    }
    public void deleteTask_sub(View view) {
        Intent intent = getIntent();
        final String id = intent.getStringExtra("id");
        View parent = (View) view.getParent().getParent();
        TextView id_sub_TextView = (TextView) parent.findViewById(R.id.task_id_sub);
        String id_sub = String.valueOf(id_sub_TextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry_sub.TABLE,
                TaskContract.TaskEntry_sub.COL_TASK_ID_PARENT + " = ? and "+TaskContract.TaskEntry_sub._ID + "= ? ",
                new String[]{id,id_sub});
        db.close();
        updateUI_sub();
    }
    public void edit_sub(View view){
        View parent = (View) view.getParent().getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title_sub);
        final String old_task = String.valueOf(taskTextView.getText());
        TextView idTextView = (TextView) parent.findViewById(R.id.task_id_sub);
        final String id = String.valueOf(idTextView.getText());
        final EditText taskEditText = new EditText(this);
        taskEditText.setText(old_task);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("编辑任务")
                .setMessage("你想做什么?")
                .setView(taskEditText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        SQLiteDatabase db = mHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(TaskContract.TaskEntry_sub.COL_TASK_TITLE_sub, task);
                        db.updateWithOnConflict(TaskContract.TaskEntry_sub.TABLE,
                                values,
                                TaskContract.TaskEntry_sub._ID + " = ?",
                                new String[]{id},
                                SQLiteDatabase.CONFLICT_REPLACE
                        );
                        db.close();
                        updateUI_sub();
                    }
                })
                .setNegativeButton("取消", null)
                .create();
        dialog.show();
    }
    private void updateUI_sub() {
        Intent intent = getIntent();
        final String title = intent.getStringExtra("title");
        final String father_id = intent.getStringExtra("id");
        ArrayList<String> taskList1 = new ArrayList<>();
        ArrayList<String> taskList_sub = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        ArrayList<String> idList1 = new ArrayList<>();
        ArrayList<String> idList_sub = new ArrayList<>();
        String selection = "id=?";
        String[] selectionArgs = new String[]{father_id};
        Cursor cursor = db.query(TaskContract.TaskEntry_sub.TABLE,
                new String[]{TaskContract.TaskEntry_sub._ID, TaskContract.TaskEntry_sub.COL_TASK_ID_PARENT,TaskContract.TaskEntry_sub.COL_TASK_TITLE_sub,TaskContract.TaskEntry_sub.COL_TASK_TITLE_sub_done},
                selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            int status = cursor.getColumnIndex(TaskContract.TaskEntry_sub.COL_TASK_TITLE_sub_done);
            if(cursor.getString(status).equals("unchecked")){
                int task = cursor.getColumnIndex(TaskContract.TaskEntry_sub.COL_TASK_TITLE_sub);
                taskList1.add(cursor.getString(task));
                int idx = cursor.getColumnIndex(TaskContract.TaskEntry_sub._ID);
                idList1.add(cursor.getString(idx));
            }else{
                int task = cursor.getColumnIndex(TaskContract.TaskEntry_sub.COL_TASK_TITLE_sub);
                taskList_sub.add(cursor.getString(task));
                int idx = cursor.getColumnIndex(TaskContract.TaskEntry_sub._ID);
                idList_sub.add(cursor.getString(idx));
            }


        }

        int size1=taskList1.size();
        int size2=taskList_sub.size();
        String[] id1=new String[size1];
        String[] task1=new String[size1];
        String[] id2=new String[size2];
        String[] task2=new String[size2];
            for(int i=0;i<size1;i++){
                id1[i]=(String)idList1.get(i);
                task1[i]=(String)taskList1.get(i);
            }
            List<Map<String, Object>> list1 = new ArrayList<>();
            if(id1.length==0){
                todotext.setVisibility(View.GONE);
            }else{
                todotext.setVisibility(View.VISIBLE);
            }
            for(int i = 0; i < id1.length; i++){
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", id1[i]);
                map.put("title", task1[i]);
                list1.add(map);
            }
            //每个数据项对应一个Map，from表示的是Map中key的数组
            String[] from = {"id", "title"};

            //数据项Map中的每个key都在layout中有对应的View，
            //to表示数据项对应的View的ID数组
            int[] to = {R.id.task_id_sub, R.id.task_title_sub};

            //R.layout.item表示数据项UI所对应的layout文件
            SimpleAdapter adapter1 = new SimpleAdapter(this, list1, R.layout.item_todo_sub, from, to);
            mTaskListView.setAdapter(adapter1);
            for(int j=0;j<size2;j++){
                id2[j]=(String)idList_sub.get(j);
                task2[j]=(String)taskList_sub.get(j);
            }
            List<Map<String, Object>> list2 = new ArrayList<>();
            if(id2.length==0){
                donetext.setVisibility(View.GONE);
            }else{
                donetext.setVisibility(View.VISIBLE);
            }
            for(int i = 0; i < id2.length; i++){
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", id2[i]);
                map.put("title", task2[i]);
                list2.add(map);
            }
            SimpleAdapter adapter2 = new SimpleAdapter(this, list2, R.layout.item_todo_sub_done, from, to);
            mTaskListView1.setAdapter(adapter2);

        cursor.close();
        db.close();
    }
}
