package com.manoj.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.manoj.adapter.UserAdapter;
import com.manoj.model.User;
import com.manoj.peoplechat.R;

import java.util.ArrayList;
import java.util.List;


public class UsersFragment extends Fragment {

    private static final String TAG = "UsersFragment";
    private RecyclerView recyclerView;
    private String currentPhone;
    private String currentUser;
    private UserAdapter userAdapter;
    private List<User> mUsers;

    EditText search_users;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentPhone = getArguments().getString("userPhone");
        currentUser = getArguments().getString("currentUser");
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mUsers = new ArrayList<>();
        readUsers();
        // search_users = view.findViewById(R.id.search_users);
    /*    search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUsers(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/

        return view;
    }

    //TODO - This is cureenrly not used
    private void searchUsers(String s) {

        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username")
                .startAt(s)
                .endAt(s + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    Log.i(TAG, "Login Phone Number : " + currentPhone);
                    Log.i(TAG, "DB Phone Number : " + user.getPhoneNo());
                    if (!user.getPhoneNo().equals(currentPhone)) {
                        mUsers.add(user);
                    }
                }

                userAdapter = new UserAdapter(getContext(), mUsers, currentPhone, currentUser, true);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readUsers() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Messanger").child("loginUser");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //if (search_users.getText().toString().equals("")) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "snapshot Value:" + snapshot.getValue().toString());
                    User user = snapshot.getValue(User.class);
                    Log.d(TAG, "User details:" + user.toString());
                    if (!user.getPhoneNo().equals(currentPhone)) {
                        mUsers.add(user);
                    }
                }
                Log.d(TAG, "mUsers details:" + mUsers.toString());
                userAdapter = new UserAdapter(getContext(), mUsers, currentPhone, currentUser, true);
                recyclerView.setAdapter(userAdapter);
                //}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
