package com.codigohacks.metroapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Result extends AppCompatActivity {

    private RecyclerView recyclerView;
    TextView fare,time,stops,switches;
    private RecyclerView.Adapter adapter;
    private List<Route_items> listItems;
    Button imap;
    String s1,s2,path;
    int src_code,dest_code;
    ProgressDialog progressDialog ;
    File mydownload = new File (Environment.getExternalStorageDirectory()+ "/METRO/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        recyclerView = (RecyclerView)findViewById(R.id.route);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        imap = (Button)findViewById(R.id.imap);

        imap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Result.this,InteractiveMap.class);
                startActivity(i);
            }
        });

        fare = (TextView)findViewById(R.id.fare);
        time = (TextView)findViewById(R.id.time);
        stops = (TextView)findViewById(R.id.stops);
        switches = (TextView)findViewById(R.id.switches);

        fare.setText("-");
        time.setText("-");
        stops.setText("-");
        switches.setText("-");

        path = mydownload.getAbsolutePath() + "/routes.json" ;

        listItems = new ArrayList<>();
        Intent intent = getIntent();
        s1=intent.getStringExtra("src");
        s2=intent.getStringExtra("des");
        src_code=intent.getIntExtra("src_code",-1);
        dest_code=intent.getIntExtra("dest_code",-1);
        TextView source_station = (TextView)findViewById(R.id.source);
        source_station.setText(s1);
        TextView dest_station = (TextView)findViewById(R.id.destination);
        dest_station.setText(s2);

        loadRecylerViewData();
    }

    private void loadRecylerViewData(){

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        listItems.clear();

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray(String.valueOf(src_code));

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                if(jo_inside.getString("destination").equals(String.valueOf(dest_code)))
                {
                    JSONArray interchanges = jo_inside.getJSONArray("interchanges");

                    JSONArray routes = jo_inside.getJSONArray("route");

                    JSONObject fees = jo_inside.getJSONObject("fare");
                    fare.setText(fees.getString("normal_fare"));
                    time.setText(jo_inside.getString("time"));
                    stops.setText(String.valueOf(routes.length()-1));
                    switches.setText(String.valueOf(interchanges.length()));
                        for(int j=0; j<routes.length();j++)
                        {
                            for(int k=0;k<interchanges.length();k++)
                            {
                                if(routes.getString(j).equals(interchanges.getString(k)))
                                {
                                    Route_items item = new Route_items(
                                            interchanges.getString(k),
                                            routes.getString(j)
                                    );

                                    listItems.add(item);
                                }
                                else
                                {
                                    Route_items item = new Route_items(
                                            null,
                                            routes.getString(j)
                                    );

                                    listItems.add(item);
                                }
                            }

                        }

                    adapter = new Route_Adapter(listItems,this);
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                    progressDialog.dismiss();
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public String loadJSONFromAsset() {
        String json = null;
        try {
            FileInputStream fis = new FileInputStream (new File(path));
            int size = fis.available();
            byte[] buffer = new byte[size];
            fis.read(buffer);
            fis.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}