package mofind.common;

import java.lang.reflect.Field;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.mofind.lib.R;

public class DialogUtil {

	public static Dialog getProgressDialog(Context context, String text) {
		final Dialog dialog = new Dialog(context, R.style.Dialog);
		dialog.setContentView(R.layout.firset_dialog_view);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		int screenW = getScreenWidth(context);
		lp.width = (int) (0.6 * screenW);
		TextView titleTxtv = (TextView) dialog.findViewById(R.id.tvLoad);
		titleTxtv.setText(text);
		return dialog;
	}

	public static Dialog getCustomDialog(Context context) {
		final Dialog dialog = new Dialog(context, R.style.Dialog);
		return dialog;
	}

	/**
	 * 非activity的context获取自定义对话框
	 */
	public static Dialog getWinDialog(Context context) {
		final Dialog dialog = new Dialog(context, R.style.Dialog);
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		return dialog;
	}

	private static int getScreenWidth(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	public static void showDialog(Context c, String title, String msg, DialogInterface.OnClickListener okListener, boolean isShowCancelButton) {
		AlertDialog.Builder builder = createDialog(c, title, msg);
		builder.setPositiveButton("确定", okListener);
		if (isShowCancelButton)
			builder.setNegativeButton("取消", null);
		builder.show();
	}

	public static void showItemDialog(Context c, String title, String[] items, DialogInterface.OnClickListener listener) {
		AlertDialog.Builder builder = createDialog(c, title, null);
		builder.setItems(items, listener);
		builder.show();
	}

	private static Builder createDialog(Context c, String t, String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(c);
		builder.setCancelable(false);
		builder.setTitle(t);
		builder.setMessage(msg);
		return builder;
	}

	public static void isHideDialog(DialogInterface dialog, boolean bool) {
		// 进行以下设置将不能关闭dialog
		try {
			Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, bool);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
