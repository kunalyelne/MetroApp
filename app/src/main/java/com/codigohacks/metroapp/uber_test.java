package com.codigohacks.metroapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.uber.sdk.android.core.UberSdk;
import com.uber.sdk.android.rides.RideParameters;
import com.uber.sdk.android.rides.RideRequestButtonCallback;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.rides.client.SessionConfiguration;
import com.uber.sdk.android.rides.RideRequestButton;
import com.uber.sdk.rides.client.ServerTokenSession;
import com.uber.sdk.rides.client.error.ApiError;

public class uber_test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uber_test);
        SessionConfiguration config = new SessionConfiguration.Builder()
                // mandatory
                .setClientId("<CLIENT_ID>")
                // required for enhanced button features
                .setServerToken("<TOKEN>")
                // required for implicit grant authentication
                .setRedirectUri("<REDIRECT_URI>")
                // optional: set sandbox as operating environment
                .setEnvironment(SessionConfiguration.Environment.SANDBOX)
                .build();

        UberSdk.initialize(config);

        // get the context by invoking ``getApplicationContext()``, ``getContext()``, ``getBaseContext()`` or ``this`` when in the activity class
        RideRequestButton requestButton = new RideRequestButton(this);
        // get your layout, for instance:
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.uber);
        layout.addView(requestButton);

        RideParameters rideParams = new RideParameters.Builder()
                // Optional product_id from /v1/products endpoint (e.g. UberX). If not provided, most cost-efficient product will be used
                .setProductId("a1111c8c-c720-46c3-8534-2fcdd730040d")
                // Required for price estimates; lat (Double), lng (Double), nickname (String), formatted address (String) of dropoff location
                .setDropoffLocation(
                        (double) 37.775304f, -122.417522, "Uber HQ", "1455 Market Street, San Francisco")
                // Required for pickup estimates; lat (Double), lng (Double), nickname (String), formatted address (String) of pickup location
                .setPickupLocation((double) 37.775304f, -122.417522, "Uber HQ", "1455 Market Street, San Francisco")
                .build();
// set parameters for the RideRequestButton instance
        requestButton.setRideParameters(rideParams);

        ServerTokenSession session = new ServerTokenSession(config);
        requestButton.setSession(session);
        requestButton.loadRideInformation();

        RideRequestButtonCallback callback = new RideRequestButtonCallback() {

            @Override
            public void onRideInformationLoaded() {
                // react to the displayed estimates
            }

            @Override
            public void onError(ApiError apiError) {
                // API error details: /docs/riders/references/api#section-errors
            }

            @Override
            public void onError(Throwable throwable) {
                // Unexpected error, very likely an IOException
            }
        };
        requestButton.setCallback(callback);

    }
}
