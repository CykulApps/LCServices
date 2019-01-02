package com.cykulapps.lcservices.activities;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import com.cykulapps.lcservices.R;
import com.cykulapps.lcservices.common.Prefs;
import com.cykulapps.lcservices.login.LoginActivity;


public class SplashScreen extends Activity {

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);
        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.

        linearLayout = findViewById(R.id.splash_layout_linear);

        animateContentVisible();
        // start the animation
        linearLayout.setVisibility(View.VISIBLE);

        int SPLASH_TIME_OUT = 5000;
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Log.e("boolean","value"+Prefs.getBoolean(Prefs.LOGGED_IN,false));

                if (Prefs.getBoolean(Prefs.LOGGED_IN,false))
                {
                     startActivity(new Intent(SplashScreen.this, AdminMainActivity.class));
                     SplashScreen.this.finish();
                }
                else if(Prefs.getBoolean(Prefs.LOGGED_IN_VENDEOR,false)){
                    startActivity(new Intent(SplashScreen.this, EventsSubActivity.class));
                    SplashScreen.this.finish();
                }
                else
                {
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                    SplashScreen.this.finish();
                }

                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void animateContentVisible() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                            // If lollipop use reveal animation. On older phones use fade animation.
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                            // get the center for the animation circle
                            final int cx = (linearLayout.getLeft() + linearLayout.getRight()) / 2;
                            final int cy = (linearLayout.getTop() + linearLayout.getBottom()) / 2;

                            // get the final radius for the animation circle
                            int dx = Math.max(cx, linearLayout.getWidth() - cx);
                            int dy = Math.max(cy, linearLayout.getHeight() - cy);
                            float finalRadius = (float) Math.hypot(dx, dy);

                            try {
                                Animator animator = ViewAnimationUtils.createCircularReveal(linearLayout, cx, cy, 0, finalRadius);
                                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                                animator.setDuration(1250);
                                linearLayout.setVisibility(View.VISIBLE);
                                animator.start();
                            } catch (Exception e) {
                               // startHomeActivity();
                            }

                        } else {
                            linearLayout.setAlpha(0f);
                            linearLayout.setVisibility(View.VISIBLE);
                            linearLayout.animate()
                                    .alpha(1f)
                                    .setDuration(1000)
                                    .setListener(null);
                        }
                    }
                }, 330);
            }
        });


    }
}
