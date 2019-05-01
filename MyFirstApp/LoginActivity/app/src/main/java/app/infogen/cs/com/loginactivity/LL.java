package app.infogen.cs.com.loginactivity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class LL extends AppCompatActivity {

    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "manoj@example.com:manoj", "kumar@example.com:kumar"
    };

    private EditText llPassword;
    private EditText llUserName;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ll);

        EditText signup = (EditText) findViewById(R.id.signup);

        llPassword = (EditText) findViewById(R.id.pwd);

        llUserName = (EditText) findViewById(R.id.userName);

        login = (Button) findViewById(R.id.Login);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(LL.this, RLSignUp.class);
                startActivity(signup);
                finish();
            }
        });

        signup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Intent signup = new Intent(LL.this, RLSignUp.class);
                startActivity(signup);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loginAttempt()) {
                    Intent login = new Intent(LL.this, LoginSuccessActivity.class);
                    startActivity(login);
                    finish();
                } else {
                    messge();
                }

            }
        });

    }

    private void shareIt() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "AndroidSolved");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Now Learn Android with AndroidSolved clicke here to visit https://androidsolved.wordpress.com/");
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater menuInflater = new MenuInflater(this).inflate(1,menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.loginIcon:
                if (!loginAttempt()) {
                    Intent login = new Intent(LL.this, LoginSuccessActivity.class);
                    startActivity(login);
                    finish();
                } else {
                    messge();
                }
                return true;
            case R.id.share:
                shareIt();
                Toast.makeText(this, "Share Clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.setting:
                startActivity(new Intent(Settings.ACTION_SETTINGS));
                return true;
            case R.id.chats:
                PackageManager pm = this.getPackageManager();
                List<ApplicationInfo> pkg = pm.getInstalledApplications(PackageManager.GET_META_DATA);
                for (ApplicationInfo pkgInfo : pkg) {
                    Log.d("LL PKG INSTALL : ", "Launch Activity  CLASS NAME :" + pkgInfo.className);
                    Log.d("LL PKG INSTALL : ", "Launch Activity :" + pm.getLaunchIntentForPackage(pkgInfo.packageName));
                }
                Intent launch = new Intent(Intent.ACTION_MAIN);
                launch = pm.getLaunchIntentForPackage("com.google.android.talk");
                launch.addCategory(Intent.CATEGORY_LAUNCHER);
                startActivity(launch);
            case R.id.more:
                View pop = findViewById(R.id.shareID);
                PopupMenu popupMenu = new PopupMenu(this, pop);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return false;
                    }
                });
                popupMenu.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void messge() {
        Toast.makeText(this, "Please register with your email", LENGTH_SHORT).show();
    }


    private boolean loginAttempt() {

        llPassword.setError(null);
        llUserName.setError(null);

        String userName = llUserName.getText().toString();
        String password = llPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPassword(password)) {
            llPassword.setError(getString(R.string.error_invalid_password));
            focusView = llPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(userName)) {
            llUserName.setError(getString(R.string.error_field_required));
            focusView = llUserName;
            cancel = true;

        } else if (!TextUtils.isEmpty(userName) && !isEmailValid(userName, password)) {
            llUserName.setError(getString(R.string.error_field_required));
            focusView = llUserName;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }
        return cancel;
    }

    private boolean isEmailValid(String userName, String pwd) {

        boolean valid = true;

        if (userName.contains("@")) {
            valid = true;
        }

        if (userName.contains(".com")) {
            valid = true;
        }

        if (valid) {
            for (String credential : DUMMY_CREDENTIALS) {

                String[] usr = credential.split(":");

                if (usr[0].equals(userName)) {

                    return usr[1].equals(pwd) ? true : false;
                }
            }
        }
        return false;
    }

    private boolean isPassword(String password) {
        return password.length() > 4;
    }
}
