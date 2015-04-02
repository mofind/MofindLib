package mofind.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

public class AppInfoUtils {
	
	public static List<AppInfo> getAppInfos(Context c) {
		ArrayList<AppInfo> appList = new ArrayList<AppInfo>(); // 用来存储获取的应用信息数据
		List<PackageInfo> packages = c.getPackageManager().getInstalledPackages(0);

		for (int i = 0; i < packages.size(); i++) {
			PackageInfo packageInfo = packages.get(i);
			AppInfo tmpInfo = new AppInfo();
			tmpInfo.setAppName(packageInfo.applicationInfo.loadLabel(c.getPackageManager()).toString());
			tmpInfo.setPackageName(packageInfo.packageName);
			tmpInfo.setVersionName(packageInfo.versionName);;
			tmpInfo.setVersionCode(packageInfo.versionCode);;
			tmpInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(c.getPackageManager()));
			appList.add(tmpInfo);
		}

		return appList;
	}
	
	public static String getTopPackageName(Context c) {
		ActivityManager am = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> infos = am.getRunningTasks(1);
		RunningTaskInfo info = infos.get(0);
		return info.topActivity.getPackageName();
	}

	/**
	 * 返回应用名称
	 */
	public static String getAppName(List<AppInfo> apps, String packageName) {
		for (AppInfo appInfo : apps) {
			if (packageName.equals(appInfo.getPackageName())) {
				return appInfo.getAppName();
			}
		}
		return packageName;
	}
	
	/**
	 * 返回应用图标
	 */
	public static Drawable getAppIcon(List<AppInfo> apps, String packageName) {
		for (AppInfo appInfo : apps) {
			if (packageName.equals(appInfo.getPackageName())) {
				return appInfo.getAppIcon();
			}
		}
		return null;
	}

}
