package mofind.widget.selectphotoview;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.mofind.lib.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class DisplayPictureUtil {

	public static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

	public static ImageLoader getImageLoader() {
		return ImageLoader.getInstance();
	}

	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY) // 加载图片的线程数
				.denyCacheImageMultipleSizesInMemory() // 解码图像的大尺寸将在内存中缓存先前解码图像的小尺寸。
				.discCacheFileNameGenerator(new Md5FileNameGenerator()) // 设置磁盘缓存文件名称
				.tasksProcessingOrder(QueueProcessingType.LIFO) // 设置加载显示图片队列进程
				.build();
		getImageLoader().init(config);
	}

	public static void setImageUrl(String url, ImageView iv) {
		getImageLoader().displayImage(url, iv, getDisplayImageOptions(), new AnimateFirstDisplayListener());
	}

	private static DisplayImageOptions getDisplayImageOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.t) // 在ImageView加载过程中显示图片
				.showImageForEmptyUri(R.drawable.t) // image连接地址为空时
				.showImageOnFail(R.drawable.t) // image加载失败
				.cacheInMemory(true) // 加载图片时会在内存中加载缓存
				.cacheOnDisc(true) // 加载图片时会在磁盘中加载缓存
				// .displayer(new RoundedBitmapDisplayer(2)) //
				// 设置用户加载图片task(这里是圆角图片显示)
				.build();
		return options;
	}

	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500); // 设置image隐藏动画500ms
					displayedImages.add(imageUri); // 将图片uri添加到集合中
				}
			}
		}
	}

}
