package com.example.mpermssionproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends GetPermssion {

    //Our button
    private Button buttonRequestPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initializing button
        buttonRequestPermission = (Button) findViewById(R.id.buttonRequestPermission);
        buttonRequestPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        requestAppPermssion(this);
    }
}