package mofind.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.view.View;

/**
 * 处理图片的工具类.
 */
public class BitmapUtils {

	public static void scaleFile(File fileFrom, File fileTo, PointF maxSize) {
		Bitmap bm = loadBm_limitSize(fileFrom, maxSize);
		saveTmpImage(bm, fileTo);
	}

	/**
	 * 加载-图片,限定-最大维度>>>注:1.缩放时"四舍五入" 故存在误差(或多或少)
	 * 
	 * @param path
	 * @param maxSize
	 * @return
	 */
	public static Bitmap loadBm_limitSize(File file, PointF maxSize) {
		String path = file.getPath();
		// 获取-旋转角
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(path);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		int rot = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
		int rotAngle = 0;
		switch (rot) {
		case ExifInterface.ORIENTATION_ROTATE_90:
			rotAngle = 90;
			break;
		case ExifInterface.ORIENTATION_ROTATE_180:
			rotAngle = 180;
			break;
		case ExifInterface.ORIENTATION_ROTATE_270:
			rotAngle = 270;
			break;

		default:
			break;
		}

		// 限定-尺寸
		float wMax = maxSize.x;
		float hMax = maxSize.y;

		Options opt = new Options();
		opt.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, opt);
		if (opt.mCancel || opt.outWidth == -1 || opt.outHeight == -1) {
			System.out.println("====w:" + opt.outWidth + ",h:" + opt.outHeight + ",cancel:" + opt.mCancel);
			return null;
		}

		float wIn = opt.outWidth;
		float hIn = opt.outHeight;

		// 若"横向-拍摄" 则调整"源尺寸",只为获取"缩放比例" 故调wIn、wMax均无所谓
		if (rotAngle == 90 || rotAngle == 270) {
			wIn = opt.outHeight;
			hIn = opt.outWidth;
		}

		float scaleIn = wIn / hIn;
		float scaleMax = wMax / hMax;

		// 若较宽 则限制宽
		opt.inJustDecodeBounds = false;
		if (scaleIn > scaleMax)
			opt.inSampleSize = Math.round(wIn / wMax); // "1/inSampleSize"表缩放比率
		else
			opt.inSampleSize = Math.round(hIn / hMax);

		// return BitmapFactory.decodeFile(path, opt);

		Bitmap bmScaled = BitmapFactory.decodeFile(path, opt);

