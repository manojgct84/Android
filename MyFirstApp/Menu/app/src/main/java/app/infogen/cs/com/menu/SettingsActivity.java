package app.infogen.cs.com.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        MenuItem settings = menu.findItem(R.id.settings);
        settings.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemID = item.getItemId();

        if (itemID == R.id.home) {
            Intent home = new Intent(SettingsActivity.this, MenuActivity.class);
            startActivity(home);
            finish();
        } else if (itemID == R.id.about) {
            Intent about = new Intent(SettingsActivity.this, AboutActivity.class);
            startActivity(about);
            finish();
        } else if (itemID == R.id.contacts) {
            Intent contacts = new Intent(SettingsActivity.this, ContactsActivity.class);
            startActivity(contacts);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
