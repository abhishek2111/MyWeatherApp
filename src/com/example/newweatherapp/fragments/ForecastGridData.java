package com.example.newweatherapp.fragments;

public class ForecastGridData {

	long date = 0;
	int iconid = 0;
	String description = null;
	float maxtemp = 0;
	float mintemp = 0;
	int humidity = 60;

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public int getIconid() {
		return iconid;
	}

	public void setIconid(int iconid) {
		this.iconid = iconid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getMaxtemp() {
		return maxtemp;
	}

	public void setMaxtemp(float maxtemp) {
		this.maxtemp = maxtemp;
	}

	public float getMintemp() {
		return mintemp;
	}

	public void setMintemp(float mintemp) {
		this.mintemp = mintemp;
	}

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

}
