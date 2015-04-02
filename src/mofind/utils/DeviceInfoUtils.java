package mofind.utils;

import java.util.UUID;

import mofind.common.L;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class DeviceInfoUtils {

	/**
	 * 获取电话号码
	 * @param c
	 * @return
	 */
	public static String getPhoneNumber(Context c) {
		TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
		String number = tm.getLine1Number();
		return number == null ? "" : number;
	}

	/**
	 * 获取电话IMEI号码
	 * @param c
	 * @return
	 */
	public static String getIMEI(Context c) {
		TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
		return imei == null ? "" : imei;
	}

	/**
	 * 获取电话IMSI号码
	 * @param c
	 * @return
	 */
	public static String getIMSI(Context c) {
		TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = tm.getSubscriberId();
		return imsi == null ? "" : imsi;
	}

	/**
	 * 获取手机mac地址 错误返回12个0
	 */
	public static String getMacAddress(Context context) {
		// 获取mac地址：
		String macAddress = "000000000000";
		try {
			WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
			if (null != info) {
				if (!TextUtils.isEmpty(info.getMacAddress()))
					macAddress = info.getMacAddress().replace(":", "");
				else
					return macAddress;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return macAddress;
		}
		return macAddress;
	}

	/**
	 * 获取手机型号
	 */
	public static String getPhoneModel() {
		return android.os.Build.MODEL;
	}

	/**
	 * 获取系统版本号
	 */
	public static String getRelease() {
		return Build.VERSION.RELEASE;
	}

	/**
	 * 获取SDK版本号
	 */
	public static String getSDK() {
		return Build.VERSION.SDK;
	}

	/**
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 */
	public static String getVersion(Context c) {
		try {
			PackageManager manager = c.getPackageManager();
			PackageInfo info = manager.getPackageInfo(c.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}

	/**
	 * 获取电话UUID
	 * @param c
	 * @return
	 */
	public static String getMyUUID(Context c) {
		final TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice, tmSerial, tmPhone, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = "" + android.provider.Settings.Secure.getString(c.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
		UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String uniqueId = deviceUuid.toString();
		L.d("uuid= " + uniqueId);
		return uniqueId;
	}

}
