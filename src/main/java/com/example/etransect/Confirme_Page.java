package com.example.etransect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class Confirme_Page extends AppCompatActivity {

    TextView get_description, get_price, seller_get, new_descrip;
    ImageView get_image;
    Button confirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_confirme__page);
        confirm = (Button) findViewById(R.id.confirm_button);
        new_descrip = (TextView) findViewById(R.id.descrip_2);
        get_description = (TextView) findViewById(R.id.get_description);
        get_price = (TextView) findViewById(R.id.get_price);
        seller_get = (TextView) findViewById(R.id.sellerInfo);
        get_image = (ImageView) findViewById(R.id.imageView2);
        String descri = getIntent().getStringExtra("descrip");
        String pri = getIntent().getStringExtra("price");
        String p_name = getIntent().getStringExtra("name");
        Picasso.get().load(getIntent().getStringExtra("img")).into(get_image);
        get_description.setText(p_name);
        new_descrip.setText(descri);
        get_price.setText(pri);
        seller_get.setText("MIKE PHONE:07835456");
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String confirmed_name = get_description.getText().toString();
                String confirmed_price = get_price.getText().toString();
                Intent intent = new Intent(Confirme_Page.this, com.example.etransect.My_store.class);
                intent.putExtra("confirmed_name", confirmed_name);
                intent.putExtra("confirmed_price", confirmed_price);
                startActivity(intent);
            }
        });


    }
}