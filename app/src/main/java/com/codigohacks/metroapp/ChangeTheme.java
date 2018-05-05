package com.codigohacks.metroapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ChangeTheme extends AppCompatActivity {

    Button red,blue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_theme);

        red = (Button)findViewById(R.id.red_theme);
        blue = (Button)findViewById(R.id.blue_theme);

        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    setTheme(R.style.AppTheme);
                Toast.makeText(getApplicationContext(),"Please Restart",Toast.LENGTH_SHORT).show();
            }
        });

        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTheme(R.style.blue_theme);
                Toast.makeText(getApplicationContext(),"Please Restart",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
