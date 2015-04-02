package mofind.net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

public class HttpService {
	public static String post(String url, Map<String, String> params) throws Exception {
		HttpPost httpost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		String postData = "";
		for (String key : params.keySet()) {
			nvps.add(new BasicNameValuePair(key, params.get(key)));
			try {
				postData += key + "=" + URLEncoder.encode(params.get(key), "UTF-8") + "&";
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (postData.length() > 1)
			postData = postData.substring(0, postData.length() - 1);

		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
		}
		return execute(httpost, postData);
	}

	/**
	 * Post request (upload files)
	 * 
	 * @param url
	 * @param params
	 *            form data
	 * @param file
	 * @return result 0 for success, 1 for fail
	 * @throws Exception 
	 */
	public static String post(String url, Map<String, String> params, String filepath) throws Exception {
		InputStream response = null;
		try {
			String lineEnd = "\r\n";
			String twoHyphens = "--";
			String boundary = "---------------------------" + System.currentTimeMillis();

			File file = new File(filepath);
			FileInputStream fileInputStream = new FileInputStream(file);
			int fileSize = (int)file.length();
//			System.out.println("\n文件大小:"+file.length());

			HttpURLConnection conn = (HttpURLConnection) (new URL(url)).openConnection();

			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setConnectTimeout(20000);
			
			conn.setReadTimeout(20000);
 
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("User-Agent", "MyExampleApp/");
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			conn.connect();
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

			for (String key : params.keySet()) {
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
				dos.writeBytes(lineEnd);
//				dos.writeBytes(params.get(key));
				dos.writeBytes(new String(params.get(key).getBytes(), "ISO-8859-1"));
				dos.writeBytes(lineEnd);

//				System.out.print(twoHyphens + boundary + lineEnd);
//				System.out.print("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
//				System.out.print(lineEnd);
//				System.out.print(new String(params.get(key).getBytes(), "UTF-8"));
//				System.out.print(lineEnd);
			}

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"file1\"; filename=\"" + file.getName() + "\"" + lineEnd);
			dos.writeBytes(lineEnd);

//			System.out.print(twoHyphens + boundary + lineEnd);
//			System.out.print("Content-Disposition: form-data; name=\"file1\"; filename=\"" + file.getName() + "\"" + lineEnd);
//			System.out.print(lineEnd);

			int bytesAvailable;

			while ((bytesAvailable = fileInputStream.available()) > 0) {
				int bufferSize = Math.min(bytesAvailable, 4096);
				byte[] buffer = new byte[bufferSize];
				int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				dos.write(buffer, 0, bytesRead);
//				System.out.println("buffer.length: "+buffer.length);
//				System.out.print("\nbufferSize: "+bufferSize);
//				System.out.print("\nfileInputStream.available(): "+fileInputStream.available());
				
				if (onProgressListener != null) {
					onProgressListener.onProgress(fileSize, fileInputStream.available());
				}
			}

			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

//			System.out.print(lineEnd);
//			System.out.print(twoHyphens + boundary + twoHyphens + lineEnd);

			fileInputStream.close();
			dos.flush();
			dos.close();
			response = conn.getInputStream();
		} catch (Exception e) {
			System.out.println("upload error: " + e.toString());
			e.printStackTrace();
		}
		return convertStreamToString(response);
	}

	public static String get(String url) throws Exception {
		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("User-Agent", "Android");
		String s = execute(httpget, "");

		if (s == null || s.length() == 0)
			throw new Exception("can't get content from url");

		return s;
	}
	
	public static String get(String url, String encode) throws Exception {
		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("User-Agent", "Android");
		String s = execute(httpget, "UTF-8", encode);

		if (s == null || s.length() == 0)
			throw new Exception("can't get content from url");

		return s;
	}
	
	private static String convertStreamToString(InputStream is) throws Exception {
		InputStreamReader r;
		r = new InputStreamReader(is);
		
		BufferedReader reader = new BufferedReader(r);
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	private static String convertStreamToString(InputStream is, String encode) throws Exception {
		InputStreamReader r;
		if (encode!=null) {
			r = new InputStreamReader(is, encode);
		} else {
			r = new InputStreamReader(is);
		}

		BufferedReader reader = new BufferedReader(r);
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	private static String execute(HttpUriRequest req, String postData) throws Exception {
		HttpParams httpParameters = getDefaultHttpParams();
		/* 连接超时 */
		HttpConnectionParams.setConnectionTimeout(httpParameters, 20000);
		/* 请求超时 */
		HttpConnectionParams.setSoTimeout(httpParameters, 20000);
		HttpClient httpclient = new DefaultHttpClient(httpParameters);

		httpclient.getParams().setParameter("http.protocol.content-charset", "UTF-8");
		
		// Execute the request
		String result = null;
		HttpResponse response = null;
		InputStream instream = null;
		try {
			response = httpclient.execute(req);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				instream = entity.getContent();
				result = convertStreamToString(instream);
			}
		} finally {
			try {
				if (instream != null)
					instream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}

	private static String execute(HttpUriRequest req, String postData, String encode) throws Exception {
		int timeout = 10000;

		HttpParams httpParameters = getDefaultHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
		HttpConnectionParams.setSoTimeout(httpParameters, timeout);
		HttpClient httpclient = new DefaultHttpClient(httpParameters);
		httpclient.getParams().setParameter("http.protocol.content-charset",
				"UTF-8");

		// Execute the request
		String result = null;
		HttpResponse response = null;
		InputStream instream = null;
		try {
			response = httpclient.execute(req);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				instream = entity.getContent();
				result = convertStreamToString(instream, encode);
			}
		} finally {
			try {
				if (instream != null)
					instream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}
	
	private static HttpParams getDefaultHttpParams() {
		final HttpParams baseParams = new BasicHttpParams();
		return baseParams;
	}
	
	

	public static OnProgressListener getOnProgressListener() {
		return onProgressListener;
	}
	public static void setOnProgressListener(OnProgressListener onProgressListener) {
		HttpService.onProgressListener = onProgressListener;
	}
	private static OnProgressListener onProgressListener;
	
	public interface OnProgressListener {
		void onProgress(int fileSize,int uploadSize);
	}
}
