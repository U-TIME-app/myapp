package nk2018.UTime.nankai;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.UTime.nankai.R;

import nk2018.UTime.nankai.database.Record;
import nk2018.UTime.nankai.database.RecordCRUD;
import nk2018.UTime.nankai.database.RecordGraph;
import nk2018.UTime.nankai.database.RecordGraphCRUD;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;

import static android.graphics.Color.parseColor;

public class ImageActivity extends AppCompatActivity {
    private LineChartView lineChart;
    private String[] datex =new String[7];//X轴的标注
    private int[] datay ;//X轴的标注
    private int[] score= {0,0,0,0,0,0,0};//图表的数据
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private List<AxisValue> mAxisYValues = new ArrayList<AxisValue>();

    private PieChartView chart;
    private PieChartData datapie;

    private boolean hasLabels = true;//是否在薄片上显示label
    private boolean hasLabelsOutside = false;//是否在薄片外显示label
    private boolean hasCenterCircle = false;//是否中间掏空一个圈
    private boolean isExploded = false;//薄片是否分离
    private boolean hasLabelForSelected = false;

    private String max_all="";
    private String min_all="";
    private String max7="";
    private String min7 = "";
    private TextView max7text;
    private TextView min7text;
    private TextView maxalltext;
    private TextView minalltext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Intent intent=getIntent();
        Bundle bundle=this.getIntent().getExtras();
        String ID=bundle.getString("ID");
        initx();
        initData(Integer.valueOf(ID));
        initY();
        lineChart = (LineChartView)findViewById(R.id.line_chart);
        getAxisXLables();//获取x轴的标注
        getAxisYLables();//获取y轴标注
        getAxisPoints();//获取坐标点
        initLineChart();//初始化

