package com.uniits.carlink.test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.nutz.http.Http;
import org.nutz.http.Response;

import com.uniits.carlink.utils.Config;
import com.uniits.carlink.utils.HttpConnAssist;
import com.uniits.carlink.utils.NewsAssist;

public class NetTest {

	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 */
	private final static String url="http://localhost:8080/Struts2_Spring3_Hibernate3/weather?district=%E5%8C%97%E4%BA%AC";
	private final static String newUrl = "http://news.baidu.com/ns?word=战事+sina&tn=newsfcu&from=news&cl=2&rn=10&ct=0";
	public static void main(String[] args) throws UnsupportedEncodingException {
		long start = System.currentTimeMillis();
		NewsAssist assist = new NewsAssist();
		System.out.println(assist.getNewsByKeyWord("时事"));
		long end = System.currentTimeMillis();
		System.out.println(start - end);
	}
	
	
	public static void parseXML(String newStr) {
		newStr = newStr.replace("document.write('", "");
		newStr = newStr.replace("target=\"_blank\"", "");
		newStr = newStr.replace("')", "");
		System.out.println(newStr);
		Pattern p = Pattern.compile("<a href=.*?</a>");
		Matcher m = p.matcher(newStr);
		List<String> result=new ArrayList<String>();
		while(m.find()){
			result.add(m.group());
			System.out.println(m.group());
			Pattern p1 = Pattern.compile("\"(.*)?\"");
			Matcher m1 = p1.matcher(m.group());
			if(m1.find()) {
				if(m1.group().contains("sina.com.cn")) {
					System.out.println(m1.group(1));
				}
				p1 = Pattern.compile(">(.*)?</a>");
				m1 = p1.matcher(m.group());
				if(m1.find()) {
					System.out.println(m1.group(1));
				}
			}
		}
		String testStr = "12315<Test>show me</Text>show me</Text>";  
		Pattern p2 = Pattern.compile("<Test>(.*)</Text>");  
		Matcher m2 = p2.matcher(testStr);  
		while(m2.find()){  
			System.out.println(m2.group(1));  
		}
		
	}
	
	@Test
	public void testNutzHttp() {
		Http.setHttpProxy("36.250.69.4",80);
		Response resp = Http.get("http://api.36wu.com/Weather/GetMoreWeather?district=%E5%8C%97%E4%BA%AC");
		if (resp.isOK()) {
		    System.out.println(resp.getContent());
		}
	}
}
