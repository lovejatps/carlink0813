package com.uniits.carlink.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.uniits.carlink.pojo.AppBaseInfo;

public class ParseApkUtil {
	public static final String APPLICATION = "application:";
	public static final String PACKAGE = "package";
	private static final String SPLIT_REGEX = "(: )|(=')|(' )|'";

	static String[] shellCommand;
	static String softName = "";
	static {
		shellCommand = new String[2];
		final String anOSName = System.getProperty("os.name");
		if (anOSName.toLowerCase().startsWith("windows")) {
			// Windows XP, Vista ...
			shellCommand[0] = "cmd";
			shellCommand[1] = "/C";
			softName = "aapt.exe";
		} else {
			// Unix, Linux ...
			shellCommand[0] = "/bin/sh";
			shellCommand[1] = "-c";
			softName = "aapt";
		}
	}

	public void parseApk(String apkPath, String savePath,
			AppBaseInfo appBaseInfo) throws Exception {
		String command = Config.AppStorePath.aaptPath + softName
				+ " d badging \"" + apkPath + "\"";
		Process process;
		process = Runtime.getRuntime().exec(
				new String[] { shellCommand[0], shellCommand[1], command });
		if (process != null) {
			InputStream is = process.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					"utf8"));
			String tmp = br.readLine();
			try {
				if (tmp == null || !tmp.startsWith("package")) {
					throw new Exception("参数不正确，无法正常解析APK包。输出结果为:\n" + tmp
							+ "...");
				}
				do {
					setApkInfoProperty(appBaseInfo, tmp);
				} while ((tmp = br.readLine()) != null);
				savePath = savePath+appBaseInfo.getIconUrl().substring(appBaseInfo.getIconUrl().lastIndexOf("/") + 1) ;
				extractFileFromApk(apkPath, appBaseInfo.getIconUrl(), savePath);
				appBaseInfo.setIconUrl(savePath);
				long size = getFileSizes(new File(apkPath));
				appBaseInfo.setFileSize("" + size);
			} catch (Exception e) {
				throw e;
			} finally {
				process.destroy();
				is.close();
				br.close();
			}
		}
	}

	private void splitPackageInfo(AppBaseInfo appBaseInfo, String packageSource) {
		String[] packageInfo = packageSource.split(SPLIT_REGEX);
		appBaseInfo.setVersion(packageInfo[6]);
	}

	private void splitApplicationInfo(AppBaseInfo appBaseInfo,
			String packageSource) {
		String[] packageInfo = packageSource.split(SPLIT_REGEX);
		//appBaseInfo.setName(packageInfo[2]);
		appBaseInfo.setIconUrl(packageInfo[4]);
	}

	/**
	 * 设置APK的属性信息。
	 * 
	 * @param apkInfo
	 * @param source
	 */
	private void setApkInfoProperty(AppBaseInfo appBaseInfo, String source) {
		System.out.println(source);
		if (source.startsWith(APPLICATION)) {
			splitApplicationInfo(appBaseInfo, source);
		} else if (source.startsWith(PACKAGE)) {
			splitPackageInfo(appBaseInfo, source);
		}
		try {
			// apkInfo.setApkFileSize(getFileSizes(new File(apkPath)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从指定的apk文件里获取指定file的流
	 * 
	 * @param apkpath
	 * @param fileName
	 * @return
	 */
	public InputStream extractFileFromApk(String apkpath, String fileName) {
		try {
			ZipFile zFile = new ZipFile(apkpath);
			ZipEntry entry = zFile.getEntry(fileName);
			entry.getComment();
			entry.getCompressedSize();
			entry.getCrc();
			entry.isDirectory();
			entry.getSize();
			entry.getMethod();
			InputStream stream = zFile.getInputStream(entry);
			return stream;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void extractFileFromApk(String apkpath, String fileName,
			String outputPath) throws Exception {
		InputStream is = extractFileFromApk(apkpath, fileName);

		File file = new File(outputPath);
		if(!file.exists()) {
			file.createNewFile();
		}
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(file), 1024);
		byte[] b = new byte[1024];
		BufferedInputStream bis = new BufferedInputStream(is, 1024);
		while (bis.read(b) != -1) {
			bos.write(b);
		}
		bos.flush();
		is.close();
		bis.close();
		bos.close();
	}

	public long getFileSizes(File f) throws Exception {
		long s = 0;
		if (f.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(f);
			s = fis.available();
		} else {
			System.out.println("文件不存在");
		}
		return s;
	}

	public static void main(String[] args) {
		ParseApkUtil apkUtil = new ParseApkUtil();
		AppBaseInfo appBaseInfo = new AppBaseInfo();
		try {
			String apkpath = "C:\\Users\\lcw\\Desktop\\temp\\testapk\\58tongcheng_6020.apk";
			String savepath = "C:\\Users\\lcw\\Desktop\\temp\\testapk\\crawler\\icon2.png";
			apkUtil.parseApk(apkpath, savepath, appBaseInfo);
			System.out.println(appBaseInfo.getName());
			System.out.println(appBaseInfo.getIconUrl());
			System.out.println(appBaseInfo.getVersion());
//			int a = appBaseInfo.getIconUrl().lastIndexOf("/");
//			String outputName = appBaseInfo.getIconUrl().substring(a + 1);
//			System.out.println(outputName);
//			apkUtil.extractFileFromApk("E:/XxBrightBeanLauncher.apk",
//					appBaseInfo.getIconUrl(), "E:/icon.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
