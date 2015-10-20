package com.uniits.carlink.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Lang;
import org.nutz.lang.Stopwatch;
import org.springframework.stereotype.Component;

import com.uniits.carlink.domain.BaiduMusicResultInfo;
import com.uniits.carlink.domain.vo.baidu.BaiduMusicFirstVo;
import com.uniits.carlink.domain.vo.baidu.BaiduMusicSecondDataListVo;
import com.uniits.carlink.domain.vo.baidu.BaiduMusicSecondVo;
import com.uniits.carlink.spair.MusicSearchSpair;


@Component("musicAssist")
public class MusicAssist {
	
	public static CacheTop cacheTop = new CacheTop();

	/**
	 * 第一次获取百度音乐
	 * 
	 * @param keyWord
	 * @return
	 */
	public String getBaiduMusicListOne(String keyWord) {
		long start = System.currentTimeMillis();
		StringBuffer sbuff = new StringBuffer();
		boolean b = false;
		for (int i = 0; i < 3; i++) {

			String json = HttpConnAssist.getJsonInfoFromNet(
					Config.MusicURL.MUSICURL_ONE + keyWord,
					Config.MusicURL.CHARSET);
			if (json != null && !json.trim().equals("[]")) {
				sbuff.append(json);
				b = true;
				break;
			}
		}

		String songIds = "";
		if (b) {
			List<BaiduMusicFirstVo> list = Json.fromJsonAsList(BaiduMusicFirstVo.class, sbuff.toString());
			if (list != null && list.size() > 0) {
				songIds = firstSongIdsFilter(list);
			}
		}

		long end = System.currentTimeMillis();
		if (b) {
			System.out.println(end - start);
		}

		return songIds;
	}

	private String firstSongIdsFilter(final List<BaiduMusicFirstVo> list) {
		StringBuffer sb = new StringBuffer();
		if (list != null && list.size() > 0) {
			List<BaiduMusicFirstVo> tmpList = BaiduMusicFirstVo.singleElement(list);
			sb.append(tmpList.get(0).getSong_id());
			for (int i = 1; i < tmpList.size(); i++) {
				BaiduMusicFirstVo bm = tmpList.get(i);
				sb.append(",").append(bm.getSong_id());
			}
		}
		return sb.toString();
	}

	/**
	 * 第二次获取百度音乐（2015-03-18 百度接口一次取多个失败）
	 * 
	 * @param songIds
	 * @return
	 */
	public String getBaiduMusicListTwo2(String songIds,String keyword) {
		
		List<BaiduMusicResultInfo> list = new ArrayList<BaiduMusicResultInfo>();
		boolean b = false;
		String resultJson = "";
		if(!Lang.isEmpty(songIds)) {
		    String[] ids = songIds.split(",");
		    int count = 0;
		    for(String id : ids ) {
		        if(count++ > 10) break;
		        String json = "";
		        for (int i = 0; i < 3; i++) {//发请求
		            json = HttpConnAssist.getJsonInfoFromNet(Config.MusicURL.MUSICURL_TWO + id, Config.MusicURL.CHARSET);
		            if (json != null && !json.trim().equals("[]")) {
		                b = true;
		                break;
		            }
		        }
		        if (b) {
		            BaiduMusicSecondVo bms = Json.fromJson(BaiduMusicSecondVo.class, json);
		            if (bms != null && bms.getData() != null && bms.getData().getSongList() != null) {
		                list.addAll(secondResultFilter(bms.getData().getSongList()));
		            }
		        }
		    }
		}
		
		//排序 歌名-歌手-专辑 中含有关键词 
        if(StringUtils.isNotBlank(keyword)) {
            list = sortByKeyword(list, keyword);
        }
        
        Map<String, List<BaiduMusicResultInfo>> map = new HashMap<String, List<BaiduMusicResultInfo>>();
        map.put("result", list);
        resultJson = Json.toJson(map,JsonFormat.compact());
		
		return resultJson;
	}
	
