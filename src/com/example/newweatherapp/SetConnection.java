package com.example.newweatherapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.location.Location;

public class SetConnection {
	private static final String CURR_WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&APPID=81edca9054f660d320540941d3eee42a&mode=json";
	private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?q=%s&units=metric&cnt=14&APPID=81edca9054f660d320540941d3eee42a&mode=json";
	private static final String GEO_BASE_URL = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric&APPID=81edca9054f660d320540941d3eee42a&mode=json";
	private static final String GEO_FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?lat=%s&lon=%s&units=metric&cnt=14&APPID=81edca9054f660d320540941d3eee42a&mode=json";
	
	public static String getforecastweatherdata(Context context, String city) {
		return setforecastconnection(context, city, BASE_URL);
	}
	
	public static String getforecastlocationweatherdata(Context context, Location location) {
		return setlocationforecastconnection(context, location, GEO_FORECAST_URL);
	}

	public static String getlocationdata(Context context, Location location) {
		return setlocationconnection(context, location, GEO_BASE_URL);
	}
	
	public static String getcurrentweatherdata(Context context , String city){
		return setcurrentconnection(context, city, CURR_WEATHER_URL);
	}

	private static String setcurrentconnection(Context context, String city,
			String currWeatherUrl) {
		String url = String.format(currWeatherUrl, city);
		return establishconnection(url);
	}

	private static String setlocationconnection(Context context,
			Location location, String geoBaseUrl) {
		String url = String.format(geoBaseUrl, location.getLatitude() + "",
				location.getLongitude() + "");
		return establishconnection(url);
	}

	private static String setforecastconnection(Context context, String city,
			String baseUrl) {
		String url = String.format(baseUrl, city);
		return establishconnection(url);
	}

	private static String setlocationforecastconnection(Context context, Location location,
			String geoforecasturl) {
		String url = String.format(geoforecasturl, location.getLatitude() + "",
				location.getLongitude() + "");
		return establishconnection(url);
	}
	
	private static String establishconnection(String url) {
		String data = null;
		BufferedReader br = null;

		try {
			URL mainurl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) mainurl
					.openConnection();
			br = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			StringBuffer sbuf = new StringBuffer();
			String tmp = "";

			while ((tmp = br.readLine()) != null) {
				sbuf.append(tmp + "\r\n");
			}
			data = sbuf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return data;
	}

}
