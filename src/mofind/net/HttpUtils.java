package mofind.net;

import java.util.Map;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;

public class HttpUtils {

	private static final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(dialog != null) dialog.cancel();
			switch (msg.what) {
			case RESULT_SUCCESS:
				httpResultCallback.result(http_url, true, msg.obj.toString().trim());
				break;

			case RESULT_FAIL:
				httpResultCallback.result(http_url, false, "网络异常");
				break;
			}
			
		}
	};

//	public static synchronized void startHttpGet(Context context, String url, HttpResultCallback callback) {
//		httpResultCallback = callback;
//		http_url = url;
//		dialog = DialogUtil.getProgressDialog(context, "请稍等...");
//		dialog.show();
//		DataService.get(url, handler, RESULT_SUCCESS, RESULT_FAIL);
//	}
	
	public static synchronized void startHttpGet(String url, HttpResultCallback callback) {
		httpResultCallback = callback;
		http_url = url;
		DataService.get(url, handler, RESULT_SUCCESS, RESULT_FAIL);
	}
	
	public static synchronized void startHttpPost(String url, Map<String, String> params, HttpResultCallback callback) {
		httpResultCallback = callback;
		http_url = url;
		DataService.post(url, params, handler, RESULT_SUCCESS, RESULT_FAIL);
	}

	private static String http_url;
	private static HttpResultCallback httpResultCallback;

	public interface HttpResultCallback {

		public void result(String url, boolean isResult, String jsonStr);
	}

	private static final int RESULT_SUCCESS = 1;
	private static final int RESULT_FAIL = 0;
	
	private static Dialog dialog;
}
