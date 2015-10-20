/**
 * 
 */
package com.uniits.carlink.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.nutz.lang.Lang;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.uniits.carlink.utils.Config.OpenWeather;

/**
 * @author Administrator
 * 
 */
public class AreaTest {

	public final static Map<String, String> AREA_MAP = new HashMap<String, String>();

	// 配置文件
	private static Properties prop = new Properties();

	private static final Log log = Logs.get();

	static {

		try {
			prop.load(OpenWeather.class.getClassLoader().getResourceAsStream(
					"areaId.properties"));
//			Set<Object> sets = prop.keySet();
//			if (!Lang.isEmpty(sets)) {
//				for (Object o : sets) {
//					System.out.println(o.toString() + "="
//							+ prop.getProperty(o.toString()));
//				}
//			}
			Set<Entry<Object, Object>> areaSet = prop.entrySet();
			if(!Lang.isEmpty(areaSet)) {
				for(Entry<Object, Object> o : areaSet) {
					System.out.println(o.getKey() + "///=" + o.getValue());
					
				}
			}
			// prop.entrySet();
		} catch (IOException e) {
			log.error("系统配置文件读取错误！！！" + e.getMessage());
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println(">>>>>");
	}
}