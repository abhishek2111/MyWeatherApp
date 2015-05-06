package com.example.newweatherapp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.newweatherapp.fragments.ForecastGridData;

import android.R.integer;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

public class ForecastLocationAsyncTask extends AsyncTask<Location , integer , Boolean> {
	
	Context context;
	ProgressDialog prodialog;
	View rootview;
	ArrayList<ForecastGridData> griddata;
	private LocationTaskCompletedListener listener;

	public ForecastLocationAsyncTask(Context ctx, View view) {
		context = ctx;
		rootview = view;
	}

	public void setLocationTaskCompletedListener(LocationTaskCompletedListener listener) {
		this.listener = listener;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		prodialog = new ProgressDialog(context);
		prodialog.setMessage("Loading data...");
		prodialog.show();
	}
	
	@Override
	protected Boolean doInBackground(Location... params) {
		String data = SetConnection.getforecastlocationweatherdata(context,
				params[0]);
		
		JSONObject jobj = null;
		if (data != null) {
			try {
				jobj = new JSONObject(data);

				if (jobj.getInt("cod") == 200) {
					JSONArray jarr = jobj.getJSONArray("list");
					griddata = new ArrayList<ForecastGridData>();
					for (int i = 0; i < jarr.length(); i++) {
						ForecastGridData maindata = new ForecastGridData();
						JSONObject jmainarrobj = jarr.getJSONObject(i);
						maindata.setDate(jmainarrobj.getLong("dt") * 1000);
						maindata.setDescription(jmainarrobj
								.getJSONArray("weather").getJSONObject(0)
								.getString("description"));
						maindata.setHumidity(jmainarrobj.getInt("humidity"));
						maindata.setIconid(jmainarrobj.getJSONArray("weather")
								.getJSONObject(0).getInt("id"));
						maindata.setMaxtemp(jmainarrobj.getJSONObject("temp")
								.getLong("max"));
						maindata.setMintemp(jmainarrobj.getJSONObject("temp")
								.getLong("min"));

						griddata.add(maindata);
					}
//					return true;
				}else{
					return false;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		prodialog.dismiss();
		if (result) {
			listener.onLoadComplete(griddata);

		} else {
			Toast.makeText(context, "Data not received for forecast.Please check your internet connection.", Toast.LENGTH_LONG)
					.show();
		}
	}
	
	public interface LocationTaskCompletedListener {
		public void onLoadComplete(ArrayList<ForecastGridData> griddata);
	}
}
