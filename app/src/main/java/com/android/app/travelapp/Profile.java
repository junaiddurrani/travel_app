package com.android.app.travelapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class Profile extends Fragment {

    private static final int GALLERY_PICK = 1;
    private View mView;
    private Button btnSignOut;
    private FirebaseAuth mAuth;

    private TextView editProfile;
    private TextView txtName, txtEmail, txtGender, txtPhone, txtAge;
    private CircleImageView profileImage;
    private DatabaseReference mUserDatabase;
    private Uri imageUri;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private FirebaseUser currentUser;

    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();

        mProgress = new ProgressDialog(getActivity());

        currentUser = mAuth.getCurrentUser();


        mStorage = FirebaseStorage.getInstance().getReference().child("user_images");
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());

        imageUri = null;

        txtAge = mView.findViewById(R.id.txtAge);
        txtEmail = mView.findViewById(R.id.txtEmail);
        txtGender = mView.findViewById(R.id.txtGender);
        txtName = mView.findViewById(R.id.profileName);
        txtPhone = mView.findViewById(R.id.txtPhone);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    String name = (String) dataSnapshot.child("name").getValue();
                    String email = (String) dataSnapshot.child("email").getValue();
                    String gender = (String) dataSnapshot.child("gender").getValue();
                    String age = (String) dataSnapshot.child("age").getValue();
                    String phone = (String) dataSnapshot.child("phone").getValue();
                    String image = (String) dataSnapshot.child("image").getValue();
                    txtAge.setText(age);
                    txtEmail.setText(email);
                    txtGender.setText(gender);
                    txtPhone.setText(phone);
                    txtName.setText(name);
                    Picasso.with(getActivity()).load(image).placeholder(R.drawable.user).into(profileImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mUserDatabase.keepSynced(true);

        profileImage = mView.findViewById(R.id.profileImage);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), GALLERY_PICK);
            }
        });

        btnSignOut = mView.findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(container.getContext(), WelcomeScreen.class));
                getActivity().finish();
            }
        });

        editProfile = mView.findViewById(R.id.editProfile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfile.class);
                intent.putExtra("name", txtName.getText().toString());
                startActivity(intent);
            }
        });

        return mView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mProgress.setTitle("Uplaoding");
        mProgress.setMessage("Wait a moment");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            imageUri = data.getData();
            if (mAuth.getCurrentUser() != null) {
                StorageReference reference = mStorage.child(mAuth.getCurrentUser().getUid() + ".jpg");
                reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String downloadUrl = String.valueOf(taskSnapshot.getDownloadUrl());
                        mUserDatabase.child("image").setValue(downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mProgress.hide();
                                profileImage.setImageURI(imageUri);
                            }
                        });
                    }
                });
            }
        }
    }
}
