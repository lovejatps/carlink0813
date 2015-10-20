package com.uniits.carlink.dao;

import java.util.List;

import com.uniits.carlink.pojo.AppBaseInfo;
import com.uniits.carlink.pojo.AppDetailInfo;

public interface IAppStoreDAO {
	
	public long getAppNextId() throws Exception;
	
	public long addAppInfo(AppBaseInfo appBaseInfo) throws Exception;
	
	public List<AppBaseInfo> queryAppList(final int page ,final int num ,final int category) throws Exception;
	
	public AppDetailInfo queryAppDetailInfo(long appid)throws Exception;
	
	public AppBaseInfo queryAppBaseInfo(long appid)throws Exception;
	
	public List<AppBaseInfo> queryAppListByKeyword(final String keyword , final int page ,final int num ,final int category) throws Exception;
	
	public long updateLoadCount(final long appid) throws Exception;
}
