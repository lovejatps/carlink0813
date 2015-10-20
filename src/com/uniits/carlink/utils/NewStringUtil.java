package com.uniits.carlink.utils;

public class NewStringUtil {
	public static String disposeString(String str){
		String city = str;
		if(str.indexOf("区") != -1){
			city = str.substring(str.indexOf("市")+1, str.length());
		}else if(str.indexOf("县") != -1){
			city = str.substring(str.indexOf("市")+1, str.length());
		}else if(str.indexOf("市") != -1){
			city = str.substring(str.indexOf("省")+1,str.length());
		}
		if(str.indexOf("自治县") != -1){
			city = str.substring(str.indexOf("自治县")+3,str.length());
		}else if(str.indexOf("藏族自治州") != -1){
			city = str.substring(0,str.indexOf("藏族自治州"));
		}		
		else if(str.indexOf("自治州") != -1){
			city = str.substring(str.indexOf("自治州")+3,str.length());
		}
		if(str.indexOf("特别行政区") != -1){
			city = str.substring(0, str.indexOf("特别行政区"));
		}
		if(city.indexOf("市") != -1 && (city.indexOf("市")+1<city.length())){
			city = city.substring(city.indexOf("市")+1, city.length());
		}
		if(city.indexOf("区") != -1){
			city = city.substring(0, city.length()-1);
		}else if(city.indexOf("县") != -1){
			city = city.substring(0, city.length()-1);
		}else if(city.indexOf("市") != -1){
			city = city.substring(0, city.length()-1);
		}
		System.out.println("city:"+city);
		return city;
	}
	
	
	/*public static String disposeStringNew(String str){
		String city = str;
		if(str.indexOf("区") != -1){
			city = str.substring(str.indexOf("市")+1, str.length());
		}else if(str.indexOf("县") != -1){
			city = str.substring(str.indexOf("市")+1, str.length());
		}else if(str.indexOf("市") != -1){
			city = str.substring(str.indexOf("省")+1,str.length());
		}
		if(str.indexOf("自治县") != -1){
			city = str.substring(str.indexOf("自治县")+3,str.length());
		}else if(str.indexOf("自治州") != -1){
			city = str.substring(str.indexOf("自治州")+3,str.length());
		}
		if(str.indexOf("特别行政区") != -1){
			city = str.substring(0, str.indexOf("特别行政区"));
		}
		if(city.indexOf("市") != -1 && (city.indexOf("市")<city.length())){
			city = city.substring(city.indexOf("市")+1, city.length());
		}
		if(city.indexOf("区") != -1){
			city = city.substring(0, city.length()-1);
		}else if(city.indexOf("县") != -1){
			city = city.substring(0, city.length()-1);
		}else if(city.indexOf("市") != -1){
			city = city.substring(0, city.length()-1);
		}
		return city;
	}
	*/
}
