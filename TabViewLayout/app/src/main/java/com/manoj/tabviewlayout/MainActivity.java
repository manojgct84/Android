package com.manoj.tabviewlayout;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private int[] tabIcons = {
            R.drawable.ic_people_black_24dp,
            R.drawable.ic_chat_bubble_black_24dp
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabsLayout);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu1:
                Toast.makeText(getApplicationContext(), "Menu one selected", Toast.LENGTH_LONG);
                break;
            case R.id.menu2:
                Toast.makeText(getApplicationContext(), "Menu two selected", Toast.LENGTH_LONG);
                break;
            case R.id.menu3:
                Toast.makeText(getApplicationContext(), "Menu three selected", Toast.LENGTH_LONG);
                break;
            case R.id.cart:
                Toast.makeText(getApplicationContext(), "cart selected", Toast.LENGTH_LONG);
                break;
            case R.id.search:
                Toast.makeText(getApplicationContext(), "search selected", Toast.LENGTH_LONG);
                break;
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }

    private void setupViewPager(ViewPager viewPager) {
        FragmentViewAdapter fragmentViewAdapter = new FragmentViewAdapter(getSupportFragmentManager());
        fragmentViewAdapter.addFragments(new UserFragment(), "Chats");
        fragmentViewAdapter.addFragments(new ChatsFragment(), "Users");
        viewPager.setAdapter(fragmentViewAdapter);

    }
}
