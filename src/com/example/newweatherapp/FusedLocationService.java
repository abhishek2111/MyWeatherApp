package com.example.newweatherapp;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;

public class FusedLocationService implements LocationListener,
		ConnectionCallbacks, OnConnectionFailedListener {

	private LocationRequest locationRequest;
	private GoogleApiClient mGoogleApiClient;
	private Location location;
	private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;
	private static final long INTERVAL = 1000 * 30;
	private static final long FASTEST_INTERVAL = 1000 * 5;
	private static final long ONE_MIN = 1000 * 60;
	private static final long REFRESH_TIME = ONE_MIN * 5;
	private static final float MINIMUM_ACCURACY = 50.0f;
	Activity activity;

	public FusedLocationService(Activity activity) {
		locationRequest = LocationRequest.create();
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
				.setInterval(INTERVAL).setFastestInterval(FASTEST_INTERVAL);
		this.activity = activity;

		mGoogleApiClient = new GoogleApiClient.Builder(activity)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();

		if (mGoogleApiClient != null) {
			mGoogleApiClient.connect();
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {

	}

	@Override
	public void onConnected(Bundle arg0) {
		Location curLoc = fusedLocationProviderApi
				.getLastLocation(mGoogleApiClient);
		if (curLoc != null && curLoc.getTime() > REFRESH_TIME) {
			location = curLoc;
		} else {
			fusedLocationProviderApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
			Executors.newScheduledThreadPool(1).schedule(new Runnable() {
                @Override
                public void run() {
                    fusedLocationProviderApi.removeLocationUpdates(mGoogleApiClient,
                            FusedLocationService.this);
                }
            }, ONE_MIN, TimeUnit.MILLISECONDS);
		}
	}

	@Override
	public void onConnectionSuspended(int arg0) {

	}

	@Override
	public void onLocationChanged(Location location) {
        if (null == this.location || location.getAccuracy() < this.location.getAccuracy()) {
            this.location = location;
            if(location!=null){
            	if (this.location.getAccuracy() < MINIMUM_ACCURACY) {
            		fusedLocationProviderApi.removeLocationUpdates(mGoogleApiClient, this);
            	}
            }
        }
	}
	
	public Location getLocation() {
        return this.location;
    }
}
