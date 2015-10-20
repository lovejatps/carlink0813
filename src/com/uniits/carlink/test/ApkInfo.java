/**
 * 
 */
package com.uniits.carlink.test;

import java.io.IOException;
import java.util.List;

import org.apkinfo.api.GetApkInfo;
import org.junit.Test;

/**
 * @author lcw
 *
 */
public class ApkInfo {

	@Test
	public void test() {
		String strpath = "WebRoot/app/58tongcheng_6020.apk";
		try {
			List<org.apkinfo.api.domain.ApkInfo> list = GetApkInfo.listApkInfoByDir("WebRoot/app/");
			System.out.println(list);
			System.out.println(GetApkInfo.getApkInfoByFilePath(strpath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
