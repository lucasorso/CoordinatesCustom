package com.orsomob.coordinates

import android.app.ActivityOptions
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.transition.Explode
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import com.orsomob.coordinates.activitys.MainActivity

/**
 * Created by LucasOrso on 5/22/17.
 */
class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        val deploy = true
        if (deploy) {
            startActivity(Intent(this, MainActivity::class.java),
                    ActivityOptions.makeSceneTransitionAnimation(this@SplashScreen).toBundle())
            finish()
        } else {
            init()
        }
    }

    private fun init() {
        val lAirplaneSplash = findViewById(R.id.iv_airplane_splash) as ImageView

        val lMediaPlayer = MediaPlayer.create(this, R.raw.airplane_landing_01)
        val metrics = this.resources.displayMetrics

        val width = metrics.widthPixels.toFloat()
        val height = metrics.heightPixels.toFloat()

        val lTranslateAnimation: TranslateAnimation
        lTranslateAnimation = TranslateAnimation(-width, width, height, -height)
        lTranslateAnimation.duration = 12000
        lTranslateAnimation.repeatCount = 5
        lTranslateAnimation.repeatMode = 2
        lTranslateAnimation.fillAfter = true
        lMediaPlayer.start()
        lAirplaneSplash.startAnimation(lTranslateAnimation)

        window.exitTransition = Explode()

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java),
                    ActivityOptions.makeSceneTransitionAnimation(this@SplashScreen).toBundle())
            finish()
        }, 12000)
    }

}