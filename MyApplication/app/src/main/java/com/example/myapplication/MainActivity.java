package com.example.myapplication;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.myapplication.database.Record;
import com.example.myapplication.database.RecordCRUD;
import com.example.myapplication.database.RecordGraph;
import com.example.myapplication.database.RecordGraphCRUD;
import com.example.myapplication.db.TaskContract;
import com.example.myapplication.db.TaskDbHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.myapplication.db.Users;
import com.example.myapplication.db.UsersCRUD;
import com.example.myapplication.db.notifcontract;
import com.example.myapplication.db.notifdbhelper;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


//extends AppCompatActivity implements View.OnClickListener
public class MainActivity extends AppCompatActivity implements
        CalendarView.OnDateSelectedListener,
        CalendarView.OnMonthChangeListener,
        CalendarView.OnYearChangeListener,
        CalendarView.OnDateLongClickListener,
        View.OnClickListener{

    private TextView mTextMessage;
    private TextView usertele;
    private Button mAddButton;
    private TaskDbHelper mHelper;
    private ListView mTaskListView;
    private RelativeLayout todolistre,screenre;
    private RelativeLayout notificationli;
    private ConstraintLayout footprintco;
    private TextView notiftodotext,notifdonetext;
    //notification用到的变量
    TextView mTextMonthDay;
    TextView mTextYear;
    TextView mTextLunar;
    TextView mTextCurrentDay;
    CalendarView mCalendarView;
    RelativeLayout mRelativeTool;
    private int mcYear;
    CalendarLayout mCalendarLayout;
    private notifdbhelper nHelper;
    private ListView notifListView;
    private ListView notifListView_done;
    int mYear;
    int mMonth;
    int mDay;
    String today;
    //screen用到的变量
    private int time_long;
    private ImageButton ten_min_up,one_min_up,ten_sec_up,one_sec_up;
    private ImageButton ten_min_down,one_min_down,ten_sec_down,one_sec_down;
    private TextView ten_min,one_min,ten_sec,one_sec;
    private int[] time_value={1,2,3,4,5,6,7,8,9,0};
    private int [] time_now={0,9,9,9};
    //footprint
    private ArrayList<Integer>mData,times,ID,cal,colors;
    private ArrayList<String>things;
    private boolean isShowDelete=false;
    private GridView mGridView;
    private gridadapter adapter;
    final static int UPDATEUI=0;
    final static int BACKUP=1;
    private ImageButton add;
    private Handler mhandler=new Handler(){
        public void handleMessage(Message msg){
            //如果返现msg.what=SHOW_RESPONSE，则进行制定操作，如想进行其他操作，则在子线程里将SHOW_RESPONSE改变
            switch (msg.what){
                case UPDATEUI:
                    updateUI();
                    AlertDialog.Builder builder  = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("确认" ) ;
                    builder.setMessage("恢复备份成功" ) ;
                    builder.setPositiveButton("是" ,  null );
                    builder.show();
                    break;
                case BACKUP:
                    AlertDialog.Builder builder_bu  = new AlertDialog.Builder(MainActivity.this);
                    builder_bu.setTitle("确认" ) ;
                    builder_bu.setMessage("备份成功" ) ;
                    builder_bu.setPositiveButton("是" ,  null );
                    builder_bu.show();
            }
        }
    };
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_todo:
                    todolistre.setVisibility(View.VISIBLE);
                    notificationli.setVisibility(View.GONE);
                    screenre.setVisibility(View.GONE);
                    footprintco.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_notifications:
                    todolistre.setVisibility(View.GONE);
                    notificationli.setVisibility(View.VISIBLE);
                    screenre.setVisibility(View.GONE);
                    footprintco.setVisibility(View.GONE);
                    initnotifView();
                    return true;
                case R.id.navigation_footprint:
                    todolistre.setVisibility(View.GONE);
                    notificationli.setVisibility(View.GONE);
                    screenre.setVisibility(View.GONE);
                    footprintco.setVisibility(View.VISIBLE);
                    initfootprint();
                    return true;
                case R.id.navigation_screen:
                    todolistre.setVisibility(View.GONE);
                    notificationli.setVisibility(View.GONE);
                    screenre.setVisibility(View.VISIBLE);
                    footprintco.setVisibility(View.GONE);
                    initScreen();
                    return true;
            }
            return false;
        }
    };

    private UsersCRUD ucrdb=new UsersCRUD(this);
    private RecordCRUD rcrud=new RecordCRUD(this);
    private RecordGraphCRUD rgcrud=new RecordGraphCRUD(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_main);
        todolistre=(RelativeLayout)findViewById(R.id.todolist);
        notificationli=(RelativeLayout)findViewById(R.id.notification);
        screenre=(RelativeLayout)findViewById(R.id.screen_counter);
        footprintco=(ConstraintLayout)findViewById(R.id.footprint);
        mHelper = new TaskDbHelper(this);
        mTextMessage = (TextView) findViewById(R.id.message);
        mTaskListView = (ListView) findViewById(R.id.list_todo);
        mAddButton = (Button)findViewById(R.id.add_task);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mTextMessage.setVisibility(View.GONE);
        mTaskListView.setVisibility(View.VISIBLE);
        mAddButton.setVisibility(View.VISIBLE);
        mAddButton.setOnClickListener(this);
        updateUI();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @SuppressWarnings("StatementWithEmptyBody")
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_restore) {
                    // Handle the camera action
                    backup();
                }else if(id == R.id.nav_recovery){
                    recovery();
                }
                else if (id == R.id.nav_notify) {

                }  else if (id == R.id.nav_share) {
                    Intent intent=new Intent(MainActivity.this,share.class);
                    startActivity(intent);
                } else if (id == R.id.nav_login) {
                    int i=ucrdb.getsum();
                    if(i==0){
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    else{
                        AlertDialog.Builder builder_bu  = new AlertDialog.Builder(MainActivity.this);
                        builder_bu.setTitle("提示" ) ;
                        builder_bu.setMessage("当前用户已登录" ) ;
                        builder_bu.setPositiveButton("知道了" ,  null );
                        builder_bu.show();
                    }
                } else if (id == R.id.nav_contact) {
                    Intent intent=new Intent(MainActivity.this,contact_us.class);
                    startActivity(intent);
                } else if (id == R.id.nav_exit) {
                    ucrdb.deleteall();
                }


                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }

    public void backup(){

        Users user=ucrdb.getUser();
        Log.e("A",user.idsql+"xx");
        if(user ==null){
            AlertDialog.Builder builder_bu  = new AlertDialog.Builder(MainActivity.this);
            builder_bu.setTitle("确认" ) ;
            builder_bu.setMessage("未登录，请登陆后尝试" ) ;
            builder_bu.setPositiveButton("是" ,  null );
            builder_bu.show();
            return;
        }
        final JSONObject object=new JSONObject();


        //record和图表相关备份恢复
        JSONArray record=rcrud.RecoveryJson();
        try{
            object.put("record", record);
        }
        catch (JSONException e){

        }
        JSONArray rg=rgcrud.RecoveryJson();
        try{
            object.put("record_graph", rg);
        }
        catch (JSONException e){

        }
        //备忘录相关备份恢复
        JSONArray array=new JSONArray();
        try{
            object.put("user_id", user.idsql);

        }
        catch (JSONException e){

        }
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String selectsql="SELECT "+
                TaskContract.TaskEntry._ID+ "," +
                TaskContract.TaskEntry.COL_TASK_TITLE+ " " +
                " FROM " + TaskContract.TaskEntry.TABLE;
        Cursor cursor = db.rawQuery(selectsql,null);
        while (cursor.moveToNext()) {
            int task_content = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            int id = cursor.getColumnIndex(TaskContract.TaskEntry._ID);
            try{
                JSONObject task=new JSONObject();
                task.put("id", cursor.getString(id));
                task.put("content",cursor.getString(task_content));
                array.put(task);
            }
            catch (JSONException e){

            }

        }
        try{
            object.put("task", array);
        }
        catch (JSONException e){

        }
        JSONArray array_sub=new JSONArray();

        SQLiteDatabase db_sub = mHelper.getReadableDatabase();
        Cursor cursor_sub = db_sub.query(TaskContract.TaskEntry_sub.TABLE,
                new String[]{TaskContract.TaskEntry_sub._ID, TaskContract.TaskEntry_sub.COL_TASK_ID_PARENT,TaskContract.TaskEntry_sub.COL_TASK_TITLE_sub,TaskContract.TaskEntry_sub.COL_TASK_TITLE_sub_done},
                null, null, null, null, null);
        while (cursor_sub.moveToNext()) {
            int status = cursor_sub.getColumnIndex(TaskContract.TaskEntry_sub.COL_TASK_TITLE_sub_done);
            int task_content = cursor_sub.getColumnIndex(TaskContract.TaskEntry_sub.COL_TASK_TITLE_sub);
            int id = cursor_sub.getColumnIndex(TaskContract.TaskEntry_sub._ID);
            int parent_id = cursor_sub.getColumnIndex(TaskContract.TaskEntry_sub.COL_TASK_ID_PARENT);
            try{
                JSONObject task=new JSONObject();
                task.put("id", cursor_sub.getString(id));
                task.put("pid",cursor_sub.getString(parent_id));
                task.put("content",cursor_sub.getString(task_content));
                task.put("status",cursor_sub.getString(status));
                array_sub.put(task);
            }
            catch (JSONException e){

            }

        }
        try{
            object.put("task_sub", array_sub);
        }
        catch (JSONException e){

        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                try{
                    URL url=new URL("http://39.107.253.131:3389/backup");
                    connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    OutputStreamWriter out=new OutputStreamWriter (connection.getOutputStream());
                    out.append(object.toString());
                    out.flush();
                    out.close();
                    InputStream in=connection.getInputStream();
                    BufferedReader bufr=new BufferedReader(new InputStreamReader(in));
                    String line=null;
                    StringBuilder response=new StringBuilder();
                    while((line=bufr.readLine())!=null){
                        response.append(line);
                    }
                    line=response.toString();
                    JSONObject jsonObject = new JSONObject(line);
                    String name = jsonObject.optString("su");
                    Log.e("su",name);
                    Message message=new Message();
                    message.what=BACKUP;
                    mhandler.sendMessage(message);
                    bufr.close();
                    in.close();
                }catch(Exception e){
                    e.printStackTrace();
                }finally {
                    if(connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public void recovery(){
        Users user=ucrdb.getUser();
        if(user==null){
            AlertDialog.Builder builder_bu  = new AlertDialog.Builder(MainActivity.this);
            builder_bu.setTitle("确认" ) ;
            builder_bu.setMessage("未登录，请登陆后尝试" ) ;
            builder_bu.setPositiveButton("是" ,  null );
            builder_bu.show();
            return;
        }
        final int  user_id = user.idsql;
        rcrud.deleteall();
        rgcrud.deleteall();
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE,null,
                null);
        db.delete(TaskContract.TaskEntry_sub.TABLE,
                null,
                null);
        db.close();

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                try{
                    URL url=new URL("http://39.107.253.131:3389/down");
                    connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    OutputStreamWriter out=new OutputStreamWriter (connection.getOutputStream());
                    JSONObject user=new JSONObject();
                    user.put("id", user_id);
                    out.append(user.toString());
                    out.flush();
                    out.close();
                    InputStream in=connection.getInputStream();
                    BufferedReader bufr=new BufferedReader(new InputStreamReader(in));
                    StringBuilder response=new StringBuilder();
                    String line=null;
                    while((line=bufr.readLine())!=null){
                        response.append(line);
                    }
                    line=response.toString();
                    JSONObject jsonObject = new JSONObject(line);
                    JSONArray jsonArray = jsonObject.getJSONArray("task");
                    JSONArray jsonArray_sub = jsonObject.getJSONArray("task_sub");
                    JSONArray jsonArray_record = jsonObject.getJSONArray("record");
                    JSONArray jsonArray_rg = jsonObject.getJSONArray("record_graph");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject_task = jsonArray.getJSONObject(i);

                        if (jsonObject_task != null) {
                            // int id = jsonObject_task.optInt("id");
                            String content = jsonObject_task.optString("content");
                            SQLiteDatabase db = mHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put(TaskContract.TaskEntry.COL_TASK_TITLE, content);
                            db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                                    null,
                                    values,
                                    SQLiteDatabase.CONFLICT_REPLACE);
                            db.close();
                        }
                    }
                    for (int i = 0; i < jsonArray_sub.length(); i++) {
                        JSONObject jsonObject_task = jsonArray_sub.getJSONObject(i);

                        if (jsonObject_task != null) {
                            int pid = jsonObject_task.optInt("pid");
                            String content = jsonObject_task.optString("content");
                            String status = jsonObject_task.optString("status");
                            SQLiteDatabase db = mHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put(TaskContract.TaskEntry_sub.COL_TASK_ID_PARENT, pid);
                            values.put(TaskContract.TaskEntry_sub.COL_TASK_TITLE_sub, content);
                            values.put(TaskContract.TaskEntry_sub.COL_TASK_TITLE_sub_done, status);
                            db.insertWithOnConflict(TaskContract.TaskEntry_sub.TABLE,
                                    null,
                                    values,
                                    SQLiteDatabase.CONFLICT_REPLACE);
                            db.close();

                        }
                    }
                    for (int i = 0; i < jsonArray_record.length(); i++) {
                        JSONObject jsonObject_r = jsonArray_record.getJSONObject(i);

                        if (jsonObject_r != null) {
                            Record re=new Record();
                            re.calendar=jsonObject_r.optInt("calendar");
                            re.color=jsonObject_r.optInt("color");
                            re.image=jsonObject_r.optInt("image");
                            re.name=jsonObject_r.optString("name");
                            re.times=jsonObject_r.optInt("times");
                            rcrud.insert(re);
                        }
                    }
                    for (int i = 0; i < jsonArray_rg.length(); i++) {
                        JSONObject jsonObject_r = jsonArray_rg.getJSONObject(i);
                        if (jsonObject_r != null) {
                            RecordGraph rg=new RecordGraph();
                            rg.date=jsonObject_r.optInt("date");
                            rg.record_id=jsonObject_r.optInt("record");
                            rgcrud.insert(rg);
                        }
                    }
                    Message message=new Message();
                    message.what=UPDATEUI;
                    mhandler.sendMessage(message);
                    bufr.close();
                    in.close();
                }catch(Exception e){
                    e.printStackTrace();
                }finally {
                    if(connection!=null){
                        connection.disconnect();

                    }
                }
            }
        }).start();

    }

    public void leftexist(View view){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            drawer.openDrawer(GravityCompat.START);
            usertele=(TextView)findViewById(R.id.usertelephone);
            Users user=ucrdb.getUser();
            if(user!=null){
                usertele.setText(user.phone);
            }
            else{
                usertele.setText("请登录");
            }
        }
    }

    public void onClick(View v) {
        if(v.getId()==R.id.add_task){
            addTask();
        }
    }

    //todolist
    public void edit(View view){
        View parent = (View) view.getParent().getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        final String old_task = String.valueOf(taskTextView.getText());
        TextView idTextView = (TextView) parent.findViewById(R.id.task_id);
        final String id = String.valueOf(idTextView.getText());
        final EditText taskEditText = new EditText(this);
        taskEditText.setText(old_task);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("编辑任务分类")
                .setMessage("准备做哪一类事情?")
                .setView(taskEditText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        SQLiteDatabase db = mHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
                        db.updateWithOnConflict(TaskContract.TaskEntry.TABLE,
                                                values,
                                                TaskContract.TaskEntry._ID + " = ?",
                                                 new String[]{id},
                                                 SQLiteDatabase.CONFLICT_REPLACE
                                                );
                        db.close();
                        updateUI();
                    }
                })
                .setNegativeButton("取消", null)
                .create();
        dialog.show();
    }



    public void addTask(){
        final EditText taskEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("新增任务分类")
                .setMessage("准备做哪一类事情?")
                .setView(taskEditText)
                .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        SQLiteDatabase db = mHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
                        db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                                null,
                                values,
                                SQLiteDatabase.CONFLICT_REPLACE);
                        db.close();
                        updateUI();
                    }
                })
                .setNegativeButton("取消", null)
                .create();
        dialog.show();

    }
    public void deleteTask(View view) {
        View parent = (View) view.getParent().getParent();
        TextView idTextView = (TextView) parent.findViewById(R.id.task_id);
        String id = String.valueOf(idTextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE,
                TaskContract.TaskEntry._ID + " = ?",
                new String[]{id});
        db.delete(TaskContract.TaskEntry_sub.TABLE,
                TaskContract.TaskEntry_sub.COL_TASK_ID_PARENT + " = ?",
                new String[]{id});
        db.close();
        updateUI();
    }
    public void enter(View view) {
        TextView taskTextView = (TextView) view.findViewById(R.id.task_title);
        TextView idTextView = (TextView) view.findViewById(R.id.task_id);
        String task = String.valueOf(taskTextView.getText());
        String id = String.valueOf(idTextView.getText());
        Intent intent =new Intent(MainActivity.this, nextpage.class);
        intent.putExtra("id",id);
        intent.putExtra("title",task);
        startActivity(intent);
    }
    private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
        ArrayList<String> idList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int task = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            int id = cursor.getColumnIndex(TaskContract.TaskEntry._ID);
            taskList.add(cursor.getString(task));
            idList.add(cursor.getString(id));
        }
        int size=taskList.size();
        String[] id=new String[size];
        String[] task=new String[size];
        for(int i=0;i<size;i++){
            id[i]=(String)idList.get(i);
        }
        for(int i=0;i<size;i++){
            task[i]=(String)taskList.get(i);
        }


        List<Map<String, Object>> list = new ArrayList<>();

        for(int i = 0; i < id.length; i++){
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id[i]);
            map.put("title", task[i]);
            list.add(map);
        }


        //每个数据项对应一个Map，from表示的是Map中key的数组
        String[] from = {"id", "title"};

        //数据项Map中的每个key都在layout中有对应的View，
        //to表示数据项对应的View的ID数组
        int[] to = {R.id.task_id, R.id.task_title};

        //R.layout.item表示数据项UI所对应的layout文件
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.item_todo, from, to);

        mTaskListView.setAdapter(adapter);

        cursor.close();
        db.close();
    }



    //以下是notification相关代码

    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @SuppressLint("SetTextI18n")

    protected void initnotifView() {
        //setStatusBarDarkMode();
        mTextMonthDay = (TextView) findViewById(R.id.tv_month_day);
        mTextYear = (TextView) findViewById(R.id.tv_year);
        mTextLunar = (TextView) findViewById(R.id.tv_lunar);
        mRelativeTool = (RelativeLayout) findViewById(R.id.rl_tool);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        mTextCurrentDay = (TextView) findViewById(R.id.tv_current_day);
        notiftodotext=(TextView) findViewById(R.id.notiftodotext);
        notifdonetext=(TextView) findViewById(R.id.notifdonetext);
        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarView.showYearSelectLayout(mcYear);
                    return;
                }
                mCalendarView.showYearSelectLayout(mcYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mcYear));
            }
        });
        findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });

        mCalendarLayout = (CalendarLayout) findViewById(R.id.calendarLayout);
        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnDateSelectedListener(this);
        mCalendarView.setOnMonthChangeListener(this);
        mCalendarView.setOnDateLongClickListener(this, true);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mcYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        mTextLunar.setText("今日");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));

        /*mTextMonthDay.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextLunar.setVisibility(View.VISIBLE);
        mRelativeTool.setVisibility(View.VISIBLE);
        mCalendarView.setVisibility(View.VISIBLE);
        mTextCurrentDay.setVisibility(View.VISIBLE);*/


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("refreshnotifUI");
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);
        nHelper = new notifdbhelper(this);
        notifListView = (ListView) findViewById(R.id.list_notif);
        notifListView_done = (ListView) findViewById(R.id.list_notif_done);
        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        today=standarddate(mYear,mMonth+1,mDay);
        updatenotifUI();
    }

    protected void initData() {
        final List<com.haibin.calendarview.Calendar> schemes = new ArrayList<>();
    }


    private com.haibin.calendarview.Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        com.haibin.calendarview.Calendar calendar = new com.haibin.calendarview.Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        return calendar;
    }

    @SuppressLint("SetTextI18n")
    public void onDateSelected(com.haibin.calendarview.Calendar calendar, boolean isClick) {
        //Log.e("onDateSelected", "  -- " + calendar.getYear() + "  --  " + calendar.getMonth() + "  -- " + calendar.getDay());
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mcYear = calendar.getYear();
        if (isClick) {
            Toast.makeText(this, getCalendarText(calendar), Toast.LENGTH_SHORT).show();
        }
    }

    public void onDateLongClick(com.haibin.calendarview.Calendar calendar) {
        Toast.makeText(this, "长按不选择日期\n" + getCalendarText(calendar), Toast.LENGTH_SHORT).show();
    }

    private static String getCalendarText(com.haibin.calendarview.Calendar calendar) {
        return String.format("新历%s \n 农历%s \n 公历节日：%s \n 农历节日：%s \n 节气：%s \n 是否闰月：%s",
                calendar.getMonth() + "月" + calendar.getDay() + "日",
                calendar.getLunarCakendar().getMonth() + "月" + calendar.getLunarCakendar().getDay() + "日",
                TextUtils.isEmpty(calendar.getGregorianFestival()) ? "无" : calendar.getGregorianFestival(),
                TextUtils.isEmpty(calendar.getTraditionFestival()) ? "无" : calendar.getTraditionFestival(),
                TextUtils.isEmpty(calendar.getSolarTerm()) ? "无" : calendar.getSolarTerm(),
                calendar.getLeapMonth() == 0 ? "否" : String.format("闰%s月", calendar.getLeapMonth()));
    }


    public void onMonthChange(int year, int month) {
        //Log.e("onMonthChange", "  -- " + year + "  --  " + month);
    }

    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mRefreshBroadcastReceiver);
    }

    public int nDaysBetweenTwoDate(String firstString, String secondString) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date firstDate = null;
        Date secondDate = null;
        try {
            firstDate = df.parse(firstString);
            secondDate = df.parse(secondString);
        } catch (Exception e) {
            // 日期型字符串格式错误
            System.out.println("日期型字符串格式错误");
        }
        int nDay = (int) ((secondDate.getTime() - firstDate.getTime()) / (24 * 60 * 60 * 1000));
        return nDay;
    }

    String standarddate(int year,int month,int date){
        String mmon,mdate;
        if(month<10){
            mmon="0"+month;
        }
        else{
            mmon=""+month;
        }
        if(date<10)
            mdate="0"+date;
        else
            mdate=""+date;
        return year+"-"+mmon+"-"+mdate;
    }
    public void addnotif(View view){
        Intent intent =new Intent(MainActivity.this, addnotif.class);
        startActivity(intent);
    }

    public void modifynotif(View view){
        //View parent = (View) view.getParent();
        TextView idTextView = (TextView) view.findViewById(R.id.notif_id);
        String id = String.valueOf(idTextView.getText());
        TextView titleTextView = (TextView) view.findViewById(R.id.notif_title);
        String title = String.valueOf(titleTextView.getText());
        TextView dateTextView = (TextView) view.findViewById(R.id.notif_date);
        String date = String.valueOf(dateTextView.getText());
        TextView lefttimeTextView=(TextView) view.findViewById(R.id.left_day);
        String leftday=String.valueOf(lefttimeTextView.getText());
        Intent intent =new Intent(MainActivity.this, modifynotif.class);
        intent.putExtra("id",id);
        intent.putExtra("title",title);
        intent.putExtra("date",date);
        intent.putExtra("leftday",leftday);
        startActivity(intent);
    }

    public void deletenotif(View view) {
        View parent = (View) view.getParent().getParent();
        TextView idTextView = (TextView) parent.findViewById(R.id.notif_id);
        String id = String.valueOf(idTextView.getText());
        SQLiteDatabase db = nHelper.getWritableDatabase();
        db.delete(notifcontract.notifEntry.TABLE,
                notifcontract.notifEntry._ID + " = ?",
                new String[]{id});
        db.close();
        updatenotifUI();
    }


    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("refreshnotifUI"))
            {
                updatenotifUI();
            }else if(action.equals("addactivity")){
                initfootprint();
            }
        }
    };

    public void done_noti(View view){
        notifdbhelper mHelper = new notifdbhelper(this);
        View parent = (View) view.getParent().getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.notif_title);
        CheckBox done = (CheckBox) parent.findViewById(R.id.noti_done);
        if(done.isChecked()){
            taskTextView.setText(taskTextView.getText());
            taskTextView.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
            ContentValues values = new ContentValues();
            Intent intent = getIntent();
            SQLiteDatabase db = mHelper.getWritableDatabase();
            values.put(notifcontract.notifEntry.COL_NOTIF_STATUS, "1");
            TextView id_sub_TextView = (TextView) parent.findViewById(R.id.notif_id);
            String id = String.valueOf(id_sub_TextView.getText());
            db.updateWithOnConflict(notifcontract.notifEntry.TABLE,
                    values,
                    notifcontract.notifEntry._ID + "= ? ",
                    new String[]{id},
                    SQLiteDatabase.CONFLICT_REPLACE
            );
            db.close();
            updatenotifUI();
        }
        else{
            taskTextView.setText(taskTextView.getText());
            taskTextView.getPaint().setFlags(0);
            ContentValues values = new ContentValues();
            TextView idTextView = (TextView) parent.findViewById(R.id.notif_id);
            final String id = String.valueOf(idTextView.getText());
            SQLiteDatabase db = mHelper.getWritableDatabase();
            values.put(notifcontract.notifEntry.COL_NOTIF_STATUS, "0");
            db.updateWithOnConflict(notifcontract.notifEntry.TABLE,
                    values,
                    notifcontract.notifEntry._ID + " = ?",
                    new String[]{id},
                    SQLiteDatabase.CONFLICT_REPLACE
            );
            db.close();
            updatenotifUI();
        }
    }
    public void updatenotifUI(){
        ArrayList<String> notiftitleList = new ArrayList<>();
        ArrayList<String> notifidList = new ArrayList<>();
        ArrayList<String> notifdateList = new ArrayList<>();
        ArrayList<String> notifdayList = new ArrayList<>();
        ArrayList<String> notiftitleList_done = new ArrayList<>();
        ArrayList<String> notifidList_done = new ArrayList<>();
        ArrayList<String> notifdateList_done = new ArrayList<>();
        ArrayList<String> notifdayList_done = new ArrayList<>();
        SQLiteDatabase db = nHelper.getReadableDatabase();
        Cursor cursor = db.query(notifcontract.notifEntry.TABLE,
                new String[]{notifcontract.notifEntry._ID, notifcontract.notifEntry.COL_NOTIF_TITLE,notifcontract.notifEntry.COL_NOTIF_YEAR,notifcontract.notifEntry.COL_NOTIF_MONTH,notifcontract.notifEntry.COL_NOTIF_DAY,notifcontract.notifEntry.COL_NOTIF_STATUS},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int status = cursor.getColumnIndex(notifcontract.notifEntry.COL_NOTIF_STATUS);
            int notiftitle = cursor.getColumnIndex(notifcontract.notifEntry.COL_NOTIF_TITLE);
            int notifid = cursor.getColumnIndex(notifcontract.notifEntry._ID);
            int year= cursor.getColumnIndex(notifcontract.notifEntry.COL_NOTIF_YEAR);
            int month = cursor.getColumnIndex(notifcontract.notifEntry.COL_NOTIF_MONTH);
            int date= cursor.getColumnIndex(notifcontract.notifEntry.COL_NOTIF_DAY);
            //System.out.println("year:"+year+"month"+month+"date"+date);
            String end=standarddate(Integer.valueOf(cursor.getString(year)),Integer.valueOf(cursor.getString(month)),Integer.valueOf(cursor.getString(date)));
            String left_day=""+nDaysBetweenTwoDate(today,end);
            if(cursor.getString(status).equals("0")) {
                notiftitleList.add(cursor.getString(notiftitle));
                notifidList.add(cursor.getString(notifid));
                notifdateList.add(cursor.getString(year) + "-" + cursor.getString(month) + "-" + cursor.getString(date));
                notifdayList.add(left_day);
            }
            else {
                notiftitleList_done.add(cursor.getString(notiftitle));
                notifidList_done.add(cursor.getString(notifid));
                notifdateList_done.add(cursor.getString(year) + "-" + cursor.getString(month) + "-" + cursor.getString(date));
                notifdayList_done.add(left_day);
            }
        }

        int size=notiftitleList.size();
        int size_done=notiftitleList_done.size();
        String[] notifid=new String[size];
        String[] notiftitle=new String[size];
        String[] notifdate=new String[size];
        String[] notifday=new String[size];
        String[] notifid_done=new String[size_done];
        String[] notiftitle_done=new String[size_done];
        String[] notifdate_done=new String[size_done];
        String[] notifday_done=new String[size_done];
        for(int i=0;i<size;i++){
            notifid[i]=(String)notifidList.get(i);
            notiftitle[i]=(String)notiftitleList.get(i);
            notifdate[i]=(String)notifdateList.get(i);
            notifday[i]=(String)notifdayList.get(i);

        }
        for(int i=0;i<size_done;i++){
            notifid_done[i]=(String)notifidList_done.get(i);
            notiftitle_done[i]=(String)notiftitleList_done.get(i);
            notifdate_done[i]=(String)notifdateList_done.get(i);
            notifday_done[i]=(String)notifdayList_done.get(i);

        }
        List<Map<String, Object>> list = new ArrayList<>();
        List<Map<String, Object>> list_done = new ArrayList<>();

        if(notifid.length==0){
            notiftodotext.setVisibility(View.GONE);
        }else{
            notiftodotext.setVisibility(View.VISIBLE);
        }

        if(notifid_done.length==0){
            notifdonetext.setVisibility(View.GONE);
        }else{
            notifdonetext.setVisibility(View.VISIBLE);
        }

        for(int i = 0; i < notifid.length; i++){
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", notifid[i]);
            map.put("title", notiftitle[i]);
            map.put("date",notifdate[i]);
            map.put("day",notifday[i]);
            list.add(map);
        }
        for(int i = 0; i < notifid_done.length; i++){
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", notifid_done[i]);
            map.put("title", notiftitle_done[i]);
            map.put("date",notifdate_done[i]);
            map.put("day",notifday_done[i]);
            list_done.add(map);
        }


        //每个数据项对应一个Map，from表示的是Map中key的数组
        String[] from = {"id", "title","date","day"};

        //数据项Map中的每个key都在layout中有对应的View，
        //to表示数据项对应的View的ID数组
        int[] to = {R.id.notif_id, R.id.notif_title,R.id.notif_date,R.id.left_day};

        //R.layout.item表示数据项UI所对应的layout文件
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.item_notif, from, to);
        SimpleAdapter adapter_done = new SimpleAdapter(this, list_done, R.layout.item_notif_done, from, to);
        notifListView.setAdapter(adapter);
        notifListView_done.setAdapter(adapter_done);
        cursor.close();
        db.close();
    }

    //screen代码

    protected void initScreen(){
        time_long=10*60;
        ten_min=findViewById(R.id.ten_min);
        one_min=findViewById((R.id.one_min));
        ten_sec=findViewById(R.id.ten_sec);
        one_sec=findViewById(R.id.one_sec);
        ten_min_up=(ImageButton)findViewById(R.id.ten_min_up);
        ten_min_down=(ImageButton)findViewById(R.id.ten_min_down);
        one_min_up=(ImageButton)findViewById(R.id.one_min_up);
        one_min_down=(ImageButton)findViewById(R.id.one_min_down);
        ten_sec_up=(ImageButton)findViewById(R.id.ten_sec_up);
        ten_sec_down=(ImageButton)findViewById(R.id.ten_sec_down);
        one_sec_up=(ImageButton)findViewById(R.id.one_sec_up);
        one_sec_down=(ImageButton)findViewById(R.id.one_sec_down);
    }

    public int settime(){
        return time_value[time_now[0]]*600+time_value[time_now[1]]*60+time_value[time_now[2]]*10+time_value[time_now[3]];
    }
    public void turn_time(int where,int action){
        if(where==2){
            if(action ==1 && time_now[where]==4){
                time_now[where]=9;
            }
            else if(action ==0 &&time_now[where]==9){
                time_now[where] = 4;
            }
            else if(action ==1&& time_now[where]==9){
                time_now[where]=0;
            }
            else if(action ==0&& time_now[where]==0){
                time_now[where]=9;
            }
            else if(action==1){
                time_now[where]++;
            }
            else if(action ==0){
                time_now[where]--;
            }
        }
        else if(action==1) {
            if (time_now[where] == 9)
                time_now[where] = 0;
            else time_now[where]++;
        }
        else{
            if (time_now[where] == 0)
                time_now[where] = 9;
            else time_now[where]--;
        }
        String text;
        switch (where){
            case 0:
                text=Integer.toString(time_value[time_now[where]]);
                ten_min.setText(text);
                break;
            case 1:
                text=Integer.toString(time_value[time_now[where]]);
                one_min.setText(text);
                break;
            case 2:
                text=Integer.toString(time_value[time_now[where]]);
                ten_sec.setText(text);
                break;
            case 3:
                text=Integer.toString(time_value[time_now[where]]);
                one_sec.setText(text);
                break;
        }

    }

    public void ten_minu(View e){
        turn_time(0,1);
    }
    public void one_minu(View e){
        turn_time(1,1);
    }
    public void ten_secu(View e){
        turn_time(2,1);
    }
    public void one_secu(View e){
        turn_time(3,1);
    }
    public void ten_mind(View e){
        turn_time(0,0);
    }
    public void one_mind(View e){
        turn_time(1,0);
    }
    public void ten_secd(View e){
        turn_time(2,0);
    }
    public void one_secd(View e){
        turn_time(3,0);
    }
    public void start_cou(View e){
        Intent intent=new Intent(MainActivity.this, catcount.class);
        Bundle bundle=new Bundle();
        //传递name参数为tinyphp
        bundle.putInt("time",this.settime());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //footprint代码
    protected void initfootprint(){
        mGridView = (GridView) findViewById(R.id.grid);
        add=(ImageButton)findViewById(R.id.floatingActionButton);
        initDatas();

        adapter = new gridadapter(MainActivity.this, mData,ID,things,times,cal,colors,isShowDelete);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                // TODO Auto-generated method stub
                if(!isShowDelete)
                    isShowDelete=true;
                else isShowDelete=false;
                adapter = new gridadapter(MainActivity.this, mData,ID,things,times,cal,colors,isShowDelete);
                mGridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            }

        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(position<parent.getChildCount()) {
                    times.set(position, times.get(position) + 1);

                    update_times(ID.get(position));
                    adapter = new gridadapter(MainActivity.this, mData, ID, things, times, cal, colors, isShowDelete);
                    mGridView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }


            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AddActivity.class);
                startActivity(intent);

            }
        });
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("addactivity");
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);
    }
    private void initDatas() {
        mData=new ArrayList<>();
        things=new ArrayList<>();
        times=new ArrayList<>();
        ID=new ArrayList<>();
        colors=new ArrayList<>();
        cal=new ArrayList<>();
        RecordCRUD database=new RecordCRUD(this);
        List<Record> init_list=database.getRecordList();
        int l=init_list.size();
        for(int i=0;i<l;i++){
            Record temp=init_list.get(i);
            mData.add(temp.image);
            colors.add(temp.color);
            cal.add(temp.calendar);
            times.add(temp.times);
            things.add(temp.name);
            ID.add(temp.id);

        }

    }
    private void update_times(int k){
        RecordCRUD database=new RecordCRUD(this);
        database.update(k);

        Date d = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date=sdf.format(d);
        RecordGraph rg=new RecordGraph();
        rg.record_id=k;
        long tt=  d.getTime()/1000;
        rg.date=  d.getTime()/1000;
        RecordGraphCRUD dbrg=new RecordGraphCRUD(this);
        int h =dbrg.insert(rg);
        int f=h;
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*public void enter(View view) {
        Intent intent =new Intent(MainActivity.this, login.class);
        startActivity(intent);
    }*/


}
