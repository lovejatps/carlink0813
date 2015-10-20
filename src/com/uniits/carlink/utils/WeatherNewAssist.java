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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.uniits.carlink.domain.WeatherInfo;
import com.uniits.carlink.domain.WeatherXML;
import com.uniits.carlink.domain.WeatherInfo.FutureWeatherInfo;
import com.uniits.carlink.domain.WeatherInfo.TodayWeatherInfo;
import com.uniits.carlink.domain.openweather.Weather;
import com.uniits.carlink.domain.openweather.WeatherList;

//适当的时候可以用RMI去执行
public class WeatherNewAssist {

	/**
	 * 缓存天气信息
	 */
	private static Map<String, WeatherInfo> weatherInfos;
	private static Map<String, WeatherXML> weatherXMLs;

	private static WeatherNewAssist weatherAssist;

	public static WeatherNewAssist getInstance() {
		if (weatherAssist == null) {
			synchronized (WeatherNewAssist.class) {
				if (weatherAssist == null) {
					weatherAssist = new WeatherNewAssist();
				}
			}
		}
		return weatherAssist;
	}

	private WeatherNewAssist() {
		weatherInfos = new HashMap<String, WeatherInfo>();
		weatherXMLs = new HashMap<String, WeatherXML>();
	}

	public synchronized String getCityWeatherInfo(String city)
			throws ParseException, UnsupportedEncodingException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat(Config.standardDateFormat);
	//	System.out.println(dateFormat);
		String cityID = Config.OpenWeather.getCityID(city);
		if (null == cityID){
			System.out.println(city +"Not cityId");
			return "找不到你输入的城市"; // 查询的城市不在供应商提供的城市中
		}
		WeatherXML weatherXml = weatherXMLs.get(cityID);
		boolean bGetMoreWeather = true;
		if (weatherXml != null) {
			if (!bNewDate(calendar, weatherXml.getMills())) {// 不跨天
				// Date xmlDate = dateFormat.parse(weatherXml.getDate());
				// long dValue = calendar.getTimeInMillis() - xmlDate.getTime();
				int xmlHour = weatherXml.getHour();
				Calendar cal = Calendar.getInstance();
				int hour = cal.get(Calendar.HOUR_OF_DAY);
				if (hour - xmlHour < 1) {// 小于1小时
					// weatherXml.setDistrict(city);
					JSONObject jsonObject = JSONObject.fromObject(weatherXml.getWeather());
					jsonObject.remove("city");
					jsonObject.put("city", city);
					return jsonObject.toString();
				} else {
					bGetMoreWeather = false;// 不跨天超过1小时
				}
			}
		} else {
			weatherXml = new WeatherXML();
		}
		
		
		// get json from net
		int bSucc = 0;
		
	//--------------------20150831更新-----------------------------
//		if (bGetMoreWeather) {
//			// 更新全部 跨天
//			try {
//				bSucc = updateWeather(cityID, city);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} else {
//			try {
//				bSucc = updateWeather(cityID, city);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}// 不跨天更新当前天气
//		}

		try {
			bSucc = updateWeather(cityID, city);
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		if (bSucc == 0) {
			weatherXml = weatherXMLs.get(cityID);
		} else {
			weatherXml.setWeather(bSucc + "");
		}
		return weatherXml.getWeather();
	}

