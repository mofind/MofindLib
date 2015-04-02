package mofind.widget.selectphotoview;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mofind.lib.R;

public class SelectPhotoActivity extends Activity {

	private SelectPhotoActivity mActivity = null;

	private static final int PHOTO_GRAPH = 1;

	public static final String IMAGES = "images";

	private GridView gv;

	private ImageAdapter adapter;

	private Button cancelBtn, okBtn;

	private List<AlbumBean> mAlbumBeans;

	private AlbumBean sysAlbumBean;

	private List<ImageBean> selecteds = new ArrayList<ImageBean>();

	private int maxCount = 6;

	String fileName;// 文件名，路径
	String dirPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.widget_act_select_photo_view);
		initData();
		initView();
		setData();
	}

	private void initData() {
		mActivity = this;
		maxCount = getIntent().getIntExtra("MAX_COUNT", 6);
	}

	private void initView() {
		gv = (GridView) findViewById(R.id.gv);
		cancelBtn = (Button) findViewById(R.id.cancelBtn);
		okBtn = (Button) findViewById(R.id.okBtn);
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ok();
			}
		});
		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position == 0) {
					takePhoto();
				} else {
					// 刷新选中状态
					final ImageBean bean = (ImageBean) adapter.getItem(position);
					if (selecteds.size() < maxCount) {
						bean.isChecked = !bean.isChecked;
						adapter.notifyDataSetChanged();
						// 添加到数组
						if (bean.isChecked || selecteds.isEmpty()) {
							selecteds.add(bean);
						} else {
							selecteds.remove(bean);
						}
						// 按钮
						if (selecteds.isEmpty()) {
							okBtn.setEnabled(false);
							okBtn.setTextColor(0xffcccccc);
							okBtn.setText("完成");
						} else {
							okBtn.setEnabled(true);
							okBtn.setTextColor(Color.WHITE);
							okBtn.setText("完成(" + selecteds.size() + "/" + maxCount + ")");
						}
					} else {
						if (bean.isChecked) {
							bean.isChecked = false;
							adapter.notifyDataSetChanged();
							selecteds.remove(bean);
							okBtn.setEnabled(true);
							okBtn.setTextColor(Color.WHITE);
							okBtn.setText("完成(" + selecteds.size() + "/" + maxCount + ")");
						} else {
							Toast.makeText(mActivity, "已经超过最大数量", 0).show();
						}
					}
				}
			}
		});
	}

	private void setData() {
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				mAlbumBeans = AlbumHelper.newInstance(mActivity).getFolders();
				if (mAlbumBeans != null && mAlbumBeans.size() != 0) {
					sysAlbumBean = AlbumHelper.newInstance(mActivity).getSystemAlbum(mAlbumBeans);
					adapter = new ImageAdapter(sysAlbumBean.sets);
					gv.setAdapter(adapter);
				} else {
					Toast.makeText(mActivity, "无照片", 0).show();
				}
			}
		});
	}

	private void takePhoto() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			fileName = getFileName();
			dirPath = Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + File.separator
					+ AlbumHelper.PATH_CAMERA;
			File tempFile = new File(dirPath);
			if (!tempFile.exists()) {
				tempFile.mkdirs();
			}
			File saveFile = new File(tempFile, fileName + ".jpg");
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(saveFile));
			startActivityForResult(intent, PHOTO_GRAPH);
		} else {
			Toast.makeText(mActivity, "未检测到存储设备，拍照不可用!", Toast.LENGTH_SHORT).show();
		}
	}

	private String getFileName() {
		StringBuffer sb = new StringBuffer();
		Calendar calendar = Calendar.getInstance();
		long millis = calendar.getTimeInMillis();
		String[] dictionaries = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
				"X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w",
				"x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
		sb.append("dzc");
		sb.append(millis);
		Random random = new Random();
		for (int i = 0; i < 5; i++) {
			sb.append(dictionaries[random.nextInt(dictionaries.length - 1)]);
		}
		return sb.toString();
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PHOTO_GRAPH && resultCode == RESULT_OK) {
			List<ImageBean> selecteds = new ArrayList<ImageBean>();
			selecteds.add(new ImageBean(null, 0l, null, dirPath + "/" + fileName + ".jpg", false));
			Intent intent = new Intent();
			intent.putExtra(IMAGES, (Serializable) selecteds);
			setResult(RESULT_OK, intent);
			finish();
		}
	}

	private void ok() {
		if (!selecteds.isEmpty() && selecteds.size() <= maxCount) {
			Intent intent = new Intent();
			intent.putExtra(IMAGES, (Serializable) selecteds);
			setResult(RESULT_OK, intent);
			finish();
		} else {
			Toast.makeText(mActivity, "选择错误", 0).show();
		}
	}

	private class ImageAdapter extends BaseAdapter {

		private List<ImageBean> images = new ArrayList<ImageBean>();
		private int screenWidth = 320;

		public ImageAdapter(List<ImageBean> imageBeans) {
			screenWidth = getScreenWidth(mActivity);
			Collections.reverse(imageBeans);
			images = imageBeans;
			images.add(0, new ImageBean());
		}

		@Override
		public int getCount() {
			return images == null ? 1 : images.size();
		}

		@Override
		public Object getItem(int position) {
			return images.get(position); // 如果取第一项，则数组越界
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getLayoutInflater().inflate(R.layout.widget_item_iv, null);
				holder.img = (ImageView) convertView.findViewById(R.id.imgIv);
				holder.check = (ImageView) convertView.findViewById(R.id.check);
				holder.shadow = convertView.findViewById(R.id.shadow);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			int wh = screenWidth / 3 - px2dip(mActivity, 2);
			holder.img.setLayoutParams(new RelativeLayout.LayoutParams(wh, wh));
			holder.shadow.setLayoutParams(new RelativeLayout.LayoutParams(wh, wh));
			if (position == 0) {
				holder.check.setVisibility(View.GONE);
				holder.img.setImageResource(R.drawable.btn_pic_takephoto);
			} else {
				final ImageBean bean = images.get(position);
				holder.check.setVisibility(View.VISIBLE);
				DisplayPictureUtil.setImageUrl("file://" + bean.path, holder.img);
				if (bean.isChecked) {
					holder.check.setImageResource(R.drawable.ab9);
					holder.shadow.setVisibility(View.VISIBLE);
				} else {
					holder.check.setImageResource(R.drawable.ab8);
					holder.shadow.setVisibility(View.GONE);
				}
			}
			return convertView;
		}

		public class ViewHolder {
			public ImageView img;
			public ImageView check;
			public View shadow;
		}

	}

	/**
	 * px转dp
	 */
	public static int px2dip(Context c, int px) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, c.getResources().getDisplayMetrics());
	}

	/**
	 * 获取手机屏幕宽度
	 */
	public static int getScreenWidth(Context context) {
		DisplayMetrics dm = getDisplayMetrics(context);
		return dm.widthPixels;
	}

	/**
	 * 获取手机屏幕高度
	 */
	public static int getScreenHeight(Context context) {
		DisplayMetrics dm = getDisplayMetrics(context);
		return dm.heightPixels;
	}

	private static DisplayMetrics getDisplayMetrics(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm;
	}

}
