package com.example.newweatherapp.utilities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.location.Location;

public class CityPreference {

	public static String DEFAULT_CITY = "Jammu";
//	public static double DEFAULT_LATITUDE = 32.73;
//	public static double DEFAULT_LONGITUDE = 74.87;
//	double latitude;

	public static String getcity(Activity activity) {
		return activity.getPreferences(Activity.MODE_PRIVATE).getString("city",
				DEFAULT_CITY);
	}

	public static void setcity(Activity activity, String city) {
		activity.getPreferences(Activity.MODE_PRIVATE).edit()
				.putString("city", city).commit();
	}

//	public static Location getLocation(Activity activity) {
//		Location userlocation = new Location("");
//
//		SharedPreferences prefs = activity
//				.getPreferences(Activity.MODE_PRIVATE);
//		userlocation.setLatitude(prefs.getLong("latitude",
//				(long) DEFAULT_LATITUDE));
//		userlocation.setLongitude(prefs.getLong("longitude",
//				(long) DEFAULT_LONGITUDE));
//
//		return userlocation;
//
//	}
//
//	public static void setLocation(Activity activity, long latitude,
//			long longitude) {
//		SharedPreferences.Editor editor = activity.getPreferences(
//				Activity.MODE_PRIVATE).edit();
//		editor.putLong("latitude", latitude);
//		editor.putLong("longitude", longitude);
//		editor.commit();
//	}
	
	public static void setlocationcity(Activity activity , String location){
		activity.getPreferences(Activity.MODE_PRIVATE).edit()
		.putString("location", location).commit();
	}
	
	public static String getlocationcity(Activity activity){
		return activity.getPreferences(Activity.MODE_PRIVATE).getString("location",
				DEFAULT_CITY);
	}
}
