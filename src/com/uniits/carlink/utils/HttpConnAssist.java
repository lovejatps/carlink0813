package com.uniits.carlink.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnAssist {
	final static int BUFFER_SIZE = 4096;

	@SuppressWarnings("finally")
	public static String getJsonInfoFromNet(String weatherUrl , String charSet) {
		URL url = null;
		BufferedReader in = null;
		StringBuffer sb = new StringBuffer();
		boolean bError = false ;
		try {
			url = new URL(weatherUrl);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.130 Safari/537.36");
            connection.setUseCaches(false);
            InputStream is = connection.getInputStream();//if empty.
			
			in = new BufferedReader(new InputStreamReader(is,charSet));
			//System.out.println(in);
			String str = null;
			while ((str = in.readLine()) != null) {
				sb.append(str);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			bError = true;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
			}
			if(bError) {
				return null;
			}else {
				return sb.toString();
			}
		}
		
	}

}
