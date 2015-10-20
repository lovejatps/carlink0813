package com.uniits.carlink.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apkinfo.api.GetApkInfo;
import org.apkinfo.api.domain.ApkInfo;
import org.nutz.json.Json;
import org.nutz.lang.Files;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.uniits.carlink.dao.IAppStoreDAO;
import com.uniits.carlink.pojo.AppBaseInfo;
import com.uniits.carlink.pojo.AppDetailInfo;
import com.uniits.carlink.service.IAppStoreService;
import com.uniits.carlink.utils.Config;
import com.uniits.carlink.utils.ParseApkUtil;
import com.uniits.carlink.utils.SpringBeansFactory;


@Component(value="appStoreService")
@Scope(value="prototype")
public class AppStoreServiceImpl implements IAppStoreService {

	private IAppStoreDAO appStoreDAO;

	public IAppStoreDAO getAppStoreDAO() {
		return appStoreDAO;
	}

	@Resource(name = "appStoreDAO")
	public void setAppStoreDAO(IAppStoreDAO appStoreDAO) {
		this.appStoreDAO = appStoreDAO;
	}

	public String addApp(File[] files, String[] filesFileName,
			String[] filesContentType, String introduce, String[] categorys , String apkName) {
		AppBaseInfo appBaseInfo = new AppBaseInfo();
		AppDetailInfo appDetailInfo = new AppDetailInfo();
		int fileLen = files.length;
		StringBuffer sbuff = new StringBuffer();
		sbuff.append(Config.AppStorePath.basePath);
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		SimpleDateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		appBaseInfo.setUpTime(dateFormat.format(date));
		sbuff.append(calendar.get(Calendar.YEAR)).append("/");
		sbuff.append(calendar.get(Calendar.MONTH) + 1).append("/");
		StringBuffer imagesPath = new StringBuffer();
		try {
			long appid = appStoreDAO.getAppNextId();
			appBaseInfo.setAppid(appid);
			appDetailInfo.setAppId(appid);
			appDetailInfo.setIntroduce(introduce);
			sbuff.append(appid).append("/");
			sbuff.append(appBaseInfo.getUpVersion()).append("/");
			String basePath = sbuff.toString();
			File file = new File(basePath);
			if(!file.exists()) {
				file.mkdirs();
			}
			for (int i = 0; i < fileLen; i++) {
				StringBuffer fileBuff = new StringBuffer(basePath);
				if (filesContentType[i].equals("application/octet-stream") || filesContentType[i].equals("application/vnd.android.package-archive") || filesFileName[i].endsWith(".apk")) {
					sbuff.append(filesFileName[i]);
					fileBuff.append(filesFileName[i]);
				}else {
					fileBuff.append(filesFileName[i]);
					if(imagesPath.length() == 0) {
						imagesPath.append(filesFileName[i]);
					}else {
						imagesPath.append(";");
						imagesPath.append(filesFileName[i]);
					}
				}
				saveFile(files[i], fileBuff.toString());
			}
			int baseCategory = 0;
			for(int i=0 ; i < categorys.length ; i++) {
				baseCategory |= Integer.parseInt(categorys[i]);
			}
			appBaseInfo.setCategory(baseCategory);
			ParseApkUtil apkUtil = (ParseApkUtil) SpringBeansFactory.getBeansFactory().getBean("parapkUtil");
			apkUtil.parseApk(sbuff.toString(), basePath, appBaseInfo);
			appDetailInfo.setImageBasePath(basePath);
			appDetailInfo.setImages(imagesPath.toString());
			appBaseInfo.setName(apkName);
			appBaseInfo.setApkUrl(sbuff.toString());
			appBaseInfo.setAppDetailInfo(appDetailInfo);
			appDetailInfo.setBaseInfo(appBaseInfo);
			appStoreDAO.addAppInfo(appBaseInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "finally" })
	public String queryAppList(int page , int num , int category) {
		JSONObject jsonObject = null;
		StringBuffer sbuff = new StringBuffer();
		try {
			if(page < 0 || num < 1) {
				sbuff.append(Config.ReturnCode.paramError); 
			}else {
				List<AppBaseInfo> infos = appStoreDAO.queryAppList(page, num, category);
				if(infos != null && infos.size() != 0) {
					Map baseMap = packaging(infos , page);
					jsonObject = JSONObject.fromObject(baseMap);
					sbuff.append(jsonObject.toString());
				}else {
					sbuff.append(Config.ReturnCode.finalData);  
				}
			}
		} catch (Exception e) {
			sbuff.append(Config.error); 
			e.printStackTrace();
		} finally {
			return sbuff.toString();
		}
	}
	@SuppressWarnings({ "unchecked", "finally" })
	public String queryAppByKeyword(String keyword , int category , int page , int num) {
		JSONObject jsonObject = null;
		StringBuffer sbuff = new StringBuffer();
		try {
			if(keyword == null || page < 0 || num < 1) {
				sbuff.append(Config.ReturnCode.paramError);
			}else {
				StringBuffer keywordBuf = new StringBuffer();
				int keywordLen = keyword.length();
				if(keywordLen < 1) {
					sbuff.append(Config.ReturnCode.paramError);
				} else {
					keywordBuf.append("%");
					for(int i=0 ; i < keywordLen ; i++) {
						keywordBuf.append(keyword.substring(i, i+1));
						keywordBuf.append("%");
					}
					List<AppBaseInfo> infos = appStoreDAO.queryAppListByKeyword(keywordBuf.toString(), page, num, category);
					
					if(infos != null && infos.size() != 0) {
						Map baseMap = packaging(infos , page);
						jsonObject = JSONObject.fromObject(baseMap);
						sbuff.append(jsonObject.toString());
					}else {
						sbuff.append(Config.ReturnCode.finalData);  //没有数据了
					}
				}
			}
		} catch (Exception e) {
			sbuff.append(Config.ReturnCode.finalData); // 出错了
			e.printStackTrace();
		}finally{
			return sbuff.toString();
		}
	}
	
	
	@SuppressWarnings({ "unchecked", "finally" })
	public String queryAppDetailInfo(long id) {
		JSONObject jsonObject = null;
		StringBuffer sbuff = new StringBuffer();
		try {
			AppDetailInfo appDetailInfo = appStoreDAO.queryAppDetailInfo(id);
			if(appDetailInfo != null) {
				Map baseMap = new HashMap();
				List<String> imagesList = new ArrayList<String>();
				String basePath = appDetailInfo.getImageBasePath();
				String[] imagesNames = appDetailInfo.getImages().split(";");
				int imagesNameLen = imagesNames.length;
				for(int i=0; i<imagesNameLen ; i++) {
					imagesList.add(basePath + imagesNames[i]);
				}
				baseMap.put("images", imagesList);
				baseMap.put("description", appDetailInfo.getIntroduce());
				baseMap.put("appid", id);
				jsonObject = JSONObject.fromObject(baseMap);
				sbuff.append(jsonObject.toString());
			}else {
				sbuff.append(Config.ReturnCode.noFind);
			}
		} catch (Exception e) {
			sbuff.append(Config.error);
			e.printStackTrace();
		} finally {
			return sbuff.toString();
		}
	}
	
	public String queryAppPath(long id) {
		StringBuffer buffer = new StringBuffer();
		try {
			AppBaseInfo appBaseInfo = appStoreDAO.queryAppBaseInfo(id);
			buffer.append(appBaseInfo.getApkUrl());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}
	
	
	
	@SuppressWarnings("unchecked")
	private Map packaging(List<AppBaseInfo> infos , int page) {
		Map baseMap = new HashMap();
		List<Map> infosMap = new ArrayList<Map>();
		int infosLen = infos.size();
		for(int i=0 ; i < infosLen ; i++) {
			Map infoMap = new HashMap();
			AppBaseInfo info = infos.get(i);
			infoMap.put("app_id", info.getAppid());
			infoMap.put("app_name", info.getName());
			infoMap.put("download_count", info.getDownloadCount());
			infoMap.put("filesize", info.getFileSize());
			infoMap.put("version", info.getVersion());
			infoMap.put("recommend_index", info.getRecommendationIndex());
			infoMap.put("url", info.getApkUrl());
			infosMap.add(infoMap);
		}
		baseMap.put("result", infosMap);
		baseMap.put("page", page);
		return baseMap;
	}

	private void saveFile(File file, String savePath) throws Exception {
		InputStream inputStream = new FileInputStream(file);
		OutputStream outputStream = new FileOutputStream(savePath);
		byte buffer[] = new byte[2048];
		int length = 0;
		while ((length = inputStream.read(buffer)) > 0) {
			outputStream.write(buffer, 0, length);
			outputStream.flush();
		}
		inputStream.close();
		outputStream.close();
	}
	
	public long updateLoadCount(long appid){
		try {
			appStoreDAO.updateLoadCount(appid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0 ;
	}

	@Override
	public String findNewVersion(String strpath) throws IOException {
		ApkInfo apk = GetApkInfo.getApkInfoByFilePath(strpath);
		Map<String, String> map = new HashMap<String, String>();
		if (apk != null) {
			map.put("packageName", apk.getPackageName());
			map.put("versionName", apk.getVersionName());
			map.put("versionCode", apk.getVersionCode());
			String desc = "这家伙好懒，什么也没说 *_* ";
			File f = new File(strpath + ".version");
			if(Files.isFile(f)) {
				desc = Files.read(f);
			}
			map.put("description", desc);
		}
		return Json.toJson(map);
	}
}

