package com.manoj.peoplechat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.manoj.model.User;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    EditText _nameText;
    EditText _emailText, _input_mobile;
    EditText _passwordText;
    Button _signupButton;
    TextView _loginLink;

    private DatabaseReference addUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        _nameText = findViewById(R.id.input_name);
        _emailText = findViewById(R.id.input_email);
        _input_mobile = findViewById(R.id.input_mobile);
        _passwordText = findViewById(R.id.input_password);
        _loginLink = findViewById(R.id.link_login);
        _signupButton = findViewById(R.id.btn_signup);


        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this, R.style.Theme_AppCompat_DayNight_DarkActionBar);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String name = _nameText.getText().toString();
        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();
        final String mobile = _input_mobile.getText().toString();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed depending on success
                        if (dbUpdateToFireBase(name, email, password, mobile)) {
                            Log.d(TAG, "Db Update completed");
                            onSignupSuccess();
                            progressDialog.dismiss();
                        } else {
                            Log.d(TAG, "It has failed");
                            onSignupFailed();
                            progressDialog.dismiss();
                        }
                    }
                }, 3000);
    }

    private boolean dbUpdateToFireBase(String name, String email, String password, String mobile) {
        addUser = FirebaseDatabase.getInstance().getReference("Messanger");
        User user = new User(email, name, password, mobile, "default", "offline", null);
        Log.d(TAG, "Adding the users " + user.toString());
        addUser.child("loginUser").push().setValue(user);
        return true;
    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        Intent intent = new Intent();

        Log.d(TAG, "Phone number :" + _input_mobile.getText().toString());
        Log.d(TAG, "password :" + _passwordText.getText().toString());

        intent.putExtra("password", _passwordText.getText().toString());
        intent.putExtra("phoneNo", _input_mobile.getText().toString());

        setResult(RESULT_OK, intent);

        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}

