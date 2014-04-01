package com.kyleduo.icomet;

/**
 * 
 * you should implement this interface to connect to your own server for the channel, token, sequence
 * 
 * @author kyleduo
 * 
 */
public interface ChannelAllocator {
	/**
	 * you should never return null for this method
	 * 
	 * @return Channel channel
	 */
	public Channel allocate();
}
