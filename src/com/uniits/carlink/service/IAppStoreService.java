package com.uniits.carlink.service;

import java.io.File;
import java.io.IOException;

public interface IAppStoreService {
	
	public String addApp(File[] files, String[] filesFileName,String[] filesContent, String introduce, String[] categorys,String apkName);

	public String queryAppList(int page, int num, int category);
	
	public String queryAppByKeyword(String keyword , int category , int page , int num);

	public String queryAppDetailInfo(long id);
	
	public String queryAppPath(long id);
	
	public long updateLoadCount(long appid);
	
	/**
	 * 获取最新app版本信息
	 * @return
	 */
	public String findNewVersion(String strpath) throws IOException;
}
