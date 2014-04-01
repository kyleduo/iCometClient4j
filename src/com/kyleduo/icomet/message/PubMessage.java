package com.kyleduo.icomet.message;

/**
 * 
 * message class for publish
 * 
 * @author zhagnduo
 *
 */
public class PubMessage {
	public String cname;
	public PubMessage.Content content;

	public static class Content {
		public String uid;
		public String nickname;
		public String content;

		@Override
		public String toString() {
			return "Content [uid=" + uid + ", nickname=" + nickname + ", content=" + content + "]";
		}

	}

	@Override
	public String toString() {
		return "PubMessage [cname=" + cname + ", content=" + content + "]";
	}

}
