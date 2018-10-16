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

public class AddPost extends AppCompatActivity {

    private static final int GALLERY_PICK = 1;
    private Button mAddPost;
    private EditText hotel_name, place_name, price;
    private ImageView mPostImage;
    private Uri imageUri;
    private DatabaseReference mPostDatabase;
    private ProgressDialog mProgress;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        mAddPost = findViewById(R.id.addPost);
        hotel_name = findViewById(R.id.hotel_name);
        place_name = findViewById(R.id.place_name);
        price = findViewById(R.id.price);

        mPostImage = findViewById(R.id.postImage);

        mAddPost = findViewById(R.id.btnPost);

        imageUri = null;
        mProgress = new ProgressDialog(this);

        mPostDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        mStorage = FirebaseStorage.getInstance().getReference().child("Post_image");

        mAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgress.setTitle("Posting");
                mProgress.setMessage("Wait a moment");
                mProgress.show();
                String name = hotel_name.getText().toString();
                String place = place_name.getText().toString();
                String p = price.getText().toString();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(place) && !TextUtils.isEmpty(p)) {
                    createPost(name, place, p);
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

    private void createPost(final String name, final String place, final String p) {

        StorageReference reference = mStorage.child(imageUri.getLastPathSegment());
        reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                Map<String, String> map = new HashMap<>();
                map.put("name", name);
                map.put("place", place);
                map.put("price", p);
                map.put("image", downloadUrl);
                mPostDatabase.push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mProgress.hide();
                            startActivity(new Intent(AddPost.this, MainActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(AddPost.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            mProgress.dismiss();
                        }
                    }
                });
            }
        });
    }

}
