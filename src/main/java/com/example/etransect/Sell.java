package com.example.etransect;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Sell extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private  Button btn,btn_upload;
    private Uri uri;
    private EditText pro_price, pro_descrip, name;
    private ImageView imageView;
    private static final int SELECT_IMAGE_CODE = 1;
    ProgressBar progressBar;
    BottomNavigationView bottomNavigationView;
    String[] username_ ;


    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private StorageTask storageTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_sell);


        Spinner spinner = findViewById(R.id.category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        
        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.sell);
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
                        startActivity(new Intent(getApplicationContext(), com.example.etransect.My_store.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.sell:
                        return true;
                }
                return false;
            }
        });

        pro_descrip = (EditText) findViewById(R.id.description);
        name = (EditText) findViewById(R.id.name);
        pro_price = (EditText) findViewById(R.id.price);
        //catagory = (EditText) findViewById(R.id.category);
        btn = findViewById(R.id.btn);
        progressBar = findViewById(R.id.progressBar3);
        username_ = new String[1];

        imageView = findViewById(R.id.imageView);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = snapshot.child("Email").getValue().toString();
                username_[0] = username;
                new Global(username_[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Sell.this,"Something went wrong",Toast.LENGTH_LONG).show();

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, SELECT_IMAGE_CODE);

            }

        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = username_[0];
                String pro_name = name.getText().toString();
                String descrip = pro_descrip.getText().toString();
                String price_pro = pro_price.getText().toString();
                Intent intent = new Intent(Sell.this, My_store.class);
                intent.putExtra("descrip", descrip);
                intent.putExtra("name", pro_name);
                intent.putExtra("img", uri);
                intent.putExtra("price", price_pro);
                intent.putExtra("user", username);
                if(storageTask!=null){
                    Toast.makeText(Sell.this,"Some file is uploading,relax ",Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadFile();
                    Toast.makeText(Sell.this,"Uploaded successfully",Toast.LENGTH_SHORT).show();
                }
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            uri = data.getData();
           // imageView.setImageURI(uri);
            Picasso.get().load(uri).fit().into(imageView);

        }
    }
    private  String getfileEXtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadFile(){
        if(uri!=null){
            StorageReference reference = storageReference.child((System.currentTimeMillis()+"."+getfileEXtension(uri)));

            storageTask = reference.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    progressBar.setProgress(0);
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Upload upload = new Upload(name.getText().toString().trim(),
                                    uri.toString(),
                                    pro_descrip.getText().toString().trim(),
                                    pro_price.getText().toString().trim(),"No name",username_[0]
//                            catagory.getText().toString().trim()
                            );
                            String upload_id = databaseReference.push().getKey();
                            databaseReference.child(upload_id).setValue(upload);
                            Toast.makeText(Sell.this,"Upload successfully",Toast.LENGTH_LONG).show();
                        }
                    });
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    },500);
                    Toast.makeText(Sell.this,"Upload successfully",Toast.LENGTH_LONG).show();
//                    Upload upload = new Upload(name.getText().toString().trim(),
//                            taskSnapshot.getMetadata().getReference().getDownloadUrl().toString(),
//                            pro_descrip.getText().toString().trim(),
//                            pro_price.getText().toString().trim(),"No name"
//                            catagory.getText().toString().trim()
//                            );
//                    String upload_id = databaseReference.push().getKey();
//                    databaseReference.child(upload_id).setValue(upload);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull  Exception e) {
                    Toast.makeText(Sell.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull  UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                    progressBar.setProgress((int) progress);
                }
            });
        }
        else {
            Toast.makeText(this,"No file selected",Toast.LENGTH_SHORT).show();
            return;
        }

    }


    private  String getFile(){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), "You have selected"+text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
