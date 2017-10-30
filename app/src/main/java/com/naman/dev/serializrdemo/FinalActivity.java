package com.naman.dev.serializrdemo;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class FinalActivity extends AppCompatActivity {

    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);


//        Intent i = getIntent();
//        try {
//
//            double lat = Double.parseDouble(i.getStringExtra("lat"));
//            double longi = Double.parseDouble(i.getStringExtra("long"));
//            location.setLatitude(lat);
//            location.setLongitude(longi);
//        } catch (Exception e){
//            e.printStackTrace();
//        }



        TextView textView = (TextView) findViewById(R.id.status);

    }
}
