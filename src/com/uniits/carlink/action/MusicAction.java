/**
 * 
 */
package com.uniits.carlink.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.uniits.carlink.service.IMusicService;

/**
 * @author lcw
 * @since 2015-2-6 上午10:22:08
 */
@Controller("music")
@Scope("prototype")
public class MusicAction extends BaseAction{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private IMusicService musicService;
	
	private String song;
	

	public String getSong() {
		return song;
	}

	public void setSong(String song) {
		this.song = song;
	}

	public String findMusic () throws IOException {
		String ret = "";
		if (StringUtils.isEmpty(song)) {
			ret = "-2";
		} else {
			Map<String, String> query = new HashMap<String, String>();
			query.put("song", song);
			ret = musicService.findMusic(query);
		}
		writeOut(ret);
		return null;
	}
	
	public String findMusicTop () throws IOException {
		Map<String, String> query = new HashMap<String, String>();
//		query.put("cycle", "week");
//		query.put("type", "new");
		query.put("type", "hot");
		
		String ret = musicService.findMusicTop(query);
		writeOut(ret);
		return null;
	}
	
	public String findMusicLrc() throws IOException{
		String ret = "";
		if (StringUtils.isEmpty(song)) {
			ret = "-1";
		} else {
			ret = musicService.findMusicLrc(song);
		}
		writeOut(ret);
		
		return null;
	}
	
}
