package com.example.newweatherapp.utilities;

import java.util.Date;

import com.example.newweatherapp.R;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utilities {
	
	public static final String FORECAST_CACHE_KEY = "_forecast";
	public static final String CURRENT_CACHE_KEY = "_current";
//	public static final String CURRENT_LOCATION_CACHE_KEY = "_current_location";
//	public static final String FORECAST_LOCATION_CACHE_KEY = "_forecast_location";
	public static final String WEATHER_FONT_PATH = "fonts/weathericons-regular-webfont.ttf";
	public static Typeface weatherfont;

	public static Integer[] aThunderstorm = { R.drawable.cloud_lightning_sun,
			R.drawable.cloud_lightning_moon };

	public static Integer[] aDrizzle = { R.drawable.cloud_drizzle_sun,
			R.drawable.cloud_drizzle_moon };

	public static Integer[] aRain = { R.drawable.cloud_light_rain_sun,
			R.drawable.cloud_light_rain_moon, R.drawable.cloud_heavy_rain_sun,
			R.drawable.cloud_heavy_rain_moon };

	public static Integer[] aSnow = { R.drawable.cloud_snow,
			R.drawable.cloud_snow_sun, R.drawable.cloud_snow_moon };

	public static Integer[] aCloud = { R.drawable.cloud_sun,
			R.drawable.cloud_moon, R.drawable.cloud_fog_sun,
			R.drawable.cloud_fog_moon };

	public static Integer[] aNormal = { R.drawable.sun, R.drawable.moon,
			R.drawable.sunrise, R.drawable.sunset, R.drawable.wind,
			R.drawable.tornado, R.drawable.cloud, R.drawable.normal };

	// public static Typeface getweatherfont(Context ctx){
	// if(weatherfont == null){
	// weatherfont = Typeface.createFromAsset(ctx.getAssets(),
	// WEATHER_FONT_PATH);
	// }
	// return weatherfont;
	// }

	public static int getImageDrawable(int code, long sunrise, long sunset) {
		int imageid = 0;
		long currentTime = new Date().getTime();
		boolean isDay = (currentTime >= sunrise && currentTime <= sunset)
				|| (sunrise == -1 && sunset == -1);
		int gid = code / 100;

		switch (gid) {
		case 2:
			imageid = isDay ? aThunderstorm[0] : aThunderstorm[1];
			break;
		case 3:
			imageid = isDay ? aDrizzle[0] : aDrizzle[1];
			break;
		case 5:
			imageid = getRainicon(isDay, code);
			break;
		case 6:
			imageid = getSnowicon(isDay, code);
			break;
		case 8:
			imageid = getcloudicon(isDay, code);
			break;
		case 7:
		case 9:
			imageid = getdefaulticon(isDay, code);
			break;
		}

		return imageid;

	}

	private static int getdefaulticon(boolean isDay, int code) {
		switch (code) {
		case 731:
		case 751:
		case 761:
		case 905:
		case 952:
		case 953:
		case 954:
			return aNormal[4];
		case 781:
		case 900:
		case 902:
		case 960:
		case 962:
			return aNormal[5];

		default:
			return aNormal[7];
		}
	}

	private static int getcloudicon(boolean isDay, int code) {
		switch (code) {
		case 800:
			return isDay ? aNormal[0] : aNormal[1];
		case 801:
		case 803:
			return isDay ? aCloud[0] : aCloud[1];
		default:
			return isDay ? aCloud[2] : aCloud[3];
		}
	}

	private static int getSnowicon(boolean isDay, int code) {
		switch (code) {
		case 602:
		case 622:
			return aSnow[0];
		default:
			return isDay ? aSnow[1] : aSnow[2];
		}
	}

	private static int getRainicon(boolean isDay, int code) {
		switch (code) {
		// light rain
		case 500:
		case 501:
		case 520:
		case 521:
		case 531:
			return isDay ? aRain[0] : aRain[1];
			// heavy rain
		default:
			// case 502:
			// case 503:
			// case 504:
			// case 511:
			// case 522:
			return isDay ? aRain[2] : aRain[3];
		}
		// return aRain[0];
	}

	public static boolean isConnected(Context context) {
		boolean haveWifi = false;
		boolean haveMobiledata = false;

		ConnectivityManager conman = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
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
	
	public static String getLastLoc(Context context , Location currentlocation){
		if(currentlocation != null){
			return context.getString(R.string.latitude_longitude,currentlocation.getLatitude() , currentlocation.getLongitude());
		}else {
			return "";
		}
	}

}
