package app.infogen.cs.com.infogenfirstapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    private Properties properties;
    private AssetsPropertyReader assetsPropertyReader;
    private Context context;
    private TextView userName;
    private TextView password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this, "This is mainAcitivity", Toast.LENGTH_SHORT).show();

        /*
         This is the activity for the name and password
        */
        userName = (TextView) findViewById(R.id.userName);
        password = (TextView) findViewById(R.id.pwd);

        /*
        All the button for each activity
        */
        Button login = (Button) findViewById(R.id.mainLogin);
        Button second = (Button) findViewById(R.id.second_1);
        Button third = (Button) findViewById(R.id.third_1);
        Button fourth = (Button) findViewById(R.id.fourth_1);
        Button five = (Button) findViewById(R.id.five_1);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getLogin()) {
                    Intent basic = new Intent(MainActivity.this, Basic.class);
                    startActivity(basic);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Please enter the password", Toast.LENGTH_SHORT).show();
                    /*Intent basic = new Intent(MainActivity.this, Basic.class);
                    startActivity(basic);*/
                }
            }
        });

        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent second = new Intent(MainActivity.this, Basic.class);
                startActivity(second);
                 finish();
            }
        });

        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent third = new Intent(MainActivity.this, FullscreenActivity.class);
                startActivity(third);
                 finish();
            }
        });

        fourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fourth = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(fourth);
                  finish();
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent five = new Intent(MainActivity.this, MasterDetailsListActivity.class);
                startActivity(five);
                 finish();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(MainActivity.this, "This is onstart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(MainActivity.this, "This onResume", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(MainActivity.this, "This onPause", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(MainActivity.this, "This onStop", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(MainActivity.this, "This onRestart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(MainActivity.this, "This onDestory", Toast.LENGTH_SHORT).show();
    }

    private boolean getLogin() {
        context = this;
        assetsPropertyReader = new AssetsPropertyReader(context);
        properties = assetsPropertyReader.getProperties("config.properties");

        Log.i("MainActivity", "userName " + properties.getProperty("USERNAME") + " : " + userName.getText());
        Log.i("MainActivity", "Password " + properties.getProperty("PWD") + " : " + password.getText().toString());
        View focusView = null;
        boolean cancel = false;

        if (TextUtils.isEmpty(userName.getText().toString())) {
            userName.setError(getString(R.string.error_field_required));
            userName.requestFocus();
            focusView = userName;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }

        String uName = (String) properties.getProperty("USERNAME");
        String pwd = (String) properties.getProperty("PWD");

        if (TextUtils.isEmpty(userName.getText()) && userName.getText() != null && userName.getText().equals(uName) && TextUtils.isEmpty(password.getText())&& password.getText() != null) {
            String enteredPwd = password.getText().toString();
            if (TextUtils.isEmpty(enteredPwd) && enteredPwd.equals(pwd)) {
                return true;
            }
        }
         return false;
    }
}


