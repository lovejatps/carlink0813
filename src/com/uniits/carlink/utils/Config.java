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

public class Config {
	
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
		
		//http://www.weather.com.cn/adat/cityinfo/101200803.html
		public static String URL_REAL = "http://www.weather.com.cn/adat/cityinfo/%s.html";//%s是城市ID 实时数据 
		public static String URL = "http://open.weather.com.cn/data/?";//三天数据
		public static String PRIVATE_KEY;
		public static String APPID;
		
		// 配置文件
	    private static Properties prop = new Properties();
	    
	    private static final Log log = Logs.get();
	    
	    /**
		 * 区域城市压
		 */
	    private final static Map<String, String> AREA_MAP = new HashMap<String, String>();
	    
	    
	    /**
	     * 新天气接城市
	     */
	    private final static Map<String, String> CityID_MAP = new HashMap<String, String>();
	    
		/**
		 * 风向风力
		 */
		private final static Map<String, String> WIND_MAP = new HashMap<String, String>();
		
		/**
		 * 天气现象
		 */
		private final static Map<String, String> WEATHER_MAP = new HashMap<String, String>();
		
		static {
			 try {
				prop.load(OpenWeather.class.getClassLoader().getResourceAsStream("openWeatherAuth.properties"));
				PRIVATE_KEY = prop.getProperty("privateKey", "ba2366_SmartWeatherAPI_82e688d");
				APPID = prop.getProperty("appId", "53af92ebbada50bf");
				URL = prop.getProperty("url", "http://open.weather.com.cn/data/?");
				prop.clear();
				
				prop.load(OpenWeather.class.getClassLoader().getResourceAsStream("areaId.properties"));
	            Set<Entry<Object, Object>> areaSet = prop.entrySet();
				if(!Lang.isEmpty(areaSet)) {
					for(Entry<Object, Object> o : areaSet) {
						System.out.println("" + o.getKey() + "" + o.getValue());
						AREA_MAP.put("" + o.getKey(), "" + o.getValue());
					}
				}
				
				prop.clear();
				
				prop.load(OpenWeather.class.getClassLoader().getResourceAsStream("cityID.properties"));
	            Set<Entry<Object, Object>> cityID = prop.entrySet();
				if(!Lang.isEmpty(cityID)) {
					for(Entry<Object, Object> o : cityID) {
						System.out.println("" + o.getKey() + "" + o.getValue());
						CityID_MAP.put("" + o.getKey(), "" + o.getValue());
					}
				}
				
				prop.clear();
				
				//天气现象
	            prop.load(OpenWeather.class.getClassLoader().getResourceAsStream("weather.properties"));
	            areaSet = prop.entrySet();
				if(!Lang.isEmpty(areaSet)) {
					for(Entry<Object, Object> o : areaSet) {
						System.out.println("" + o.getKey() + "" + o.getValue());
						WEATHER_MAP.put("" + o.getKey(), "" + o.getValue());
					}
				}
				
				prop.clear();
				
				//风向风力
				prop.load(OpenWeather.class.getClassLoader().getResourceAsStream("wind.properties"));
	            areaSet = prop.entrySet();
				if(!Lang.isEmpty(areaSet)) {
					for(Entry<Object, Object> o : areaSet) {
						System.out.println("" + o.getKey() + "" + o.getValue());
						WIND_MAP.put("" + o.getKey(), "" + o.getValue());
					}
				}
				System.out.println("..");
	        }
	        catch (IOException e) {
	            log.error("中国气象系统配置文件读取错误！！！" + e.getMessage());
	            e.printStackTrace();
	        }
		}
		
		/**
		 * 获取风力风向
		 * @param code
		 * @return
		 */
		public static String getWind(String code) {
			return WIND_MAP.get(code);
		}
		
		/**
		 * 获取天气
		 * @param code
		 * @return
		 */
		public static String getWeather(String code) {
			return WEATHER_MAP.get(code);
		}
		
