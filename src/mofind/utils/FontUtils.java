package mofind.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FontUtils {

	public static void changeFonts(Context context) {

		Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/STXINWEI.TTF");
		ViewGroup root = getContentView(context);

		for (int i = 0; i < root.getChildCount(); i++) {
			View v = root.getChildAt(i);
			if (v instanceof TextView) {
				((TextView) v).setTypeface(tf);
			} else if (v instanceof Button) {
				((Button) v).setTypeface(tf);
			} else if (v instanceof EditText) {
				((EditText) v).setTypeface(tf);
			}
		}
	}

	public static void changeFonts(Context context, ViewGroup root) {

		Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/STXINWEI.TTF");

		for (int i = 0; i < root.getChildCount(); i++) {
			View v = root.getChildAt(i);
			if (v instanceof TextView) {
				((TextView) v).setTypeface(tf);
			} else if (v instanceof Button) {
				((Button) v).setTypeface(tf);
			} else if (v instanceof EditText) {
				((EditText) v).setTypeface(tf);
			} else if (v instanceof ViewGroup) {
				changeFonts(context, (ViewGroup) v);
			}
		}
	}

	/**
	 * Get root content view.
	 */
	public static ViewGroup getContentView(Context context) {
		ViewGroup systemContent = (ViewGroup) ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
		ViewGroup content = null;
		if (systemContent.getChildCount() > 0 && systemContent.getChildAt(0) instanceof ViewGroup) {
			content = (ViewGroup) systemContent.getChildAt(0);
		}
		return content;
	}

}
