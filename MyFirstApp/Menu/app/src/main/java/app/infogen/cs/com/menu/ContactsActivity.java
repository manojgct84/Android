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

public class ContactsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
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
        MenuItem contacts = menu.findItem(R.id.contacts);
        contacts.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.home) {
            Intent home = new Intent(ContactsActivity.this, MenuActivity.class);
            startActivity(home);
            finish();
            return true;
        } else if (itemId == R.id.about) {
            Intent about = new Intent(ContactsActivity.this, AboutActivity.class);
            startActivity(about);
            finish();
            return true;
        } else if (itemId == R.id.settings) {
            Intent settings = new Intent(ContactsActivity.this, SettingsActivity.class);
            startActivity(settings);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
