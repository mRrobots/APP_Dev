package com.example.etransect;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class Login extends AppCompatActivity {

    EditText mEmail,mPassword;
    Button mLogin;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.etEmail);
        mPassword = findViewById(R.id.etPassword);
        mLogin = findViewById(R.id.button);
        progressBar = findViewById(R.id.progressBar2);
        firebaseAuth= FirebaseAuth.getInstance();
    }
    public void Go_Register(View view) {
        startActivity(new Intent(getApplicationContext(), com.example.etransect.Register.class));
    }

    public void Login(View view) {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            mEmail.setError("Email is required");
            return;
        }
        if(TextUtils.isEmpty(password)){
            mPassword.setError("password is needed");
            return;
        }
        if(password.length()<6){
            mPassword.setError("Password must be >= 6 Characters");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                 new Global(email);
                Toast.makeText(Login.this,"Welcome "+email, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                progressBar.setVisibility(View.GONE);

            }
            else{
                Toast.makeText(Login.this,"error"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void Password(View view) {
        startActivity(new Intent(getApplicationContext(),Forgot_Password.class));
    }
}