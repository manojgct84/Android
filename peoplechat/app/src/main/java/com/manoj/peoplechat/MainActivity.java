package com.manoj.peoplechat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.manoj.adapter.MessageAdapter;
import com.manoj.model.Chat;
import com.manoj.permission.GetPermission;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {


    private String wantPermission = Manifest.permission.READ_PHONE_STATE;
    private String TAG = "PhoneActivityTAG";
    private GetPermission getPermission;


    private DatabaseReference databaseReference;
    private ValueEventListener seenListener;

    private RecyclerView recyclerView;

    private ImageButton btn_send;
    private EditText text_send;
    private TextView username;
    private CircleImageView profile_image;
    private List<Chat> mchat;
    private String currentUser;
    private String phoneNumber;
    private String selectedPhone;

    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserAndChatsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("phoneNo", phoneNumber);
                intent.putExtra("userName", currentUser);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        getPermission = new GetPermission(MainActivity.this);

        if (!getPermission.checkPermission(wantPermission)) {
            getPermission.getPermission();
            Intent intent = getIntent();
            currentUser = intent.getStringExtra("Username");
            Log.i(TAG, "Current User :" + currentUser);
        } else {
            Intent intent = getIntent();

            currentUser = intent.getStringExtra("userName");
            phoneNumber = intent.getStringExtra("phoneNo");
            selectedPhone = intent.getStringExtra("selectedPhone");

            Log.i(TAG, "Current User :" + currentUser);
            Log.i(TAG, "Current User Phone :" + phoneNumber);
            Log.d(TAG, "Phone number: " + currentUser());
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Messanger");

        profile_image = findViewById(R.id.profile_image);

        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);

        text_send = findViewById(R.id.text_send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text_send = findViewById(R.id.text_send);
                String msg = text_send.getText().toString();
                if (!msg.equals("")) {
                    sendMessage(phoneNumber, selectedPhone, msg);
                } else {
                    Toast.makeText(MainActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Messanger").child("loginUser");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.d(TAG, "Phone number : " + snapshot.child("phoneNo").getValue());
                        if (snapshot.child("imageURL").getValue().toString().equals("default")) {
                            profile_image.setImageResource(R.mipmap.ic_karthik_profile_round);
                            username.setText(snapshot.child("username").getValue().toString());
                            currentUser = snapshot.child("username").getValue().toString();
                        } else if (snapshot.child("phoneNo").getValue().toString().equals(phoneNumber)) {
                            username.setText(snapshot.child("username").getValue().toString());
                            currentUser = snapshot.child("username").getValue().toString();
                            Glide.with(getApplicationContext()).load(snapshot.child("imageURL").getValue().toString()).into(profile_image);
                        }
                        if (snapshot.child("phoneNo").getValue().toString().equals(selectedPhone)) {
                            Log.d(TAG, "imageURL  " + snapshot.child("imageURL").getValue() + " Selected Phone " + selectedPhone);
                            readMesagges(phoneNumber, selectedPhone, snapshot.child("imageURL").getValue().toString());
                        }
                    }
                } else {
                    username.setText(currentUser);
                    profile_image.setImageResource(R.mipmap.ic_karthik_profile_round);
                    readMesagges(phoneNumber, selectedPhone, null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        seenMessage(selectedPhone);
    }


    private String currentUser() {
        TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (getPermission.checkPermission(wantPermission)) {
            return tMgr.getLine1Number();
        }
        return null;
    }


    public void sendMessage(String sender, final String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Messanger");
        Chat chat = new Chat(sender, receiver, message, false);
        reference.child("Chats").push().setValue(chat);
    }


    private void readMesagges(final String myid, final String userid, final String imageurl) {
        mchat = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Messanger").child("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.i("DB Message Value ", snapshot.getValue().toString());
                    Chat chat = snapshot.getValue(Chat.class);
                    Log.i("Chat Message Value ", chat.toString());
                    if (chat != null && chat.getReceiver().equals(myid) && chat.getSender().equals(userid) || chat != null && chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                        mchat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MainActivity.this, mchat, imageurl, phoneNumber);
                    recyclerView.setAdapter(messageAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void seenMessage(final String selectedPhone) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Messanger").child("Chats");
        seenListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("receiver").getValue().toString().equals(phoneNumber) && snapshot.child("sender").getValue().toString().equals(selectedPhone)) {
                        String key = snapshot.getKey();
                        Log.d(TAG, "The message has been see" + selectedPhone);
                        databaseReference.child(key).child("isseen").setValue(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void status(final String status) {

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Messanger").child("loginUser");

        Query query = ref.orderByChild("phoneNo").equalTo(StringUtils.trim(phoneNumber)).limitToFirst(1);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("phoneNo").getValue().toString().equals(phoneNumber)) {
                        String key = snapshot.getKey();
                        Log.d(TAG, "Update the profile status " + phoneNumber);
                        ref.child(key).child("status").setValue(status);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void currentUser(String userid) {
        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("currentuser", userid);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
        currentUser(currentUser);
    }

    @Override
    protected void onPause() {
        super.onPause();
        databaseReference.removeEventListener(seenListener);
        status("offline");
        currentUser("none");
    }
}