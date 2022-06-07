package com.example.etransect;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Open_Loading extends AppCompatActivity {
    private static int SPLASH_SCREEN = 5000;
 Animation topAnim,BottomAnim;
 ImageView imageView;
 TextView logo , slogon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open__loading);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
      topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
      BottomAnim =AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

      imageView = findViewById(R.id.loading_image);
      logo= findViewById(R.id.loading_name);
      slogon = findViewById(R.id.loading_slogan);

      imageView.setAnimation(topAnim);
      logo.setAnimation(BottomAnim);
      slogon.setAnimation(BottomAnim);

      new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
              Intent intent = new Intent(Open_Loading.this, com.example.etransect.Login.class);
              startActivity(intent);
              finish();
          }
      },SPLASH_SCREEN);

    }
}