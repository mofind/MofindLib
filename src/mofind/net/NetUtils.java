package mofind.net;

import mofind.common.L;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;
import android.widget.Toast;

public class NetUtils {
	
	public static boolean isNetConnected(Context context) {
		boolean isNetConnected;
		// 获得网络连接服务
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			String name = info.getTypeName();
			L.i("当前网络名称：" + name);
			isNetConnected = true;
			State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
			if (State.CONNECTED == state) Log.i("通知", "GPRS网络已连接");
			state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
			if (State.CONNECTED == state) Log.i("通知", "WIFI网络已连接");
		} else {
			L.i("没有可用网络");
			isNetConnected = false;
		}
		return isNetConnected;
	}
	
	public static void initNet(final Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
		// 获取代表联网状态的NetWorkInfo对象
		NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
		// 获取当前的网络连接是否可用
		if (null == networkInfo) {
			Toast.makeText(context, "当前的网络连接不可用", Toast.LENGTH_SHORT).show();
			// 当网络不可用时，跳转到网络设置页面
			new AlertDialog.Builder(context).setTitle("连接网络").setMessage("是否连接网络？")
					.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							((Activity) context).startActivityForResult(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS), 1);
						}
					}).setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					}).show();
		} else {
			boolean available = networkInfo.isAvailable();
			if (available) {
				Log.i("通知", "当前的网络连接可用");
				Toast.makeText(context, "当前的网络连接可用", Toast.LENGTH_SHORT).show();
			} else {
				Log.i("通知", "当前的网络连接不可用");
				Toast.makeText(context, "当前的网络连接不可用", Toast.LENGTH_SHORT).show();
			}
		}

		
	}
}
