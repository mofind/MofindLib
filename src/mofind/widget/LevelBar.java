package mofind.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LevelBar extends LinearLayout {

	private LinearLayout indexLayout;
	private TextView indexTv, rectTv;
	private int level;

	public static final String[] LEVELS = { "级别一", "级别二", "级别三", "级别四", "级别五", "级别六", "级别七", "级别八", "级别九", "级别十" };
	public static final int[] COLORS = { 0xff003300, 0xff006600, 0xff009900, 0xff333300, 0xff663300, 0xff993300, 0xff003333, 0xff003366, 0xff003399,
			0xff990000 };

	public LevelBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public LevelBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	@SuppressLint("NewApi")
	public LevelBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	private void initView(Context context) {
		setOrientation(VERTICAL);
		indexLayout = new LinearLayout(context);
		indexLayout.setOrientation(VERTICAL);
		indexLayout.setGravity(Gravity.CENTER);
		// 级别
		indexTv = new TextView(context);
		indexTv.setText(LEVELS[0]);
		indexTv.setTextColor(Color.WHITE);
		indexTv.setBackgroundColor(COLORS[0]);
		indexTv.setSingleLine();
		indexTv.setGravity(Gravity.CENTER);
		// 方块
		rectTv = new TextView(context);
		rectTv.setBackgroundColor(COLORS[0]);
		// 背景
		ImageView iv = new ImageView(context);
		iv.setBackgroundColor(Color.GRAY);

		indexLayout.addView(indexTv, 100, 40);
		indexLayout.addView(rectTv, 10, 10);
		addView(indexLayout, 100, 50);
		addView(iv, -1, 10);
	}

	boolean running = false;

	public void initLevel(int lv) {
		this.level = lv;
		running = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (running) {
					for (int i = 0; i < level; i++) {
						Message msg = mHandler.obtainMessage();
						msg.arg1 = i;
						mHandler.sendMessage(msg);
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					// 关闭线程
					running = false;
				}
			}
		}).start();
	}

	final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int lv = msg.arg1;
			setData(lv);
		}
	};

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
		setData(level);
	}

	private void setData(int level) {

		if (level < LEVELS.length) {
			indexTv.setText(LEVELS[level]);
			indexTv.setBackgroundColor(COLORS[level]);
			rectTv.setBackgroundColor(COLORS[level]);
			int offset = (getWidth() - indexTv.getWidth() / 2) / LEVELS.length;
			int before = (level - 1) * offset;
			int distance = level * offset;

			TranslateAnimation animation = new TranslateAnimation(before, distance, 0, 0);
			animation.setDuration(300);
			animation.setFillAfter(true);
			indexLayout.startAnimation(animation);
		} else {
			System.out.println("LevelBar 数组越界");
		}
	}
}
