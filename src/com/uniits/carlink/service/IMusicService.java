/**
 * 
 */
package com.uniits.carlink.service;

import java.util.Map;

/**
 * @author lcw
 * @since 2015-2-6 上午10:30:03
 */
public interface IMusicService {

	/**
	 * 查询音乐（音乐名|歌手）
	 * @param query
	 * 
	 * @return
	 */
	public String findMusic(Map<String,String> query);
	
	/**
	 * 查询音乐榜（分日｜周｜月榜/热歌｜新歌｜歌手｜百度king｜原创音乐...） 目前实现新歌周榜
	 * @param query <br/>
	 * <li><span style="margin-right:50px;">key</span>		<span>value</span>	</li>
	 * <li><span style="margin-right:50px;">cycle</span>		<span>day日｜week周｜month月</span>	</li>
	 * <li><span style="margin-right:50px;">type</span>		<span>hot热歌｜new新歌</span>	</li>
	 * @return
	 */
	public String findMusicTop(Map<String,String> query);
	
	public String findMusicLrc(String song);
	
}
