package com.manoj.peoplechat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.manoj.model.User;

import org.apache.commons.lang3.StringUtils;

import timber.log.Timber;

import static android.support.design.widget.Snackbar.LENGTH_LONG;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    EditText phone;
    EditText password;
    Button btn_login;
    TextView signupLink;

    View contextView;

    private DatabaseReference auth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        signupLink = findViewById(R.id.link_signup);
        contextView = findViewById(R.id.relativeLayout);

        getFirebaseConnection();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                intent.putExtra("phoneNo", phone.getText().toString());
                intent.putExtra("password", password.getText().toString());
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    private void getFirebaseConnection() {
        auth = FirebaseDatabase.getInstance().getReference("Messanger").child("loginUser");
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        btn_login.setEnabled(false);


        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        //progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        if (onLoginSuccess()) {
                            progressDialog.dismiss();
                        } else {
                            onLoginFailed();
                            progressDialog.dismiss();
                        }
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // By default we just finish the Activity and log them in automatically
                phone.setText(data.getStringExtra("phoneNo"));
                password.setText(data.getStringExtra("password"));

                Log.d(TAG, "Phone number :" + phone.getText().toString());
                Log.d(TAG, "password :" + password.getText().toString());

                //this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public boolean onLoginSuccess() {
        btn_login.setEnabled(true);
        return callMessageActivity();
    }

    private boolean callMessageActivity() {
        final String txt_phone = phone.getText().toString();
        final String txt_password = password.getText().toString();
        final boolean[] existsFlag = {true};

        Log.i(TAG, "Login User Details Phone :" + txt_phone);
        if (!txt_phone.equals("")) {
            Query query = auth.orderByChild("phoneNo").equalTo(StringUtils.trim(txt_phone)).limitToFirst(1);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User exisitngUser = null;
                    String key = null;
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            exisitngUser = snapshot.getValue(User.class);
                            key = snapshot.getKey();
                        }
                        Log.d(TAG, "Exisiting User" + exisitngUser.toString());
                        if (exisitngUser != null && (exisitngUser.getPhoneNo() != null && exisitngUser.getPhoneNo().equalsIgnoreCase(txt_phone) &&
                                (exisitngUser.getPassword() != null && exisitngUser.getPassword().equalsIgnoreCase(txt_password)))) {

                            Log.d(TAG, "The message has been see " + key);
                            auth.child(key).child("status").setValue("online");

                            Intent intent = new Intent(LoginActivity.this, UserAndChatsActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("phoneNo", txt_phone);
                            intent.putExtra("userName", exisitngUser.getUsername());
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG);
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "Login details don't exists", Toast.LENGTH_LONG);
                        existsFlag[0] = false;
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                }
            });

        }
        if (!existsFlag[0])
            return false;
        else
            return true;
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        btn_login.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String txt_phone = phone.getText().toString();
        String txt_password = password.getText().toString();

        Timber.d("Paswword %s", txt_password);
        Log.d(TAG, "Password " + txt_password);

        if (txt_phone.isEmpty() || !android.util.Patterns.PHONE.matcher(txt_phone).matches()) {
            phone.setError("enter a valid phone number");
            valid = false;
        } else {
            phone.setError(null);
        }

        if (txt_password.isEmpty() || txt_password.length() < 4 || txt_password.length() > 10) {
            password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            password.setError(null);
        }
        return valid;
    }
}
