package mofind.widget.netiv;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ImageView;

public class NetImageView extends ImageView {
	private String webUrl;

	public NetImageView(Context context) {
		super(context);
	}

	public NetImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NetImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setImageUrl(String url) {
		webUrl = url;
		loadImage(url);
	}

	private void loadImage(String url) {
		if (NetImageViewCache.getInstance().isBitmapExit(url)) { //�ж��ڴ�ͱ������Ƿ����
			// ��ȡHashMap�д洢��ͼƬ
			Bitmap bitmap = NetImageViewCache.getInstance().get(url);
			this.setImageBitmap(bitmap);

		} else {
			// �������ͼƬ
			new NetImageDownLoadTask().execute(webUrl);
		}
	}

	private byte[] getBytesFromStream(InputStream is) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] bytes = new byte[1024];
		int len = 0;
		while (len != -1) {
			try {
				len = is.read(bytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (len != -1) {
				baos.write(bytes, 0, len);
			}
		}

		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return baos.toByteArray();
	}

	private class NetImageDownLoadTask extends AsyncTask<String, Void, Bitmap> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			URL url = null;
			InputStream is = null;
			HttpURLConnection urlConnection = null;
			Bitmap bmp = null;
			try {
				url = new URL(params[0]);
				urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setRequestMethod("GET");
				urlConnection.setConnectTimeout(10000);
				is = urlConnection.getInputStream();
				byte[] bytes = getBytesFromStream(is);
				bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
						is = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (urlConnection != null) {
					urlConnection.disconnect();
					urlConnection = null;
				}
			}
			return bmp;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (result != null) {
				NetImageView.this.setImageBitmap(result);
				NetImageViewCache.getInstance().put(webUrl, result, true);
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
	}
}
