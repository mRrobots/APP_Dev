package com.example.etransect;
import java.util.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.util.NumberUtils;
import com.squareup.picasso.Picasso;

public class Item_details extends AppCompatActivity {
     TextView fromGridName,fromGridPrice,fromGridseller,fromGridlike,fromGriddescr;
     Button back,buy;
     ImageView fromGridImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_item_details);

        String get_confirmed_name_grid = getIntent().getStringExtra("confirmed_name");
        String get_confirme_price_grid= getIntent().getStringExtra("confirmed_price");
        String get_confirmed_descrip_grid = getIntent().getStringExtra("confirmed_descrip");
//        String get_confirmed_image = getIntent().getStringExtra("image");
        String image = getIntent().getStringExtra("image_");
        String seller = getIntent().getStringExtra("seller");
        String likes = getIntent().getStringExtra("likes");

        fromGridName = (TextView)findViewById(R.id.detailed_name);
        fromGridPrice =(TextView)findViewById(R.id.detailed_price);
        fromGriddescr =(TextView)findViewById(R.id.detailed_description);
        fromGridImage =(ImageView)findViewById(R.id.detailed_image);
        fromGridseller =(TextView) findViewById(R.id.seller_detatials);
        fromGridlike =(TextView) findViewById(R.id.numberOfLikes);


      //  fromGridImage.setImageResource(Integer.parseInt(get_confirmed_image));
        Post_picture(image);
        fromGridName.setText(get_confirmed_name_grid);
        fromGridPrice.setText(get_confirme_price_grid);
        fromGriddescr.setText(get_confirmed_descrip_grid);
        fromGridseller.setText(seller);
        fromGridlike.setText(likes);


        back=(Button) findViewById(R.id.back_button);
        buy = (Button) findViewById(R.id.buy); 
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Item_details.this,MainActivity.class);
                startActivity(intent);
            }
        });
        
    }

    public void buy(View view) {
        Intent intent= new Intent(this,Payment.class);
        startActivity(intent);
    }

    public void Post_picture(String str){
        int check;
        try{
          check  = Integer.parseInt(str);
            Picasso.get().load(check).fit().into(fromGridImage);
        }
        catch (NumberFormatException numberFormatException){
            Picasso.get().
                    load(str).
                    fit().
                    into(fromGridImage);
        }
    }
}