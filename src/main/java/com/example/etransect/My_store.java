package com.example.etransect;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class My_store extends AppCompatActivity {

    RecyclerView recyclerView;
    com.example.etransect.Mystore_Adapter adaptor;
    ArrayList<String> product_name;
    ArrayList<String>product_price;
    TextView my_store;
    BottomNavigationView bottomNavigationView;

    List<Upload> uploads;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    String[] username_ ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_my_store);

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.MyStore);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.profile_page:
                        startActivity(new Intent(getApplicationContext(),Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.MyStore:

                        return true;
                    case R.id.sell:
                        startActivity(new Intent(getApplicationContext(),Sell.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


        my_store = (TextView)findViewById(R.id.my_store);
        product_name=new ArrayList<>();
        product_price=new ArrayList<>();
        username_ = new String[1];
        uploads = new ArrayList<>();

        String get_confirmed_name = getIntent().getStringExtra("confirmed_name");
        String get_confirme_price= getIntent().getStringExtra("confirmed_price");
        product_name.add(get_confirmed_name);
        product_price.add(get_confirme_price);
        product_name.add("ph pc");
        product_name.add("iphone sx6");
        product_name.add("lenove pc");
        product_name.add("Nike AF1");
        product_price.add("R 5000");
        product_price.add("R 23000");
        product_price.add("R 4550");
        product_price.add("R 1800");



        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = snapshot.child("Name").getValue().toString();
                username_[0] = username;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(My_store.this,"Something went wrong",Toast.LENGTH_LONG).show();

            }
        });

        String userName = username_[0];
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot post:snapshot.getChildren()){
                    Upload upload = post.getValue(Upload.class);
                    uploads.add(upload);
                }
                adaptor=new com.example.etransect.Mystore_Adapter(My_store.this,product_name,product_price,uploads,userName);
                recyclerView.setAdapter(adaptor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(My_store.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });




        recyclerView= findViewById(R.id.reclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        my_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(My_store.this,MainActivity.class);
                startActivity(intent);
            }
        });

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                switch (direction){
                    case ItemTouchHelper.LEFT:
                        product_name.remove(position);
                        product_price.remove(position);
                        adaptor.notifyItemRemoved(position);
                        break;
                    case ItemTouchHelper.RIGHT:
                        product_name.remove(position);
                        product_price.remove(position);
                        adaptor.notifyItemRemoved(position);
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


    }

}