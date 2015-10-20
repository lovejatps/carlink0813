/**
 * 
 */
package com.uniits.carlink.domain.vo.baidu;

import java.util.List;

import lombok.Data;

/**
 * @author lcw
 * @since 2015-2-6 下午1:45:34
 */
@Data
public class BaiduMusicSecondDataVo {
	private String xcode;
	private List<BaiduMusicSecondDataListVo> songList;
	public String getXcode() {
		return xcode;
	}
	public void setXcode(String xcode) {
		this.xcode = xcode;
	}
	public List<BaiduMusicSecondDataListVo> getSongList() {
		return songList;
	}
	public void setSongList(List<BaiduMusicSecondDataListVo> songList) {
		this.songList = songList;
	}
	
	
}
