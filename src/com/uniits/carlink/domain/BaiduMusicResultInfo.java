/**
 * 
 */
package com.uniits.carlink.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.uniits.carlink.domain.vo.baidu.BaiduMusicSecondDataListVo;
import com.uniits.carlink.utils.Config;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 百度音乐请求列表(包含音乐详细信息)
 * 
 * @author lcw
 * @since 2015-2-6 上午11:10:06
 */
@Data
@NoArgsConstructor
public class BaiduMusicResultInfo {

	private String songname;
	private String songid;
	private String singer;
	private String album;
	private String songlink;
	private String lrclink;
	private long time;
	private long size;
	private String format;
	
	

	public String getSongname() {
		return songname;
	}

	public void setSongname(String songname) {
		this.songname = songname;
	}

	public String getSongid() {
		return songid;
	}

	public void setSongid(String songid) {
		this.songid = songid;
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

	public String getSonglink() {
		return songlink;
	}

	public void setSonglink(String songlink) {
		this.songlink = songlink;
	}

	public String getLrclink() {
		return lrclink;
	}

	public void setLrclink(String lrclink) {
		this.lrclink = lrclink;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
	public BaiduMusicResultInfo() {}
	public BaiduMusicResultInfo(BaiduMusicSecondDataListVo obj) {
		super();
		this.songname = obj.getSongName();
		this.songid = obj.getSongId();
		this.singer = obj.getArtistName();
		this.album = obj.getAlbumName();
		this.songlink = obj.getSongLink();
		if (StringUtils.isNotBlank(obj.getLrcLink()))
			this.lrclink = Config.MusicURL.MUSICURL_LRC_PREFIX + obj.getLrcLink();
		this.time = obj.getTime();
		this.size = obj.getSize();
		this.format = obj.getFormat();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaiduMusicResultInfo other = (BaiduMusicResultInfo) obj;
		if (singer == null) {
			if (other.singer != null)
				return false;
		} else if (!singer.equals(other.singer))
			return false;
		if (songname == null) {
			if (other.songname != null)
				return false;
		} else if (!songname.equals(other.songname))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((singer == null) ? 0 : singer.hashCode());
		result = prime * result
				+ ((songname == null) ? 0 : songname.hashCode());
		return result;
	}

	// 返回List合适些
	public static List<BaiduMusicResultInfo> singleElement(
			List<BaiduMusicResultInfo> al) {
		// 定义一个临时容器
		List<BaiduMusicResultInfo> newAl = new ArrayList<BaiduMusicResultInfo>();
		// 在迭代是循环中next调用一次，就要hasNext判断一次
		Iterator<BaiduMusicResultInfo> it = al.iterator();

		while (it.hasNext()) {
			BaiduMusicResultInfo obj = it.next();// next()最好调用一次就hasNext()判断一次否则容易发生异常
			if (obj != null && obj.isLegal() && !newAl.contains(obj))
				newAl.add(obj);
		}
		return newAl;
	}
	
	/**
	 * 是否为合法的音乐 （有下载地址、歌词下载地址、有音乐格式）
	 * @return
	 */
	public boolean isLegal() {
		if(this.format == null || this.format.isEmpty()) return false;
		if(this.songlink == null || this.songlink.isEmpty()) return false;
		if( null == this.lrclink || this.lrclink.isEmpty()) return false;
		return true;
	}

}
