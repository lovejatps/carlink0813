package com.uniits.carlink.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;

public class NewConfig {
	private static Map<String,String> map = new HashMap<String, String>();
	static{
		map.put("0", "00");
		map.put("1", "00");
		map.put("2", "00");
		map.put("3", "00");
		map.put("4", "01");
		map.put("5", "01");
		map.put("6", "01");
		map.put("7", "01");
		map.put("8", "01");
		map.put("9", "02");
		map.put("10", "03");
		map.put("11", "04");
		map.put("12", "05");
		map.put("13", "07");
		map.put("14", "08");
		map.put("15", "09");
		map.put("16", "10");
		map.put("17", "11");
		map.put("18", "12");
		map.put("19", "19");
		map.put("20", "06");
		map.put("21", "13");
		map.put("22", "14");
		map.put("23", "15");
		map.put("24", "16");
		map.put("25", "17");
		map.put("26", "29");
		map.put("27", "30");
		map.put("28", "20");
		map.put("29", "31");
		map.put("30", "18");
		map.put("31", "53");
		map.put("32", "02");
		map.put("33", "17");
		map.put("34", "17");
		map.put("35", "17");
		map.put("36", "02");
		map.put("37", "00");
		map.put("99", "99");
	}
	public static Map<String,String> findValue(){
		return map;
	}
	public static final String standardDateFormat = "yyyy-MM-dd hh:mm:ss" ;
	public static final String error = "-1" ; //系统出现错误
	
	public static class WeatherURL {
		public static final String CHARSET = "utf-8" ;
		public static final String WEATHERDISTRICTURL = "http://api.36wu.com/Weather/GetDistrictList";  
		public static final String WEATHERMOREURL = "http://api.36wu.com/Weather/GetMoreWeather?format=json&district=" ;
		public static final String WEATHERCURRENTURL = "http://api.36wu.com/Weather/GetWeather?format=json&district="; 
		public static final String LIFTADVICEURL = "http://api.36wu.com/Weather/GetWeatherIndex?format=json&district="; 
	}
	
	public static class NewsURL {
		public static final String CHARSET = "gb2312" ;
		public static final String NEWSKEYWORDHEADURL = "http://news.baidu.com/ns?word=";
		public static final String NEWSKEYWORDTAILURL = "+sina&tn=newsfcu&from=news&cl=2&rn=10&ct=0";
		
		public static final String NEWS_YOUDAO_URL = "http://news.yodao.com/search?start=0&ue=utf8&s=rank&tl=&keyfrom=news.result.top&q=";
	}
	
	public static class AppStorePath {
		public static final String basePath = "D:/appstore/";
		public static final String aaptPath = "E:/tools/ide/install/android-sdk_r22.3-windows/android-sdk-windows/build-tools/19.0.2/" ;
	}
	
	public static class ReturnCode {
		public static final String finalData = "-2";  //数据已经到最后了
		public static final String paramError = "-3";  //参数错误
		public static final String noFind = "-4";   //为找到有效数据
		
		//天气预报返回值常量 开始
		/**免费请求失败，参数异常或缺少参数*/
		public static final int PARAM_EXCEPTION = 201;   
		/**免费用户访问次数已上限*/
		public static final int ACCESS_LIMIT = 301;  
		/**ak错误，未授权的ak*/
		public static final int AK_ERROR = 401;   
		/**请求失败，请求天气网站服务内部异常*/
		public static final int SERVICE_ERROR = 501;   
		//天气预报返回值常量 结束
	}
	
	public static class MusicURL {
		public static final String CHARSET = "utf-8" ;
		public static final String MUSICURL_ONE = "http://mp3.baidu.com/dev/api/?tn=getinfo&ct=0&ie=utf-8&format=json&word=" ;
		public static final String MUSICURL_TWO = "http://ting.baidu.com/data/music/links?qq-pf-to=pcqq.temporaryc2c&songIds=" ;
		/**歌词格式 lrc地址为lrcLink前加http://ting.baidu.com*/
		public static final String MUSICURL_LRC_PREFIX = "http://ting.baidu.com" ;
		
		public static final String BAIDU_MUSICURL_LRC = "http://music.baidu.com";
		
		public static final String BAIDU_MUSICURL_LOAD = "http://music.baidu.com/search/lrc?qq-pf-to=pcqq.c2c&key=";
		
		/**新歌周榜*/
		public static final String MUSICURL_TOP_NEW_WEEK = "http://music.baidu.com/top/new/week/" ;
		
		/**
		 * 热歌榜
		 */
		public static final String MUSICURL_TOP_DAYHOT = "http://music.baidu.com/top/dayhot" ;
		
		/**
		 * 歌榜类型
		 */
		public static final String MUSICURL_TYPE_NEW = "new" ;
		public static final String MUSICURL_TYPE_HOT = "hot" ;
		
		/***
		 * 新歌日周月榜
		 */
		public static final String MUSICURL_CYCLE_DAY = "day" ;
		public static final String MUSICURL_CYCLE_WEEK = "week" ;
		public static final String MUSICURL_CYCLE_MONTH = "month" ;
		
		public static boolean isWeekCycle(String cycle) {
			return MUSICURL_CYCLE_WEEK.equals(cycle);
		}
		
		public static boolean isNewType(String type) {
			return MUSICURL_TYPE_NEW.equals(type);
		}
		
		public static boolean isHotType(String type) {
			return MUSICURL_TYPE_HOT.equals(type);
		}
	}
	
	public static class OpenWeather {
		
		public static String URL_REAL = "https://www.weather.com.cn/adat/cityinfo/%s.html";//%s是城市ID 实时数据 
		public static String NEWURL = "http://api.thinkpage.cn/v2/weather/all.json?";
		public static String PRIVATE_KEY;
		public static String APPID;
		
		// 配置文件
	    private static Properties prop = new Properties();
	    
	    private static final Log log = Logs.get();
		static {
			 try {
				prop.load(OpenWeather.class.getClassLoader().getResourceAsStream("openWeatherAuth.properties"));
				PRIVATE_KEY = prop.getProperty("NewprivateKey", "FSUZWU8RKI");
				NEWURL = prop.getProperty("newUrl", "https://api.thinkpage.cn/v2/weather/all.json?");
				prop.clear();
			 }
	        catch (IOException e) {
	            log.error("心知天气网配置文件读取错误！！！" + e.getMessage());
	            e.printStackTrace();
	        }
		}
		
	}
	
}
