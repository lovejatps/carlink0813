package com.uniits.carlink.pojo;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "T_APPDETAILINFO")
public class AppDetailInfo {
	private long appId;
	private String introduce;
	private String images;
	private String imageBasePath;
	private AppBaseInfo baseInfo;
	
	
	@Id
	public long getAppId() {
		return appId;
	}
	
	public void setAppId(long appId) {
		this.appId = appId;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}
	@OneToOne(cascade = CascadeType.REFRESH , fetch=FetchType.LAZY)
	@JoinColumn(name = "appId", referencedColumnName = "appid", unique = true)
	public AppBaseInfo getBaseInfo() {
		return baseInfo;
	}
	public void setBaseInfo(AppBaseInfo baseInfo) {
		this.baseInfo = baseInfo;
	}

	public String getImageBasePath() {
		return imageBasePath;
	}

	public void setImageBasePath(String imageBasePath) {
		this.imageBasePath = imageBasePath;
	}
}
