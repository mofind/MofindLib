package mofind.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {
	public static SharedPreferences spf;

	public static SharedPreferences initspf(Context context) {
		return initspf(context, context.getPackageName());
	}

	public static SharedPreferences initspf(Context context, String spfname) {
		if (spf == null)
			spf = context.getSharedPreferences(spfname, Context.MODE_PRIVATE);
		return spf;
	}

	public static boolean readBoolean(String tag, boolean flag) {
		return spf.getBoolean(tag, flag);
	}

	public static void writeBoolean(String tag, boolean flag) {
		SharedPreferences.Editor editor = spf.edit();
		editor.putBoolean(tag, flag);
		editor.commit();
	}

	public static String readString(String tag, String flag) {
		return spf.getString(tag, flag);
	}

	public static void writeString(String tag, String flag) {
		SharedPreferences.Editor editor = spf.edit();
		editor.putString(tag, flag);
		editor.commit();
	}

	public static int readInt(String tag, int flag) {
		return spf.getInt(tag, flag);
	}

	public static void writeInt(String tag, int flag) {
		SharedPreferences.Editor editor = spf.edit();
		editor.putInt(tag, flag);
		editor.commit();
	}

	public static void clean(String key) {
		SharedPreferences.Editor editor = spf.edit();
		editor.remove(key);
		editor.commit();
	}

	/**
	 * ------------------------------------------------------
	 */
	public static void saveUserName(String username, String password) {
		writeString("username", username);
		writeString("password", password);
	}

	public static String readUserName() {
		return readString("username", "");
	}

	public static String readPassword() {
		return readString("password", "");
	}

}
