package com.codigohacks.metroapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class MainActivity extends AppCompatActivity {

    MaterialSpinner fstation,dstation;
    String item,undo=null;
    String [] x={};
    int pos=-1,pos2=-1;
    ArrayList<String> stat= new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    final Intent i = new Intent(MainActivity.this, IntroTour.class);

                    runOnUiThread(new Runnable() {
                        @Override public void run() {
                            startActivity(i);
                        }
                    });

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();



        fstation = (MaterialSpinner)findViewById(R.id.fstation);
        dstation = (MaterialSpinner)findViewById(R.id.dstation);
        Button sub = (Button)findViewById(R.id.submit);

        InputStream is=getResources().openRawResource(R.raw.station);
        int size = 0;
        try {
            size = is.available();
            String STN;
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buffer = new byte[size];
        try {
            is.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String json = null;
        try {
            json = new String(buffer, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray array = null;
        try {
            array = jsonObject.getJSONArray("station");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONObject obj = new JSONObject(json);
            JSONArray m_jArry = obj.getJSONArray("station");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                stat.add(jo_inside.getString("station"));
                //STN[i]=(jo_inside.getString("station"));
                //Add your values in your `ArrayList` as below:
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(stat);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stat);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fstation.setAdapter(adapter);


        // Spinner click listener
        fstation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                item = parent.getItemAtPosition(position).toString();

                pos=position;

                if (position!=-1) {

                    Toast.makeText(MainActivity.this,"Selected:"+item,Toast.LENGTH_SHORT).show();

                }
                else if(pos==pos2 && pos2!=-1 && pos!=-1)
                {
                    Toast.makeText(MainActivity.this,"Congrats Genius! You've reached your destination",Toast.LENGTH_SHORT).show();
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stat);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dstation.setAdapter(adapter);

        dstation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                item = parent.getItemAtPosition(position).toString();

                pos2=position;

                if (position!=-1 && pos!=pos2) {

                    Toast.makeText(MainActivity.this,"Selected:"+item,Toast.LENGTH_SHORT).show();

                }
                else if(pos==pos2 && pos!=-1 && pos2!=-1)
                {
                    Toast.makeText(MainActivity.this,"Congrats Genius! You've reached your destination",Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pos!=-1 && pos2!=-1) {
                    ;
                }
                else {
                    Toast.makeText(MainActivity.this,"Please select Source and Destination",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


}
