package com.example.mavhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText,passwordEditText;
    private Button loginBtn;
    private ProgressBar progressBar;
    private TextView registerBtnTextView;
    private TextView forgotPassBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.login_page_email_edit_text);
        passwordEditText = findViewById(R.id.login_page_password_edit_text);
        loginBtn = findViewById(R.id.login_page_login_btn);
        progressBar = findViewById(R.id.login_page_progress_bar);
        registerBtnTextView = findViewById(R.id.login_page_register_btn);
        forgotPassBtnTextView = findViewById(R.id.login_page_reset_btn);


        loginBtn.setOnClickListener((v)-> loginUser());
        registerBtnTextView.setOnClickListener((v)-> startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class)));
        forgotPassBtnTextView.setOnClickListener((v) -> startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));

    }

    void loginUser(){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        boolean isValidated = validateData(email, password);

        if(!isValidated){
            return;
        }

        loginAccountInFirebase(email, password);

    }

    void loginAccountInFirebase(String email, String password){

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        //show progress bar
        changeInProgress(true);

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //remove progress bar
                changeInProgress(false);

                if(task.isSuccessful()){
                    //login successful

                    //check if email is verified
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        //go to main activity
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                    else{
                        Utility.showToast(LoginActivity.this, "Email not verified, Please verify your email");
                    }
                }
                else{
                    //login failed
                    Utility.showToast(LoginActivity.this, task.getException().getLocalizedMessage());
                }
            }
        });


    }

    void changeInProgress(boolean inProgress){

        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email, String password){
        //validate data input by user

        if(!(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
            emailEditText.setError("Email is invalid");
            return false;
        }

        if(password.length() < 8){
            passwordEditText.setError("Password length is invalid");
            return  false;
        }

        return true;
    }
}