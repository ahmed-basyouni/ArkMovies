package com.ark.movieapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.ark.movieapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreen extends AppCompatActivity {

    @BindView(R.id.splashImage) ImageView splashImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ButterKnife.bind(this);

        getSupportActionBar().hide();

        if(savedInstanceState == null){

            Animation splashAnmation = AnimationUtils.loadAnimation(this, R.anim.splash_animation);

            splashAnmation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            startActivity(new Intent(SplashScreen.this, HomeActivity.class));
                            SplashScreen.this.finish();

                        }
                    }, 1000);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            splashImage.startAnimation(splashAnmation);
        }

    }
}
