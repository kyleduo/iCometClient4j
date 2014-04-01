package com.kyleduo.icomet;

import java.util.Map;

public class ICometConf {
	public String host;
	public String port;
	public String conn_url = "stream";
	public String pub_url = "pub";
	public Map<String, String> args;

	public ChannelAllocator channelAllocator;

	public ICometCallback iCometCallback;
	public IConnCallback iConnCallback;
}
