package app.infogen.cs.com.loginactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_SHORT;

public class RL extends AppCompatActivity {

    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "manoj@example.com:manoj", "kumar@example.com:kumar"
    };

    private EditText rlPassword;
    private EditText rlUserName;
    private Button Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rl);

        EditText signup = (EditText) findViewById(R.id.signup);

        rlPassword = (EditText) findViewById(R.id.pwd);

        rlUserName = (EditText) findViewById(R.id.userName);

        Login = (Button) findViewById(R.id.Login);

        rlPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == R.id.Login || actionId == R.id.pwd) {
                    loginAttempt();
                    return true;
                }
                return false;
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(RL.this, RLSignUp.class);
                startActivity(signup);
                finish();
            }
        });

        signup.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Intent signup = new Intent(RL.this, RLSignUp.class);
                startActivity(signup);
                finish();
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loginAttempt()) {
                    Intent login = new Intent(RL.this, LoginSuccessActivity.class);
                    startActivity(login);
                    finish();
                } else {
                    message();
                }
            }
        });
    }

    private void message() {
        Toast.makeText(this, "Please register with your email", LENGTH_SHORT).show();

    }

    private boolean loginAttempt() {

        rlPassword.setError(null);
        rlUserName.setError(null);

        String userName = rlUserName.getText().toString();
        String password = rlPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPassword(password)) {
            rlPassword.setError(getString(R.string.error_invalid_password));
            focusView = rlPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(userName)) {
            rlUserName.setError(getString(R.string.error_field_required));
            focusView = rlUserName;
            cancel = true;

        } else if (!TextUtils.isEmpty(userName) && !isEmailValid(userName, password)) {
            rlUserName.setError(getString(R.string.error_field_required));
            focusView = rlUserName;
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

