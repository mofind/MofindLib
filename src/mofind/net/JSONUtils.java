package mofind.net;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {
	
	public static final JSONObject getJSONObject(final JSONObject jo, final String key) {
		try {
			return jo.getJSONObject(key);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static final JSONArray getJSONArray(final JSONObject jo, final String key) {
		try {
			return jo.getJSONArray(key);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static final String getString(final JSONObject jo, final String key) {
		try {
			return jo.getString(key);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static final Double getDouble(final JSONObject jo, final String key) {
		try {
			return jo.getDouble(key);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static final int getInt(final JSONObject jo, final String key) {
		try {
			return jo.getInt(key);
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static final JSONObject getJSONObject(final JSONArray ja, final int index) {
		try {
			return ja.getJSONObject(index);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static final String getString(final JSONArray ja, final int index) {
		try {
			return ja.getString(index);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static final JSONObject getJSONObject(final String source) {
		try {
			return new JSONObject(source);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static JSONArray getJSONArray(String source) {
		
			try {
				return new JSONArray(source);
			} catch (JSONException e) {
				return null;
			} catch (Exception e) {
				return null;
			}
		
	}
}
