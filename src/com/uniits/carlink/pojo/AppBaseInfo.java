package com.uniits.carlink.pojo;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_appbaseinfo")
public class AppBaseInfo {

	private long appid; // apkid
	private String name; // apk名字
	private long downloadCount; // 下载次数
	private int category; // 种类
	private String fileSize; // 文件大小
	private String version; // 版本号
	private int recommendationIndex; // 推荐指数
	private String iconUrl; // icon 链接
	private String apkUrl; // apk 链接
	private int priorLevel; // 优先等级   -1为不可用 不予提供       0为默认等级    
	private int upVersion;
	private String upTime ;

	@Id
	public long getAppid() {
		return appid;
	}
	

	public void setAppid(long appid) {
		this.appid = appid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(long downloadCount) {
		this.downloadCount = downloadCount;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getRecommendationIndex() {
		return recommendationIndex;
	}

	public void setRecommendationIndex(int recommendationIndex) {
		this.recommendationIndex = recommendationIndex;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getApkUrl() {
		return apkUrl;
	}

	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}

	public int getPriorLevel() {
		return priorLevel;
	}

	public void setPriorLevel(int priorLevel) {
		this.priorLevel = priorLevel;
	}
	
	private AppDetailInfo appDetailInfo;	
	@OneToOne(cascade=CascadeType.ALL , fetch=FetchType.LAZY  , mappedBy="baseInfo")
	public AppDetailInfo getAppDetailInfo() {
		return appDetailInfo;
	}
	public void setAppDetailInfo(AppDetailInfo appDetailInfo) {
		this.appDetailInfo = appDetailInfo;
	}

	public int getUpVersion() {
		return upVersion;
	}

	public void setUpVersion(int upVersion) {
		this.upVersion = upVersion;
	}
	
	public String getUpTime() {
		return upTime;
	}


	public void setUpTime(String upTime) {
		this.upTime = upTime;
	}	
}
