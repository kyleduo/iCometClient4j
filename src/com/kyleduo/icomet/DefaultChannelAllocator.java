package com.kyleduo.icomet;

/**
 * a default channel allocator, it is suggested that you should use your own allocator class
 * 
 * @author kyleduo
 *
 */
public class DefaultChannelAllocator implements ChannelAllocator {

	@Override
	public Channel allocate() {
		Channel channel = new Channel();
		channel.cname = "";
		channel.token = "";
		channel.seq = 1;
		return channel;
	}

}
