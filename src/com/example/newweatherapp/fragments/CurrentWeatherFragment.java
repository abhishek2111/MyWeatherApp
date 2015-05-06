package com.example.newweatherapp.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.newweatherapp.CurrentLocationAsyncTask;
import com.example.newweatherapp.CurrentWeatherAsyncTask;
import com.example.newweatherapp.R;
import com.example.newweatherapp.utilities.CityPreference;

public class CurrentWeatherFragment extends Fragment {

	private Activity activity;
	View rootview;
	Context ctx;
	static boolean isLocationData = false;

	public CurrentWeatherFragment() {

	}

	public static CurrentWeatherFragment newinstance(boolean islocdata) {
		CurrentWeatherFragment CurFrag = new CurrentWeatherFragment();
		Bundle args = new Bundle();

		CurFrag.setArguments(args);
		isLocationData = islocdata;
		return CurFrag;
	}

	public void refresh(boolean locdata) {
		if (locdata) {
			updateweatherdata(locdata);
		} else {
			updateweatherdata(locdata);
		}
	}

	public void locationrefresh(Location location) {
		if (haveNetwork()) {
			new CurrentLocationAsyncTask(activity, rootview).execute(location);
		} else {
			Toast.makeText(
					ctx,
					"No network Connection.Please connect to Wifi or Mobile Data",
					Toast.LENGTH_LONG).show();
		}
	}
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
		ctx = activity.getBaseContext();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootview = inflater.inflate(R.layout.fragment_current_weather,
				container, false);
		if (isLocationData) {
			updateweatherdata(isLocationData);
		} else {
			updateweatherdata(isLocationData);
		}
		return rootview;

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void updateweatherdata(boolean islocdata) {
		if (haveNetwork()) {
			new CurrentWeatherAsyncTask(activity, rootview).execute(islocdata);
		} else {
			Toast.makeText(
					ctx,
					"No network Connection.Please connect to Wifi or Mobile Data",
					Toast.LENGTH_LONG).show();
		}
	}

	private boolean haveNetwork() {
		boolean haveWifi = false;
		boolean haveMobiledata = false;

		ConnectivityManager conman = (ConnectivityManager) ctx
				.getSystemService(ctx.CONNECTIVITY_SERVICE);
		NetworkInfo[] netinfo = conman.getAllNetworkInfo();
		for (NetworkInfo ni : netinfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
				if (ni.isConnected())
					haveWifi = true;
			}
			if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
				if (ni.isConnected())
					haveMobiledata = true;
			}
		}
		return haveWifi || haveMobiledata;
	}

	
}
