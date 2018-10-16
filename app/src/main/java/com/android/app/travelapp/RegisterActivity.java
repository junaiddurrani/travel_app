package com.android.app.travelapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout name, email, password;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        Button button = findViewById(R.id.btnSignIn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        mProgress = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        name = findViewById(R.id.txtName);
        email = findViewById(R.id.txtEmail);
        password = findViewById(R.id.txtPassword);

        Button button1 = findViewById(R.id.btnSignUp);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgress.setVisibility(View.VISIBLE);
                String n = name.getEditText().getText().toString();
                String e = email.getEditText().getText().toString();
                String p = password.getEditText().getText().toString();

                if (!TextUtils.isEmpty(n) && !TextUtils.isEmpty(e) && !TextUtils.isEmpty(p)) {
                    registerUser(n, e, p);
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Enter your data", Toast.LENGTH_SHORT).show();
                    mProgress.setVisibility(View.INVISIBLE);
                }

            }
        });


    }

    private void registerUser(final String n, final String e, String p) {
        mAuth.createUserWithEmailAndPassword(e, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String user_id = mAuth.getCurrentUser().getUid();
                    InsertData(n, e, user_id);
                }
                else {
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    mProgress.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void InsertData(String n, String e, String user_id) {
        Map<String, String> userMap = new HashMap<>();
        userMap.put("name", n);
        userMap.put("email", e);
        userMap.put("image", "not set");
        userMap.put("age", "not set");
        userMap.put("gender", "not set");
        userMap.put("phone", "not set");
        mUserDatabase.child(user_id).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    sendToMain();
                }
                else {
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    mProgress.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void sendToMain() {
        mProgress.setVisibility(View.INVISIBLE);
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
