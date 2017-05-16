package uk.org.myutcreading.utcr_travel;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashcon on 5/15/17.
 */

public class app_perms_screen extends AppCompatActivity {
    public static Button button_menu;
    public static TextView text;
    public static Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_permissions_screen);
        text = (TextView)findViewById(R.id.guide_text);
        button = (Button) findViewById(R.id.next_button_perms);
        button_menu = (Button) findViewById(R.id.next_button_menu);
        button_menu.setVisibility(View.INVISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    new wait_for_perms().execute("");
                    int perms_to_add = 0;
                    List<String> perms_temp = new ArrayList<>();
                    if (ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        perms_temp.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
                        perms_to_add++;
                    }
                    if (perms_to_add > 0) {
                        String[] perms = new String[perms_temp.size()];
                        perms_temp.toArray(perms);
                        askForPermission(perms);
                    }
                }
                if (Build.VERSION.SDK_INT < 23) {
                    Toast.makeText(getApplicationContext(), "PLEASE GO TO APPLICATION SETTINGS TO GRANT PERMISSIONS", Toast.LENGTH_LONG).show();
                }
            }
        });
        button_menu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getBaseContext(), app_menu.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void askForPermission(String [] permission){
        ActivityCompat.requestPermissions(this, permission, 0x1);
    }
    private class wait_for_perms extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            boolean completed = false;
            while(completed !=true){
                System.out.println("in loop");
                if (ContextCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    completed = true;
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            System.out.println("loop finished"+result);
            text.setText("Permissions granted - please press");
            button.setVisibility(View.INVISIBLE);
            button_menu.setVisibility(View.VISIBLE);
        }
    }
}
