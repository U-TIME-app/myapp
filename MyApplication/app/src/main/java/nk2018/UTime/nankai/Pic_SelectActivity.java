package nk2018.UTime.nankai;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.UTime.nankai.R;

import java.util.ArrayList;


public class Pic_SelectActivity extends AppCompatActivity {
    private ArrayList<Integer> mDatas;
    private GridView mGridView;
    private ImageView show_image,cal_img;
    private TextView color_name;
    private pic_gridadapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGridView = (GridView) findViewById(R.id.pic_list);
        show_image=(ImageView)findViewById(R.id.imageView);
        cal_img=(ImageView)findViewById(R.id.cal_view);
        color_name=(TextView)findViewById(R.id.textView);
        show_image.setBackgroundResource(R.drawable.r1);
        cal_img.setBackgroundResource(R.drawable.bc);
        color_name.setText("蓝色");
        color_name.setBackgroundColor(getResources().getColor(R.color.bg));
        color_name.setTextColor(getResources().getColor(R.color.wihte));
        initDatas();
        adapter = new pic_gridadapter(Pic_SelectActivity.this, mDatas);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                show_image.setBackgroundResource(mDatas.get(position));
                int color=setcolor(position);
                cal_img.setBackgroundResource(color);
                setText(color);

                }

            }
        );
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
        }
        else if(cal_id==R.drawable.gc){
            color_name.setText("绿色");
            color_name.setBackgroundColor(getResources().getColor(R.color.green));
        }
        else if(cal_id==R.drawable.bc){
            color_name.setText("蓝色");
            color_name.setBackgroundColor(getResources().getColor(R.color.blue));
        }
        else if(cal_id==R.drawable.pc){
            color_name.setText("紫色");
            color_name.setBackgroundColor(getResources().getColor(R.color.purple));
        }
        else if(cal_id==R.drawable.yc){
            color_name.setText("黄色");
            color_name.setBackgroundColor(getResources().getColor(R.color.yellow));
        }
        else {
            color_name.setText("淡蓝色");
            color_name.setBackgroundColor(getResources().getColor(R.color.bg));
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
