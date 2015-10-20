/**
 * 
 */
package com.uniits.carlink.domain.vo.baidu;

import lombok.Data;

/**
 * 百度音乐第二次请求列表(包含音乐详细信息)
 * 
 * @author lcw
 * @since 2015-2-6 上午11:10:06
 */
@Data
public class BaiduMusicSecondVo {

	private long errorCode;
	private BaiduMusicSecondDataVo data;
	public long getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(long errorCode) {
		this.errorCode = errorCode;
	}
	public BaiduMusicSecondDataVo getData() {
		return data;
	}
	public void setData(BaiduMusicSecondDataVo data) {
		this.data = data;
	}
	
	

}
