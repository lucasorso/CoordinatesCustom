package com.orsomob.coordinates;

import android.app.ActivityOptions;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.util.DisplayMetrics;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * Created by LucasOrso on 5/12/17.
 */

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        boolean deploy = true;
        if (deploy) {
            startActivity(new Intent(Splash.this, MainActivity.class),
                    ActivityOptions.makeSceneTransitionAnimation(Splash.this).toBundle());
            finish();
        } else {
            init();
        }
    }

    private void init() {
        ImageView lAirplaneSplash = (ImageView) findViewById(R.id.iv_airplane_splash);

        MediaPlayer lMediaPlayer = MediaPlayer.create(this, R.raw.airplane_landing_01);
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();

        float width = metrics.widthPixels;
        float height = metrics.heightPixels;

        TranslateAnimation lTranslateAnimation;
        lTranslateAnimation = new TranslateAnimation(-width, width, height, -height);
        lTranslateAnimation.setDuration(12000);
        lTranslateAnimation.setRepeatCount(5);
        lTranslateAnimation.setRepeatMode(2);
        lTranslateAnimation.setFillAfter(true);
        lMediaPlayer.start();
        lAirplaneSplash.startAnimation(lTranslateAnimation);

        getWindow().setExitTransition(new Explode());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Splash.this, MainActivity.class),
                        ActivityOptions.makeSceneTransitionAnimation(Splash.this).toBundle());
                finish();
            }
        }, 12000);
    }
}
