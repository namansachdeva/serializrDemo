package com.naman.dev.serializrdemo;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hypertrack.lib.HyperTrack;
import com.hypertrack.lib.callbacks.HyperTrackCallback;
import com.hypertrack.lib.models.ErrorResponse;
import com.hypertrack.lib.models.SuccessResponse;

public class LandingActivity extends AppCompatActivity {
    private Location location;
    TextView locationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        setTitle("Landing Activity");
        // Initialize UI Views
        initUIViews();
        Gson gson;
        HyperTrack.requestPermissions(this);
        HyperTrack.requestLocationServices(this);
        HyperTrack.getCurrentLocation(new HyperTrackCallback() {
            @Override
            public void onSuccess(@NonNull SuccessResponse successResponse) {
                location = (Location) successResponse.getResponseObject();
                Toast.makeText(LandingActivity.this, "LOCATION" + location.toString(), Toast.LENGTH_LONG).show();
                locationTextView.setText("Latitude is: " + location.getLatitude() + "\nLongitude is:" + location.getLongitude());
                String url = "http://maps.googleapis.com/maps/api/distancematrix/json?origins=28.5450,77.1926&destinations=" + location.getLatitude() + "," + location.getLongitude() + "&mode=driving&language=en-EN&sensor=false";
                
            }

            @Override
            public void onError(@NonNull ErrorResponse errorResponse) {
                Toast.makeText(LandingActivity.this, "ERROR" + errorResponse.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void initUIViews() {
        // Initialize AssignAction Button
        Button logoutButton = (Button) findViewById(R.id.logout_btn);
        Button redeemButton = (Button) findViewById(R.id.redeembtn);
        locationTextView = (TextView) findViewById(R.id.locationTextView);

        if (logoutButton != null)
            logoutButton.setOnClickListener(logoutButtonClickListener);
        if (redeemButton != null)
            redeemButton.setOnClickListener(redeemButtonClickListener);
    }

    private View.OnClickListener redeemButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(LandingActivity.this, FinalActivity.class);
            i.putExtra("lat", location.getLatitude());
            i.putExtra("long", location.getLongitude());
            startActivity(i);
        }
    };
    // Click Listener for AssignAction Button
    private View.OnClickListener logoutButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(LandingActivity.this, "Successfully logged out",
                    Toast.LENGTH_SHORT).show();

            // Stop HyperTrack SDK
            HyperTrack.stopTracking();

            // Proceed to LoginActivity for a fresh User Login
            Intent loginIntent = new Intent(LandingActivity.this,
                    MainActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
            finish();
        }
    };

}
