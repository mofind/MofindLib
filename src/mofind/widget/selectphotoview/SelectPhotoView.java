package mofind.widget.selectphotoview;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.mofind.lib.R;

public class SelectPhotoView extends GridView implements android.widget.AdapterView.OnItemClickListener,
		android.widget.AdapterView.OnItemLongClickListener {

	public SelectPhotoView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public SelectPhotoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public SelectPhotoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context);
	}

	private void init(Context context) {
		gridSize = SelectPhotoActivity.px2dip(getContext(), 80);
		adapter = new PhotoAdapter();
		setCacheColorHint(Color.TRANSPARENT);
		int padding = SelectPhotoActivity.px2dip(context, 5);
		setHorizontalSpacing(padding);
		setVerticalSpacing(padding);
		setNumColumns(4);
		setAdapter(adapter);
		setOnItemClickListener(this);
		setOnItemLongClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (position == getAddBtnIndex() && isHasAddBtn()) {
			Activity act = (Activity) getContext();
			Intent intent = new Intent(act, SelectPhotoActivity.class);
			intent.putExtra("MAX_COUNT", maxPhotoCount - photos.size());
			act.startActivityForResult(intent, 0x123);
		} else {
			final ImageBean bean = (ImageBean) adapter.getItem(position);
			// Toast.makeText(getContext(), "查看图片" + position, 0).show();
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
		if (position != getAddBtnIndex()) {
			final ImageBean bean = (ImageBean) adapter.getItem(position);
			AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			builder.setMessage("是否删除这张图片？");
			builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					photos.remove(bean);
					adapter.notifyDataSetChanged();
				}
			});
			builder.setNegativeButton("取消", null);
			builder.show();
		}
		return true;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0x123 && resultCode == Activity.RESULT_OK) {
			List<ImageBean> selecteds = (List<ImageBean>) data.getExtras().getSerializable(SelectPhotoActivity.IMAGES);
			photos.addAll(selecteds);
			adapter.notifyDataSetChanged();
		}
	}

	private int getAddBtnIndex() {
		return photos.size();
	}

	private boolean isHasAddBtn() {
		return photos.size() < maxPhotoCount;
	}

	private class PhotoAdapter extends BaseAdapter {

		public PhotoAdapter() {
		}

		@Override
		public int getCount() {
			if (isHasAddBtn()) {
				return photos.size() + 1;
			} else {
				return photos.size();
			}
		}

		@Override
		public Object getItem(int position) {
			return photos.get(position); // 如果取最后一项，则数组越界
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView iv = new ImageView(getContext());
			iv.setScaleType(ScaleType.CENTER_CROP);
			iv.setLayoutParams(new AbsListView.LayoutParams(gridSize, gridSize));
			if (isHasAddBtn()) {
				if (position == getAddBtnIndex()) {
					iv.setImageResource(R.drawable.spv_btn_add);
				} else {
					final ImageBean bean = photos.get(position);
					DisplayPictureUtil.setImageUrl("file://" + bean.path, iv);
				}
			} else {
				final ImageBean bean = photos.get(position);
				DisplayPictureUtil.setImageUrl("file://" + bean.path, iv);
			}
			return iv;
		}

	}

	public List<ImageBean> getPhotos() {
		return photos;
	}

	public void setPhotos(List<ImageBean> photos) {
		this.photos = photos;
	}

	public int getGridSize() {
		return gridSize;
	}

	public void setGridSize(int gridSize) {
		this.gridSize = gridSize;
	}

	public int getMaxPhotoCount() {
		return maxPhotoCount;
	}

	public void setMaxPhotoCount(int maxPhotoCount) {
		this.maxPhotoCount = maxPhotoCount;
	}

	private List<ImageBean> photos = new ArrayList<ImageBean>();

	private int gridSize = 0;

	private int maxPhotoCount = 6;

	private PhotoAdapter adapter;

}
