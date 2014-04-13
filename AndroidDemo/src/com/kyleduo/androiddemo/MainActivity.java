package com.kyleduo.androiddemo;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.kyleduo.androiddemo.pub.AckMessage;
import com.kyleduo.androiddemo.pub.PubMessage;
import com.kyleduo.androiddemo.util.FormatUtil;
import com.kyleduo.androiddemo.util.HttpUtil;
import com.kyleduo.androiddemo.util.UIUtil;
import com.kyleduo.icomet.Channel;
import com.kyleduo.icomet.ChannelAllocator;
import com.kyleduo.icomet.ICometCallback;
import com.kyleduo.icomet.ICometClient;
import com.kyleduo.icomet.ICometConf;
import com.kyleduo.icomet.IConnCallback;
import com.kyleduo.icomet.message.Message;
import com.kyleduo.icomet.message.Message.Content;

public class MainActivity extends Activity implements OnClickListener {

	private Button mSendButton;
	private static EditText messages;
	private TextView mChannelHint;
	private EditText input;

	private static String cname, nickname, uid;

	private static ICometClient mClient;

	static Context mContext = null;

	static final Handler mHandler = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Message.Content content = (Content) msg.obj;
				String say = content.nickname + ":  " + FormatUtil.htmlUnescape(content.content) + "\n";
				messages.append(say);
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	private ProgressDialog mProgressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		Intent intent = getIntent();
		if (intent == null) {
			this.finish();
		}

		cname = intent.getStringExtra("channel");
		nickname = intent.getStringExtra("nickname");
		uid = intent.getStringExtra("uid");

		mContext = this;
		mSendButton = (Button) findViewById(R.id.bt_send);
		mSendButton.setOnClickListener(this);
		messages = (EditText) findViewById(R.id.messages);
		input = (EditText) findViewById(R.id.input);
		mChannelHint = (TextView) findViewById(R.id.hint_channel);
		mChannelHint.append(cname);

		initialClient();

	}

	@Override
	protected void onDestroy() {
		if (mClient.currStatus() == ICometClient.State.STATE_COMET) {
			mClient.stopComet();
			mClient = null;
		}
		super.onDestroy();
	}

	private void initialClient() {
		mClient = com.kyleduo.icomet.ICometClient.getInstance();
		new ConnectTask().execute();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_send:
			doSend();
			break;

		default:
			break;
		}
	}

	private void doSend() {
		String inputStr = input.getText().toString().trim();
		if (TextUtils.isEmpty(inputStr)) {
			UIUtil.showToast(mContext, "please input something");
			return;
		}
		input.setText("");
		PubMessage message = new PubMessage();
		message.cname = cname;
		PubMessage.Content content = new PubMessage.Content();
		content.uid = uid;
		content.nickname = nickname;
		content.content = inputStr;
		message.content = content;
		new PubTask("www.ideawu.com/icomet/php/pub.php", message).execute();
	}

	private boolean isProgressDialogShowing() {
		return mProgressDialog != null && mProgressDialog.isShowing();
	}

	private void showProgressDialog(String msg) {
		if (isProgressDialogShowing()) {
			return;
		}

		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(mContext);
			mProgressDialog.setMessage(msg);
			mProgressDialog.setCancelable(false);
			mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "cancel", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					mClient.stopConnect();
					dialog.dismiss();
				}
			});
			mProgressDialog.show();
		}
	}

	private void dismissProgressDialog() {
		if (isProgressDialogShowing()) {
			mProgressDialog.dismiss();
		}
	}

	class ConnectTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			showProgressDialog("connecting...");
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			ICometConf conf = new ICometConf();
			conf.host = "www.ideawu.com";
			conf.port = "8100";
			conf.url = "stream";
			conf.iConnCallback = new MyConnCallback();
			conf.iCometCallback = new MyCometCallback();
			conf.channelAllocator = new NoneAuthChannelAllocator();
			mClient.prepare(conf);
			mClient.connect();
			return null;
		}

	}

	public class MyConnCallback implements IConnCallback {

		@Override
		public void onFail(String msg) {
			dismissProgressDialog();
			System.err.println(msg);
		}

		@Override
		public void onSuccess() {
			System.out.println("connection ok");
			dismissProgressDialog();
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

	public class MyCometCallback implements ICometCallback {

		@Override
		public void onDataMsgArrived(Content content) {
			System.out.println("data msg arrived: " + content);
			android.os.Message message = mHandler.obtainMessage();
			message.obj = content;
			mHandler.sendMessage(message);
		}

		@Override
		public void onMsgArrived(Message msg) {
			System.out.println("msg arrived: " + msg);
		}

		@Override
		public void onErrorMsgArrived(Message msg) {
			System.err.println("error message arrived with type: " + msg.type);
		}

		@Override
		public void onMsgFormatError() {
			System.err.println("message format error");
		}
	}

	public class NoneAuthChannelAllocator implements ChannelAllocator {

		@Override
		public Channel allocate() {
			Channel channel = new Channel();
			channel.cname = cname;
			channel.token = "";
			channel.seq = -1;
			return channel;
		}

	}

	class PubTask extends AsyncTask<String, Void, String> {

		private String url;
		private PubMessage message;
		private Gson gson = new Gson();

		public PubTask(String url, PubMessage message) {
			this.url = url;
			this.message = message;
		}

		@Override
		protected String doInBackground(String... p) {

			Map<String, String> params = new HashMap<String, String>();
			params.put("cname", this.message.cname);
			String content = null;
			message.content.content = FormatUtil.htmlEscape(message.content.content);
			String json = gson.toJson(message.content);
			System.out.println("pure json: " + json);
			try {
				content = FormatUtil.urlEncodeJson(json);
			} catch (UnsupportedEncodingException e) {
				return null;
			}
			params.put("content", content);

			String response = HttpUtil.doGet(url, params);

			return response;
		}

		@Override
		protected void onPostExecute(String result) {

			/*System.out.println(result);
			if (true) {
				return;
			}*/

			System.out.println(result);

			if (TextUtils.isEmpty(result)) {
				UIUtil.showToast(mContext, "send failed");
				return;
			}

			int start = 0;
			int end = result.length();

			while (result.charAt(start) != '{' && start < end) {
				start++;
			}

			while (result.charAt(end - 1) != '}' && start < end) {
				end--;
			}

			result = result.substring(start, end);

			System.out.println("result:" + result);

			AckMessage ack = null;
			try {
				ack = gson.fromJson(result, AckMessage.class);
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
				UIUtil.showToast(mContext, "Parse Json failed");
				return;
			}
			if (ack != null && ack.type.equals("ok")) {
				UIUtil.showToast(mContext, "pub success");
			}

			super.onPostExecute(result);
		}
	}
}
