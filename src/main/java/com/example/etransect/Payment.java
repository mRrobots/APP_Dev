package com.example.etransect;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Payment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
    }

    public void Pay(View view) {
        Toast.makeText(Payment.this,"Seller of this product will contact you",Toast.LENGTH_SHORT).show();

    }
}