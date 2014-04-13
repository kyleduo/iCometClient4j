package com.kyleduo.androiddemo;

import com.kyleduo.androiddemo.util.UIUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ChannelActivity extends Activity {

	private EditText mChannelEditText, mNicknameEditText, mUidEditText;
	private Button mConnectButton;
	private String mChannel, mNickname, mUid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_channel);

		mChannelEditText = (EditText) findViewById(R.id.et_channel);
		mNicknameEditText = (EditText) findViewById(R.id.et_nickname);
		mUidEditText = (EditText) findViewById(R.id.et_uid);
		mConnectButton = (Button) findViewById(R.id.bt_connect);

		mConnectButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mChannel = mChannelEditText.getText().toString();
				mNickname = mNicknameEditText.getText().toString();
				mUid = mUidEditText.getText().toString();

				if (TextUtils.isEmpty(mChannel) || TextUtils.isEmpty(mNickname) || TextUtils.isEmpty(mUid)) {
					UIUtil.showToast(ChannelActivity.this, "null is not allowed!");
					return;
				}

				Intent intent = new Intent(ChannelActivity.this, MainActivity.class);
				Bundle extras = new Bundle();
				extras.putString("channel", mChannel);
				extras.putString("nickname", mNickname);
				extras.putString("uid", mUid);
				intent.putExtras(extras);

				ChannelActivity.this.startActivity(intent);
				ChannelActivity.this.finish();
			}
		});

	}
}