		/**
		 * 获取城市ID
		 * @param cityName
		 * @return
		 */
		public static String getAreaId(String cityName) {
			String citycode = AREA_MAP.get(cityName); // 完整匹配
			
			if(citycode != null) {
				return citycode;
			}else { // 
				int length = cityName.length();
				if(cityName.contains("市")) {//左asdf
					if(cityName.indexOf("市") + 1 < length) {
						citycode = AREA_MAP.get(cityName.substring(cityName.indexOf("市") + 1));//取市后边的
						if(citycode != null) return citycode;
						
						citycode = AREA_MAP.get(cityName.substring(cityName.indexOf("市") + 1,length - 1));
						if(citycode != null) return citycode;
						
					} else {
						citycode = AREA_MAP.get(cityName.substring(0, cityName.indexOf("市")));//取市前边的
						if(citycode != null) return citycode;
					}
				}
				
				//** 市  **县
				citycode = AREA_MAP.get(cityName.substring(0, length - 1));
				
			}
			
			
	    	return citycode;
	    }
		
		public static String getCityID(String cityName){
			String citystr = cityName;
			String cityID  = CityID_MAP.get(cityName);
			if(cityID != null && !"".equals(cityID)){
				return cityID;
			}else{
			cityName = StringUtil.disposeStringNew(cityName);
			System.out.println(cityName);
			cityID  = CityID_MAP.get(cityName);
			int length = cityName.length();
		
			if(cityID != null){
				 return cityID;
			}else{
				
				if(cityName.indexOf("市") != -1 && (cityName.indexOf("市")+1<=length)){
					String names [] = cityName.split("市");
					cityID = CityID_MAP.get(names[names.length-1]);
					if(null != cityID) return cityID ;
					cityID = CityID_MAP.get(names[names.length-1]+"市");
					if(null != cityID) return cityID ;
				}	
				
				if(cityName.indexOf("区") != -1 && (cityName.indexOf("区")+1<=length)){
					String names [] = cityName.split("区");
					cityID = CityID_MAP.get(names[names.length-1]);
					if(null != cityID) return cityID ;
					cityID = CityID_MAP.get(names[names.length-1]+"区");
					if(null != cityID) return cityID ;
				}	
				if(cityName.indexOf("县") != -1 && (cityName.indexOf("县")+1<=length)){
					String names [] = cityName.split("县");
					cityID = CityID_MAP.get(names[names.length-1]);
					if(null != cityID) return cityID ;
					cityID = CityID_MAP.get(names[names.length-1]+"县");
					if(null != cityID) return cityID ;
				}
				if(cityName.indexOf("旗") != -1 && (cityName.indexOf("旗")+1<=length)){
					String names [] = cityName.split("旗");
					cityID = CityID_MAP.get(names[names.length-1]);
					if(null != cityID) return cityID ;
					cityID = CityID_MAP.get(names[names.length-1]+"旗");
					if(null != cityID) return cityID ;
				}
				
			}		
			
			if(null == cityID){
				if(citystr.indexOf("市") != -1 && (citystr.indexOf("市")+1<=length)){
					String names [] = citystr.split("市");
					cityID = CityID_MAP.get(names[0]);
					if(null != cityID) return cityID ;
					cityID = CityID_MAP.get(names[0]+"市");
					if(null != cityID) return cityID ;
				}
			}
			if(null == cityID){
				if(citystr.indexOf("省") != -1 && (citystr.indexOf("省")+1<=length)){
					String names [] = citystr.split("省");
					cityID = CityID_MAP.get(names[0]);
					if(null != cityID) return cityID ;
					cityID = CityID_MAP.get(names[0]+"省");
					if(null != cityID) return cityID ;
				}
			}
			
			return cityID ;
		}
		
		}
	}
	
	public static void main(String[] args) {
	//	System.out.println(">>>>> " + Config.OpenWeather.getWind("0"));
	//	System.out.println(">>>>> " + Config.OpenWeather.getWeather("02"));
	//	System.out.println(">>>>> " + Config.OpenWeather.getCityID("丰宁满族自治县"));
		System.out.println(">>>>> " + Config.OpenWeather.getCityID("玉树藏族自治州囊谦县"));
		
	}
}
