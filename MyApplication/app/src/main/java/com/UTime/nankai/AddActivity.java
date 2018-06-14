package com.UTime.nankai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import com.UTime.nankai.database.Record;
import com.UTime.nankai.database.RecordCRUD;


public class AddActivity extends AppCompatActivity {
    private EditText NameView;
    //private EditText ColorView;
    private int colorid,imgid,cioid;
    private View mAddView;
    private Spinner ImageSpinner;
    private ArrayList<Integer> mDatas;
    private GridView mGridView;
    private ImageView show_image,cal_img;
    private TextView color_name;
    private pic_gridadapter adapter;

    private int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_item);
        NameView = (EditText) findViewById(R.id.add_name);
        mGridView = (GridView) findViewById(R.id.pic_list);
        show_image=(ImageView)findViewById(R.id.imageView);
        cal_img=(ImageView)findViewById(R.id.cal_view);
        color_name=(TextView)findViewById(R.id.textView);
        imgid=R.drawable.r1;
        show_image.setBackgroundResource(imgid);
        colorid=R.color.bg;
        cioid=R.drawable.bc;
        cal_img.setBackgroundResource(R.drawable.bc);
        color_name.setText("蓝色");
        color_name.setBackgroundColor(getResources().getColor(R.color.bg));
        color_name.setTextColor(getResources().getColor(R.color.wihte));
        initDatas();
        adapter = new pic_gridadapter(AddActivity.this, mDatas);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                             @Override
                                             public void onItemClick(AdapterView<?> parent, View view,
                                                                     int position, long id) {
                                                 show_image.setBackgroundResource(mDatas.get(position));
                                                 imgid=mDatas.get(position);
                                                 int color=setcolor(position);
                                                 cioid=color;
                                                 cal_img.setBackgroundResource(color);
                                                 setText(color);

                                             }

                                         }
        );
        NameView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    check();
                    return true;
                }
                return false;
            }
        });

//        ColorView = (EditText) findViewById(R.id.add_color);
//        ColorView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
//                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
//                    check();
//                    return true;
//                }
//                return false;
//            }
//        });


        final Button AddButton = (Button) findViewById(R.id.record_add_button);
        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toAddRecord();
            }
        });

        //mAddView=findViewById(R.id.record_basic);
    }
    private boolean check(){
        NameView.setError(null);
        //ColorView.setError(null);
        View focusView=null;
        String name=NameView.getText().toString();
        //String color=ColorView.getText().toString();
        boolean cancel=false;
        RecordCRUD rcrud=new RecordCRUD(this);
        Record record=new Record();
        record.name=name;
        if(TextUtils.isEmpty(name)){
            NameView.setError(getString(R.string.name_empty));
            focusView=NameView;
            cancel=true;
        }

        if(rcrud.getByName(name)!=null){
            NameView.setError(getString(R.string.twice));
            cancel=true;
        }
        if(cancel){
            focusView.requestFocus();
        }
        return  cancel;
    }
    private void toAddRecord(){
        boolean result=check();
        if(!result){
            String name=NameView.getText().toString();
            RecordCRUD rcrud=new RecordCRUD(this);
            Record record=new Record();
            record.name=name;
            record.color=colorid;
            record.times=0;
            record.calendar=cioid;
            record.image=imgid;
            rcrud.insert(record);
            //Intent intent=new Intent(AddActivity.this,MainActivity.class);
            //startActivity(intent);
            Intent refreshintent = new Intent();
            refreshintent.setAction("addactivity");
            sendBroadcast(refreshintent);
            onBackPressed();

        }

    }
    private int setcolor(int picture_id){
        if(picture_id<7||picture_id==24)
            return R.drawable.gbc;
        else if(picture_id <11||(picture_id>19&&picture_id<22)||picture_id==23)
            return R.drawable.gc;
        else if (picture_id <13)
            return R.drawable.bc;
        else if (picture_id<15)
            return R.drawable.pc;
        else if(picture_id<18)
            return R.drawable.yc;
        else if(picture_id<19)
            return R.drawable.pc;
        else return R.drawable.pc;

    }
    private void  setText(int cal_id){
        if(cal_id==R.drawable.gbc){
            color_name.setText("淡蓝色");
            color_name.setBackgroundColor(getResources().getColor(R.color.bg));
            colorid=R.color.bg;
        }
        else if(cal_id==R.drawable.gc){
            color_name.setText("绿色");
            color_name.setBackgroundColor(getResources().getColor(R.color.green));
            colorid=R.color.green;
        }
        else if(cal_id==R.drawable.bc){
            color_name.setText("蓝色");
            color_name.setBackgroundColor(getResources().getColor(R.color.blue));
            colorid=R.color.blue;
        }
        else if(cal_id==R.drawable.pc){
            color_name.setText("紫色");
            color_name.setBackgroundColor(getResources().getColor(R.color.purple));
            colorid=R.color.purple;
        }
        else if(cal_id==R.drawable.yc){
            color_name.setText("黄色");
            color_name.setBackgroundColor(getResources().getColor(R.color.yellow));
            colorid=R.color.yellow;
        }
        else {
            color_name.setText("淡蓝色");
            color_name.setBackgroundColor(getResources().getColor(R.color.bg));
            colorid=R.color.bg;
        }
        color_name.setTextColor(getResources().getColor(R.color.wihte));
    }

    private void initDatas() {
        mDatas = new ArrayList<>();
        mDatas.add(R.drawable.r1);
        mDatas.add(R.drawable.r2);
        mDatas.add(R.drawable.r3);
        mDatas.add(R.drawable.r4);
        mDatas.add(R.drawable.r5);
        mDatas.add(R.drawable.r6);
        mDatas.add(R.drawable.r7);
        mDatas.add(R.drawable.r8);
        mDatas.add(R.drawable.r9);
        mDatas.add(R.drawable.r10);
        mDatas.add(R.drawable.r11);
        mDatas.add(R.drawable.r12);
        mDatas.add(R.drawable.r13);
        mDatas.add(R.drawable.r14);
        mDatas.add(R.drawable.r15);
        mDatas.add(R.drawable.r16);
        mDatas.add(R.drawable.r17);
        mDatas.add(R.drawable.r18);
        mDatas.add(R.drawable.r19);
        mDatas.add(R.drawable.r20);
        mDatas.add(R.drawable.r21);
        mDatas.add(R.drawable.r22);
        mDatas.add(R.drawable.r23);
        mDatas.add(R.drawable.r24);
        mDatas.add(R.drawable.r25);
    }


}
