package com.uniits.carlink.domain;

import java.util.List;
import java.util.Map;

import lombok.Data;

public @Data class WeatherInfo {
	
	private String city;
	private long sutime; //时间
	private int pm2_5;
	private Map<String, String> lifeAdvice;
	private TodayWeatherInfo todayWeatherInfo;
	private List<FutureWeatherInfo> futureWeatherInfos;
	
	

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public long getSutime() {
		return sutime;
	}

	public void setSutime(long sutime) {
		this.sutime = sutime;
	}

	public int getPm2_5() {
		return pm2_5;
	}

	public void setPm2_5(int pm2_5) {
		this.pm2_5 = pm2_5;
	}

	public Map<String, String> getLifeAdvice() {
		return lifeAdvice;
	}

	public void setLifeAdvice(Map<String, String> lifeAdvice) {
		this.lifeAdvice = lifeAdvice;
	}

	public TodayWeatherInfo getTodayWeatherInfo() {
		return todayWeatherInfo;
	}

	public void setTodayWeatherInfo(TodayWeatherInfo todayWeatherInfo) {
		this.todayWeatherInfo = todayWeatherInfo;
	}

	public List<FutureWeatherInfo> getFutureWeatherInfos() {
		return futureWeatherInfos;
	}

	public void setFutureWeatherInfos(List<FutureWeatherInfo> futureWeatherInfos) {
		this.futureWeatherInfos = futureWeatherInfos;
	}

	public FutureWeatherInfo getNewFutureWeatherInfo() {
		return new FutureWeatherInfo();
	}
	
	public TodayWeatherInfo getNewTodayWeatherInfo() {
		return new TodayWeatherInfo();
	}

	public @Data class TodayWeatherInfo {
		private String curTemp; // 当前温度
		private String lowTemp; // 最低温度
		private String highTemp; // 最高温度
		private String curDumidity; // 湿度
		private String getTime; // 时间
		private String weather; // 天气
		private String wind; // from MoreWeather
		private String[] image; // from MoreWeather
		private String index_xc = "";
		
		
		public String getCurTemp() {
			return curTemp;
		}
		public void setCurTemp(String curTemp) {
			this.curTemp = curTemp;
		}
		public String getLowTemp() {
			return lowTemp;
		}
		public void setLowTemp(String lowTemp) {
			this.lowTemp = lowTemp;
		}
		public String getHighTemp() {
			return highTemp;
		}
		public void setHighTemp(String highTemp) {
			this.highTemp = highTemp;
		}
		public String getCurDumidity() {
			return curDumidity;
		}
		public void setCurDumidity(String curDumidity) {
			this.curDumidity = curDumidity;
		}
		public String getGetTime() {
			return getTime;
		}
		public void setGetTime(String getTime) {
			this.getTime = getTime;
		}
		public String getWeather() {
			return weather;
		}
		public void setWeather(String weather) {
			this.weather = weather;
		}
		public String getWind() {
			return wind;
		}
		public void setWind(String wind) {
			this.wind = wind;
		}
		public String[] getImage() {
			return image;
		}
		public void setImage(String[] image) {
			this.image = image;
		}
		public String getIndex_xc() {
			return index_xc;
		}
		public void setIndex_xc(String index_xc) {
			this.index_xc = index_xc;
		}
		
		

	}

	public @Data class FutureWeatherInfo {
		private String lowTemp; //最低温度
		private String highTemp; // 最高温度
		private String weather; // 天气
		private String wind ;  //风
		private String[] image; // from MoreWeather
		public String getLowTemp() {
			return lowTemp;
		}
		public void setLowTemp(String lowTemp) {
			this.lowTemp = lowTemp;
		}
		public String getHighTemp() {
			return highTemp;
		}
		public void setHighTemp(String highTemp) {
			this.highTemp = highTemp;
		}
		public String getWeather() {
			return weather;
		}
		public void setWeather(String weather) {
			this.weather = weather;
		}
		public String getWind() {
			return wind;
		}
		public void setWind(String wind) {
			this.wind = wind;
		}
		public String[] getImage() {
			return image;
		}
		public void setImage(String[] image) {
			this.image = image;
		}
		
		
		
	}
}
