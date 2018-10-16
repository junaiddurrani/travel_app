package com.android.app.travelapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputLayout email, password;
    private ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mProgress = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.txtEmail);
        password = findViewById(R.id.txtPassword);

        Button button = findViewById(R.id.btnSignUp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        Button button1 = findViewById(R.id.btnSignIn);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgress.setVisibility(View.VISIBLE);
                String e = email.getEditText().getText().toString();
                String p = password.getEditText().getText().toString();

                if (!TextUtils.isEmpty(e) && !TextUtils.isEmpty(p)) {
                        loginUser(e, p);
                }
                else {
                    Toast.makeText(LoginActivity.this, "Enter your data", Toast.LENGTH_SHORT).show();
                    mProgress.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void loginUser(String e, String p) {
        mAuth.signInWithEmailAndPassword(e, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mProgress.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
                else {
                    mProgress.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
