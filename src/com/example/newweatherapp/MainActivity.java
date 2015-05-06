package com.example.newweatherapp;

import java.util.Locale;
import java.util.regex.Pattern;

import com.example.newweatherapp.fragments.CurrentWeatherFragment;
import com.example.newweatherapp.fragments.ForecastWeatherFragment;
import com.example.newweatherapp.utilities.CityPreference;
import com.example.newweatherapp.utilities.ZoomOutPageAnimation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationListener;

import android.R.string;
import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v13.app.FragmentPagerAdapter;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements ActionBar.TabListener {

	private static final int TOTAL_PAGES = 2;
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	static long latitude;
	static long longitude;
	FusedLocationService fusedLocationService;
	ImageButton mFloatingButton;
	boolean mShowChangecitymenu = false;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// actionBar.setBackgroundDrawable(new ColorDrawable(Color
		// .parseColor("#00B8D4")));

		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		mViewPager.setPageTransformer(true, new ZoomOutPageAnimation());

		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

		ViewStub fabViewStub = (ViewStub) findViewById(R.id.fab_layout);
		if (fabViewStub != null) {
			LinearLayout fabbuttonlayout = (LinearLayout) fabViewStub.inflate();
			fabbuttonlayout.setVisibility(View.VISIBLE);
		}
		mFloatingButton = (ImageButton) findViewById(R.id.fab_image_button);
		mFloatingButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showalertdialog();
			}
		});
		fusedLocationService = new FusedLocationService(this);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		if (!mShowChangecitymenu) {
			menu.findItem(R.id.change_city).setVisible(false);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.change_city:
			showalertdialog();
			break;
		case R.id.get_location:
			if (Islocationon() && haveNetwork()) {
				if (isGooglePlayServicesAvailable()) {
					Location location = fusedLocationService.getLocation();
					if (location != null) {
						Toast.makeText(
								this,
								"Latitude : " + location.getLatitude()
										+ " Longitude : "
										+ location.getLongitude(),
								Toast.LENGTH_SHORT).show();
						latitude = (long) location.getLatitude();
						longitude = (long) location.getLongitude();
						// CityPreference.setLocation(MainActivity.this,
						// latitude, longitude);
						locationrefresh(location);
					}
				} else {
					Toast.makeText(this,
							"Wait .... Getting Location ,Please tap again",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "Switch on location and internet",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.share:
			shareoption();
			break;
		case R.id.Rate:
			rateoption();
			break;
		default:
			break;
		}
		return false;
	}

	private void rateoption() {
		Toast.makeText(this, "Working on this",
				Toast.LENGTH_SHORT).show();
//		try {
//			Intent i = new Intent(Intent.ACTION_VIEW,
//					Uri.parse("market://details?id=helper.plus"));
//			startActivity(i);
//		} catch (ActivityNotFoundException error) {
//			Intent i = new Intent(
//					Intent.ACTION_VIEW,
//					Uri.parse("https://play.google.com/store/apps/details?id=helper.plus"));
//			startActivity(i);
//		}
	}

	private void shareoption() {
		Intent i = new Intent("android.intent.action.SEND");
		i.setType("text/plain");
		i.putExtra(
				"android.intent.extra.TEXT",
				"Get Weather Forecast of every town in the world. Download My Weather App:- Enter the playstore link here ");//https://play.google.com/store/apps/details?id=helper.plus");
		startActivity(Intent.createChooser(i, "Share via : "));
	}

	private void showalertdialog() {

		LayoutInflater inflater = LayoutInflater.from(this);

		View citynameview = inflater.inflate(R.layout.dialog_view, null);
		final EditText city = (EditText) citynameview
				.findViewById(R.id.change_city);

		city.setFilters(new InputFilter[] { mtextFilter });
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Change City");
		builder.setMessage("Enter the city name");
		builder.setView(citynameview);
		builder.setPositiveButton("Done", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				CityPreference.setcity(MainActivity.this, city.getText()
						.toString().trim());
				refresh(false);
			}
		});
		builder.setNegativeButton("Cancel", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
		final int pos = tab.getPosition();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				if (pos == 0) {
					mShowChangecitymenu = false;
					mFloatingButton.setVisibility(View.VISIBLE);
				} else {
					mShowChangecitymenu = true;
					mFloatingButton.setVisibility(View.INVISIBLE);
				}
				invalidateOptionsMenu();
			}
		}, 50);
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			switch (position) {
			case 0:
				return CurrentWeatherFragment.newinstance(false);
			case 1:
				return ForecastWeatherFragment.newinstance(false);
			default:
				return null;
			}
		}

		@Override
		public int getCount() {
			return TOTAL_PAGES;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.Current_Weather).toUpperCase(l);
			case 1:
				return getString(R.string.Forecast_14_days).toUpperCase(l);
			}
			return null;
		}

		private String getFragmentTag(int pos) {
			return "android:switcher:" + R.id.pager + ":" + pos;
		}

		public CurrentWeatherFragment getCurrentWeatherFragment() {
			return (CurrentWeatherFragment) getFragmentManager()
					.findFragmentByTag(getFragmentTag(0));
		}

		public ForecastWeatherFragment getForecastWeatherFragment() {
			return (ForecastWeatherFragment) getFragmentManager()
					.findFragmentByTag(getFragmentTag(1));
		}
	}

	public void refresh(boolean locdata) {
		CurrentWeatherFragment curFrag = mSectionsPagerAdapter
				.getCurrentWeatherFragment();
		if (curFrag != null) {
			curFrag.refresh(locdata);
		}

		ForecastWeatherFragment forFrag = mSectionsPagerAdapter
				.getForecastWeatherFragment();
		if (forFrag != null) {
			forFrag.refresh(locdata);
		}
	}

	public void locationrefresh(Location location) {
		CurrentWeatherFragment curFrag = mSectionsPagerAdapter
				.getCurrentWeatherFragment();
		if (curFrag != null) {
			curFrag.locationrefresh(location);
		}

		ForecastWeatherFragment forFrag = mSectionsPagerAdapter
				.getForecastWeatherFragment();
		if (forFrag != null) {
			forFrag.locationrefresh(location);
		}
	}

	private boolean Islocationon() {
		LocationManager locman = (LocationManager) getSystemService(LOCATION_SERVICE);
		if (locman.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
				|| locman.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean haveNetwork() {
		boolean haveWifi = false;
		boolean haveMobiledata = false;

		ConnectivityManager conman = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
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

	private boolean isGooglePlayServicesAvailable() {
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (ConnectionResult.SUCCESS == status) {
			return true;
		} else {
			GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
			return false;
		}
	}

	private final InputFilter mtextFilter = new InputFilter() {

		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			// Pattern ps = Pattern.compile("^[a-zA-Z\\s]+$");
			// if (!ps.matcher(source).matches()) {
			// return "";
			// }
			// return null;

			if (source instanceof SpannableStringBuilder) {
				SpannableStringBuilder sourceAsSpannableBuilder = (SpannableStringBuilder) source;
				for (int i = end - 1; i >= start; i--) {
					char currentChar = source.charAt(i);
					if (!Character.isLetterOrDigit(currentChar)
							&& !Character.isSpaceChar(currentChar)) {
						sourceAsSpannableBuilder.delete(i, i + 1);
					}
				}
				return source;
			} else {
				StringBuilder filteredStringBuilder = new StringBuilder();
				for (int i = start; i < end; i++) {
					char currentChar = source.charAt(i);
					if (Character.isLetterOrDigit(currentChar)
							|| Character.isSpaceChar(currentChar)) {
						filteredStringBuilder.append(currentChar);
					}
				}
				return filteredStringBuilder.toString();
			}
		}
	};

}
