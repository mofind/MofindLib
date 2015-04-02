package mofind.net;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Message;

public class DataService {

	private static ExecutorService executorService = null;

	public static synchronized void get(final String url, final Handler handler, final int success, final int fail) {
		if (executorService == null) {
			executorService = Executors.newSingleThreadExecutor();
		}
		executorService.submit(new Runnable() {
			public void run() {
				final Message msg = handler.obtainMessage();
				try {
					final String result = HttpService.get(url);
					msg.obj = result;
					msg.what = success;
				} catch (Exception e) {
					msg.what = fail;
					e.printStackTrace();
				}

				handler.sendMessage(msg);
			}
		});
	}

	public static synchronized void post(final String url, final Map<String, String> params, final Handler handler, final int success, final int fail) {
		if (executorService == null) {
			executorService = Executors.newSingleThreadExecutor();
		}
		executorService.submit(new Runnable() {
			public void run() {
				final Message msg = handler.obtainMessage();
				try {
					final String result = HttpService.post(url, params);
					msg.obj = result;
					msg.what = success;
				} catch (Exception e) {
					msg.what = fail;
					e.printStackTrace();
				}

				handler.sendMessage(msg);
			}
		});
	}

	public static synchronized void post(final String url, final Map<String, String> params, final String filepath, final Handler handler,
			final int success, final int fail) {
		if (executorService == null) {
			executorService = Executors.newSingleThreadExecutor();
		}
		executorService.submit(new Runnable() {
			public void run() {
				final Message msg = handler.obtainMessage();
				try {
					final String result = HttpService.post(url, params, filepath);
					msg.obj = result;
					msg.what = success;
				} catch (Exception e) {
					msg.what = fail;
					e.printStackTrace();
				}

				handler.sendMessage(msg);
			}
		});
	}
}
