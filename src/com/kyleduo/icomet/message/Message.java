package com.kyleduo.icomet.message;


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

	public static class Content {
		public String uid;
		public String nickname;
		public String content;
		@Override
		public String toString() {
			return "Content [uid=" + uid + ", nickname=" + nickname
					+ ", content=" + content + "]";
		}
	}

	@Override
	public String toString() {
		return "Message [type=" + type + ", cname=" + cname + ", seq=" + seq
				+ ", content=" + content + "]";
	}

}
