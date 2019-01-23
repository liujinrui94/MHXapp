package com.ljr.hxapp.utils;



import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.ljr.hxapp.HXApplication;


public class ToastUtil {
	private static Toast toast;
	private static Context context = HXApplication.getInstance();
	/**
	 * 显示单例的吐司，能连续快速弹的吐司
	 * 
	 * @param text
	 */
	public static void showToast(Context mContext,String text) {
		if (toast == null) {
			toast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
		}
		toast.setText(text);
		toast.show();
	}

	public static void showShortToast(String msg) {
		if (toast != null) {
			toast.cancel();
		}
		toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.show();
	}

	public static void showLongToast(String msg) {
		if (toast != null) {
			toast.cancel();
		}
		toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
		toast.show();
	}

	public static void showCenterToast(String msg) {
		if (toast != null) {
			toast.cancel();
		}
		toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public static void toastCancel() {
		if (toast != null) {
			toast.cancel();
		}
	}
}
