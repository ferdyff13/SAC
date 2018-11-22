package id.itsofteam.sac.sac;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import static id.itsofteam.sac.sac.utilities.ColorUtil.getSplashScreenColor;
import static id.itsofteam.sac.sac.utilities.UtilMethods.getWindowSize;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 3;
    private ImageView splashImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().getDecorView().setBackgroundColor(getSplashScreenColor(this));
        setResponsiveSplashLogo();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(id.itsofteam.sac.sac.SplashActivity.this, MainActivity.class));
                finish();
            }
        }, SPLASH_DURATION * 1000);
    }

    private void setResponsiveSplashLogo() {
        splashImageView = (ImageView)findViewById(R.id.splashImageView);
        splashImageView.getLayoutParams().width = (int) (getWindowSize(this).x * 0.75);
    }
}
