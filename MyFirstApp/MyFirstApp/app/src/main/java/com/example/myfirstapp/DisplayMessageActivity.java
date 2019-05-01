package com.example.myfirstapp;

import android.content.Intent;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity {
    private static final String TAG = "DisplayMessageActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        if (intent.getStringExtra(MainActivity.EXTRA_MESSAGE) instanceof  String) {

            String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
            if (message !=null) {
                TextView textView = new TextView(this);
                textView.setTextSize(40);
                textView.setText(message);
                ViewGroup layout = (ViewGroup) findViewById(R.id.activity_display_message);
                layout.addView(textView);
            }
        }else {
            TextView textView = new TextView(this);
            textView.setTextSize(40);

            int chargePlug = intent.getIntExtra(MainActivity.EXTRA_INT,-1);
            int smsCount = intent.getIntExtra(MainActivity.SMS_COUNT,-1);
            Log.i(TAG,"smsCount:" + smsCount);
            boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
            boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
            BatteryManager bm = (BatteryManager)getSystemService(BATTERY_SERVICE);
            int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            if (usbCharge){
                textView.setText("Usb Charging " + batLevel + "%");
            }else if (acCharge) {
                textView.setText("AC Charging " + batLevel + "%");
            }else {
                StringBuilder text = new StringBuilder();
                text.append("Not Charging").append(":").append("SMS COUNT : ").append(smsCount);
                textView.setText(text);
            }
            ViewGroup layout = (ViewGroup) findViewById(R.id.activity_display_message);
            layout.addView(textView);
        }
    }
}
