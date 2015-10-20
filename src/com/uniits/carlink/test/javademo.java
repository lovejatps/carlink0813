package com.uniits.carlink.test;

import javax.crypto.Mac;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.jsoup.Jsoup;
import org.nutz.lang.Times;

public class javademo {

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
	
	
	public static void main(String[] args) {
		try {
			
			//需要加密的数据  
            String data = "http://open.weather.com.cn/data/?areaid=xxxxxxxxxx&type=xxxxxxxx&date=xxxxxxxxx&appid=xxxxxxx"; 
            
            data = "http://open.weather.com.cn/data/?areaid=101010100&type=index_f&date=201504101836&appid=53af92ebbada50bf";
            //密钥  
            String key = "ba2366_SmartWeatherAPI_82e688d";  
            
            String str = standardURLEncoder(data, key);
            
            System.out.println(str);
			t();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void t() throws IOException {
			/*
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
		//http://openweather.weather.com.cn/Home/Help/area.html
		String privatekey = "ba2366_SmartWeatherAPI_82e688d";
		String appId  = "53af92ebbada50bf";
		String appId6 = "53af92";
		
		String areaId = "101010100";
		String type = "forecast_f";//index_v,index_f
		String date = Times.format("yyyyMMddHHmm", new Date());
		
		String base =String.format("http://open.weather.com.cn/data/?areaid=%s&type=%s&date=%s&appid=%s",
				areaId,type,date,appId);
		String key = standardURLEncoder(base, privatekey);
		
		String url = String.format("http://open.weather.com.cn/data/?areaid=%s&type=%s&date=%s&appid=%s&key=%s",
				areaId,type,date,appId6,key);
		
		System.out.println(">>>:" + url);
		
		String json = Jsoup.connect(url).get().body().ownText().toString();
		System.out.println("json:" + json);
	}
	
}