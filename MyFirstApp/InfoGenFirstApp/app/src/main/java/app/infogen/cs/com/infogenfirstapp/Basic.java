package app.infogen.cs.com.infogenfirstapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.Manifest.permission.READ_CONTACTS;

public class Basic extends AppCompatActivity {
    private static final int REQUEST_READ_CONTACTS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        Button next = (Button) findViewById(R.id.nextBasic);

        Button five = (Button) findViewById(R.id.five_2);
        Button first = (Button) findViewById(R.id.first_2);
        Button third = (Button) findViewById(R.id.third_2);
        Button fourth = (Button) findViewById(R.id.fourth_2);


        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        mayRequestContacts();
     //   getContactDetails();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                 Intent nextBasic = new Intent(Basic.this, FullscreenActivity.class);
                //nextBasic.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(nextBasic);
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextBasic = new Intent(Basic.this, FullscreenActivity.class);
              //  nextBasic.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(nextBasic);
                finish();
            }
        });


        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent first = new Intent(Basic.this,MainActivity.class);
                startActivity(first);
                finish();
            }
        });
        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent third = new Intent(Basic.this,FullscreenActivity.class);
                startActivity(third);
                finish();
            }
        });

        fourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fourth = new Intent(Basic.this,LoginActivity.class);
                startActivity(fourth);
                finish();
            }
        });
    }

    private void getContactDetails(){
        Log.i("Basic Contact : " , "getContactDetails");
        Cursor listCursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,null,null,null);
        while(listCursor.moveToNext()){
            Log.i("Basic Contact : ",listCursor.getString(listCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
            String contactID = listCursor.getString(listCursor.getColumnIndex(ContactsContract.Contacts._ID));
            Log.i("Basic contactID : ",contactID);
            String hasPhone = listCursor.getString(listCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            Log.i("Basic hasPhone : ",hasPhone);
            if(Integer.parseInt(hasPhone) != -1){
                Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID + '=' + contactID,null,null);
                if (phoneCursor !=null) {
                    while (phoneCursor.moveToNext()) {
                        Log.i("Basic Contact : ", phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    }
                    phoneCursor.close();
                }
            }
        }
        listCursor.close();
    }

    private boolean mayRequestContacts(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},REQUEST_READ_CONTACTS);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted to access location", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}
