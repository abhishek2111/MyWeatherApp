package com.example.newweatherapp.fragments;

import java.util.ArrayList;

import com.example.newweatherapp.ForecastLocationAsyncTask;
import com.example.newweatherapp.ForecastLocationAsyncTask.LocationTaskCompletedListener;
import com.example.newweatherapp.ForecastWeatherAsyncTask;
import com.example.newweatherapp.ForecastWeatherAsyncTask.TaskCompletedListener;
import com.example.newweatherapp.R;

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
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

public class ForecastWeatherFragment extends Fragment implements TaskCompletedListener ,LocationTaskCompletedListener {

	static boolean isLocationData = false;
	View rootview;
	private Activity activity;
	Context ctx;
	ArrayList<ForecastGridData> griddata = new ArrayList<>();
	ForecastWeatherGridAdapter adapter;
	GridView gridview;
	ImageButton mFloatingButton;
	
	public ForecastWeatherFragment() {

	}

	public static ForecastWeatherFragment newinstance(boolean islocdata) {
		ForecastWeatherFragment ForFrag = new ForecastWeatherFragment();
		Bundle args = new Bundle();

		ForFrag.setArguments(args);
		isLocationData = islocdata;
		return ForFrag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootview = inflater.inflate(R.layout.fragment_forecast, container,
				false);
	    gridview = (GridView) rootview.findViewById(R.id.gridlayout);
	    adapter = new ForecastWeatherGridAdapter(ctx, griddata);
		gridview.setAdapter(adapter);
//		adapter.notifyDataSetChanged();
		if (isLocationData) {
			updateforecastweatherdata(isLocationData);
		} else {
			updateforecastweatherdata(isLocationData);
		}
		return rootview;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
		ctx = activity.getBaseContext();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void updateforecastweatherdata(boolean islocdata) {
		if (haveNetwork()) {
			ForecastWeatherAsyncTask forwea = new ForecastWeatherAsyncTask(activity, rootview);
			forwea.setTaskCompletedListener(this);
			forwea.execute(islocdata);
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

	public void refresh(boolean locdata) {
		if (locdata) {
			updateforecastweatherdata(locdata);
		} else {
			updateforecastweatherdata(locdata);
		}
	}
	
	public void locationrefresh(Location location) {
		if (haveNetwork()) {
			ForecastLocationAsyncTask forlocwea = new ForecastLocationAsyncTask(activity, rootview);
			forlocwea.setLocationTaskCompletedListener(this);
			forlocwea.execute(location);
		} else {
			Toast.makeText(
					ctx,
					"No network Connection.Please connect to Wifi or Mobile Data",
					Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onLoadComplete(ArrayList<ForecastGridData> griddata) {
		updatedata(griddata);
	}

	private void updatedata(ArrayList<ForecastGridData> newgriddata) {
		adapter.setGriddata(newgriddata);
		adapter.notifyDataSetChanged();
		
	}

	

}
