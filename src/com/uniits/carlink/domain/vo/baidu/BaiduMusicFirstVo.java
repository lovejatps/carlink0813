/**
 * 
 */
package com.uniits.carlink.domain.vo.baidu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.Data;

/**
 * 百度音乐第一次请求列表(包含音乐基本信息)
 * 
 * @author lcw
 * @since 2015-2-6 上午11:10:06
 */
@Data
public class BaiduMusicFirstVo {

	private String song;
	private String song_id;
	private String singer;
	private String album;
	private String singerPicSmall;
	private String singerPicLarge;
	private String albumPicLarge;
	private String albumPicSmall;
	
	

	public String getSong() {
		return song;
	}

	public void setSong(String song) {
		this.song = song;
	}

	public String getSong_id() {
		return song_id;
	}

	public void setSong_id(String song_id) {
		this.song_id = song_id;
	}

	public String getSinger() {
		return singer;
	}

	public void setSinger(String singer) {
		this.singer = singer;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getSingerPicSmall() {
		return singerPicSmall;
	}

	public void setSingerPicSmall(String singerPicSmall) {
		this.singerPicSmall = singerPicSmall;
	}

	public String getSingerPicLarge() {
		return singerPicLarge;
	}

	public void setSingerPicLarge(String singerPicLarge) {
		this.singerPicLarge = singerPicLarge;
	}

	public String getAlbumPicLarge() {
		return albumPicLarge;
	}

	public void setAlbumPicLarge(String albumPicLarge) {
		this.albumPicLarge = albumPicLarge;
	}

	public String getAlbumPicSmall() {
		return albumPicSmall;
	}

	public void setAlbumPicSmall(String albumPicSmall) {
		this.albumPicSmall = albumPicSmall;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaiduMusicFirstVo other = (BaiduMusicFirstVo) obj;
		if (singer == null) {
			if (other.singer != null)
				return false;
		} else if (!singer.equals(other.singer))
			return false;
		if (song == null) {
			if (other.song != null)
				return false;
		} else if (!song.equals(other.song))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((singer == null) ? 0 : singer.hashCode());
		result = prime * result + ((song == null) ? 0 : song.hashCode());
		return result;
	}

	// 返回List合适些
	public static List<BaiduMusicFirstVo> singleElement(
			List<BaiduMusicFirstVo> al) {
		// 定义一个临时容器
		List<BaiduMusicFirstVo> newAl = new ArrayList<BaiduMusicFirstVo>();
		// 在迭代是循环中next调用一次，就要hasNext判断一次
		Iterator<BaiduMusicFirstVo> it = al.iterator();

		while (it.hasNext()) {
			BaiduMusicFirstVo obj = it.next();// next()最好调用一次就hasNext()判断一次否则容易发生异常
			if (!newAl.contains(obj))
				newAl.add(obj);
		}
		return newAl;
	}
	
}
