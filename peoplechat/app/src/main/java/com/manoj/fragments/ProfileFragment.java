package com.manoj.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.manoj.model.User;
import com.manoj.peoplechat.MainActivity;
import com.manoj.peoplechat.R;
import com.manoj.permission.GetPermission;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private CircleImageView image_profile;
    private View view;
    private GetPermission getPermission;
    private String wantPermission = Manifest.permission.INTERNET;
    private TextView username;

    private DatabaseReference reference;

    StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;
    private String currentPhone;
    private String currentUser;
    private ImageView editProfileName;
    private String TAG = "ProfileFragment";

    private String profileNameChange;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        currentPhone = getArguments().getString("userPhone");
        currentUser = getArguments().getString("currentUser");

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        image_profile = view.findViewById(R.id.profile_image);
        username = view.findViewById(R.id.username);
        editProfileName = view.findViewById(R.id.editprofileName);

        storageReference = FirebaseStorage.getInstance().getReference().child("profile");

        Log.d(TAG, "Storgae Path :" + storageReference.getPath());

        reference = FirebaseDatabase.getInstance().getReference("Messanger").child("loginUser");

        setProfileImage();

        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });

        editProfileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfileNameDialog();
            }
        });

        return view;
    }

    private void openProfileNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter the Name");

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.edit_profile_name, (ViewGroup) getView(), false);

        final EditText input = viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);
        input.setText(currentUser);

        // Set up the buttons - Save button
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                profileNameChange = input.getText().toString();
                Log.d(TAG, "ProfileName change:" + profileNameChange);
                updateProfileNameInDB();
            }
        });

        // Set up the buttons - Cancel button
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void updateProfileNameInDB() {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Uploading");
        pd.show();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Messanger").child("loginUser");
        Query query = ref.orderByChild("phoneNo").equalTo(StringUtils.trim(currentPhone)).limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("phoneNo").getValue().toString().equals(currentPhone)) {
                        String key = snapshot.getKey();
                        Log.d(TAG, "Update the profile status " + currentPhone);
                        ref.child(key).child("username").setValue(profileNameChange);
                        username.setText(profileNameChange);
                        pd.dismiss();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "The profile name is not update");
                pd.dismiss();
            }
        });

    }

    private void setProfileImage() {
        username.setText(currentUser);
        Query query = reference.orderByChild("phoneNo").equalTo(StringUtils.trim(currentPhone)).limitToFirst(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("imageURL").getValue() != null && snapshot.child("imageURL").getValue().toString().equals("default")) {
                        image_profile.setImageResource(R.drawable.ic_chat_bubble_black_24dp);
                    } else {
                        if (getActivity() == null) {
                            return;
                        }

                        //Set the profile relative layout with the profile image
                        Task<byte[]> storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(snapshot.child("imageURL").getValue().toString()).getBytes(100 * 1024 * 1024);
                        storageRef.addOnCompleteListener(new OnCompleteListener<byte[]>() {
                            @Override
                            public void onComplete(@NonNull Task<byte[]> task) {
                                RelativeLayout rLayout = view.findViewById(R.id.profileBg);
                                boolean downloadSuccessful = task.isSuccessful();
                                if (downloadSuccessful) {
                                    byte[] imageBytes = task.getResult();
                                    Log.d(TAG, "imageBytes " + imageBytes.length);
                                    Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                                    rLayout.getBackground().setAlpha(80);
                                    rLayout.setBackground(new BitmapDrawable(imageBitmap));
                                } else {
                                    Log.d(TAG, "Download was successful for profile image");
                                }
                            }
                        });
                        Glide.with(getActivity()).load(snapshot.child("imageURL").getValue().toString()).into(image_profile);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private Bitmap getBitmapFromURL(String imageURL) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            getPermission = new GetPermission(getActivity());
            if (!getPermission.checkPermission(wantPermission)) {
                getPermission.getPermission();
            }
            String[] relativeImageURL = imageURL.split(Pattern.quote("?"));
            Log.d(TAG, "Get Image download " + relativeImageURL[0] + " Original URL " + imageURL);
            URL url = new URL(imageURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            Log.d(TAG, "Unable to download the file" + e.fillInStackTrace());
            return null;
        }
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Uploading");
        pd.show();

        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child(currentPhone + "." + getFileExtension(imageUri));

            StorageMetadata metadata = new StorageMetadata.Builder().setContentType("image/" + getFileExtension(imageUri)).setCustomMetadata(currentUser, currentPhone).setCustomMetadata("location", "India").build();

            uploadTask = fileReference.putFile(imageUri, metadata);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        final String mUri = downloadUri.toString();
                        reference = FirebaseDatabase.getInstance().getReference("Messanger").child("loginUser");
                        reference.orderByChild("phoneNo").equalTo(currentPhone).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        String key = snapshot.getKey();
                                        Log.d(TAG, "URL Details for IMAGE : " + mUri);
                                        reference.child(key).child("imageURL").setValue(mUri);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        pd.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(getContext(), "Upload in preogress", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }
}
