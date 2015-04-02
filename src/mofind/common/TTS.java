package mofind.common;

import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.widget.Toast;

/**
 * @author jiang 2014-8-13 上午11:33:08
 */
public class TTS implements OnInitListener {

	private static final String TAG = "TTS Demo";

	private static final int REQ_TTS_STATUS_CHECK = 0;
	private boolean speakAvailable = false;
	private TextToSpeech mTts;
	private Activity mActivity;
	
	private static HashMap<String, String> params = new HashMap<String, String>();

	private static TTS _instance = null;

	public static TTS getInstance() {
		if (_instance == null) {
			_instance = new TTS();
			return _instance;
		}
		return _instance;
	}

	private TTS() {

	}

	public void init(Activity act) {
		mActivity = act;
		Intent intent = new Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		act.startActivityForResult(intent, REQ_TTS_STATUS_CHECK);
	}

	public void speakAll(String value) {
		//app被杀死时,mActivity可能不存在
		if(mActivity==null)
			return;
		
		if (!speakAvailable) {
			Toast.makeText(mActivity, "Got a failure. TTS apparently not available", 0).show();
			return;
		}
		mTts.speak(value, TextToSpeech.QUEUE_ADD, params);
	}
	
	public void speakAll_withToast(String value){
		speakAll(value);
		Toast.makeText(mActivity, value, 0).show();
	}

	public void speak(String value) {
		if (!speakAvailable) {
			Toast.makeText(mActivity, "Got a failure. TTS apparently not available", 0).show();
			return;
		}
		String str = addSpace(value);
		mTts.speak(str, TextToSpeech.QUEUE_ADD, params);
	}

	public static String addSpace(String value) {
		if (value != null && value.length() > 0) {
			StringBuffer sb = new StringBuffer();
			for (int index = 0, count = value.length(); index < count; index++) {
				sb.append(value.charAt(index) + "").append(" ");
			}
			return sb.toString();
		}
		return "";
	}

	public void onTTSActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQ_TTS_STATUS_CHECK) {
			switch (resultCode) {
			case TextToSpeech.Engine.CHECK_VOICE_DATA_PASS:
				// 这个返回结果表明TTS Engine可以用
				mTts = new TextToSpeech(mActivity, this);
				Log.v(TAG, "TTS Engine is installed!");
				speakAvailable = true;

				break;
			case TextToSpeech.Engine.CHECK_VOICE_DATA_BAD_DATA:
				// 需要的语音数据已损坏
			case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_DATA:
				// 缺少需要语言的语音数据
			case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_VOLUME:
				// 缺少需要语言的发音数据
				// 这三种情况都表明数据有错,重新下载安装需要的数据
				Log.v(TAG, "Need language stuff:" + resultCode);
				// Intent dataIntent = new Intent();
				// dataIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				// mActivity.startActivity(dataIntent);
				speakAvailable = false;
				break;
			case TextToSpeech.Engine.CHECK_VOICE_DATA_FAIL:
				speakAvailable = false;
				// 检查失败
			default:
				Log.v(TAG, "Got a failure. TTS apparently not available");
				speakAvailable = false;
				break;
			}
		} else {
			// 其他Intent返回的结果
			speakAvailable = false;
		}

	}

	public void onTTSInit(int status) {
		// TTS Engine初始化完成
		if (status == TextToSpeech.SUCCESS) {
			int result = mTts.setLanguage(Locale.US);
			// 设置发音语言
			if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
			// 判断语言是否可用
			{
				Log.v(TAG, "Language is not available");
				speakAvailable = false;
			} else {
				// mTts.speak("ha ha ha", TextToSpeech.QUEUE_ADD, null);
				speakAvailable = true;
			}
		}
	}

	public void onTTSPause() {
		// TODO Auto-generated method stub
		if (mTts != null) {
			mTts.stop();
		}
	}

	public void onTTSDestroy() {
		// TODO Auto-generated method stub
		if (mTts != null) {
			mTts.shutdown();
		}
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		onTTSInit(status);
		// speakAll("Ready");
	}

	// ----------------api-------------------------

	// 断句
	// 注:1>","、" "均无效,1次最多可有3个点
	public static String comma() {
		return ". ";
	}

}
