package com.wowtechnow.nasaphotoviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private ImageView SplashScreenImage;
    private TextView appName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        SplashScreenImage = findViewById(R.id.SplashScreen);
        appName = findViewById(R.id.appname);
        Splashscreen splashscreen = new Splashscreen();
        splashscreen.start();
    }
    public class Splashscreen extends Thread{
        public void run(){
            try {

                Animation anima = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anima);
                SplashScreenImage.setAnimation(anima);
                appName.setAnimation(anima);
                sleep(1000*2);
            }catch (Exception e){
                e.printStackTrace();
            }
            Intent intent = new Intent(getApplicationContext(),APODFragmentActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            SplashScreen.this.finish();
        }
    }
}