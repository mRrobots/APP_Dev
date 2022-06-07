package com.example.etransect;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {


    private FirebaseUser firebaseAuth;
    private DatabaseReference databaseReference;
    private String UserId;


     BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        UserId  = firebaseAuth.getUid();

        final TextView nameText = (TextView)findViewById(R.id.profile_name);
        final TextView lastText = (TextView)findViewById(R.id.Profile_surname);
        final TextView emailText = (TextView)findViewById(R.id.Profile_email);
        final TextView phoneText = (TextView)findViewById(R.id.Profile_phone);
        final TextView addText = (TextView)findViewById(R.id.country);


        databaseReference.child(UserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                User userprofile = snapshot.getValue(User.class);
                if(userprofile!=null){
                    String FullName = userprofile.Name;
                    String SurName = userprofile.Surname;
                    String Email = userprofile.Email;
                    String Country = userprofile.Address;
                    String Phone = userprofile.Phone;

                    nameText.setText(FullName);
                    lastText.setText(SurName);
                    emailText.setText(Email);
                    phoneText.setText(Phone);
                    addText.setText(Country);
                   // new Global(FullName);

                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Toast.makeText(Profile.this,"Something went wrong",Toast.LENGTH_LONG).show();
            }
        });



        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.profile_page);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.profile_page:

                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.MyStore:
                        startActivity(new Intent(getApplicationContext(), com.example.etransect.My_store.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.sell:
                        startActivity(new Intent(getApplicationContext(), com.example.etransect.Sell.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}