	@SuppressWarnings("unchecked")
	public String createCityXML(WeatherInfo weatherInfo) throws ParseException {
		Map baseMap = new HashMap();
		baseMap.put("city", weatherInfo.getCity());
		baseMap.put("sutime", weatherInfo.getSutime());
		baseMap.put("pm2_5", weatherInfo.getPm2_5());
		baseMap.put("lifeadvice", weatherInfo.getLifeAdvice());
		Map todayMap = new HashMap();
		WeatherInfo.TodayWeatherInfo todayInfo = weatherInfo
				.getTodayWeatherInfo();
		todayMap.put("sktemp", todayInfo.getCurTemp());
		todayMap.put("sksd", todayInfo.getCurDumidity());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				Config.standardDateFormat);
		Date curDate = dateFormat.parse(todayInfo.getGetTime());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(curDate);
		todayMap.put("skhour", calendar.get(Calendar.HOUR_OF_DAY));
		todayMap.put("skmin", calendar.get(Calendar.MINUTE));
		todayMap.put("year", calendar.get(Calendar.YEAR));
		todayMap.put("month", calendar.get(Calendar.MONTH) + 1);
		todayMap.put("day", calendar.get(Calendar.DAY_OF_MONTH));
		todayMap.put("weather", todayInfo.getWeather());
		if (todayInfo.getWeather() == null)
			todayMap.put("weather", "未知");
		todayMap.put("img", todayInfo.getImage());
		todayMap.put("hightemp", todayInfo.getHighTemp());
		todayMap.put("lowtemp", todayInfo.getLowTemp());
		todayMap.put("wind", todayInfo.getWind());
		if (todayInfo.getWind() == null)
			todayMap.put("wind", "未知");
		todayMap.put("index_xc", todayInfo.getIndex_xc());
		baseMap.put("today", todayMap);
		List<Map> futureMaps = new ArrayList<Map>();
		List<FutureWeatherInfo> futureWeathers = weatherInfo
				.getFutureWeatherInfos();
		int futureInfosLen = futureWeathers.size();
		for (int i = 0; i < futureInfosLen; i++) {
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
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	private String getNvl(String s1, String s2) {
		if (!Lang.isEmpty(s1) && !"".equals(s1.trim())) {
			return s1;
		}

		if (!Lang.isEmpty(s2) && !"".equals(s2.trim())) {
			return s2;
		}
		return s1;
	}

	/**
	 * 去掉数字前边所有的0
	 * 
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

	public int updateWeather(String cityID, String cityName)
			throws ParseException, IOException {
		WeatherInfo info = new WeatherInfo();
		List<FutureWeatherInfo> futureWeatherInfos = new ArrayList<FutureWeatherInfo>();

		String moreWeatherUrl = null;
		// String disposeCity = NewStringUtil.disposeString(city);
		moreWeatherUrl = OpenWeatherNewHttp.getWeather(cityID);
		System.out.println(moreWeatherUrl);

		// WeatherList wl = Json.fromJson(WeatherList.class,moreWeatherUrl);
		// JSONObject json = JSONObject.fromObject(moreWeatherUrl);
		Object object1 = JSONObject.fromObject(moreWeatherUrl).get("weather");
		Object object2 = JSONObject.fromObject(
				JSONArray.fromObject(object1).get(0)).get("future");
		JSONObject object5 = (JSONObject) JSONObject.fromObject(
				JSONArray.fromObject(object1).get(0)).get("now");
		TodayWeatherInfo todayWeatherInfo = info.getNewTodayWeatherInfo();
		if (object2 != null) {
			JSONArray jsonArray1 = JSONArray.fromObject(object2);
			JSONObject object3 = (JSONObject) jsonArray1.get(0);
			todayWeatherInfo.setWind(object3.get("wind").toString());
			String weather = object3.get("text").toString();
			if (weather.indexOf("/") != -1) {
				String[] weathers = weather.split("/");
				Calendar cal = Calendar.getInstance();
				int hour = cal.get(Calendar.HOUR_OF_DAY);
				if (hour < 18) {
					todayWeatherInfo.setWeather(weathers[0]);
				} else {
					todayWeatherInfo.setWeather(weathers[1]);
				}
			} else {
				todayWeatherInfo.setWeather(weather);
			}

			todayWeatherInfo.setHighTemp(object3.get("high") + "℃");
			todayWeatherInfo.setLowTemp(object3.get("low") + "℃");
			String s[] = { NewConfig.findValue().get(object3.get("code1")),
					NewConfig.findValue().get(object3.get("code2")) };
			todayWeatherInfo.setImage(s);
			if (jsonArray1.size() > 1) {
				for (int i = 1; i < jsonArray1.size(); i++) {
					// 未来几天的时间
					JSONObject object4 = (JSONObject) jsonArray1.get(i);
					FutureWeatherInfo futureWeatherInfo = info
							.getNewFutureWeatherInfo();
					futureWeatherInfo.setWind(object4.get("wind").toString());
					String weatherAfter = object4.get("text").toString();
					if (weather.indexOf("/") != -1) {
						String[] weatherAfters = weatherAfter.split("/");
						Calendar cal = Calendar.getInstance();
						int hour = cal.get(Calendar.HOUR_OF_DAY);
						if (hour < 18) {
							futureWeatherInfo.setWeather(weatherAfters[0]);
						} else {
							futureWeatherInfo.setWeather(weatherAfters[1]);
						}
					} else {
						futureWeatherInfo.setWeather(weatherAfter);
					}
					// futureWeatherInfo.setWeather(object4.get("text").toString());
					futureWeatherInfo.setHighTemp(object4.get("high") + "℃");
					futureWeatherInfo.setLowTemp(object4.get("low") + "℃");
					String ss[] = {
							NewConfig.findValue().get(object4.get("code1")),
							NewConfig.findValue().get(object4.get("code2")) };
					todayWeatherInfo.setImage(s);
					futureWeatherInfo.setImage(ss);
					futureWeatherInfos.add(futureWeatherInfo);
				}
			}
			todayWeatherInfo.setCurDumidity(object5.getString("humidity"));
			todayWeatherInfo.setCurTemp(object5.getString("temperature") + "℃");
			Date date = new Date();
			String finalDateStr = Times.format("yyyy-MM-dd HH:mm:ss", date);
			todayWeatherInfo.setGetTime(finalDateStr);
			info.setTodayWeatherInfo(todayWeatherInfo);
			info.setFutureWeatherInfos(futureWeatherInfos);
			info.setCity(cityName);
			info.setSutime(date.getTime());
			if ("null".equals(object5.get("air_quality"))) {
				JSONObject josnObject5 = (JSONObject) object5
						.get("air_quality");
				JSONObject jsonObject6 = (JSONObject) josnObject5.get("city");

				if (jsonObject6.getString("pm25") != null
						&& !jsonObject6.getString("pm25").equals("")) {
					info.setPm2_5(Integer.parseInt(jsonObject6
							.getString("pm25").toString()));
				}
			}

			JSONObject jsonObject7 = (JSONObject) JSONObject.fromObject(
					JSONArray.fromObject(object1).get(0)).get("today");
			Map<String, String> lifeAdviceMap = new HashMap<String, String>();
			lifeAdviceMap.put("jt_hint", ""); // 交通指数
			lifeAdviceMap.put("jt_des", "");

			lifeAdviceMap.put("lk_hint", ""); // 路况指数
			lifeAdviceMap.put("lk_des", "");

			lifeAdviceMap.put("pl_hint", ""); // 空气污染扩散条件指数
			lifeAdviceMap.put("pl_des", "");
			if (!jsonObject7.get("suggestion").equals("null")) {
				JSONObject jsonObject8 = (JSONObject) jsonObject7
						.get("suggestion");

				JSONObject jsonObject9 = (JSONObject) jsonObject8.get("travel");
				lifeAdviceMap.put("tr_hint", jsonObject9.getString("brief")); // 旅游指数
				lifeAdviceMap.put("tr_des", jsonObject9.getString("details"));

				JSONObject jsonObject10 = (JSONObject) jsonObject8
						.get("car_washing");
				lifeAdviceMap.put("xc_hint", jsonObject10.getString("brief")); // 洗车指数
				lifeAdviceMap.put("xc_des", jsonObject10.getString("details"));

				todayWeatherInfo.setIndex_xc("");
				JSONObject jsonObject11 = (JSONObject) jsonObject8.get("sport");

				lifeAdviceMap.put("yd_hint", jsonObject11.getString("brief")); // 运动指数
				lifeAdviceMap.put("yd_des", jsonObject11.getString("details"));
			} else {
				lifeAdviceMap.put("tr_hint", ""); // 旅游指数
				lifeAdviceMap.put("tr_des", "");

				lifeAdviceMap.put("xc_hint", ""); // 洗车指数
				lifeAdviceMap.put("xc_des", "");

				lifeAdviceMap.put("yd_hint", ""); // 运动指数
				lifeAdviceMap.put("yd_des", "");
			}
			lifeAdviceMap.put("zs_hint", ""); // 中暑指数
			lifeAdviceMap.put("zs_des", "");
			info.setLifeAdvice(lifeAdviceMap);
			WeatherXML xml = new WeatherXML();
			xml.setMills(date.getTime());
			xml.setWeather(createCityXML(info));
			xml.setDistrict(cityName);
			xml.setDate(finalDateStr);
			xml.setDistrictID(null);
			Calendar cal = Calendar.getInstance();
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			xml.setHour(hour);
			weatherInfos.put(cityID, info);
			weatherXMLs.put(cityID, xml);
		}
		return 0;
	}

	/**
	 * 数组第一个为高温，第二个为低温
	 * 
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
		if (a == null)
			a = 0;
		if (b == null)
			b = 0;
		res[0] = Math.max(a, b) + "℃";
		res[1] = Math.min(a, b) + "℃";
		if (ts[0] == null || ts[0].trim().length() == 0)
			res[0] = "未知";
		if (ts[1] == null || ts[1].trim().length() == 0)
			res[1] = "未知";

		return res;
	}

}
