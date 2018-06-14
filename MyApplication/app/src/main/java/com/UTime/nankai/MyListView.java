package com.UTime.nankai;

import android.content.Context;
import android.widget.ListView;

/**
 * Created by Administrator on 2018/5/11 0011.
 */

public class MyListView extends ListView
{

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context,android.util.AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, mExpandSpec);
    }

}