package app.infogen.cs.com.tabapplication;

import android.content.Context;
import android.media.AudioManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.SeekBar;

import app.infogen.cs.com.tabapplication.Swipe.Pager;

public class TabActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private TabLayout tabLayout;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private Pager pager;

    private SeekBar musicVolume;
    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        pager = new Pager(getSupportFragmentManager());
        pager.addFragment(new OneFragment(), "ONE");
        pager.addFragment(new TwoFragment(), "TWO");
        pager.addFragment(new ThreeFragment(), "THREE");
        viewPager.setAdapter(pager);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        musicVolume = (SeekBar) findViewById(R.id.volumeMusic);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        musicVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}