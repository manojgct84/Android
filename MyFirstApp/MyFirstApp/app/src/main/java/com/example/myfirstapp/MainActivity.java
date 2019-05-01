package com.example.myfirstapp;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AndroidException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends GetPermssion {

    public static final  String EXTRA_MESSAGE ="com.example.myfirstapp.MESSAGE";
    public static final  String EXTRA_INT ="com.example.myfirstapp.INT";
    public static final  String SMS_COUNT ="com.example.myfirstapp.COUNT";
    public static  final  int REQUEST_PERMSSION =10;
  //  private static Context context;
    private static final String TAG = "Battery_Status";
    //private GoogleApiClient mGoogleApiClient;
    MusicIntentReceiver musicIntentReceiver;

    IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    //Our button
    private Button buttonRequestPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       //requestAppPermssion(new String[] {Manifest.permission.READ_SMS},R.string.permssion,REQUEST_PERMSSION);

      //  getReadAllSms();
        musicIntentReceiver = new MusicIntentReceiver();

        buttonRequestPermission = (Button) findViewById(R.id.buttonRequestPermission);
        buttonRequestPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"buttonRequestPermission");
                requestAppPermssion();
            }
        });
    }


  /*  @Override
    protected void onStart()
    {
                registerReceiver(musicIntentReceiver,new IntentFilter(AudioManager.ACTION_HEADSET_PLUG));
        super.onStart();
    }*/


   public void sendMessage(View view){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        if (message !=null) {
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
    }

    public void showBattery(View view){

        Intent intent = new Intent(this, DisplayMessageActivity.class);
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        IntentFilter headSetPlugin = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        view.getContext().registerReceiver(musicIntentReceiver, headSetPlugin);

        Intent batteryStatus = view.getContext().registerReceiver(null, ifilter);
        Log.i(TAG,"Batery Life:" + batteryStatus);
        // Are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        Log.i(TAG,"status:" + status);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||  status == BatteryManager.BATTERY_STATUS_FULL;
        Log.i(TAG,"isCharging:" + isCharging);

        // How are we charging?
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        Log.i(TAG,"chargePlug:" + chargePlug);
        intent.putExtra(EXTRA_INT,chargePlug);
        startActivity(intent);
        //musicIntentReceiver.onReceive(view.getContext(),intent );
    }


    public List<String> getReadAllSms() {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        List<String> smsBody = new ArrayList<String>();
        String columns[] = {"person", "address", "body", "date", "status"};
        String sortOrder = "date ASC";
        int hasSmsReadPermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS);
        if (hasSmsReadPermission == PackageManager.PERMISSION_GRANTED) {
            Uri smsUri = Uri.parse("content://sms/inbox");
            Cursor getSms = getContentResolver().query(smsUri, columns, null, null, sortOrder);
            intent.putExtra(SMS_COUNT, getSms.getCount());
            while (getSms.moveToNext()) {
                //smsBody.add(getSms.getString(getSms.getColumnIndexOrThrow("address")));
                smsBody.add(getSms.getString(getSms.getColumnIndexOrThrow("body")));
            }
        }else {
            Log.i(TAG, "hasSmsReadPermission :" + hasSmsReadPermission);
        }
            Log.i(TAG, "SMS Body:" + smsBody);
            return smsBody;
        }

    @Override
    public void onPermssionGranted() {
        Toast.makeText(getApplicationContext(),"Permssion Granted", Toast.LENGTH_LONG).show();
        getReadAllSms();

    }

  /*  private void startPlayback() {
        registerReceiver(musicIntentReceiver, intentFilter);
    }*/

 /*   @Override
    protected void onStop()
    {
        unregisterReceiver(musicIntentReceiver);
        super.onStop();
    }*/
}