	/**
     * 第二次获取百度音乐（2015-03-18 百度接口一次取多个失败）
     * 
     * @param songIds
     * @return
     */
    public String getBaiduMusicListTwo3(String songIds,String keyword) {
        
        List<BaiduMusicResultInfo> list = new ArrayList<BaiduMusicResultInfo>();
        boolean b = false;
        String resultJson = "";
        if(!Lang.isEmpty(songIds)) {
            String[] ids = songIds.split(",");
            int count = 0;
            for(String id : ids ) {
                if(count++ > 2) break;
                String json = "";
                for (int i = 0; i < 3; i++) {//发请求
                    json = HttpConnAssist.getJsonInfoFromNet(Config.MusicURL.MUSICURL_TWO + id, Config.MusicURL.CHARSET);
                    if (json != null && !json.trim().equals("[]")) {
                        b = true;
                        break;
                    }
                }
                if (b) {
                    BaiduMusicSecondVo bms = Json.fromJson(BaiduMusicSecondVo.class, json);
                    if (bms != null && bms.getData() != null && bms.getData().getSongList() != null) {
                        list.addAll(secondResultFilter(bms.getData().getSongList()));
                    }
                }
            }
        }
        
        //排序 歌名-歌手-专辑 中含有关键词 
        if(StringUtils.isNotBlank(keyword)) {
            list = sortByKeyword(list, keyword);
        }
        
        Map<String, List<BaiduMusicResultInfo>> map = new HashMap<String, List<BaiduMusicResultInfo>>();
        map.put("result", list);
        resultJson = Json.toJson(map,JsonFormat.compact());
        
        return resultJson;
    }
    /**
     * 第二次获取百度音乐（2015-03-18 百度接口一次取多个失败）
     * 
     * @param songIds
     * @return
     */
    public String getBaiduMusicListTwo4(List<String> songIds,String keyword) {
        Stopwatch sw = Stopwatch.begin();
        String json = "";
        List<BaiduMusicResultInfo> list = MusicSearchSpair.startRun(songIds);
        //排序 歌名-歌手-专辑 中含有关键词 
        if(StringUtils.isNotBlank(keyword)) {
            list = sortByKeyword(list, keyword);
        }
        
        Map<String, List<BaiduMusicResultInfo>> map = new HashMap<String, List<BaiduMusicResultInfo>>();
        map.put("result", list);
        json = Json.toJson(map,JsonFormat.compact());
        sw.stop();
        System.out.println(">>>>>一共花了：" + sw.getDuration() + " 秒");
        return json;
        
    }
    
	/**
     * 第二次获取百度音乐
     * 
     * @param songIds
     * @return
     */
    public String getBaiduMusicListTwo(String songIds,String keyword) {
        long start = System.currentTimeMillis();
        StringBuffer sbuff = new StringBuffer();
        boolean b = false;
        for (int i = 0; i < 5; i++) {

            String json = HttpConnAssist.getJsonInfoFromNet(Config.MusicURL.MUSICURL_TWO + songIds, Config.MusicURL.CHARSET);
            if (json != null && !json.trim().equals("[]")) {
                sbuff.append(json);
                b = true;
                break;
            }
        }
        String resultJson = "";
        if (b) {
            BaiduMusicSecondVo bms = Json.fromJson(BaiduMusicSecondVo.class, sbuff.toString());
            if (bms != null && bms.getData() != null && bms.getData().getSongList() != null) {
                List<BaiduMusicResultInfo> list = secondResultFilter(bms.getData().getSongList());
                
                //排序 歌名-歌手-专辑 中含有关键词 
                if(StringUtils.isNotBlank(keyword)) {
                    list = sortByKeyword(list, keyword);
                }
                
                Map<String, List<BaiduMusicResultInfo>> map = new HashMap<String, List<BaiduMusicResultInfo>>();
                map.put("result", list);
                resultJson = Json.toJson(map,JsonFormat.compact());
            }
        }

        long end = System.currentTimeMillis();
        if (b) {
            System.out.println(end - start);
        }
        return resultJson;
    }

	public static List<BaiduMusicResultInfo> secondResultFilter(
			List<BaiduMusicSecondDataListVo> songList) {
		List<BaiduMusicResultInfo> rlist = null;
		if (songList != null && songList.size() > 0) {
			rlist = new ArrayList<BaiduMusicResultInfo>();
			for (BaiduMusicSecondDataListVo b : songList) {
				rlist.add(new BaiduMusicResultInfo(b));
			}
			List<BaiduMusicResultInfo> temp = BaiduMusicResultInfo.singleElement(rlist);
			if (temp != null && temp.size() > 0) rlist = temp;// 如果全不符合要求，则不过滤
		}
		return rlist;
	}

	/**
	 * 根据关键词获取百度音乐
	 * 
	 * @param keyWord
	 * @return
	 */
	public String getBaiduMusicListByKeyWord(String keyword) {
	    Stopwatch st = Stopwatch.begin();
	    String s = getBaiduMusicListTwo4(findBaiduMusicSongIds(keyword),keyword);
	    st.stop();
	    System.out.println(">>>>>>>baidu：爬取:" + st.getDuration());
		return s;
//		return getBaiduMusicListTwo4(getBaiduMusicListOne(keyword),keyword);
	}
	