		if (rotAngle == 0)
			return bmScaled;
		else
			return rotateBitmap(bmScaled, rotAngle);
	}

	/**
	 * 选转,正时针
	 * 
	 * @param bmpIn
	 * @param angle
	 * @return
	 */
	public static Bitmap rotateBitmap(Bitmap bmpIn, int angle) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(bmpIn, 0, 0, bmpIn.getWidth(), bmpIn.getHeight(), matrix, true);
	}

	public static void saveTmpImage(Bitmap bm, File file) {
		File fileToSave = file;
		try {
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileToSave));
			bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 压缩bitmap
	 */
	public static void zoomImage(Bitmap bitmap, String path, double maxSize) {
		// 图片允许最大空间 单位：KB
		// double maxSize = 150.00;
		// 将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		// 将字节换成KB
		double mid = b.length / 1024;
		// 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
		if (mid > maxSize) {
			// 获取bitmap大小 是允许最大大小的多少倍
			double i = mid / maxSize;
			// 开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍
			// （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
			bitmap = zoomImage(bitmap, bitmap.getWidth() / Math.sqrt(i), bitmap.getHeight() / Math.sqrt(i));
		}

		// 保存图片到SD卡上
		saveJPGE_After(bitmap, path);
	}

	/***
	 * 图片的缩放方法
	 * 
	 * @param bgimage
	 *            源图片资源
	 * @param newWidth
	 *            缩放后宽度
	 * @param newHeight
	 *            缩放后高度
	 * @return
	 */
	public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		return Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
	}

	/**
	 * 保存图片为PNG
	 */
	public static void savePNG_After(Bitmap bitmap, String name) {
		File file = new File(name);
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存图片为JPEG
	 */

	public static void saveJPGE_After(Bitmap bitmap, String path) {
		File file = new File(path);
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 水印
	 */
	public static Bitmap createBitmapForWatermark(Bitmap src, Bitmap watermark) {
		if (src == null) {
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();

		int ww = watermark.getWidth();
		int wh = watermark.getHeight();

		// create the new blank bitmap
		Bitmap newbmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newbmp);
		// draw src into
		cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
		// draw watermark into
		cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, null);// 在src的右下角画入水印
		// save all clip
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		// store
		cv.restore();// 存储

		return newbmp;
	}

	/**
	 * surfaceView截屏
	 */
	public static Bitmap makebitmap(Context context) {
		// View是你需要截图的View
		View view = ((Activity) context).getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap b1 = view.getDrawingCache();

		Rect frame = new Rect();
		((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;

		int width = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
		int height = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
		Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
		view.destroyDrawingCache();
		return b;
	}

	/**
	 * 从view 得到图片
	 */
	public static Bitmap getBitmapFromView(View view) {
		view.destroyDrawingCache();
		view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
				View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.setDrawingCacheEnabled(true);
		Bitmap bitmap = view.getDrawingCache(true);
		return bitmap;
	}

	/**
	 * 图片合成
	 */
	public static Bitmap potoMix(int direction, Bitmap... bitmaps) {
		if (bitmaps.length <= 0) {
			return null;
		}
		if (bitmaps.length == 1) {
			return bitmaps[0];
		}

		Bitmap newBitmap = bitmaps[0];
		// newBitmap = createBitmapForFotoMix(bitmaps[0],bitmaps[1],direction);
		for (int i = 1; i < bitmaps.length; i++) {
			newBitmap = createBitmapForFotoMix(newBitmap, bitmaps[i], direction);
		}
		return newBitmap;
	}

	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int TOP = 3;
	public static final int BOTTOM = 4;

	private static Bitmap createBitmapForFotoMix(Bitmap first, Bitmap second, int direction) {
		if (first == null) {
			return null;
		}
		if (second == null) {
			return first;
		}
		int fw = first.getWidth();
		int fh = first.getHeight();

		int sw = second.getWidth();
		int sh = second.getHeight();

		Bitmap newBitmap = null;

		if (direction == LEFT) {
			newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh, Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, sw, 0, null);
			canvas.drawBitmap(second, 0, 0, null);

		} else if (direction == RIGHT) {
			newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh, Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, 0, 0, null);
			canvas.drawBitmap(second, fw, 0, null);

		} else if (direction == TOP) {
			newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh, Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, 0, sh, null);
			canvas.drawBitmap(second, 0, 0, null);

		} else if (direction == BOTTOM) {
			newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh, Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, 0, 0, null);
			canvas.drawBitmap(second, 0, fh, null);

		}

		return newBitmap;
	}

	/**
	 * 图片去色,返回灰度图片
	 * 
	 * @param bmpOriginal
	 *            传入的图片
	 * @return 去色后的图片
	 */

	public static Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();
		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		canvas.drawBitmap(bmpOriginal, 0, 0, paint);

		return bmpGrayscale;
	}

	/**
	 * 去色同时加圆角
	 * 
	 * @param bmpOriginal
	 *            原图
	 * @param pixels
	 *            圆角弧度
	 * @return 修改后的图片
	 */

	public static Bitmap toGrayscale(Bitmap bmpOriginal, int pixels) {
		return toRoundCorner(toGrayscale(bmpOriginal), pixels);
	}

	/**
	 * 把图片变成圆角
	 * 
	 * @param bitmap
	 *            需要修改的图片
	 * @param pixels
	 *            圆角的弧度
	 * @return 圆角图片
	 */

	public static Bitmap toRoundCorner(Drawable d, int pixels) {
		Bitmap bitmap = drawable2BitmapByBD(d);
		return toRoundCorner(bitmap, pixels);
	}

	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public static Bitmap toCircleCorner(Drawable d) {
		Bitmap bitmap = drawable2BitmapByBD(d);
		return toCircleCorner(bitmap);
	}

	public static Bitmap toCircleCorner(Bitmap bitmap) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		float r = rectF.width() / 2;
		canvas.drawCircle(rectF.left + r, rectF.top + r, r, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 使圆角功能支持BitampDrawable
	 * 
	 * @param bitmapDrawable
	 * @param pixels
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable, int pixels) {
		Bitmap bitmap = bitmapDrawable.getBitmap();
		return new BitmapDrawable(toRoundCorner(bitmap, pixels));
	}

	/**
	 * 将Bitmap转换成指定大小
	 */
	public static Bitmap createBitmapBySize(Bitmap bitmap, int width, int height) {
		return Bitmap.createScaledBitmap(bitmap, width, height, true);
	}

	/**
	 * 路径转Bitmap
	 */
	public static Bitmap path2Bitmap(String pathName) {
		return BitmapFactory.decodeFile(pathName);
	}

	/**
	 * Drawable 转 Bitmap
	 */

	public static Bitmap drawable2BitmapByBD(Drawable drawable) {
		BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
		return bitmapDrawable.getBitmap();
	}

	/**
	 * Bitmap 转 Drawable
	 */
	@SuppressWarnings("deprecation")
	public static Drawable bitmap2DrawableByBD(Bitmap bitmap) {
		return new BitmapDrawable(bitmap);
	}

	/**
	 * byte[] 转 bitmap
	 */
	public static Bitmap bytesToBimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	/**
	 * bitmap 转 byte[]
	 */
	public static byte[] bitmapToBytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * Resources 转 Drawable
	 */
	public static Drawable resourcesToDrawable(Context c, int img) {
		return c.getResources().getDrawable(img);
	}

	/**
	 * Resources 转 Bitmap
	 */
	public static Bitmap resourcesToBitmap(Context c, int img) {
		Drawable d = c.getResources().getDrawable(img);
		return drawable2BitmapByBD(d);
	}

}