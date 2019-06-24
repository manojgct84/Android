package com.manoj.tabviewlayout;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class Main2Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;

    private DrawerLayout drawerLayout;

    private NavigationView navigationView;

    private AppCompatEditText userName;
    private AppCompatEditText pwd;

    private TextInputLayout textLayoutUserName;
    private TextInputLayout textLayoutPwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigate_drawer);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigate_view);

        userName = findViewById(R.id.userName);
        pwd = findViewById(R.id.password);

        textLayoutUserName = findViewById(R.id.textUserName);

        textLayoutPwd = findViewById(R.id.textpwd);

        onclickNavigation();


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Open_drawer, R.string.Close_drawer);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        validateUserInput();

    }

    private void validateUserInput() {
        userName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (userName.getText().toString().isEmpty()) {
                    userName.setError("Please Enter the User Name");
                } else {
                    userName.setError(null);
                }

            }
        });

        pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (pwd.getText().toString().isEmpty()) {
                    pwd.setError("Please Enter the User Name");
                } else {
                    pwd.setError(null);
                }

            }
        });

        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    private void onclickNavigation() {
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.inbox:
                Toast.makeText(getApplicationContext(), "Inbox selected", Toast.LENGTH_LONG);
                break;
            case R.id.stared:
                Toast.makeText(getApplicationContext(), "Started selected", Toast.LENGTH_LONG);
                break;
            case R.id.send:
                Toast.makeText(getApplicationContext(), "Send selected", Toast.LENGTH_LONG);
                break;
            case R.id.draft:
                Toast.makeText(getApplicationContext(), "Draft selected", Toast.LENGTH_LONG);
                break;
            case R.id.allemail:
                Toast.makeText(getApplicationContext(), "All email selected", Toast.LENGTH_LONG);
                break;
            case R.id.trash:
                Toast.makeText(getApplicationContext(), "Trash selected", Toast.LENGTH_LONG);
                break;
            case R.id.spam:
                Toast.makeText(getApplicationContext(), "Spam selected", Toast.LENGTH_LONG);
                break;
        }
        return false;
    }


}
