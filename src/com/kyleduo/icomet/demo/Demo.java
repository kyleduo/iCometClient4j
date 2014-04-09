package com.kyleduo.icomet.demo;

import com.kyleduo.icomet.Channel;
import com.kyleduo.icomet.ChannelAllocator;
import com.kyleduo.icomet.ICometCallback;
import com.kyleduo.icomet.ICometClient;
import com.kyleduo.icomet.ICometConf;
import com.kyleduo.icomet.IConnCallback;
import com.kyleduo.icomet.message.Message;
import com.kyleduo.icomet.message.Message.Content;

/**
 * Demo
 * 
 * @author keyleduo
 */
public class Demo {

	private static ICometClient mClient;

	public static void main(String args[]) {
		mClient = ICometClient.getInstance();
		ICometConf conf = new ICometConf();
		conf.host = "127.0.0.1";
		conf.port = "8100";
		conf.iConnCallback = new MyConnCallback();
		conf.iCometCallback = new MyCometCallback();
		conf.channelAllocator = new NoneAuthChannelAllocator();
		mClient.prepare(conf);
		mClient.connect();
	}

	public static class MyConnCallback implements IConnCallback {

		@Override
		public void onFail(String msg) {
			System.err.println(msg);
		}

		@Override
		public void onSuccess() {
			System.out.println("connection ok");
			mClient.comet();
		}

		@Override
		public void onDisconnect() {
			System.err.println("connection has been cut off");
		}

		@Override
		public void onStop() {
			System.out.println("client has been stopped");
		}

		@Override
		public boolean onReconnect(int times) {
			System.err.println("This is the " + times + "st times.");
			if (times >= 3) {
				return true;
			}
			return false;
		}

		@Override
		public void onReconnectSuccess(int times) {
			System.out.println("onReconnectSuccess at " + times + "st time");
			mClient.comet();
		}

	}

	public static class MyCometCallback implements ICometCallback {

		@Override
		public void onDataMsgArrived(Content content) {
			System.out.println("data msg arrived: " + content);
		}

		@Override
		public void onMsgArrived(Message msg) {
			System.out.println("msg arrived: " + msg);
		}

		@Override
		public void onErrorMsgArrived(Message msg) {
			System.err.println("error message arrived with type: " + msg.type);
		}

	}

	public static class NoneAuthChannelAllocator implements ChannelAllocator {

		@Override
		public Channel allocate() {
			Channel channel = new Channel();
			channel.cname = "63561661880299684166";
			channel.token = "";
			channel.seq = 1;
			return channel;
		}

	}

}
