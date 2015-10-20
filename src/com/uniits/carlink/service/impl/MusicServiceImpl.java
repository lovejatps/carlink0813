/**
 * 
 */
package com.uniits.carlink.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Lang;
import org.nutz.lang.Streams;
import org.nutz.lang.util.NutType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniits.carlink.domain.BaiduMusicResultInfo;
import com.uniits.carlink.service.IMusicService;
import com.uniits.carlink.utils.Config;
import com.uniits.carlink.utils.MusicAssist;

/**
 * @author lcw
 * @since 2015-2-6 上午10:31:55
 */
@SuppressWarnings("unchecked")
@Service(value="musicService")
public class MusicServiceImpl implements IMusicService {
	
	@Autowired
	private MusicAssist musicAssist;
	
	public static List<BaiduMusicResultInfo> andyList;
	
	public static String songer = "刘德华";;
	
	
    static {
        andyList = (List<BaiduMusicResultInfo>) Json.fromJson(NutType.list(BaiduMusicResultInfo.class),
                              Streams.fileInr("com/uniits/carlink/service/impl/songlist.txt"));
    }

	@Override
	public String findMusic(Map<String, String> query) {
		StringBuffer sbuff = new StringBuffer();
		try {
		    if(!Lang.isEmpty(query) && songer.equals(query.get("song"))) {
		        Map<String,List<BaiduMusicResultInfo>> map = new HashMap<String, List<BaiduMusicResultInfo>>();
		        map.put("result", andyList);
		        return Json.toJson(map,JsonFormat.compact());
		    }
		    
		    if(!Lang.isEmpty(query) && Lang.isEmpty(query.get("song"))) {
		    	
		    }
		    
			sbuff.append(musicAssist.getBaiduMusicListByKeyWord(query.get("song")));
		} catch (Exception e) {
			sbuff.append(Config.error);
			e.printStackTrace();
		}
		return sbuff.toString();
	}

	@Override
	public String findMusicTop(Map<String, String> query) {
		StringBuffer sbuff = new StringBuffer();
		try {
			if (query != null && Config.MusicURL.isWeekCycle(query.get("cycle"))
					&& Config.MusicURL.isNewType(query.get("type"))) {
				sbuff.append(musicAssist.getBaiduMusicListNewWeekTop());
			} else if(query != null && Config.MusicURL.isHotType(query.get("type"))) {
				sbuff.append(musicAssist.getBaiduMusicListDayHotTop());
			} else {
				sbuff.append(Config.error);
			}
		} catch (Exception e) {
			sbuff.append(Config.error);
			e.printStackTrace();
		}
		return sbuff.toString();
	}
	
	public String findMusicLrc(String song){
		StringBuffer sbuff = new StringBuffer();
		try {
			sbuff.append(musicAssist.baiduMusicLRC(song));
		} catch (Exception e) {
			sbuff.append(Config.error);
			e.printStackTrace();
		}
		return sbuff.toString();
	}

}
