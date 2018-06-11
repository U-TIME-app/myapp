package com.example.myapplication;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.database.Record;
import com.example.myapplication.database.RecordCRUD;

import static android.content.ContentValues.TAG;

public class gridadapter extends BaseAdapter{
    private Context context;
    private RecordCRUD database;
    private List<Integer> list;
    private List<String> things;
    private List<Integer> times;
    private List<Integer> ID,cal,colors;
    private boolean iShowdDelete;
    LayoutInflater layoutInflater;
    private ImageView mImageView,mImageView2,mImageView3;
    private TextView mText;

    public gridadapter(Context context, ArrayList<Integer> list,ArrayList<Integer> ID,ArrayList<String> things,ArrayList<Integer>times,ArrayList<Integer>cal,ArrayList<Integer>colors,boolean isshow) {
        this.context = context;
        this.list=list;
        this.ID=ID;
        this.things=things;
        this.colors=colors;
        this.cal=cal;
        this.times=times;
        this.iShowdDelete=isshow;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();//注意此处
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (position < list.size()) {
            convertView = layoutInflater.inflate(R.layout.card_item, null);
            mImageView = (ImageView) convertView.findViewById(R.id.item_image);
            mImageView2 = (ImageView) convertView.findViewById(R.id.cal_img);
            mImageView3 = (ImageView) convertView.findViewById(R.id.delete_markView);
            mText = (TextView) convertView.findViewById(R.id.textView);
            mImageView.setBackgroundResource(list.get(position));
            mImageView2.setBackgroundResource(cal.get(position));
            mImageView2.setOnClickListener( new View.OnClickListener(){
                    @Override
            public void onClick(View view) {
                        Intent intent = new Intent(context,ImageActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("ID",String.valueOf(ID.get(position)));
                        int k=ID.get(position);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
            }
            }
            );
            mImageView3.setOnClickListener( new View.OnClickListener(){
                                                @Override
                                                public void onClick(View view) {
                                                    RecordCRUD database=new RecordCRUD(context);
                                                    database.delete(ID.get(position));
                                                    ID.remove(position);
                                                    things.remove(position);
                                                    colors.remove(position);
                                                    cal.remove(position);
                                                    list.remove(position);
                                                    times.remove(position);
                                                    notifyDataSetChanged();

                                                }
                                            }
            );
            mImageView3.setVisibility(iShowdDelete ? View.VISIBLE : View.GONE);
            mText.setBackgroundColor(context.getResources().getColor(colors.get(position)));
            SpannableStringBuilder sb = new SpannableStringBuilder(things.get(position)+"+"+times.get(position));
            int length_start=things.get(position).length();
            int length_end=("+"+times.get(position)).length()+length_start;
            StyleSpan bss1 = new StyleSpan(Typeface.ITALIC); // 设置字体样式
            StyleSpan bss2 = new StyleSpan(Typeface.BOLD); // 设置字体样式
            AbsoluteSizeSpan ass = new AbsoluteSizeSpan(50);  // 设置字体大小
            sb.setSpan(bss1, length_start, length_end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            sb.setSpan(bss2, length_start, length_end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            sb.setSpan(ass, length_start, length_end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            mText.setText(sb);
        }
        return convertView;
    }

}
