package com.kyleduo.icomet.util;

import java.net.HttpURLConnection;
import java.net.URL;

import com.kyleduo.icomet.message.MessageInputStream;

public class HttpClient {

	/**
	 * http get method
	 * 
	 * @param url
	 * @param args
	 * @return
	 */
	public static String get(String url) {
		HttpURLConnection conn;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.connect();
			MessageInputStream input = new MessageInputStream(conn.getInputStream());
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