	public static List<String> findBaiduMusicSongIds(String key) {
        Document doc = null;
        try {
        	// Modify by Zhou  URLEncoder.encode(key, Config.MusicURL.CHARSET)
        	URL url = new URL("http://music.baidu.com/search?s=1&key=" + URLEncoder.encode(key, Config.MusicURL.CHARSET));
        //	URL url = new URL("http://music.baidu.com/search?key=%E4%B8%AD%E5%9B%BD%E4%BA%BA");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.130 Safari/537.36");
            connection.setUseCaches(false);
            InputStream is = connection.getInputStream();//if empty.
            String response = IOUtils.toString(is);
            is.close();
            doc = Jsoup.parse(response);
            //JSONObject jo = new JSONObject(response);
            //JSONObject data = jo.getJSONObject("data");
            //String html = data.getString("html");
            //html = URLDecoder.decode(html, "UTF-8");
        	// End
            ////doc = Jsoup.connect("http://music.baidu.com/search?s=1&key=" + URLEncoder.encode(key, Config.MusicURL.CHARSET)).get();
            Elements es = doc.select("li.song-item-hook");  //li.song-item-hook
            System.out.println(es.size() + " \n " + es + "\n\n");
            
            String pate = "sid&quot;:(\\d+),";
            Pattern p = Pattern.compile(pate);
            Matcher m = p.matcher(doc.toString());
            ArrayList<String> strs = new ArrayList<String>();
            while (m.find()) {
                strs.add(m.group(1));     
            } 
               
            System.out.println(strs.size());
            return strs;
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	/**
	 * 新歌周榜
	 * @return
	 */
	public String getBaiduMusicListNewWeekTop() {
		
		String json = "" ;
		try {
			URL url = new URL(Config.MusicURL.MUSICURL_TOP_NEW_WEEK);
			HttpURLConnection urlconn = (HttpURLConnection)url.openConnection();
			urlconn.setRequestMethod("GET");
			urlconn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.130 Safari/537.36");
			urlconn.setUseCaches(false);
			InputStream is = urlconn.getInputStream();//if empty.
			json = IOUtils.toString(is);
            is.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	//	String json = HttpConnAssist.getJsonInfoFromNet(Config.MusicURL.MUSICURL_TOP_NEW_WEEK,Config.MusicURL.CHARSET);
		System.out.println(json);
		String pate = "/song/(\\d*?)\"";
        Pattern p = Pattern.compile(pate);
        Matcher m = p.matcher(json);
        ArrayList<String> strs = new ArrayList<String>();
        while (m.find()) {
            strs.add(m.group(1));     
        } 
        
        StringBuilder songIds = new StringBuilder();
        if(strs.size() > 0) {
        	songIds.append(strs.get(0));
        	for (int i = 1; i < strs.size(); i++){
        		songIds.append("," + strs.get(i));
        	}
        }
        
        return getBaiduMusicListTwo2(songIds.toString(), null);
        
	}
	
	/**
	 * 热歌榜
	 * @return
	 */
	public String getBaiduMusicListDayHotTop() {
		String datestr = DateUtil.date2Str("yyyy-MM-dd", new Date());
		String json = cacheTop.getCacheTop(datestr);
		if (!StringUtils.isBlank(json)) {
			return json;
		}
		
		try {
			URL url = new URL(Config.MusicURL.MUSICURL_TOP_NEW_WEEK);
			HttpURLConnection urlconn = (HttpURLConnection)url.openConnection();
			urlconn.setRequestMethod("GET");
			urlconn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.130 Safari/537.36");
			urlconn.setUseCaches(false);
			InputStream is = urlconn.getInputStream();//if empty.
			json = IOUtils.toString(is);
            is.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	//	json = HttpConnAssist.getJsonInfoFromNet(Config.MusicURL.MUSICURL_TOP_DAYHOT,Config.MusicURL.CHARSET);
		Document doc = Jsoup.parse(json);
		Elements selected = doc.select("ul li span.song-title a");
		
		String pate = "/song/(\\d*?)\"";
        Pattern p = Pattern.compile(pate);
        Matcher m = p.matcher(selected.toString());
        ArrayList<String> strs = new ArrayList<String>();
        while (m.find()) {
            strs.add(m.group(1));  
        } 
        
        StringBuilder songIds = new StringBuilder();
        if(strs.size() > 0) {
        	songIds.append(strs.get(0));
        	for (int i = 1; i < strs.size(); i++){
        		songIds.append("," + strs.get(i));
        	}
        }
        json = getBaiduMusicListTwo2(songIds.toString(), null);
        
        cacheTop.updateCacheTopMap(json);
        return json;
        
	}
	
	public List<BaiduMusicResultInfo> sortByKeyword(List<BaiduMusicResultInfo> list,String keyword) {
		List<BaiduMusicResultInfo> reList = null;
		if(keyword != null && list != null && list.size() > 0) {
			reList = new ArrayList<BaiduMusicResultInfo>();
			//按歌名中包含keyword来确定顺序
			for(BaiduMusicResultInfo bmr : list) {
				if(bmr != null && bmr.getSongname() != null && bmr.getSongname().toLowerCase().equals(keyword.toLowerCase())) {
					reList.add(bmr);
				}
			}
			list.removeAll(reList);
			
			//按歌名中包含keyword来确定顺序
			if(list != null && list.size() > 0)
			for(BaiduMusicResultInfo bmr : list) {
				if(bmr != null && bmr.getSongname() != null && bmr.getSongname().toLowerCase().contains(keyword.toLowerCase())) {
					reList.add(bmr);
				}
			}
			list.removeAll(reList);
			
			//按歌名中包含keyword来确定顺序
			if(list != null && list.size() > 0)
			for(BaiduMusicResultInfo bmr : list) {
				if(bmr != null && bmr.getSongname() != null && bmr.getSongname().toLowerCase().contains(keyword.toLowerCase())) {
					reList.add(bmr);
				}
			}
			list.removeAll(reList);
			
			
			//按歌手中包含keyword来确定顺序
			if(list != null && list.size() > 0)
			for(BaiduMusicResultInfo bmr : list) {
				if(bmr != null && bmr.getSinger() != null && bmr.getSinger().toLowerCase().equals(keyword.toLowerCase())) {
					reList.add(bmr);
				}
				
			}
			list.removeAll(reList);
			
			//按歌手中包含keyword来确定顺序
			if(list != null && list.size() > 0)
			for(BaiduMusicResultInfo bmr : list) {
				if(bmr != null && bmr.getSinger() != null && bmr.getSinger().toLowerCase().contains(keyword.toLowerCase())) {
					reList.add(bmr);
				}
				
			}
			list.removeAll(reList);
			
			//按专辑中包含keyword来确定顺序
			if(list != null && list.size() > 0)
			for(BaiduMusicResultInfo bmr : list) {
				if(bmr != null && bmr.getAlbum() != null && bmr.getAlbum().toLowerCase().equals(keyword.toLowerCase())) {
					reList.add(bmr);
				}
			}
			list.removeAll(reList);//按专辑中包含keyword来确定顺序
			
			if(list != null && list.size() > 0)
			for(BaiduMusicResultInfo bmr : list) {
				if(bmr != null && bmr.getAlbum() != null && bmr.getAlbum().toLowerCase().contains(keyword.toLowerCase())) {
					reList.add(bmr);
				}
			}
			list.removeAll(reList);
			//不包含keyword的
			if(list != null) reList.addAll(list);
			
		} else {
			reList = list;
		}
		return reList;
	}

	public static class CacheTop {
		/**
		 * 一天更新一次 当天的音乐 key: yyyy-mm-dd  value :json
		 * 需要通过 updateCacheTopMap 来更新
		 */
		private static Map<String,String> cacheTopMap = new HashMap<String,String>();
		
		public synchronized void updateCacheTopMap(String json) {
			String curDatesStr = DateUtil.date2Str("yyyy-MM-dd", new Date());
			cacheTopMap.clear();
			cacheTopMap.put(curDatesStr, json);
		}
		
		public String getCacheTop(String datestr){
			return cacheTopMap.get(datestr);
		}
	}
	
	/**
	 * 根据歌名获取歌词    返回第一个
	 */
	public String baiduMusicLRC(String song) {
		
		String lrcUrl = "";
		String key = song;
		try {
			key = URLEncoder.encode(song,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			URL url = null;
			String str_url = Config.MusicURL.BAIDU_MUSICURL_LOAD + key;
			
			url = new URL(str_url);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.130 Safari/537.36");
            connection.setUseCaches(false);
            InputStream is = connection.getInputStream();//if empty.
			
			Document doc = Jsoup.parse(is, "utf-8", str_url);
			
			//Document doc = Jsoup.connect(url).get();
			System.out.println(doc);
			Elements els = doc.select("div.lrc-content a.down-lrc-btn");
			
			if(els!=null && els.size()>0){
				String str = els.get(0).attr("class").toString();
				String pat = "'href':'(.*?)'";
				Pattern p = Pattern.compile(pat);
				Matcher m = p.matcher(str);
				while (m.find()) {
					lrcUrl =Config.MusicURL.BAIDU_MUSICURL_LRC + m.group(1);
	            } 
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(lrcUrl);
		return lrcUrl;
	}
	
	
}
