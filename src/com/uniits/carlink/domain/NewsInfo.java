package com.uniits.carlink.domain;

import java.io.Serializable;

public class NewsInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 746810491975128810L;
	
	private String url ;
	private String title;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle1(String title) {
		this.title = title;
	}
	
}
