package com.example.newweatherapp.fragments;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.example.newweatherapp.R;
import com.example.newweatherapp.utilities.Utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ForecastWeatherGridAdapter extends BaseAdapter {

	LayoutInflater inflater;
	Context context;
	ArrayList<ForecastGridData> griddata;
	ViewHolder holder;
	private Calendar cal = Calendar.getInstance();
	Date nextdate;

	public ForecastWeatherGridAdapter(Context ctx,
			ArrayList<ForecastGridData> griddata) {
		this.context = ctx;
//		inflater = (LayoutInflater) ctx
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater = LayoutInflater.from(ctx);
		this.griddata = griddata;
	}

	public void setGriddata(ArrayList<ForecastGridData> griddata) {
		this.griddata = griddata;
	}

	@Override
	public int getCount() {
		if(griddata!=null){
			return griddata.size();
		}else{
			return 0;
		}
	}

	@Override
	public ForecastGridData getItem(int position) {
		return griddata.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.grid_view_item_layout, null);
			holder.weatherimage = (ImageView) convertView
					.findViewById(R.id.forecast_weather_icon);
			holder.NextDate = (TextView) convertView
					.findViewById(R.id.next_date);
			holder.Details = (TextView) convertView
					.findViewById(R.id.forecast_details);
//			holder.Humidity = (TextView) convertView
//					.findViewById(R.id.humidity);
			holder.MAX_MIN_Temp = (TextView) convertView
					.findViewById(R.id.max_min_temp);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		cal.setTimeInMillis(griddata.get(position).getDate());
		nextdate = cal.getTime();
		holder.NextDate.setText(DateFormat.getDateInstance().format(nextdate));
		holder.weatherimage.setImageResource(Utilities.getImageDrawable(
				griddata.get(position).getIconid(), -1, -1));

		holder.Details.setText(griddata.get(position).getDescription()
				.toUpperCase(Locale.US));
//				+ "Humidity : "
//				+ griddata.get(position).getHumidity()
//				+ "\n");
		
//		holder.Humidity.setText("Humidity : "
//				+ griddata.get(position).getHumidity());
		
		holder.MAX_MIN_Temp.setText("Max : "
				+ griddata.get(position).getMaxtemp() + "°C" + "\n"
				+ "Min : " + griddata.get(position).getMintemp() + "°C"
				+ "\n");

		return convertView;
	}

}

class ViewHolder {
	public ImageView weatherimage;
	public TextView NextDate;
	public TextView Details;
//	public TextView Humidity;
	public TextView MAX_MIN_Temp;
}