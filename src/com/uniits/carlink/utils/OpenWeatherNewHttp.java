/**
 * 中国气象网接口封装
 * 失败有3次握手
 */
package com.uniits.carlink.utils;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.jsoup.Jsoup;
import org.nutz.json.Json;
import org.nutz.lang.Stopwatch;
import org.nutz.lang.Times;

import com.uniits.carlink.domain.openweather.Weather;
import com.uniits.carlink.domain.openweather.WeatherList;

/**
 * @author Administrator
 *
 */
public class OpenWeatherNewHttp {
	
	private static final char last2byte = (char) Integer.parseInt("00000011", 2);
	private static final char last4byte = (char) Integer.parseInt("00001111", 2);
	private static final char last6byte = (char) Integer.parseInt("00111111", 2);
	private static final char lead6byte = (char) Integer.parseInt("11111100", 2);
	private static final char lead4byte = (char) Integer.parseInt("11110000", 2);
	private static final char lead2byte = (char) Integer.parseInt("11000000", 2);
	private static final char[] encodeTable = new char[] { 'A', 'B', 'C', 'D',
			'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
			'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
			'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
			'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9', '+', '/' 
	};

	public static String standardURLEncoder(String data, String key) {
		byte[] byteHMAC = null;
		String urlEncoder = "";
		try {
			Mac mac = Mac.getInstance("HmacSHA1");
			SecretKeySpec spec = new SecretKeySpec(key.getBytes(), "HmacSHA1");
			mac.init(spec);
			byteHMAC = mac.doFinal(data.getBytes());
			if (byteHMAC != null) {
				String oauth = encode(byteHMAC);
				if (oauth != null) {
					urlEncoder = URLEncoder.encode(oauth, "utf8");
				}
			}
		} catch (InvalidKeyException e1) {
			e1.printStackTrace();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		return urlEncoder;
	}

	public static String encode(byte[] from) {
		StringBuffer to = new StringBuffer((int) (from.length * 1.34) + 3);
		int num = 0;
		char currentByte = 0;
		for (int i = 0; i < from.length; i++) {
			num = num % 8;
			while (num < 8) {
				switch (num) {
				case 0:
					currentByte = (char) (from[i] & lead6byte);
					currentByte = (char) (currentByte >>> 2);
					break;
				case 2:
					currentByte = (char) (from[i] & last6byte);
					break;
				case 4:
					currentByte = (char) (from[i] & last4byte);
					currentByte = (char) (currentByte << 2);
					if ((i + 1) < from.length) {
						currentByte |= (from[i + 1] & lead2byte) >>> 6;
					}
					break;
				case 6:
					currentByte = (char) (from[i] & last2byte);
					currentByte = (char) (currentByte << 4);
					if ((i + 1) < from.length) {
						currentByte |= (from[i + 1] & lead4byte) >>> 4;
					}
					break;
				}
				to.append(encodeTable[currentByte]);
				num += 6;
			}
		}
		if (to.length() % 4 != 0) {
			for (int i = 4 - to.length() % 4; i > 0; i--) {
				to.append("=");
			}
		}
		return to.toString();
	}
	
	public static String getWeather(String areaId,String language,String unit,String api,String privatekey) throws IOException {
		
		/*String base =String.format(Config.OpenWeather.URL + "city=%s&language=%s&unit=%s&api=%s",
				areaId,language,unit,api);
		String key = standardURLEncoder(base, privatekey);*/
		String city = java.net.URLEncoder.encode(areaId,"utf-8");
		String url = String.format(NewConfig.OpenWeather.NEWURL + "city=%s&language=%s&unit=%s&api=%s&key=%s",
				city,language,unit,api,privatekey);
		System.out.println("url:::" + url);
		String json = HttpConnAssist.getJsonInfoFromNet(url, NewConfig.WeatherURL.CHARSET);
		
		
		//String json = Jsoup.connect(url).get().body().ownText().toString();
		
		//取不到再尝试2次
		if(json == null || json.trim().length() == 0) {
			json = Jsoup.connect(url).get().body().ownText().toString();
			json = HttpConnAssist.getJsonInfoFromNet(url, NewConfig.WeatherURL.CHARSET);
		}
		if(json == null || json.trim().length() == 0) {
			json = Jsoup.connect(url).get().body().ownText().toString();
			json = HttpConnAssist.getJsonInfoFromNet(url, NewConfig.WeatherURL.CHARSET);
		}
		
		return json;
	}
	
	/*public static String getWeather(String cityName,String type,String date) throws IOException {
		return getWeather(Config.OpenWeather.getAreaId(cityName),type,date,Config.OpenWeather.APPID,Config.OpenWeather.PRIVATE_KEY);
	}*/
	
	public static String getWeather(String cityName) throws IOException {
		String language = "zh-chs";
		String unit = "c";
		String api = "city";
		return getWeather(cityName,language,unit,api,NewConfig.OpenWeather.PRIVATE_KEY);
	}
	
	public static String getWeatherReal(String cityName) throws IOException {
		String cityId = Config.OpenWeather.getAreaId(cityName);
		String url = String.format(Config.OpenWeather.URL_REAL,cityId);
		String json = HttpConnAssist.getJsonInfoFromNet(url, NewConfig.WeatherURL.CHARSET);
				
		if(json == null || json.trim().length() == 0) {
			json = HttpConnAssist.getJsonInfoFromNet(url, NewConfig.WeatherURL.CHARSET);
		}
		
		if(json == null || json.trim().length() == 0) {
			json = HttpConnAssist.getJsonInfoFromNet(url, NewConfig.WeatherURL.CHARSET);
		}
		return json;
	}
	
	public static String getWeatherIndex(String cityName) throws IOException {
		String type = "index_v";
		String date = Times.format("yyyyMMddHHmm", new Date());
		return getWeather(Config.OpenWeather.getAreaId(cityName),type,date,Config.OpenWeather.APPID,Config.OpenWeather.PRIVATE_KEY);
	}
	
	public void test() {
		Stopwatch sw =Stopwatch.begin();
		try {
			System.out.println(getWeather("昆山"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sw.stop();
		System.out.println(sw.getDuration());
	}
	
	public static void main(String[] args) throws IOException {
		Stopwatch sw =Stopwatch.begin();
		System.out.println(getWeather("北京"));
		System.out.println(getWeatherReal("金坛"));
//		System.out.println(getWeather("北京","forecast7d", Times.format("yyyyMMddHHmm", new Date())));
		
//		WeatherList w = Json.fromJson(WeatherList.class,getWeather("五莲"));
//		List<Weather> list = w.getF().getF1();
		
		sw.stop();
		System.out.println(sw.getDuration());
	}
}
