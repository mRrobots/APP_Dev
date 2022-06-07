package com.example.etransect;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {


    EditText mName,mName1,mEmail,mPhone,mPassword,maddress;
    Button mRegister;
    ProgressBar progressBar;
    TextView mLogin;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mName = findViewById(R.id.etName);
        mName1 = findViewById(R.id.etName1);
        mEmail = findViewById(R.id.etEmail);
        mPhone = findViewById(R.id.etPhone);
        maddress = findViewById(R.id.etAddress);
        mPassword = findViewById(R.id.etPassword);
        mRegister = findViewById(R.id.btnRegister);
        mLogin = findViewById(R.id.textView3);
        progressBar = findViewById(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void Save(View view) {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String name = mName.getText().toString().trim();
        String surname = mName1.getText().toString().trim();
        String phone= mPhone.getText().toString().trim();
        String address = maddress.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            mEmail.setError("Email is required");
            return;
        }if(TextUtils.isEmpty(name)){
            mEmail.setError("First is required");
            return;
        }if(TextUtils.isEmpty(surname)){
            mEmail.setError("Surname is required");
            return;
        }if(TextUtils.isEmpty(phone)){
            mEmail.setError("Phone number is required");
            return;
        }if(TextUtils.isEmpty(address)){
            mEmail.setError("Address is required,how we gonn find u man");
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
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                User user = new User(name,surname,phone,email,address);
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            new Global(email);
                            Toast.makeText(Register.this,"User Created",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }

                        else {
                            Toast.makeText(Register.this,"something went wrong"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
//                Toast.makeText(Register.this,"User Created",Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
            else{
                Toast.makeText(Register.this,"error"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void Go_Login(View view) {
        startActivity(new Intent(getApplicationContext(),Login.class));
    }
}