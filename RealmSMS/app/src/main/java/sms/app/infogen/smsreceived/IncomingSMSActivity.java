package sms.app.infogen.smsreceived;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import sms.app.infogen.cs.com.realmsms.R;

/**
 * Created by Dell on 3/11/2018.
 */

public class IncomingSMSActivity extends AppCompatActivity {

    @Override
    protected  void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_incoming_sms);
    }
}
