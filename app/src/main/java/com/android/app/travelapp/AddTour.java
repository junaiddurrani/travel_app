package com.android.app.travelapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddTour extends AppCompatActivity {

    private static final int GALLERY_PICK = 1;
    private Button mAddPost;
    private EditText place_name;
    private ImageView mPostImage;
    private Uri imageUri;
    private DatabaseReference mTourDatabase;
    private ProgressDialog mProgress;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tour);

        mAddPost = findViewById(R.id.addPost);
        place_name = findViewById(R.id.place_name);

        mPostImage = findViewById(R.id.postImage);

        mAddPost = findViewById(R.id.btnPost);

        imageUri = null;
        mProgress = new ProgressDialog(this);

        mTourDatabase = FirebaseDatabase.getInstance().getReference().child("Tours");
        mStorage = FirebaseStorage.getInstance().getReference().child("Tour_image");

        mTourDatabase.keepSynced(true);

        mAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgress.setTitle("Posting");
                mProgress.setMessage("Wait a moment");
                mProgress.show();
                String place = place_name.getText().toString();
                if (!TextUtils.isEmpty(place)) {
                    createPost(place);
                }
            }
        });

        mPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Select Image"), GALLERY_PICK );
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            imageUri = data.getData();
            mPostImage.setImageURI(imageUri);
        }
    }

    private void createPost(final String place) {

        StorageReference reference = mStorage.child(imageUri.getLastPathSegment());
        reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                Map<String, String> map = new HashMap<>();
                map.put("place", place);
                map.put("image", downloadUrl);
                mTourDatabase.push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mProgress.hide();
                            startActivity(new Intent(AddTour.this, MainActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(AddTour.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            mProgress.dismiss();
                        }
                    }
                });
            }
        });
    }
}
