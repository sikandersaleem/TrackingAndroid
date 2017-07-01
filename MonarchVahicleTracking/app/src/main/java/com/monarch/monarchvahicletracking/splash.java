package com.monarch.monarchvahicletracking;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

public class splash extends AppCompatActivity {

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    /** Called when the activity is first created. */
    Thread splashTread;

    private static int SPLASH_TIME_OUT = 3000;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //splashrun();
        StartAnimations();
    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay2);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        //ImageView iv = (ImageView) findViewById(R.id.imageView2);
        TextView tv= (TextView)findViewById(R.id.textView4);
        //TextView tv2= (TextView)findViewById(R.id.textView4);
        tv.clearAnimation();
        tv.startAnimation(anim);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 3500) {
                        sleep(200);
                        waited += 200;
                    }
                    Intent intent = new Intent(splash.this,
                            LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    splash.this.finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    splash.this.finish();
                }

            }
        };
        splashTread.start();

    }

    public void splashrun(){
        new Handler().postDelayed(new Runnable() {

            public void run() {
                /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    //Toast.makeText(this, "Signed in", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(this,"check: retreving data from fire base.",Toast.LENGTH_SHORT).show();
                    finish();
                    Intent i = new Intent(splash.this, MainActivity.class);
                    startActivity(i);
                } else {
                    // User is signed out
                    finish();
                    Intent i = new Intent(splash.this, LoginActivity.class);
                    startActivity(i);
                }*/
                finish();
                Intent i = new Intent(splash.this, LoginActivity.class);
                startActivity(i);

            }
        }, SPLASH_TIME_OUT);
    }
}
