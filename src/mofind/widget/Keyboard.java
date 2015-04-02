package mofind.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

public class Keyboard extends RelativeLayout {
	public static final byte KEYBOARD_STATE_SHOW = -3;
	public static final byte KEYBOARD_STATE_HIDE = -2;
	public static final byte KEYBOARD_STATE_INIT = -1;
	private OnKeyBoardChangeListener onKeyBoardChangeListener;
	private boolean mHasInit;
	private boolean mHasKeybord;
	private int mHeight;

	public interface OnKeyBoardChangeListener {
		public void onKeyBoardStateChange(int state);
	}

	public Keyboard(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public Keyboard(Context context) {
		super(context);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (!mHasInit) {
			mHasInit = true;
			mHeight = b;
			if (null != onKeyBoardChangeListener) {
				onKeyBoardChangeListener.onKeyBoardStateChange(KEYBOARD_STATE_INIT);
			}
		} else {
			mHeight = mHeight < b ? b : mHeight;
		}
		if (mHasInit && mHeight > b) {
			mHasKeybord = true;
			if (null != onKeyBoardChangeListener) {
				onKeyBoardChangeListener.onKeyBoardStateChange(KEYBOARD_STATE_SHOW);
			}
			Log.i("keyboard", "show keyboard...");
		}
		if (mHasInit && mHasKeybord && mHeight == b) {
			mHasKeybord = false;
			if (null != onKeyBoardChangeListener) {
				onKeyBoardChangeListener.onKeyBoardStateChange(KEYBOARD_STATE_HIDE);
			}
			Log.i("keyboard", "hide keyboard...");
		}
	}

	public void setOnKeyBoardChangeListener(OnKeyBoardChangeListener onKeyBoardChangeListener) {
		this.onKeyBoardChangeListener = onKeyBoardChangeListener;
	}

}