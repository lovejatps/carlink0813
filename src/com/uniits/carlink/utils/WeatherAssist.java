package com.uniits.carlink.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.Times;

import net.sf.json.JSONObject;

import com.uniits.carlink.domain.WeatherInfo;
import com.uniits.carlink.domain.WeatherXML;
import com.uniits.carlink.domain.WeatherInfo.FutureWeatherInfo;
import com.uniits.carlink.domain.WeatherInfo.TodayWeatherInfo;
import com.uniits.carlink.domain.openweather.Weather;
import com.uniits.carlink.domain.openweather.WeatherList;
//适当的时候可以用RMI去执行
public class WeatherAssist {

	/**
	 * 缓存天气信息
	 */
	private static Map<String, WeatherInfo> weatherInfos;
	private static Map<String, WeatherXML> weatherXMLs;
	
	private static WeatherAssist weatherAssist;
	
	public static WeatherAssist getInstance() {
		if (weatherAssist == null) {
            synchronized (WeatherAssist.class) {
                if (weatherAssist == null) {
                	weatherAssist = new WeatherAssist();
                }
            }
		}
		return weatherAssist;
	}

	private WeatherAssist() {
		weatherInfos = new HashMap<String, WeatherInfo>();
		weatherXMLs = new HashMap<String, WeatherXML>();
	}

	public synchronized String getCityWeatherInfo(String city) throws ParseException, UnsupportedEncodingException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat(Config.standardDateFormat);

