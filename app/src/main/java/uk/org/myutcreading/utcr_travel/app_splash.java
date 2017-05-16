package uk.org.myutcreading.utcr_travel;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by ashcon on 5/15/17.
 */

public class app_splash extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_splash);
        TextView travel = (TextView) findViewById(R.id.UI_font);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/SourceSansPro-Light.otf");
        travel.setTypeface(typeface);
        travel.setTextColor(Color.WHITE);
        travel.setText("Travel");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(ContextCompat.checkSelfPermission(app_splash.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(app_splash.this, app_perms_screen.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(app_splash.this, app_menu.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }
}
