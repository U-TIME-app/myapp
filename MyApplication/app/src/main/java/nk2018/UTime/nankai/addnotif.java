package nk2018.UTime.nankai;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.UTime.nankai.R;

import nk2018.UTime.nankai.db.notifcontract;
import nk2018.UTime.nankai.db.notifdbhelper;

import java.util.Calendar;

/**
 * Created by Administrator on 2018/5/6 0006.
 */

public class addnotif extends Activity{
    int mYear, mMonth, mDay;
    private Button  backnotif;
    final int DATE_DIALOG = 1;
    EditText notiftitile;
    EditText notiftime;
    private notifdbhelper nHelper;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnotif);
        backnotif=findViewById(R.id.oknotifok);
        notiftitile=findViewById(R.id.notiftitle);
        notiftime=findViewById(R.id.notiftime);
        nHelper = new notifdbhelper(this);
        backnotif.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //Intent intent = new Intent(nextpage.this,MainActivity.class);
                //startActivity(intent);
                String notiftitle = String.valueOf(notiftitile.getText());
                String wholedate=String.valueOf(notiftime.getText());
                if(notiftitle.equals("")){
                    Toast.makeText(getApplicationContext(), "添加失败，内容不能为空！", Toast.LENGTH_SHORT).show();
                }else if(wholedate.equals("")){
                    Toast.makeText(getApplicationContext(), "添加失败，时间不能为空！", Toast.LENGTH_SHORT).show();
                }
                else{
                    SQLiteDatabase db = nHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    System.out.println("date:"+wholedate);
                    String splits[]=wholedate.split("-");
                    System.out.println("splits:"+splits);
                    int year=Integer.valueOf(splits[0]);
                    int month=Integer.valueOf(splits[1]);
                    int date=Integer.valueOf(splits[2]);
                    values.put(notifcontract.notifEntry.COL_NOTIF_TITLE, notiftitle);
                    values.put(notifcontract.notifEntry.COL_NOTIF_YEAR,year);
                    values.put(notifcontract.notifEntry.COL_NOTIF_MONTH,month);
                    values.put(notifcontract.notifEntry.COL_NOTIF_DAY,date);
                    values.put(notifcontract.notifEntry.COL_NOTIF_STATUS,"0");
                    db.insertWithOnConflict(notifcontract.notifEntry.TABLE,
                            null,
                            values,
                            SQLiteDatabase.CONFLICT_REPLACE);
                    db.close();
                    Intent refreshintent = new Intent();
                    refreshintent.setAction("refreshnotifUI");
                    sendBroadcast(refreshintent);
                    onBackPressed();
                }
            }
        });
        notiftime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //showDialog(DATE_DIALOG);
                new DatePickerDialog(addnotif.this, mdateListener, mYear, mMonth, mDay).show();
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
