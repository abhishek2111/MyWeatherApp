package com.example.newweatherapp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.newweatherapp.fragments.ForecastGridData;
import com.example.newweatherapp.fragments.ForecastWeatherGridAdapter;
import com.example.newweatherapp.utilities.CityPreference;
import com.example.newweatherapp.utilities.RawCache;
import com.example.newweatherapp.utilities.Utilities;

import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

public class ForecastWeatherAsyncTask extends
		AsyncTask<Boolean, integer, Boolean> {

	Context context;
	ProgressDialog prodialog;
	View rootview;
	ArrayList<ForecastGridData> griddata;
	private TaskCompletedListener listener;
	boolean isInCache = false;

	public ForecastWeatherAsyncTask(Context ctx, View view) {
		context = ctx;
		rootview = view;
	}

	public void setTaskCompletedListener(TaskCompletedListener listener) {
		this.listener = listener;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		isInCache = RawCache.isInCache(context,
				CityPreference.getcity((Activity) context)
						+ Utilities.FORECAST_CACHE_KEY);
		prodialog = new ProgressDialog(context);
		prodialog.setMessage("Loading data...");
		prodialog.show();
	}

	@Override
	protected Boolean doInBackground(Boolean... params) {
		String data = null;
		if (isInCache) {
			data = RawCache.fromCache(context,
					CityPreference.getcity((Activity) context)
							+ Utilities.FORECAST_CACHE_KEY);
		} else {
			data = SetConnection.getforecastweatherdata(context,
					CityPreference.getcity((Activity) context));
		}

		JSONObject jobj = null;
		if (data != null) {
			try {
				jobj = new JSONObject(data);

				if (jobj.getInt("cod") == 200) {
					RawCache.cache(context,
							CityPreference.getcity((Activity) context)
									+ Utilities.FORECAST_CACHE_KEY, data);
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
					// return true;
				} else {
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
			Toast.makeText(
					context,
					"Data not received for forecast.Please check your internet connection.",
					Toast.LENGTH_LONG).show();
		}
	}

	public interface TaskCompletedListener {
		public void onLoadComplete(ArrayList<ForecastGridData> griddata);
	}

}
