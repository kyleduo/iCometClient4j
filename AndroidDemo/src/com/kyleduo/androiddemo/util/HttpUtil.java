package com.kyleduo.androiddemo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpUtil {

	public static String doGet(String url, Map<String, String> params) {
		HttpClient client = new DefaultHttpClient();
		String finalUrl = buildGetUrl(url, params);
		System.out.println("pub url: " + finalUrl);
		HttpGet get = new HttpGet(finalUrl);
		HttpResponse response = null;
		try {
			response = client.execute(get);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		if (response != null) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			} catch (Exception e1) {
				e1.printStackTrace();
				return null;
			}
			StringBuilder content = new StringBuilder();
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					content.append(line);
					line = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			return content.toString();
		}

		return null;

	}

	private static String buildGetUrl(String base, Map<String, String> params) {
		StringBuilder url = new StringBuilder();
		if (!base.startsWith("http://")) {
			url.append("http://");
		}
		url.append(base);
		if (params == null || params.size() < 1) {
			return url.toString();
		}
		url.append("?");
		for (Entry<String, String> entry : params.entrySet()) {
			url.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		}
		url.deleteCharAt(url.length() - 1);

		return url.toString();
	}

}
