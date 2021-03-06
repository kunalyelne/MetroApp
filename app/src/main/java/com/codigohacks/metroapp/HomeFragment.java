package com.codigohacks.metroapp;


import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.appus.splash.Splash;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import fr.ganfra.materialspinner.MaterialSpinner;

import static android.content.Context.DOWNLOAD_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }
    MaterialSpinner fstation,dstation;
    String item1,item2,undo=null,path;
    String [] x={};
    int pos=-1,pos2=-1,src_code, dest_code;

    DownloadManager downloadManager;
    Long reference;
    private ProgressDialog mProgress;

    File mydownload = new File (Environment.getExternalStorageDirectory()+ "/METRO");

    Button submit;
    ArrayList<String> stat= new ArrayList<String>();
    ArrayList<Integer> codes= new ArrayList<Integer>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Splash.Builder splash = new Splash.Builder(getActivity(),((AppCompatActivity)getActivity()).getSupportActionBar());
        splash.setBackgroundColor(getResources().getColor(R.color.orange));
        //splash.setBackgroundImage(getResources().getDrawable(R.drawable.back));
        splash.setSplashImage(getResources().getDrawable(R.mipmap.logo2));
        splash.setSplashImageColor(getResources().getColor(R.color.white));
        splash.perform();
        getActivity().setTitle("Home");
        View result = inflater.inflate(R.layout.fragment_home, container, false);



        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    final Intent i = new Intent(getContext(), IntroTour.class);

                    getActivity().runOnUiThread(new Runnable() {
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


        if(!mydownload.exists()) {
            mydownload.mkdir();//directory is created;
        }
        path = mydownload.getAbsolutePath() + "/routes.json" ;
        File file = new File(path);
        if (!file.exists()) {
            mProgress = new ProgressDialog(getContext());
            mProgress.setTitle("Processing...");
            mProgress.setMessage("Please wait...");
            mProgress.setCancelable(false);
            mProgress.setIndeterminate(true);
            mProgress.show();
            downloadManager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
            Uri uri = Uri.parse("https://www.codigohacks.com/metro/routes.json");
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setTitle("Important files");
            request.setDestinationInExternalPublicDir("/METRO/", "routes.json");
            reference = downloadManager.enqueue(request);
            Toast.makeText(getContext(), "Downloading...", Toast.LENGTH_SHORT).show();

            BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                        long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                        DownloadManager.Query query = new DownloadManager.Query();
                        query.setFilterById(reference);
                        downloadManager = (DownloadManager)context.getSystemService(DOWNLOAD_SERVICE);
                        Cursor c = downloadManager.query(query);
                        if (c.moveToFirst()) {
                            int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                            if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                                String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                                //TODO : Use this local uri and launch intent to open file
                                mProgress.cancel();
                            }
                        }
                    }
                }
            };
            getActivity().registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        }
        fstation = (MaterialSpinner)result.findViewById(R.id.fstation);
        dstation = (MaterialSpinner)result.findViewById(R.id.dstation);
        submit = (Button)result.findViewById(R.id.submit);

        InputStream is= null;
        is = getResources().openRawResource(R.raw.station);
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
                codes.add(jo_inside.getInt("code"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(stat);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, stat);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fstation.setAdapter(adapter);


        // Spinner click listener
        fstation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                item1 = parent.getItemAtPosition(position).toString();

                pos=position;
                if(pos!=-1)
                {
                    src_code= codes.get(pos);
                }

                if (position!=-1) {

                    Toast.makeText(getContext(),"Selected:"+item1,Toast.LENGTH_SHORT).show();

                }
                else if(pos==pos2 && pos2!=-1 && pos!=-1)
                {
                    Toast.makeText(getContext(),"Congrats Genius! You've reached your destination",Toast.LENGTH_SHORT).show();
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, stat);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dstation.setAdapter(adapter);

        dstation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                item2 = parent.getItemAtPosition(position).toString();

                pos2=position;
                if(pos2!=-1)
                {
                    dest_code= codes.get(pos2);
                }

                if (position!=-1 && pos!=pos2) {

                    Toast.makeText(getActivity(),"Selected:"+item2,Toast.LENGTH_SHORT).show();

                }
                else if(pos==pos2 && pos!=-1 && pos2!=-1)
                {
                    Toast.makeText(getActivity(),"Congrats Genius! You've reached your destination",Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pos!=-1 && pos2!=-1) {

                    Intent i = new Intent(getActivity(),Result.class);
                    i.putExtra("src",item1);
                    i.putExtra("des",item2);
                    i.putExtra("src_code",src_code);
                    i.putExtra("dest_code",dest_code);
                    startActivity(i);
                }
                else {
                    Toast.makeText(getActivity(),"Please select Source and Destination",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return  result;
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
