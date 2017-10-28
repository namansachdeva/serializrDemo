package com.naman.dev.serializrdemo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hypertrack.lib.HyperTrack;
import com.hypertrack.lib.callbacks.HyperTrackCallback;
import com.hypertrack.lib.models.ErrorResponse;
import com.hypertrack.lib.models.SuccessResponse;
import com.hypertrack.lib.models.User;
import com.hypertrack.lib.models.UserParams;

public class MainActivity extends AppCompatActivity {
    private  Location location;
    private Button signInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize HyperTrack SDK with your Publishable Key here
        // Refer to documentation at
        // https://docs.hypertrack.com/gettingstarted/authentication.html
        // @NOTE: Add **"pk_ffd3d517c21aeda806c625d3a28fce3d7cf4bee7"** here for SDK to be
        // authenticated with HyperTrack Server
        HyperTrack.initialize(this, "pk_ffd3d517c21aeda806c625d3a28fce3d7cf4bee7");
        signInButton = (Button) findViewById(R.id.signInBtn);
                onLoginButtonClick(signInButton);


    }

    /**
     * Call this method when User Login button has been clicked.
     * Note that this method is linked with the layout file (content_login.xml)
     * using this button's layout's onClick attribute. So no need to invoke this
     * method or handle login button's click listener explicitly.
     *
     * @param view
     */
    public void onLoginButtonClick(View view) {
        // Check if Location Settings are enabled, if yes then attempt
        // DriverLogin
        checkForLocationSettings();
    }

    /**
     * Call this method to check Location Settings before proceeding for UserLogin
     */
    private void checkForLocationSettings() {
        // Check for Location permission
        if (!HyperTrack.checkLocationPermission(this)) {
            HyperTrack.requestPermissions(this);
            return;
        }

        // Check for Location settings
        if (!HyperTrack.checkLocationServices(this)) {
            HyperTrack.requestLocationServices(this);
        }

        // Location Permissions and Settings have been enabled
        // Proceed with your app logic here i.e User Login in this case
        attemptLogin("Dummy Name","9876543210","abcd");
    }

    /**
     * Handle on Grant Location Permissions request accepted/denied result
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions,
                grantResults);

        if (requestCode == HyperTrack.REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {
                // Check if Location Settings are enabled to proceed
                checkForLocationSettings();

            } else {
                // Handle Location Permission denied error
                Toast.makeText(this, "Location Permission denied.",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }


    /**
     * Handle on Enable Location Services request accepted/denied result
     *
     * @param requestCode
     * @param resultCode
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HyperTrack.REQUEST_CODE_LOCATION_SERVICES) {
            if (resultCode == Activity.RESULT_OK) {
                // Check if Location Settings are enabled to proceed
                checkForLocationSettings();

            } else {
                // Handle Enable Location Services request denied error
                Toast.makeText(this, "Enable location",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }
    private void attemptLogin(String name, String phoneNumber,String lookupId){
        /**
         * Get or Create a User for given lookupId on HyperTrack Server here to
         * login your user & configure HyperTrack SDK with this generated
         * HyperTrack UserId.
         * OR
         * Implement your API call for User Login and get back a HyperTrack
         * UserId from your API Server to be configured in the HyperTrack SDK.
         */
        UserParams userParams = new UserParams().setName(name).setPhone(phoneNumber).setLookupId(lookupId);
        HyperTrack.getOrCreateUser(userParams, new HyperTrackCallback() {
            @Override
            public void onSuccess(@NonNull SuccessResponse successResponse) {
                User user = (User) successResponse.getResponseObject();
                // Handle createUser success here, if required
                // HyperTrack SDK auto-configures UserId on createUser API call,
                // so no need to call HyperTrack.setUserId() API

                // On UserLogin success
                onUserLoginSuccess();
            }

            @Override
            public void onError(@NonNull ErrorResponse errorResponse) {
               Toast.makeText(MainActivity.this, "Error occurred"
                                + " " + errorResponse.getErrorMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

            }
    /**
     * Call this method when user has successfully logged in
     */
    private void onUserLoginSuccess() {
        HyperTrack.startTracking(new HyperTrackCallback() {
            @Override
            public void onSuccess(@NonNull SuccessResponse successResponse) {

                Toast.makeText(MainActivity.this, "Success",
                        Toast.LENGTH_SHORT).show();
                HyperTrack.requestPermissions(MainActivity.this);
                HyperTrack.requestLocationServices(MainActivity.this);
                HyperTrack.getCurrentLocation(new HyperTrackCallback() {
                    @Override
                    public void onSuccess(@NonNull SuccessResponse successResponse) {
                        location = (Location) successResponse.getResponseObject();
                        Toast.makeText(MainActivity.this,"LOCATION"+location.toString(),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(@NonNull ErrorResponse errorResponse) {
                        Toast.makeText(MainActivity.this,"ERROR"+errorResponse.toString(),Toast.LENGTH_LONG).show();
                    }
                });
                // Start User Session by starting MainActivity
                Intent mainActivityIntent = new Intent(
                        MainActivity.this, LandingActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainActivityIntent);
                finish();
            }

            @Override
            public void onError(@NonNull ErrorResponse errorResponse) {
                Toast.makeText(MainActivity.this, "Error"
                                + " " + errorResponse.getErrorMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}
