package mofind.utils;

import android.graphics.drawable.Drawable;

public class AppInfo {

	public AppInfo() {
	}

	public AppInfo(String appName, String packageName, String versionName, int versionCode, Drawable appIcon) {
		this.appName = appName;
		this.packageName = packageName;
		this.versionName = versionName;
		this.versionCode = versionCode;
		this.appIcon = appIcon;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public Drawable getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}

	private String appName = "";
	private String packageName = "";
	private String versionName = "";
	private int versionCode = 0;
	private Drawable appIcon = null;
}
