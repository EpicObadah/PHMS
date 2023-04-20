package com.example.mavhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Button resetPassBtn;
    private TextView backToLogin;
    private EditText emailEditText;
    private String email;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        firebaseAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.reset_pass_email_edit_text);
        resetPassBtn = findViewById(R.id.reset_pass_page_reset_btn);
        backToLogin = findViewById(R.id.forgot_pass_page_login_text_view_btn);

        backToLogin.setOnClickListener((v) -> startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class)));

        resetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

    }

    private void validateData(){
        email = emailEditText.getText().toString();

        if(email.isEmpty()){
            emailEditText.setError("Email is required");
        }
        else{
            resetPass();
        }
    }

    private void resetPass(){
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ForgotPasswordActivity.this, "Check your email", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                            finish();
                        }
                        else{
                            Toast.makeText(ForgotPasswordActivity.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}