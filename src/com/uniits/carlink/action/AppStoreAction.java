package com.uniits.carlink.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.uniits.carlink.service.IAppStoreService;
@Controller("appStore")
@Scope("prototype")
public class AppStoreAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4189779225754872015L;

	private HttpServletResponse response;
	private HttpServletRequest req;

	private String fileName; // 客户端保存文件名字

	private File[] files;
	private String[] filesFileName; // 文件名称
	private String[] filesContentType; // 文件类型
	private String introduce;
	private String[] categorys;
	private String apkName;
	private IAppStoreService appStoreService;
	private int page;
	private long appid;
	private int count;
	private int category;
	private String keyword;
	
	public String findNewVersion() throws IOException {
		String path = getSession().getServletContext().getRealPath("app");
		String json = "-1";
		try {
			if(StringUtils.isBlank(apkName)) {
				json = "-2";
			} else {
				
				File f = new File(path + "/" + apkName);
				if(!f.exists()) {
					json = "-3";
				} else {
					json = appStoreService.findNewVersion(path + "/" + apkName);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			json = "-1";
		}
		writeOut(json);
		return null;
	}

	public String uploadApp() {
		appStoreService.addApp(files, filesFileName, filesContentType,
				introduce, categorys, apkName);
		return null;
	}

	public String searchApp() throws IOException {
		String json = appStoreService.queryAppByKeyword(keyword, category,
				page, count);
		writeOut(json);
		return null;
	}

	public String getAppInfo() throws IOException {
		String json = appStoreService.queryAppList(page, count, category);
		writeOut(json);
		return null;
	}

	public String getDetailInfo() throws IOException {
		String json = appStoreService.queryAppDetailInfo(appid);
		writeOut(json);
		return null;
	}

	public InputStream getDownloadApk() throws Exception {
		try {
			InputStream stream = new FileInputStream(fileName);
			fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
			return stream;
		} catch (Exception e) {
			throw e;
		}
	}

	public String downloadApk() {
		// 修改下载次数
		try {
			BufferedOutputStream os = null;
			if (req.getHeader("startPos") != null) {
				byte[] bt = new byte[4096];
				File fs = new File(fileName);
				os = new BufferedOutputStream(response.getOutputStream());
				this.readFile(fs, bt, os);// 断点续传
			} else {// 从开始进行下载
				appStoreService.updateLoadCount(appid);
				return "loadfile";
			}
			if (os != null) {
				os.flush();
				os.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public InputStream getDownloadImage() throws Exception {
		try {
			InputStream stream = new FileInputStream(fileName);
			fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
			return stream;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 断点续传的方法
	 * 
	 * @param fs
	 *            待下载文件
	 * @param bt
	 *            下载字节数
	 * @param os
	 *            返回给客户端的输出流
	 * @throws IOException 
	 */
	private void readFile(File fs, byte[] bt, BufferedOutputStream os) throws IOException {
			String startPos = req.getHeader("startPos");
			long pastLength = Long.parseLong(startPos.trim());// 文件续传的起点
			RandomAccessFile raf = new RandomAccessFile(fs, "r");
			raf.seek(pastLength);// 设置开始读取文件的位置
			int nRead;
			// 从输入流中读入字节流，然后写到文件中
			while ((nRead = raf.read(bt, 0, 4096)) != -1) {
				os.write(bt, 0, nRead);
			}
			
			os.flush();
			os.close();
			raf.close();
			
//			String rangeBytes = req.getHeader("Range").replaceAll("bytes=", "");
//			Long contentLength = null;// 文件续传的长度
//			long pastLength = 0;// 文件续传的起点
//			RandomAccessFile raf = new RandomAccessFile(fs, "r");
//			if (rangeBytes.indexOf("-") == rangeBytes.length() - 1) {// 针对
//				// bytes=27000-
//				// 的请求
//				rangeBytes = rangeBytes.substring(0, rangeBytes.indexOf('-'));
//				pastLength = Long.parseLong(rangeBytes.trim());
//				contentLength = fs.length() - Long.parseLong(rangeBytes.trim())
//						+ 1;
//				// 设置读取内容的长度
//				response.setHeader("Content-Length", contentLength.toString());
//				raf.seek(pastLength);// 设置开始读取文件的位置
//				int nRead;
//				// 从输入流中读入字节流，然后写到文件中
//				while ((nRead = raf.read(bt, 0, 1024)) != -1) {
//					os.write(bt, 0, nRead);
//				}
//			} else {// 针对 bytes=27000-39000 的请求
//				String temp0 = rangeBytes.substring(0, rangeBytes.indexOf('-'));
//				String temp2 = rangeBytes.substring(
//						rangeBytes.indexOf('-') + 1, rangeBytes.length());
//				pastLength = Long.parseLong(temp0.trim());
//				contentLength = Long.parseLong(temp2) - Long.parseLong(temp0)
//						+ 1;
//				// 设置读取内容的长度
//				response.setHeader("Content-Length", contentLength.toString());
//				raf.seek(pastLength - 1);// 设置开始读取文件的位置
//				int n = 0;
//				long readLength = 0;// 记录已读字节数
//				while (readLength <= contentLength - 1024) {// 大部分字节在这里读取
//					n = raf.read(bt, 0, 1024);
//					readLength += 1024;
//					os.write(bt, 0, n);
//				}
//				if (readLength <= contentLength) {// 余下的不足 1024 个字节在这里读取
//					n = raf.read(bt, 0, (int) (contentLength - readLength));
//					os.write(bt, 0, n);
//				}
//			}
//			os.flush();
//			os.close();
//			raf.close();
	}

	public String downloadImage() {
		return "loadfile";
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String[] getCategorys() {
		return categorys;
	}

	public void setCategorys(String[] categorys) {
		this.categorys = categorys;
	}

	public File[] getFiles() {
		return files;
	}

	public void setFiles(File[] files) {
		this.files = files;
	}

	public String[] getFilesFileName() {
		return filesFileName;
	}

	public void setFilesFileName(String[] filesFileName) {
		this.filesFileName = filesFileName;
	}

	public String[] getFilesContentType() {
		return filesContentType;
	}

	public void setFilesContentType(String[] filesContentType) {
		this.filesContentType = filesContentType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getApkName() {
		return apkName;
	}

	public void setApkName(String apkName) {
		this.apkName = apkName;
	}

	public IAppStoreService getAppStoreService() {
		return appStoreService;
	}

	@Resource(name = "appStoreService")
	public void setAppStoreService(IAppStoreService appStoreService) {
		this.appStoreService = appStoreService;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setServletResponse(HttpServletResponse arg0) {
		response = arg0;

	}

	public long getAppid() {
		return appid;
	}

	public void setAppid(long appid) {
		this.appid = appid;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) throws UnsupportedEncodingException {
		byte[] keywordBuff = keyword.getBytes("iso-8859-1");
		this.keyword = new String(keywordBuff, "utf-8");
	}

	public void setServletRequest(HttpServletRequest arg0) {
		req = arg0;
	}

}
