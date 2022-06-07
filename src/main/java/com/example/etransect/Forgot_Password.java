package com.example.etransect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class Forgot_Password extends AppCompatActivity {

    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    EditText email0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        progressBar = findViewById(R.id.progressBar0);
        firebaseAuth = FirebaseAuth.getInstance();
        email0 = findViewById(R.id.etEmail0);
    }

    public void Password(View view) {
        resetPassword();
    }

    private void resetPassword(){
        String email = email0.getText().toString().trim();

        if(email.isEmpty()){
            email0.setError("Email required");
            email0.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email0.setError("Email is invalid");
            email0.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Forgot_Password.this,"Check your email",Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(Forgot_Password.this,"Something went wrong",Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    public void Go_login_(View view) {
        startActivity(new Intent(getApplicationContext(),Login.class));
    }
}