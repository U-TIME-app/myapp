package nk2018.UTime.nankai;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.UTime.nankai.R;

import nk2018.UTime.nankai.db.notifcontract;
import nk2018.UTime.nankai.db.notifdbhelper;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by Administrator on 2018/5/7 0007.
 */

public class modifynotif extends Activity{
    int mYear, mMonth, mDay;
    private Button backnotif;
    final int DATE_DIALOG = 1;
    EditText notiftitle;
    EditText notiftime;
    TextView notifleftday;
    private notifdbhelper nHelper;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifynotif);
        RelativeLayout modview = (RelativeLayout) findViewById(R.id.modifyview);

        int array[] = { R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4};
        Random rnd = new Random();
        int index = rnd.nextInt(4);
        Resources resources = getBaseContext().getResources();
        Drawable cur = resources.getDrawable(array[index]);
        modview.setBackgroundDrawable(cur);
        backnotif=findViewById(R.id.oknotifok);
        notiftitle=findViewById(R.id.notiftitle);
        notiftime=findViewById(R.id.notiftime);
        notifleftday=findViewById(R.id.lefttime);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        final String id = intent.getStringExtra("id");
        String date = intent.getStringExtra("date");
        String leftday=intent.getStringExtra("leftday");
        notiftitle.setText(title);
        notiftime.setText(date);
        notifleftday.setText("还有"+leftday+"天");
        nHelper = new notifdbhelper(this);
        backnotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //Intent intent = new Intent(nextpage.this,MainActivity.class);
                //startActivity(intent);
                String nnotiftitle = String.valueOf(notiftitle.getText());
                SQLiteDatabase db = nHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                String wholedate=String.valueOf(notiftime.getText());
                if(nnotiftitle.equals("")){
                    Toast.makeText(getApplicationContext(), "修改失败，内容不能为空！", Toast.LENGTH_SHORT).show();
                }else if(wholedate.equals("")){
                    Toast.makeText(getApplicationContext(), "修改失败，时间不能为空！", Toast.LENGTH_SHORT).show();
                }
                else{
                    String splits[]=wholedate.split("-");
                    int year=Integer.valueOf(splits[0]);
                    int month=Integer.valueOf(splits[1]);
                    int date=Integer.valueOf(splits[2]);
                    values.put(notifcontract.notifEntry.COL_NOTIF_TITLE, nnotiftitle);
                    values.put(notifcontract.notifEntry.COL_NOTIF_YEAR,year);
                    values.put(notifcontract.notifEntry.COL_NOTIF_MONTH,month);
                    values.put(notifcontract.notifEntry.COL_NOTIF_DAY,date);
                    db.updateWithOnConflict(notifcontract.notifEntry.TABLE,
                            values,
                            notifcontract.notifEntry._ID + " = ?",
                            new String[]{id},
                            SQLiteDatabase.CONFLICT_REPLACE
                    );
                    db.close();
                    Intent refreshintent = new Intent();
                    refreshintent.setAction("refreshnotifUI");
                    sendBroadcast(refreshintent);
                    onBackPressed();
                }
            }
        });
        notiftime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showDialog(DATE_DIALOG);
                new DatePickerDialog(modifynotif.this, mdateListener, mYear, mMonth, mDay).show();
            }
        });

        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
        }
        return null;
    }

    public void display() {
        notiftime.setText(new StringBuffer().append(mYear).append("-").append(mMonth+1).append("-").append(mDay));
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            display();
        }
    };
}
