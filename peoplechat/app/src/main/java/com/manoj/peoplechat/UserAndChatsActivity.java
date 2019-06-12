package com.manoj.peoplechat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.manoj.fragments.ChatsFragment;
import com.manoj.fragments.ProfileFragment;
import com.manoj.fragments.UsersFragment;
import com.manoj.model.Chat;
import com.manoj.model.User;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAndChatsActivity extends AppCompatActivity {

    private CircleImageView profile_image;
    private TextView username;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private String currentUser;
    private String phoneNumber;

    private String TAG = "UserAndChatsActivity";

    DatabaseReference reference;

    private int[] tabIcons = {
            R.drawable.ic_people_black_24dp,
            R.drawable.ic_chat_bubble_black_24dp
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_and_chats);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        Intent intent = getIntent();
        currentUser = intent.getStringExtra("userName");
        phoneNumber = intent.getStringExtra("phoneNo");

        Log.d(TAG, "Login user " + currentUser);
        Log.d(TAG, "Login Phone " + phoneNumber);

        setProfileImageandName();

        Log.d(TAG, "After change " + currentUser);

        getTheChatsList();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                // change this code beacuse your app will crash
                startActivity(new Intent(UserAndChatsActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
        }
        return false;
    }


    private void getTheChatsList() {
        reference = FirebaseDatabase.getInstance().getReference("Messanger").child("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                int unread = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //Chat chat = snapshot.getValue(Chat.class);
                    Log.d(TAG, "Chat Messages " + snapshot.getValue().toString());
                    if (snapshot.child("receiver").equals(phoneNumber) && !snapshot.child("status").equals("false")) {
                        unread++;
                    }
                }
                Log.d(TAG, "Chat unread " + unread);

                Bundle bundle = new Bundle();

                bundle.putString("userPhone", phoneNumber);
                bundle.putString("currentUser", currentUser);

                Log.d(TAG, "After current User " + currentUser);

                ChatsFragment chatsFragment = new ChatsFragment();

                chatsFragment.setArguments(bundle);

                if (unread == 0) {
                    viewPagerAdapter.addFragment(chatsFragment, "Chats");
                } else {
                    viewPagerAdapter.addFragment(chatsFragment, "(" + unread + ") Chats");
                }

                UsersFragment usersFragment = new UsersFragment();
                usersFragment.setArguments(bundle);

                viewPagerAdapter.addFragment(usersFragment, "Users");

                ProfileFragment profileFragment = new ProfileFragment();
                profileFragment.setArguments(bundle);

                viewPagerAdapter.addFragment(profileFragment, "Profile");

                viewPager.setAdapter(viewPagerAdapter);

                tabLayout.setupWithViewPager(viewPager);
                //setupTabIcons();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[1]);
        tabLayout.getTabAt(1).setIcon(tabIcons[0]);
    }

    private void setProfileImageandName() {
        reference = FirebaseDatabase.getInstance().getReference("Messanger").child("loginUser");

        Query query = reference.orderByChild("phoneNo").equalTo(StringUtils.trim(phoneNumber)).limitToFirst(1);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap user;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.child("imageURL").getValue() != null && snapshot.child("imageURL").getValue().toString().equals("default")) {
                            username.setText(snapshot.child("username").getValue().toString());
                            currentUser = snapshot.child("username").getValue().toString();
                            profile_image.setImageResource(R.mipmap.ic_launcher);
                        } else {
                            Log.d("imageURL ", snapshot.child("imageURL").getValue().toString());
                            username.setText(snapshot.child("username").getValue().toString());
                            currentUser = snapshot.child("username").getValue().toString();
                            Glide.with(getApplicationContext()).load(snapshot.child("imageURL").getValue().toString()).into(profile_image);
                        }
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Profile Image not found", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}






