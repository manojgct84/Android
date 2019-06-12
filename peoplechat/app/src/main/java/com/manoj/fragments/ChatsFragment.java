package com.manoj.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.manoj.adapter.UserAdapter;
import com.manoj.model.Chatlist;
import com.manoj.model.User;
import com.manoj.peoplechat.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;

    private String TAG = "ChatsFragment";

    private UserAdapter userAdapter;
    private Set<User> mUsers;
    private String currentPhone;
    private String currentUser;

    //FirebaseUser fuser;
    DatabaseReference reference;

    private List<Chatlist> usersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentPhone = getArguments().getString("userPhone");
        currentUser = getArguments().getString("currentUser");

        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        usersList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Messanger").child("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "Chat details" + snapshot.getValue());
                    if (snapshot.child("receiver").getValue().equals(currentPhone)) {
                        Chatlist chatlist = new Chatlist();
                        chatlist.setId(snapshot.child("sender").getValue().toString());
                        usersList.add(chatlist);
                    } else if (snapshot.child("sender").getValue().equals(currentPhone)) {
                        Chatlist chatlist = new Chatlist();
                        chatlist.setId(snapshot.child("receiver").getValue().toString());
                        usersList.add(chatlist);
                    }
                }
                Log.d(TAG, "UserList " + usersList.toString());

                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //updateToken(FirebaseInstanceId.getInstance().getToken());
        return view;
    }

    private void updateToken(String token) {
        //DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        //Token token1 = new Token(token);
        //reference.child(fuser.getUid()).setValue(token1);
    }

    private void chatList() {
        mUsers = new TreeSet<>(new Comparator<User>() {
            @Override
            public int compare(User user, User user1) {
                return user.getPhoneNo().compareToIgnoreCase(user1.getPhoneNo());
            }
        });
        reference = FirebaseDatabase.getInstance().getReference("Messanger").child("loginUser");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    Log.d(TAG, "User Details :" + user.toString());
                    for (Chatlist chatlist : usersList) {
                        if (user.getPhoneNo().equals(chatlist.getId())) {
                            List<User> newList = new ArrayList<>(mUsers);
                            if (newList.size() != 0) {
                                for (User user1 : newList) {
                                    Log.d(TAG, "User1 " + user1.getPhoneNo() + " User " + user.getPhoneNo());
                                    if (!user.getPhoneNo().equals(user1.getPhoneNo())) {
                                        mUsers.add(user);
                                        Log.d(TAG, "if chat List " + mUsers.toString());
                                    }
                                }
                            } else {
                                mUsers.add(user);
                                Log.d(TAG, "Else chat List " + mUsers.toString());
                            }
                        }
                    }
                }
                Log.d(TAG, "Chat List " + mUsers.toString());

                List<User> listUserChat = new ArrayList<>(mUsers);
                Log.d(TAG, "Chat listUserChat " + listUserChat.toString());
                userAdapter = new UserAdapter(getContext(), listUserChat, currentPhone, currentUser, true);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
