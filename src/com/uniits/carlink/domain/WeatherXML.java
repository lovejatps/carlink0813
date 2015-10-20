package com.uniits.carlink.domain;

public class WeatherXML {

	
	public long mills;
	public String date;  //时间
	public String district;  //地区
	public String districtID;  //地区ID
	public String weather;//天气的xml
	private int hour;

	
	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public long getMills() {
		return mills;
	}

	public void setMills(long mills) {
		this.mills = mills;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getDistrictID() {
		return districtID;
	}

	public void setDistrictID(String districtID) {
		this.districtID = districtID;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

}
