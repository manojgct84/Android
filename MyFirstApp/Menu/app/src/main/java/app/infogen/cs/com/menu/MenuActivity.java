package app.infogen.cs.com.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import app.infogen.cs.com.menu.vo.StateVO;

public class MenuActivity extends AppCompatActivity {


    private Spinner spinnerCountry;
    private List<StateVO> countryLst;
    final String[] select_qualification = {"Location", "Bangalore", "Chennai", "Cochin", "Salem", "Coimbatore"};
    private Button login;
    private Button singup;
    private Button home;

    private FragmentTransaction loginTran;
    private FragmentTransaction blankTran;
    private FragmentTransaction singUpTran;
    private FragmentTransaction homeTran;

    private BlankFragment blankFrame;
    private LoginFragment loginFrame;
    private HomeFragment homeFrame;
    private SignupFragment singupFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewInitilize();
        frameFragementInitilize();
        addLoginFrame();
        addSingUpFrame();
        addHomeFrame();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        spinnerCountry = (Spinner) findViewById(R.id.spinner);

        countryLst = new ArrayList<>();

        for (int i = 0; i < select_qualification.length; i++) {
            StateVO vo = new StateVO();
            vo.setTitle(select_qualification[i]);
            vo.setSelected(false);
            countryLst.add(vo);
        }

        CustomAdapter customAdapter = new CustomAdapter(MenuActivity.this, 0, countryLst);
        spinnerCountry.setAdapter(customAdapter);

    }

    private void addHomeFrame() {
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeTran = getSupportFragmentManager().beginTransaction();
                homeFrame = new HomeFragment();

                singUpTran = getSupportFragmentManager().beginTransaction();
                loginTran = getSupportFragmentManager().beginTransaction();
                blankTran = getSupportFragmentManager().beginTransaction();

                if (blankFrame != null) {
                    blankTran.remove(blankFrame);
                    blankTran.commit();

                    homeTran.replace(R.id.frameHome, homeFrame, "HomeFragment");
                    homeTran.commit();
                }

                if (singupFrame != null) {
                    singUpTran.remove(singupFrame);
                    singUpTran.commit();

                    homeTran.replace(R.id.frameHome, homeFrame, "HomeFragment");
                }

                if (loginFrame != null) {
                    loginTran.remove(loginFrame);
                    loginTran.commit();

                    homeTran.replace(R.id.frameHome, homeFrame, "HomeFragment");
                }

                homeTran.replace(R.id.frameHome, homeFrame, "HomeFragment");

            }
        });
    }

    private void addLoginFrame() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginTran = getSupportFragmentManager().beginTransaction();
                loginFrame = new LoginFragment();

                blankTran = getSupportFragmentManager().beginTransaction();
                singUpTran = getSupportFragmentManager().beginTransaction();
                homeTran = getSupportFragmentManager().beginTransaction();


                if (blankFrame != null) {
                    blankTran.remove(blankFrame);
                    blankTran.commit();

                    loginTran.replace(R.id.frameLogin, loginFrame, "LoginFragment");
                    loginTran.commit();
                }

                if (homeFrame != null) {
                    homeTran.remove(homeFrame);
                    homeTran.commit();

                    loginTran.replace(R.id.frameLogin, loginFrame, "LoginFragment");
                }

                if (singupFrame != null) {
                    singUpTran.remove(singupFrame);
                    singUpTran.commit();

                    loginTran.replace(R.id.frameLogin, loginFrame, "LoginFragment");
                }

                loginTran.replace(R.id.frameLogin, loginFrame, "LoginFragment");
            }
        });
    }

    private void addSingUpFrame() {
        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singUpTran = getSupportFragmentManager().beginTransaction();
                singupFrame = new SignupFragment();

                blankTran = getSupportFragmentManager().beginTransaction();
                loginTran = getSupportFragmentManager().beginTransaction();
                homeTran = getSupportFragmentManager().beginTransaction();

                if (blankFrame != null) {
                    blankTran.remove(blankFrame);
                    blankTran.commit();

                    singUpTran.replace(R.id.framesiginup, singupFrame, "SignupFragment");
                    singUpTran.commit();
                }

                if (loginFrame != null) {
                    loginTran.remove(loginFrame);
                    loginTran.commit();

                    singUpTran.replace(R.id.framesiginup, singupFrame, "SignupFragment");
                }

                if (homeFrame != null) {
                    homeTran.remove(homeFrame);
                    homeTran.commit();

                    singUpTran.replace(R.id.framesiginup, singupFrame, "SignupFragment");
                }

                singUpTran.replace(R.id.framesiginup, singupFrame, "SignupFragment");

            }
        });
    }

    private void frameFragementInitilize() {
        blankTran = getSupportFragmentManager().beginTransaction();
        blankFrame = new BlankFragment();
        blankTran.replace(R.id.frameBlank, blankFrame, "BlankFragemnt");
        blankTran.addToBackStack(null);
        blankTran.commit();
    }

    private void viewInitilize() {
        login = (Button) findViewById(R.id.flogin);
        singup = (Button) findViewById(R.id.fSingUp);
        home = (Button) findViewById(R.id.fhome);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        MenuItem home = menu.findItem(R.id.home);
        home.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about) {
            Intent about = new Intent(MenuActivity.this, AboutActivity.class);
            startActivity(about);
            finish();
            return true;
        } else if (id == R.id.settings) {
            Intent settings = new Intent(MenuActivity.this, SettingsActivity.class);
            startActivity(settings);
            finish();
            return true;
        } else if (id == R.id.contacts) {
            Intent contacts = new Intent(MenuActivity.this, ContactsActivity.class);
            startActivity(contacts);
            finish();
            return true;
        } else if (id == R.id.maps) {
            Intent contacts = new Intent(MenuActivity.this, MapsActivity.class);
            startActivity(contacts);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
