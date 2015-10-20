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
public class OpenWeatherHttp {
	
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
	
	/**
		 加密方式:
		private_key
		某某:793ffb_SmartWeatherAPI_662cbc7
		private_key仅负责与public_key共同合成key传参，私钥不可见，客户端与服务端各存储一份;
		public_key为不包含key在内的完整URL其它部分（此处appid为完整appid）
		示例:
		http://open.weather.com.cn/data/?areaid=101010100&type=index_f&date=201410230335&appid=22290667181111
		key的算法
		key=base64_encode(hash_hmac('sha1',$public_key,$private_key,TRUE));
		key加密后通过urlencode对其编码后传参
		注：每一个产品使用用户分配一个唯一标识appid,用于统计用户访问情况、区分用户提供差异服务，终端用户按照终端型号分配，一个型号对应一个标识。 
	 */
	public static String getWeather(String areaId,String type,String date,String appId,String privatekey) throws IOException {
		//http://openweather.weather.com.cn/Home/Help/area.html
		
		String appId6 = appId.substring(0,6);
		
		String base =String.format(Config.OpenWeather.URL + "areaid=%s&type=%s&date=%s&appid=%s",
				areaId,type,date,appId);
		String key = standardURLEncoder(base, privatekey);
		
		String url = String.format(Config.OpenWeather.URL + "areaid=%s&type=%s&date=%s&appid=%s&key=%s",
				areaId,type,date,appId6,key);
		System.out.println("url:::" + url);
		String json = HttpConnAssist.getJsonInfoFromNet(url, Config.WeatherURL.CHARSET);
		
		
//		String json = Jsoup.connect(url).get().body().ownText().toString();
		
		//取不到再尝试2次
		if(json == null || json.trim().length() == 0) {
//			json = Jsoup.connect(url).get().body().ownText().toString();
			json = HttpConnAssist.getJsonInfoFromNet(url, Config.WeatherURL.CHARSET);
		}
		if(json == null || json.trim().length() == 0) {
//			json = Jsoup.connect(url).get().body().ownText().toString();
			json = HttpConnAssist.getJsonInfoFromNet(url, Config.WeatherURL.CHARSET);
		}
		
		return json;
	}
	
	public static String getWeather(String cityName,String type,String date) throws IOException {
		return getWeather(Config.OpenWeather.getAreaId(cityName),type,date,Config.OpenWeather.APPID,Config.OpenWeather.PRIVATE_KEY);
	}
	
	public static String getWeather(String cityName) throws IOException {
		/* Date date=new Date();//取时间 
	     Calendar   calendar   =   new   GregorianCalendar(); 
	     calendar.setTime(date); 
	     calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
	     date=calendar.getTime();*/
	     
		String type = "forecast_v";
		String dates = Times.format("yyyyMMddHHmm", new Date());
		return getWeather(Config.OpenWeather.getAreaId(cityName),type,dates,Config.OpenWeather.APPID,Config.OpenWeather.PRIVATE_KEY);
	}
	
	public static String getWeatherReal(String cityName) throws IOException {
		String cityId = Config.OpenWeather.getAreaId(cityName);
		String url = String.format(Config.OpenWeather.URL_REAL,cityId);
		String json = HttpConnAssist.getJsonInfoFromNet(url, Config.WeatherURL.CHARSET);
				
		if(json == null || json.trim().length() == 0) {
			json = HttpConnAssist.getJsonInfoFromNet(url, Config.WeatherURL.CHARSET);
		}
		
		if(json == null || json.trim().length() == 0) {
			json = HttpConnAssist.getJsonInfoFromNet(url, Config.WeatherURL.CHARSET);
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
