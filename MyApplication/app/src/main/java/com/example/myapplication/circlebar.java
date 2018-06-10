package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by dongjie on 2018/4/20.
 */

public class circlebar extends View {
    private int circlewidth=8;
    private int ballwidth=4;
    private int textwidth=2;
    private double angle=0;
    private int radius_ball=20;
    private int maxpro=90*60,minpro=10;
    private final RectF mRectF;
    private final Paint mPaint;
    private final Context mContext;
    private String text="test";
    private Canvas init_canvas;
    public circlebar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mRectF = new RectF();
        mPaint = new Paint();
    }
    public void setminpro(int time){
        this.minpro=time;
        this.text="";
        this.text="";
        this.text=this.text+ Integer.toString(this.minpro/600)+Integer.toString(this.minpro/60%10)+":"+Integer.toString(this.minpro%60/10)+Integer.toString(this.minpro%10);
    }
    public void update(){
        this.minpro=this.minpro-1;
        this.text="";
        this.text=this.text+ Integer.toString(this.minpro/600)+Integer.toString(this.minpro/60%10)+":"+Integer.toString(this.minpro%60/10)+Integer.toString(this.minpro%10);
        this.postInvalidate();
    }
    void setpen(){
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.rgb(0xe9, 0xe9, 0xe9));
        mPaint.setStrokeWidth(circlewidth);
        mPaint.setStyle(Paint.Style.STROKE);
    }
    void drawcircle(int width,int height){
        mRectF.left = circlewidth/ 2; // 左上角x
        mRectF.top = circlewidth / 2; // 左上角y
        mRectF.right = width - circlewidth / 2; // 左下角x
        mRectF.bottom = height - circlewidth / 2; // 右下角y
        mPaint.setColor(getResources().getColor(R.color.notfinish));
        this.init_canvas.drawArc(mRectF, -90, 360, false, mPaint);
        mPaint.setColor(getResources().getColor(R.color.ballcolor));
        this.init_canvas.drawArc(mRectF, -90, ((float) minpro / maxpro) * 360, false, mPaint);
        drawball(width,height);
    }
    void drawball(int width,int height){
        double cx=width/2.0+Math.sin(angle)*(width/2-radius_ball-ballwidth);
        double cy=height/2.0-Math.cos(angle)*(width/2.0-radius_ball-ballwidth);
        this.mPaint.setColor(getResources().getColor(R.color.ballcolor));
        mPaint.setStyle(Paint.Style.FILL);
        this.init_canvas.drawCircle((float)cx,(float)cy,this.radius_ball,this.mPaint);
    }
    void updateball(){
        this.angle=this.angle+Math.PI/250;
        this.postInvalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = this.getWidth();
        int height = this.getHeight();
        if (width != height) {
            int min = Math.min(width, height);
            width = min;
            height = min;
        }
        canvas.drawColor(Color.TRANSPARENT);
        //canvas.drawColor(Color.WHITE);
        this.init_canvas=canvas;
        mPaint.setColor(getResources().getColor(R.color.colorText));
        mPaint.setStyle(Paint.Style.FILL);
        this.init_canvas.drawArc(mRectF, -90, 360, false, mPaint);
        setpen();
        drawcircle(width,height);
        mPaint.setStrokeWidth(textwidth);
        int textHeight = height / 4;
        mPaint.setTextSize(textHeight);
        int textWidth = (int) mPaint.measureText(text, 0, text.length());
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(text, width / 2 - textWidth / 2, height / 2 + textHeight / 2, mPaint);

    }

}
