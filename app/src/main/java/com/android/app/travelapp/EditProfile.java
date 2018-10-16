package com.android.app.travelapp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    private String name;
    private EditText txtName, txtAge, txtPhone;
    private RadioButton btnMale, btnFemaile;
    private Button btnUpdate;
    private DatabaseReference mUserDatabse;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        mUserDatabse = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        mProgress = new ProgressDialog(this);

        name = getIntent().getStringExtra("name");

        txtName = findViewById(R.id.txtName);
        txtAge = findViewById(R.id.txtAge);
        txtPhone = findViewById(R.id.txtPhone);

        btnMale = findViewById(R.id.radioMale);
        btnFemaile = findViewById(R.id.radioFemale);

        btnUpdate = findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgress.setTitle("Updaing");
                mProgress.setMessage("Wait a moment");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.show();
                String newName = txtName.getText().toString();
                String age = txtAge.getText().toString();
                String phone = txtPhone.getText().toString();
                String gender = selectGender();
                if (!TextUtils.isEmpty(newName) && !TextUtils.isEmpty(age) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(gender)) {

                    mUserDatabse.child("name").setValue(newName).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mProgress.hide();
                                Toast.makeText(EditProfile.this, "Updated", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                mProgress.dismiss();
                                Toast.makeText(EditProfile.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    mUserDatabse.child("age").setValue(age).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mProgress.hide();
                                Toast.makeText(EditProfile.this, "Updated", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                mProgress.dismiss();
                                Toast.makeText(EditProfile.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    mUserDatabse.child("phone").setValue(phone).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mProgress.hide();
                                Toast.makeText(EditProfile.this, "Updated", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                mProgress.dismiss();
                                Toast.makeText(EditProfile.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    mUserDatabse.child("gender").setValue(gender).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mProgress.hide();
                                Toast.makeText(EditProfile.this, "Updated", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                mProgress.dismiss();
                                Toast.makeText(EditProfile.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        txtName.setText(name);

    }

    private String selectGender() {
        if (btnMale.isChecked()) {
            return "Male";
        }
        else {
            return "Female";
        }
    }
}
