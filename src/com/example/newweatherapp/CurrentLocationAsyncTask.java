package com.example.newweatherapp;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.newweatherapp.utilities.CityPreference;
import com.example.newweatherapp.utilities.Utilities;

import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CurrentLocationAsyncTask extends
		AsyncTask<Location, integer, Boolean> {

	Context context;
	ProgressDialog prodialog;
	View rootview;
	private TextView City;
	private TextView Update_date;
	private ImageView Weather_icon;
	private TextView Details;
	private TextView Cur_Temp;
	private Calendar cal = Calendar.getInstance();
	String name;
	String country;
	long sunrise;
	long sunset;
	Date datesunrise;
	Date datesunset;
	long updatedon;
	Date dateupdatedon;
	float temp;
	String description;
	int humidity;
	int iconid;

	public CurrentLocationAsyncTask(Context ctx, View view) {
		context = ctx;
		rootview = view;
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
		String data = SetConnection.getlocationdata(context, params[0]);

		JSONObject jobj = null;
		if (data != null) {
			try {
				jobj = new JSONObject(data);

				if (jobj.getInt("cod") == 200) {
					name = jobj.getString("name").toUpperCase(Locale.US);
					country = jobj.getJSONObject("sys").getString("country");
					sunrise = jobj.getJSONObject("sys").getLong("sunrise");
					sunset = jobj.getJSONObject("sys").getLong("sunset");
					description = jobj.getJSONArray("weather").getJSONObject(0)
							.getString("description");
					humidity = jobj.getJSONObject("main").getInt("humidity");
					temp = jobj.getJSONObject("main").getInt("temp");
					updatedon = jobj.getLong("dt") * 1000;
					iconid = jobj.getJSONArray("weather").getJSONObject(0)
							.getInt("id");
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
			City = (TextView) rootview.findViewById(R.id.city);
			Update_date = (TextView) rootview.findViewById(R.id.update_date);
			Weather_icon = (ImageView) rootview.findViewById(R.id.weather_icon);
			Details = (TextView) rootview.findViewById(R.id.details_field);
			Cur_Temp = (TextView) rootview.findViewById(R.id.curr_temp);
			// Weather_icon.setTypeface(Utilities.getweatherfont(context));
			try {

				City.setText(name + " , " + country);
				CityPreference.setcity((Activity) context, name);
				cal.setTimeInMillis(sunrise);
				datesunrise = cal.getTime();
				cal.setTimeInMillis(sunset);
				datesunset = cal.getTime();
				cal.setTimeInMillis(updatedon);
				dateupdatedon = cal.getTime();
				String sunrisetime = DateFormat.getTimeInstance()
						.format(datesunrise).toString();
				String newSunRiseTime = sunrisetime.substring(0,
						sunrisetime.length() - 2)
						+ "AM";

				Details.setText(description.toUpperCase(Locale.US) + "\n\n"
						+ "Humidity : " + humidity + "\n" + "Sunrise : "
						+ newSunRiseTime + "\n" + "Sunset : "
						+ DateFormat.getTimeInstance().format(datesunset));

				Update_date.setText(DateFormat.getDateTimeInstance().format(
						dateupdatedon));
				Cur_Temp.setText("Current Temp : " + temp + " °C");

				Weather_icon.setImageResource(Utilities.getImageDrawable(
						iconid, sunrise, sunset));

			} catch (Exception e) {
				Toast.makeText(context, "Data not received properly.Please check your internet connection.",
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}

		} else {
			Toast.makeText(context, "City Not found ", Toast.LENGTH_LONG)
					.show();
		}

	}

}
