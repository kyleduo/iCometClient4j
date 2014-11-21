iCometClient4j
===
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-iCometClient4j-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1120)

Client of iComet server for Java/Android. iComet server: https://github.com/ideawu/icomet

This project is based the iComet service which can be found in https://github.com/ideawu/icomet created by ideawu(www.ideawu.com).

Simply, this project is an implements of client for iComet service. You can use it in your java project as well as Android project.

If you want to know what the effect of this client is look at, there's an demo in Java prepared for running in none auth mode. Obviously, you need an iComet server. Mine is run on my mac under default configuration.

### Demo

There are two demos in this project for Java android Android. 

The one for Android is based on the one for Java by using the release jar file. You can find it in https://github.com/ideawu/icomet-demos , where you can also find a updated website demo. 

AndroidDemo can be your reference in your own project, but it is suggested that you should implement the logic yourself to match your own business. If there are bugs, please mail me.

### Usage

Here's some decription of how to use this client.

Using getInstance() method to get an instance of ICometClient.

Before you connet to the iComet server, you should make the client prepared with the ICometConf object by calling prepare(ICometConf) method. The ICometConf object should be completely initialed with those params which you can find under.

	mClient = ICometClient.getInstance();

	ICometConf conf = new ICometConf();
	conf.host = "127.0.0.1";
	conf.port = "8100";
	conf.url = "stream";
	conf.iConnCallback = new MyConnCallback();
	conf.iCometCallback = new MyCometCallback();
	conf.channelAllocator = new NoneAuthChannelAllocator();

	mClient.prepare(conf);

As you can see, there are one ChannelAllocator and two Callbacks for your business. 

The ChannelAllocator is used to connect to your own server and request a channel for connetiong to iComet server and a token for authorization if you need it. Also, seq is configurated in this allocator, such that you can decide whether to fetch some old message of that channel. There is only one method in this interface for now:
	
	allocate();

As their names say, the two Callbacks is used for connection and fetching process separately. It is important that you should always implement these two interface and put them into ICometConf object. Following by a outline of these two Callbacks:

	IConnCallback {

		public void onFail(String msg);

		public void onSuccess();

		public void onDisconnect();

		public void onStop();
	
		public boolean onReconnect(int times);
	
		public void onReconnectSuccess(int times);
	}

	ICometCallback {

		public void onDataMsgArrived(Message.Content content);

		public void onMsgArrived(Message msg);

		public void onErrorMsgArrived(Message msg);
		
		public void onMsgFormatError();
	}

When the client is ready, just a invoke of connect() method will connect it to iComet server. If you use this client in an Android project, you should invoke this method in an children thread, otherwise you would get a NetworkOnMainThreadException.

Then the IConnCallback will be invoked, if there's nothing wrong, you can use comet() method to listen the connection and you will receive the messages from iCometServer which you can deal with them by ICometCallback.

There's something else to know about the ICometCallback, the onMsgArrived() method will be invoked when any message arrive while onDataMsgArrived() and onErrorMsgArrived() will be invoked on for arriving of specific message related to their name.

Much more information will be in wiki and docs pages soon.

Have fun with it!


