package com.example.mavhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class CreateAccountActivity extends AppCompatActivity {

    private EditText emailEditText,passwordEditText, confirmPasswordEditText;
    private Button createAccountBtn;
    private ProgressBar progressBar;
    private TextView loginBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //set variables to id's found in xml
        emailEditText = findViewById(R.id.sign_up_page_email_edit_text);
        passwordEditText = findViewById(R.id.sign_up_page_password_edit_text);
        confirmPasswordEditText = findViewById(R.id.sign_up_page_confirm_password_edit_text);
        createAccountBtn = findViewById(R.id.sign_up_page_create_account_btn);
        progressBar = findViewById(R.id.sign_up_page_progress_bar);
        loginBtnTextView = findViewById(R.id.login_text_view_btn);


        //declare onClick for create account and login button
        createAccountBtn.setOnClickListener(v-> createAccount());
        loginBtnTextView.setOnClickListener(v-> finish());

    }

    void createAccount(){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        boolean isValidated = validateData(email, password, confirmPassword);

        if(!isValidated){
            return;
        }

        createFireBaseAccount(email, password);

    }

    void createFireBaseAccount(String email, String password){
        //show progress bar instead of create account button
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccountActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //after creating account display button again
                        changeInProgress(false);

                        if(task.isSuccessful()){

                            //creating account is successful
                            Utility.showToast(CreateAccountActivity.this, "Successfully created account! Check email to verify");
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            firebaseAuth.signOut();
                            finish();
                        }
                        else{

                            //failure
                            Utility.showToast(CreateAccountActivity.this, task.getException().getLocalizedMessage());
                        }
                    }
                }
        );
    }

    void changeInProgress(boolean inProgress){
        if(inProgress){

            progressBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        }
        else{

            progressBar.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }
    }

    //validate data input by user
    boolean validateData(String email, String password, String confirmPassword){

        if(!(Patterns.EMAIL_ADDRESS.matcher(email).matches())){

            emailEditText.setError("Email is invalid");
            return false;
        }

        if(password.length() < 8){

            passwordEditText.setError("Password length is invalid");
            return false;
        }

        if(!(password.equals(confirmPassword))){

            confirmPasswordEditText.setError("Passwords do not match");
            return false;
        }

        return true;
    }
}