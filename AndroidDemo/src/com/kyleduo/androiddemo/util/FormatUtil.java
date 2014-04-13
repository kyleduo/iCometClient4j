package com.kyleduo.androiddemo.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class FormatUtil {

	public static Map<String, String> ESCAPE_TABLE = new LinkedHashMap<String, String>();

	static {
		ESCAPE_TABLE.put("&amp;", "&");
		ESCAPE_TABLE.put("&quot;", "\"");
		ESCAPE_TABLE.put("&lt;", "<");
		ESCAPE_TABLE.put("&gt;", ">");
		ESCAPE_TABLE.put("&#39;", "\'");
		ESCAPE_TABLE.put("&#092;", "\\");
	}

	public static String urlEncodeJson(String json) throws UnsupportedEncodingException {
		String encoded = null;
		json = json.replace("\"", "\\\"");
		encoded = URLEncoder.encode(json, "utf-8");
		return encoded;
	}

	public static String htmlEscape(String input) {
		if (input == null) {
			throw new IllegalArgumentException("Null 'input' argument.");
		}
		for (Entry<String, String> entry : ESCAPE_TABLE.entrySet()) {
			input = input.replace(entry.getValue(), entry.getKey());
		}
		return input;
	}

	public static String htmlUnescape(String input) {
		if (input == null) {
			throw new IllegalArgumentException("Null 'input' argument.");
		}
		for (Entry<String, String> entry : ESCAPE_TABLE.entrySet()) {
			input = input.replace(entry.getKey(), entry.getValue());
		}
		return input;

	}
}
