package com.example.myfirstapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Dell on 1/1/2017.
 */
public abstract  class AbsRuntinePermssion extends Activity {
    MusicIntentReceiver musicIntentReceiver;
    private Object readAllSms;
    private SparseIntArray mErrorString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mErrorString = new SparseIntArray();
        musicIntentReceiver = new MusicIntentReceiver();
    }

    public abstract List<String> getReadAllSms();

    public  abstract  void onPermssionGranted(int requestCode);

    public  void requestAppPermssion(final  String[] requestPermssion ,final  int stringID, final int requestCode){
        mErrorString.put(requestCode,stringID);
        int permssionCheck = PackageManager.PERMISSION_GRANTED;
        boolean aboveRequestPermssion = false;
        for (String permssion: requestPermssion){
            permssionCheck = permssionCheck + ContextCompat.checkSelfPermission(this,permssion);
            aboveRequestPermssion =aboveRequestPermssion || ActivityCompat.shouldShowRequestPermissionRationale(this, permssion);
        }
        if (permssionCheck !=PackageManager.PERMISSION_GRANTED){
            if (aboveRequestPermssion){
                Snackbar.make(findViewById(android.R.id.content),stringID,Snackbar.LENGTH_INDEFINITE).setAction("GRANT", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(AbsRuntinePermssion.this, requestPermssion, requestCode);
                    }
                }).show();
            }else {
                Toast.makeText(getApplicationContext(),"Permssion Not given", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this,requestPermssion,requestCode);
            }
        }else {
            onPermssionGranted(requestCode);
        }

    }

    @Override
    public void  onRequestPermissionsResult(int requestCode, String[] permssions, int [] grantResult){
       super.onRequestPermissionsResult(requestCode,permssions,grantResult);
        int permssionCheck = PackageManager.PERMISSION_GRANTED;

        for (int permssion: grantResult){
             permssionCheck = permssionCheck +permssion;
        }
        if (grantResult.length > 0 && PackageManager.PERMISSION_GRANTED == permssionCheck){
            onPermssionGranted(requestCode);
        }else{
            Snackbar.make(findViewById(android.R.id.content),mErrorString.get(requestCode),Snackbar.LENGTH_INDEFINITE).setAction("ENABLE", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent();
                    i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    i.setData(Uri.parse("package:" + getPackageName()));
                    i.addCategory(Intent.CATEGORY_DEFAULT);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(i);
                }
            }).show();
        }
    }
}
