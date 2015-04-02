package mofind.anim;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.mofind.lib.R;

public class AnimUtils {

	public static Animation horizontalShake(Context context, View v) {
		return shake(context, v, true);
	}

	public static Animation verticalShake(Context context, View v) {
		return shake(context, v, false);
	}

	public static Animation shake(Context context, View v, boolean isX) {
		Animation shakeAnim = AnimationUtils.loadAnimation(context, isX ? R.anim.shake_x : R.anim.shake_y);
		v.startAnimation(shakeAnim);
		return shakeAnim;
	}

	public static Rotate3D retate3D(Context context, View v, boolean type) {
		Rotate3D rotate3d = new Rotate3D(v.getWidth(), v.getHeight(), type);
		rotate3d.setDuration(800);
		v.startAnimation(rotate3d);
		return rotate3d;
	}

	public static Animation scale(Context context, View v) {
		Animation scaleAnim = AnimationUtils.loadAnimation(context, R.anim.scale_in);
		v.startAnimation(scaleAnim);
		return scaleAnim;
	}

}
