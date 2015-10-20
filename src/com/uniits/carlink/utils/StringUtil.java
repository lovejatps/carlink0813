package com.uniits.carlink.utils;

public class StringUtil {
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
			city = str.substring(str.indexOf("自治县")+3,str.length() );
		}else if(str.indexOf("自治州") != -1){
			city = str.substring(str.indexOf("自治州")+3,str.length() );
		}
		if(str.indexOf("特别行政区") != -1){
			city = str.substring(0, str.indexOf("特别行政区"));
		}
		return city;
	}
	
	public static String disposeStringNew(String str){
		String city = str;
		if(str.indexOf("区") != -1){
			if(str.indexOf("市")+1 >= str.length()){
				
			}else{
				city = str.substring(str.indexOf("市")+1, str.length());
			}
		}else if(str.indexOf("县") != -1){
			city = str.substring(str.indexOf("市")+1, str.length());
		}else if(str.indexOf("市") != -1){
			city = str.substring(str.indexOf("省")+1,str.length());
		}
		if(str.indexOf("地区")!= -1) {
			if(str.indexOf("地区")+2 < str.length()) city = str.substring(str.indexOf("地区")+2, str.length());

			if(str.indexOf("地区")+2 >= str.length())city = str.substring(0,str.indexOf("地区"));
			
		}
		// 松原市前郭尔罗斯蒙古族自治县
		/*if(str.indexOf("自治县") != -1){
				city = strSplit(str);
			
		}*/
		/*if(str.indexOf("自治州") != -1){
			city = str.substring(str.indexOf("自治州")+3,str.length() );
		}*/
		if(str.indexOf("特别行政区") != -1){
			if(str.indexOf("特别行政区") +5 < str.length()){
				city = str.substring(str.indexOf("特别行政区") +5,str.length());
			}else{
				city = str.substring(0, str.indexOf("特别行政区"));
			}
		}
		if(str.indexOf("旗")!=-1 && str.indexOf("市")!=-1){
			city = str.substring(str.indexOf("市")+1, str.length());
		}
		city = strSplit(city);
		return city;
	}
	
	
	private  static String strSplit(String city){
		for(String s :clan){
			city = city.replace(s, "");
		}
		
		return city; 
	}
	
	
	private static final  String [] clan  ={
		"苗族",
		"土家族",
		"满族",
		"白族",
		"朝鲜族",
		"蒙古族",
		"彝族",
		"回族",
		"自治县",
		"锡林郭勒盟",
		"尔罗斯",
		"自治州",
		"兴安盟",
		"阿拉善盟",
		"僳僳族",
		"普米族",
		"独龙族",
		"怒族",
		"傣族",
		"景颇族",
		"藏族",
		"羌族",
		"土族",
		"海东地区",
		"撒拉族",
		"新疆维吾尔",
		"自治区",
		//"市",
		"昌吉",
		"哈萨克",
		"巴音郭楞",
		"蒙古",
		"喀什地区",
		"塔吉克",
		"伊犁",
		"锡伯",
		"塔城地区",
		"哈密地区",
		"水族",
		"黔南",
		//"县",
		"延边",
		"呼伦贝尔",
		"自治",
		"黔西南",
		"布依族",
		"红河",
		"哈尼族",
		"文山",
		"壮族",
		"山南地区",
		"博尔塔拉",
		"德宏",
		"西双版纳",
		"临夏",
		"克孜勒苏柯尔克孜",
		"达斡尔族",
		"巴尔虎",
		"果洛",
		"湘西",
		"甘孜",
		"侗族",
		"黔东南",
		"楚雄",
		"凉山",
		"阿坝",
		"玉树",
		
		
	};
	
	public static void main(String[] args) {

		String cityID = Config.OpenWeather.getCityID(strSplit("果洛藏族自治州共和县"));
		System.out.println(">>>>> " + cityID  +"_____"+strSplit("果洛藏族自治州共和县"));
		
	}
	
}

/*if(str.indexOf("满族自治县") != -1 && str.indexOf("满族自治县") + 5 >= str.length()){
city = str.substring(0,str.indexOf("满族自治县"));
}
if(str.indexOf("满族自治县") != -1 && str.indexOf("满族自治县") +5 < str.length()){
city = str.substring(str.indexOf("满族自治县")+5,str.length());
}

if(str.indexOf("蒙古族自治县") != -1 && str.indexOf("蒙古族自治县") + 6 >= str.length()){
city = str.substring(0,str.indexOf("蒙古族自治县"));
}
if(str.indexOf("蒙古族自治县") != -1 && str.indexOf("蒙古族自治县") +6 < str.length()){
city = str.substring(str.indexOf("蒙古族自治县")+6,str.length());
}

if(str.indexOf("土家族自治县") != -1 && str.indexOf("土家族自治县") + 6 >= str.length()){
city = str.substring(0,str.indexOf("土家族自治县"));
}
if(str.indexOf("土家族自治县") != -1 && str.indexOf("土家族自治县") +6 < str.length()){
city = str.substring(str.indexOf("土家族自治县")+6,str.length());
}

if(str.indexOf("苗族土家族自治县") != -1 && str.indexOf("苗族土家族自治县") + 8 >= str.length()){
city = str.substring(0,str.indexOf("苗族土家族自治县"));
}
if(str.indexOf("苗族土家族自治县") != -1 && str.indexOf("苗族土家族自治县") +8 < str.length()){
city = str.substring(str.indexOf("苗族土家族自治县")+8,str.length());
}

if(str.indexOf("土家族苗族自治县") != -1 && str.indexOf("土家族苗族自治县") + 8 >= str.length()){
city = str.substring(0,str.indexOf("土家族苗族自治县"));
}
if(str.indexOf("土家族苗族自治县") != -1 && str.indexOf("土家族苗族自治县") +8 < str.length()){
city = str.substring(str.indexOf("土家族苗族自治县")+8,str.length());
}*/