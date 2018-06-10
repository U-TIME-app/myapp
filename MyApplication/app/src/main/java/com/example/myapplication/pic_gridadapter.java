package com.example.myapplication;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class pic_gridadapter extends BaseAdapter{
    private Context context;
    private List<Integer> list;
    LayoutInflater layoutInflater;
    private ImageView mImageView,mImageView2;
    private TextView mText;

    public pic_gridadapter(Context context, List<Integer> list) {
        this.context = context;
        this.list = list;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.select_cal, null);
        mImageView = (ImageView) convertView.findViewById(R.id.item_image);
        mImageView.setBackgroundResource(list.get(position));
        return  convertView;
    }

}
