package com.kyleduo.androiddemo.util;

import android.content.Context;
import android.widget.Toast;

public class UIUtil {
	public static void showToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
}
