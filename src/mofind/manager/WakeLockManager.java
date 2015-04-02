package mofind.manager;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class WakeLockManager {

	private WakeLock wakeLock = null;

	private static WakeLockManager _instants = null;

	public static WakeLockManager getInstants() {
		if (_instants == null) {
			return new WakeLockManager();
		}
		return _instants;
	}

	/**
	 * 获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行
	 */
	public void acquireWakeLock(Context c) {
		if (null == wakeLock) {
			PowerManager pm = (PowerManager) c.getSystemService(Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, getClass().getCanonicalName());
			if (null != wakeLock) {
				wakeLock.acquire();
			}
		}
	}

	// 释放设备电源锁
	public void releaseWakeLock() {
		if (null != wakeLock && wakeLock.isHeld()) {
			wakeLock.release();
			wakeLock = null;
		}
	}
	/*
	 * WakeLock 类型以及说明：
	 * 
	 * PARTIAL_WAKE_LOCK:保持CPU 运转，屏幕和键盘灯有可能是关闭的。
	 * SCREEN_DIM_WAKE_LOCK：保持CPU运转，允许保持屏幕显示但有可能是灰的，允许关闭键盘灯
	 * SCREEN_BRIGHT_WAKE_LOCK：保持CPU运转，允许保持屏幕高亮显示，允许关闭键盘灯 FULL_WAKE_LOCK：保持CPU
	 * 运转，保持屏幕高亮显示，键盘灯也保持亮度 ACQUIRE_CAUSES_WAKEUP：强制使屏幕亮起，这种锁主要针对一些必须通知用户的操作.
	 * ON_AFTER_RELEASE：当锁被释放时，保持屏幕亮起一段时间
	 * 
	 * 
	 * 最后 AndroidManifest.xml 声明权限：
	 * <uses-permissionandroid:name="android.permission.WAKE_LOCK"/>
	 * <uses-permissionandroid:name="android.permission.DEVICE_POWER"/>
	 */
}