        chart = (PieChartView)findViewById(R.id.pie_chart);
        generateData();
        max7text=findViewById(R.id.max7_data);
        max7text.setText(max7);
        min7text=findViewById(R.id.min7_data);
        min7text.setText(min7);
        maxalltext=findViewById(R.id.all_max_data);
        maxalltext.setText(max_all);
        minalltext=findViewById(R.id.all_min_data);
        minalltext.setText(min_all);

    }

    /**
     * 设置饼图的数据
     */
    private void generateData()
    {

        RecordCRUD database=new RecordCRUD(this);
        List<Record> init_list=database.getRecordList();
        int numValues = init_list.size();
        List<SliceValue> values = new ArrayList<SliceValue>();
        String color[]={"#FFFF00","#B0E2FF","#CCFFCC","#CCCCFF","#FFCC99","#99CCFF","#6699CC","#FFFFCC","#CCFF66"};
        int tempmax=0;
        int tempmin=100000000;
        for (int i = 0; i < numValues; ++i)
        {
            SliceValue sliceValue = new SliceValue(
                    (float) init_list.get(i).times, parseColor(color[i]));
            sliceValue.setLabel(init_list.get(i).name);//设置label
            values.add(sliceValue);
            if(init_list.get(i).times>tempmax){
                tempmax=init_list.get(i).times;
                max_all=init_list.get(i).name;
            }
            if(init_list.get(i).times<tempmin){
                tempmin=init_list.get(i).times;
                min_all=init_list.get(i).name;
            }
        }

        datapie = new PieChartData(values);
        datapie.setHasLabels(hasLabels);
        datapie.setHasLabelsOnlyForSelected(hasLabelForSelected);
        datapie.setHasLabelsOutside(hasLabelsOutside);
        datapie.setHasCenterCircle(hasCenterCircle);

        if (isExploded)
        {
            datapie.setSlicesSpacing(24);//设置分离距离
        }


        chart.setPieChartData(datapie);
        //chart.setCircleFillRatio(0.5f);//设置放大缩小范围
    }

    /**
     * 设置x轴数值
     */
    private void initx(){

        for(int i=6;i>=0;i--) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 6+i);
            Date today = calendar.getTime();
            SimpleDateFormat format = new SimpleDateFormat("MM-dd");
            String result = format.format(today);
            datex[i]=result;
        }
    }

    /**
     * 设置y轴的数值
     */
    private void initY() {
        int temp = 6;
        for (int i = 0; i < 7; i++) {
            if (score[i] > temp) {
                temp = score[i];
            }
        }
        datay =new int[6*((temp/6)+1)];
        for(int i=0;i<6*((temp/6)+1);i++){
            datay[i] = i;//y轴的标注
        }
        return;
        /*if (temp == 100) {
            for(int i=0;i<100;i++){
                datay[i] = i;//y轴的标注
            }
            return;
        } else {
            int max = temp / 100 + 1;
            for(int i=1;i<=100;i++){
                datay[i]=max*i;
            }
            datay[6] = max * 6;
            datay[5] = max * 5;
            datay[4] = max * 4;
            datay[3] = max * 3;
            datay[2] = max * 2;
            datay[1] = max * 1;
            return;
        }*/
    }
    /**
     * 从数据库获取过去七天的打卡次数
     * 图中点的数值
     * @param k
     */
    private void initData(int k){
        RecordGraphCRUD db=new RecordGraphCRUD(this);
        ArrayList<RecordGraph> rgl=db.getRecordGraphList(k);
        if (rgl.size() > 0) {

            String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            int beg = rgl.size() - 1;
            int i = beg;
           // Date d0=new Date();
            Date d24=new Date();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String temp=year + "-" + datex[6] + " 00:00:00";
            int tempmax=0;
            int tempmin=100000;
            for (int j = 6; j >= 0 && i >= 0; j--) {
                 for (i = beg; i >= 0; i--) {
                    try {
                        //d0 = sdf.parse(year + "-" + date[j] + " 00:00:00");
                        d24 = sdf.parse(year + "-" + datex[j] + " 23:59:59");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long endt=d24.getTime()/1000 ;
                    long begt=  endt-24*60*60;

                    if (rgl.get(i).date >= begt&&rgl.get(i).date <= endt) {
                        score[j]=score[j]+1;
                    } else {
                        beg = i;
                        break;
                    }
                }
                if(score[j]>tempmax){
                     tempmax=score[j];
                     max7=datex[j];
                }
                if(score[j]<tempmin){
                    tempmin=score[j];
                    min7=datex[j];
                }
            }
        }
    }

    /**
     * 初始化LineChart的一些设置
     */
    private void initLineChart(){
        Line line = new Line(mPointValues).setColor(parseColor("#87CEFA"));  //折线的颜色
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        line.setCubic(false);//曲线是否平滑
	    line.setStrokeWidth(2);//线条的粗细，默认是3
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//		line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X轴下面坐标轴字体是斜的显示还是直的，true是斜的显示
//	    axisX.setTextColor(Color.WHITE);  //设置字体颜色
        axisX.setTextColor(parseColor("#D6D6D9"));//灰色

	    axisX.setName("最近7天打卡");  //表格名称
        axisX.setTextSize(11);//设置字体大小
        axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
//	    data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(false); //x 轴分割线


        Axis axisY = new Axis();  //Y轴
        axisY.setName("");//y轴标注
        axisY.setTextSize(11);//设置字体大小
        axisY.setMaxLabelChars(7);
        axisY.setValues(mAxisYValues);
        axisY.setHasLines(true);
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边
        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);  //缩放类型，水平
        lineChart.setMaxZoom((float) 3);//缩放比例
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);

        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right= 7;
        lineChart.setCurrentViewport(v);
    }

    /**
     * X 轴的显示
     */
    private void getAxisXLables(){
        for (int i = 0; i < datex.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(datex[i]));
        }
    }

    /**
     * Y 轴的显示
     */
    private void getAxisYLables(){
        for (int i = 0; i < datay.length; i++) {
            mAxisYValues.add(new AxisValue(i).setLabel(String.valueOf(datay[i])));
        }
    }
    /**
     * 数据点赋初值
     */
    private void getAxisPoints(){
        for (int i = 0; i < score.length; i++) {
            mPointValues.add(new PointValue(i, score[i]));
        }
    }
    /*
    返回上一页
     */
    public void back(View view){
        onBackPressed();
    }
}
