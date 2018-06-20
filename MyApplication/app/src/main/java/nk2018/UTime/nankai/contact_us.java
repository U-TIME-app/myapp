package nk2018.UTime.nankai;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.UTime.nankai.R;

/**
 * Created by chen on 2018/5/8.
 */

public class contact_us extends Activity {
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v) {
                                        finish();

                                    }
                                }
        );
    }
}

