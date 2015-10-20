package com.uniits.carlink.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.nutz.lang.Lang;
import org.nutz.lang.Stopwatch;
import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;

import com.uniits.carlink.domain.NewsInfo;

@Component
public class NewsAssist {
	
	public String getNewsByKeyWord(String keyWrod) throws UnsupportedEncodingException {
		String newsStr = HttpConnAssist.getJsonInfoFromNet(Config.NewsURL.NEWSKEYWORDHEADURL+keyWrod+Config.NewsURL.NEWSKEYWORDTAILURL , Config.NewsURL.CHARSET);
		if(newsStr == null) {
			return "-1" ;
		}
		newsStr = newsStr.replace("document.write('", "");
		newsStr = newsStr.replace("target=\"_blank\"", "");
		newsStr = newsStr.replace("')", "");
		Pattern p = Pattern.compile("<a href=.*?</a>");
		Matcher m = p.matcher(newsStr);
		List<String> result=new ArrayList<String>();
		List<NewsInfo> infos = new ArrayList<NewsInfo>();
		while(m.find()){
			result.add(m.group());
			Pattern p1 = Pattern.compile("\"(.*)?\"");
			Matcher m1 = p1.matcher(m.group());
			if(m1.find()) {
				if(m1.group().contains("sina.com.cn")) {
					NewsInfo info = new NewsInfo();
					info.setUrl(m1.group(1));
					p1 = Pattern.compile(">(.*)?</a>");
					m1 = p1.matcher(m.group());
					if(m1.find()) {
						info.setTitle1(m1.group(1));
					}
					infos.add(info);
				}
			}
		}
		
		if(Lang.isEmpty(infos)) {
		    try {
                infos.addAll(youdao(keyWrod));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
		}
		
		return createXml(infos);
	}
	
	private String createXml(List<NewsInfo> infos) {
		Map<String, List<Map<String, String>>> baseMap = new HashMap<String, List<Map<String,String>>>();   
		int infosLen = infos.size();
		List<Map<String , String>> futureMaps = new ArrayList<Map<String , String>>();
		for(int i=0 ; i < infosLen ; i++) {
			NewsInfo info = infos.get(i);
			Map<String , String> results = new HashMap<String,String>();
			results.put("title", info.getTitle());
			results.put("url", info.getUrl());
			futureMaps.add(results);
		}
		baseMap.put("result", futureMaps);
		JSONObject jsonObject = JSONObject.fromObject(baseMap);
		return filterUTF8(jsonObject.toString());
	}
	
	public String filterUTF8(String ss) {
	    String pat = "([\u2E80-\u9FFF]+|[\\x09\\x0A\\x0D\\x20-\\x7E]+" + 
                "| [\\xC2-\\xDF][\\x80-\\xBF]+" +   
                "| \\xE0[\\xA0-\\xBF][\\x80-\\xBF]+" +   
                "| [\\xE1-\\xEC\\xEE\\xEF][\\x80-\\xBF]{2}+" +   
                "| \\xED[\\x80-\\x9F][\\x80-\\xBF]+" +   
                "| \\xF0[\\x90-\\xBF][\\x80-\\xBF]{2}+" +   
                "| [\\xF1-\\xF3][\\x80-\\xBF]{3}+" +   
                "| \\xF4[\\x80-\\x8F][\\x80-\\xBF]{2}+)";  
        
      
        Pattern p = Pattern.compile(pat,Pattern.MULTILINE | Pattern.DOTALL);
        Matcher m = p.matcher(ss);
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            sb.append(m.group(1));
        } 

        return sb.toString();
	}
	
	/**
	 * 有道新闻
	 * @param keyword
	 * @return
	 * @throws IOException
	 */
	public List<NewsInfo> youdao(String keyword) throws IOException {
	    List<NewsInfo> infos = null;
	    Stopwatch sw = Stopwatch.begin();
	    String url = Config.NewsURL.NEWS_YOUDAO_URL + keyword;
	    
	    Document doc = Jsoup.connect(url).get();
	    Elements els = doc.select("li>h3>a");
	    
	    System.out.println(els);
	    
	    if(!Lang.isEmpty(els)) {
	        infos = new ArrayList<NewsInfo>();
    	    for(int i= 0;i<els.size();i++) {
    	        Element e = els.get(i);
    	        NewsInfo info = new NewsInfo();
    	        info.setTitle1(e.text());
    	        info.setUrl(e.attr("href"));
    	        infos.add(info);
    	    }
	    }
	    sw.stop();
	    System.out.println("网易-youdao>>>>>: " + sw.getDuration());
	    return infos;
	}
	
	@Test
	public void test () throws IOException {
	    String key = URLEncoder.encode("美女", "UTF-8");
	    List<NewsInfo> list = youdao(key);
	    
	    for(int i = 0; i < list.size(); i++) {
	        System.out.println(">>>>> : " + i + ">title:" + list.get(i).getTitle() + ">url:" + list.get(i).getUrl());
	    }
	}
}