		WeatherXML weatherXml = weatherXMLs.get(city);
		boolean bGetMoreWeather = true ;
		if (weatherXml != null) {
			if (!bNewDate(calendar, weatherXml.getMills())) {//不跨天
				Date xmlDate = dateFormat.parse(weatherXml.getDate());
				long dValue = calendar.getTimeInMillis() - xmlDate.getTime();
				if (dValue <= (1000 * 60 * 1)) {//小于0.1小时
					return weatherXml.getWeather();
				}else {
					bGetMoreWeather  = false;//不跨天超过0.1小时
				}
			}
		} else {
			weatherXml = new WeatherXML();
		}
		// get json from net
		int bSucc = 0;
		if(bGetMoreWeather) {
			//更新全部 跨天
			try {
				bSucc = updateWeather(city);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			try {
				bSucc = updateWeather(city);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//不跨天更新当前天气
		}
		
		if(bSucc == 0) {
			weatherXml = weatherXMLs.get(city);
		}else {
			weatherXml.setWeather(bSucc + "");
		}
		return weatherXml.getWeather();
	}

	@SuppressWarnings("unchecked")
	public String createCityXML(WeatherInfo weatherInfo) throws ParseException {
		Map baseMap = new HashMap();
		baseMap.put("city", weatherInfo.getCity());
		baseMap.put("sutime", weatherInfo.getSutime());
		baseMap.put("pm2_5" , weatherInfo.getPm2_5());
		baseMap.put("lifeadvice", weatherInfo.getLifeAdvice());
		Map todayMap = new HashMap();
		WeatherInfo.TodayWeatherInfo todayInfo = weatherInfo.getTodayWeatherInfo();
		todayMap.put("sktemp", todayInfo.getCurTemp());
		todayMap.put("sksd" , todayInfo.getCurDumidity());
		SimpleDateFormat dateFormat = new SimpleDateFormat(Config.standardDateFormat);
		Date curDate = dateFormat.parse(todayInfo.getGetTime());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(curDate);
		todayMap.put("skhour" , calendar.get(Calendar.HOUR_OF_DAY));
		todayMap.put("skmin" , calendar.get(Calendar.MINUTE));
		todayMap.put("year" , calendar.get(Calendar.YEAR));
		todayMap.put("month" , calendar.get(Calendar.MONTH) + 1);
		todayMap.put("day" , calendar.get(Calendar.DAY_OF_MONTH));
		todayMap.put("weather", todayInfo.getWeather());
		if(todayInfo.getWeather() == null) todayMap.put("weather", "未知");
		todayMap.put("img", todayInfo.getImage());
		todayMap.put("hightemp" , todayInfo.getHighTemp());
		todayMap.put("lowtemp" , todayInfo.getLowTemp());
		todayMap.put("wind", todayInfo.getWind());
		if(todayInfo.getWind() == null ) todayMap.put("wind", "未知");
		todayMap.put("index_xc", todayInfo.getIndex_xc());
		baseMap.put("today", todayMap);
		List<Map> futureMaps = new ArrayList<Map>();
		List<FutureWeatherInfo> futureWeathers = weatherInfo.getFutureWeatherInfos();
		int futureInfosLen = futureWeathers.size();
		for(int i=0; i< futureInfosLen ; i++) {
			Map futureMap = new HashMap();
			FutureWeatherInfo futureInfo = futureWeathers.get(i);
			futureMap.put("weather", futureInfo.getWeather());
			futureMap.put("hightemp", futureInfo.getHighTemp());
			futureMap.put("lowtemp", futureInfo.getLowTemp());
			futureMap.put("img", futureInfo.getImage());
			futureMap.put("wind", futureInfo.getWind());
			futureMaps.add(futureMap);
		}
		baseMap.put("future", futureMaps);
		JSONObject jsonObject = JSONObject.fromObject(baseMap);
		return jsonObject.toString();
	}
	
	public boolean bNewDate(Calendar calendar, long mills) {
		Date xmlDate = new Date(mills);
		Calendar tmpCalendar = Calendar.getInstance();
		tmpCalendar.setTime(xmlDate);
		if (calendar.get(Calendar.YEAR) == tmpCalendar.get(Calendar.YEAR)
				&& calendar.get(Calendar.MONTH) == tmpCalendar
						.get(Calendar.MONTH)
				&& calendar.get(Calendar.DAY_OF_MONTH) == tmpCalendar
						.get(Calendar.DAY_OF_MONTH)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 取第一个不为null的值，如果 都为空那就返回第一个个
	 * @param s1
	 * @param s2
	 * @return
	 */
	private String getNvl(String s1,String s2) {
		if(!Lang.isEmpty(s1) && !"".equals(s1.trim())) {
			return s1;
		}
		
		if(!Lang.isEmpty(s2) && !"".equals(s2.trim())) {
			return s2;
		}
		return s1;
	}
	
	/**
	 * 去掉数字前边所有的0
	 * @param n
	 * @return
	 */
	private String fixNumPre(String n) {
		String tmp = "" + n;
		while (tmp.startsWith("0") && tmp.length() > 1) {
			tmp = tmp.substring(1);
		}
		return tmp;
	}
	
	public int updateWeather(String city) throws ParseException, IOException {
		WeatherInfo info = new WeatherInfo();
		List<FutureWeatherInfo> futureWeatherInfos = new ArrayList<FutureWeatherInfo>();

		String moreWeatherUrl = null;
		moreWeatherUrl = OpenWeatherHttp.getWeather(city);
		System.out.println(moreWeatherUrl);
		
		WeatherList wl = Json.fromJson(WeatherList.class,moreWeatherUrl);
		
		TodayWeatherInfo todayWeatherInfo = info.getNewTodayWeatherInfo();
		if(wl != null) {
			List<Weather> list = wl.getF().getF1();
			if(list != null && list.size() > 0) {
				Weather td = list.get(0);//今天
				
				todayWeatherInfo.setWind(Config.OpenWeather.getWind(getNvl(td.getFe(),td.getFf())));
				todayWeatherInfo.setWeather(Config.OpenWeather.getWeather(getNvl(td.getFa(),td.getFb())));
				String temps[] = sortTemperature(new String[]{td.getFc(),td.getFd()});
				todayWeatherInfo.setHighTemp(temps[0]);
				todayWeatherInfo.setLowTemp(temps[1]);
				String fa = td.getFa();
				if(td.getFa() == null || "".equals(td.getFa().trim())) {
					fa = td.getFb();
				}
				String[] imgStr = {fixNumPre(fa),fixNumPre(td.getFb())}; 
				todayWeatherInfo.setImage(imgStr);
				
				//未来两天
				if(list.size() > 1) {
					for(int i = 1; i < list.size(); i++) {
						Weather w = list.get(i);
						FutureWeatherInfo futureWeatherInfo = info.getNewFutureWeatherInfo();
						futureWeatherInfo.setWind(Config.OpenWeather.getWind(getNvl(w.getFe(),w.getFf())));
						futureWeatherInfo.setWeather(Config.OpenWeather.getWeather(getNvl(w.getFa(),w.getFb())));
						String temps2[] = sortTemperature(new String[]{w.getFc(),w.getFd()});
						futureWeatherInfo.setHighTemp(temps2[0]);
						futureWeatherInfo.setLowTemp(temps2[1]);
						
						fa = w.getFa();
						if(w.getFa() == null || "".equals(w.getFa().trim())) {
							fa = w.getFb();
						}
						String[] imgStr2 = {fixNumPre(fa),fixNumPre(w.getFb())}; 
						futureWeatherInfo.setImage(imgStr2);
			 			futureWeatherInfos.add(futureWeatherInfo);
					}
				}
			}
		}
		
		todayWeatherInfo.setCurDumidity("");
		todayWeatherInfo.setCurTemp("");
		String finalDateStr = wl.getF().getF0();
		Date date = new Date();
		 /*Date date=new Date();//取时间 
	     Calendar   calendar   =   new   GregorianCalendar(); 
	     calendar.setTime(date); 
	     calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
	     date=calendar.getTime();*/
		finalDateStr = Times.format("yyyy-MM-dd HH:mm:ss", date);
		
		
		todayWeatherInfo.setGetTime(finalDateStr);
		info.setTodayWeatherInfo(todayWeatherInfo);
		info.setFutureWeatherInfos(futureWeatherInfos);
		info.setCity(city); 
		info.setSutime(date.getTime());
		
		Map<String, String> lifeAdviceMap = new HashMap<String, String>();
		
		lifeAdviceMap.put("jt_hint", "");  //交通指数
		lifeAdviceMap.put("jt_des", "");
		
		lifeAdviceMap.put("lk_hint", "");  //路况指数
		lifeAdviceMap.put("lk_des", "");
		
		lifeAdviceMap.put("pl_hint", "");  //空气污染扩散条件指数
		lifeAdviceMap.put("pl_des", "");
		
		lifeAdviceMap.put("tr_hint", ""); //旅游指数
		lifeAdviceMap.put("tr_des", "");
		
		lifeAdviceMap.put("xc_hint", ""); //洗车指数
		lifeAdviceMap.put("xc_des", "");
		
		todayWeatherInfo.setIndex_xc("");
		
		lifeAdviceMap.put("yd_hint", ""); // 运动指数
		lifeAdviceMap.put("yd_des", "");
		
		lifeAdviceMap.put("zs_hint", ""); //中暑指数
		lifeAdviceMap.put("zs_des", "");
		info.setLifeAdvice(lifeAdviceMap);
		WeatherXML xml = new WeatherXML();
		xml.setMills(date.getTime());
		xml.setWeather(createCityXML(info));
		xml.setDistrict(city);
		xml.setDate(finalDateStr);
		xml.setDistrictID(null);
		weatherInfos.put(city, info);
		weatherXMLs.put(city, xml);
		return 0;
	}
	
	/**
	 * 数组第一个为高温，第二个为低温
	 * @param ts
	 * @return
	 */
	private String[] sortTemperature(String[] ts) {
		String[] res = new String[ts.length];
		Integer a = null;
		try {
			a = Integer.parseInt(ts[0].replace("℃", "").trim());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		Integer b = null;
		try {
			b = Integer.parseInt(ts[1].replace("℃", "").trim());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if(a==null) a = 0;
		if(b == null) b = 0;
		res[0] = Math.max(a, b) + "℃";
		res[1] = Math.min(a, b) + "℃";
		if(ts[0]==null || ts[0].trim().length() == 0) res[0] = "未知";
		if(ts[1]==null || ts[1].trim().length() == 0) res[1] = "未知";
		
		return res;
	}
}
