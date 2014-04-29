package com.kyleduo.icomet.message;

import java.io.Serializable;

public class Message {
	public static class Type {
		public static String TYPE_DATA = "data";
		public static String TYPE_NOOP = "noop";
		// too many channels/subscribers
		public static String TYPE_429 = "429";
		// error token
		public static String TYPE_401 = "401";
	}

	public String type;
	public String cname;
	public String seq;
	public String content;

	public static class Content implements Serializable {
		
		private static final long serialVersionUID = 4340957908804000989L;
		
		public String time;
		public String from;
		public String text;
		public String id;

		@Override
		public String toString() {
			return "Content [time=" + time + ", from=" + from + ", text=" + text + ", id=" + id + "]";
		}

	}

	@Override
	public String toString() {
		return "Message [type=" + type + ", cname=" + cname + ", seq=" + seq + ", content=" + content + "]";
	}

}
