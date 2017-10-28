package com.naman.dev.serializrdemo;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class FinalActivity extends AppCompatActivity {

    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        location.setLatitude(24.2155);
        location.setLongitude(77.2205);
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

        String url = "http://maps.googleapis.com/maps/api/distancematrix/json?origins28.5450,77.1926&destinations=" + location.getLatitude() + "," + location.getLongitude() + "&mode=driving&language=en-EN&sensor=false";
        URL obj = null;

// handle Exception
        try {
            obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while((inputLine = in.readLine())!=null){
                response.append(inputLine);
            }
            in.close();
            Log.d("Hello",response.toString());
        } catch (Exception e) {
            System.out.println("The URL is not valid.");
            System.out.println(e.getMessage());
        }

        TextView textView = (TextView) findViewById(R.id.status);

    }
}
