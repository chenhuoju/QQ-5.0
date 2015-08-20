package com.chj.tencent;

import java.util.Random;

import com.chj.tencent.drag.DragLayout;
import com.chj.tencent.drag.MyListenerLayout;
import com.chj.tencent.drag.DragLayout.OnDragStatusChangeListener;
import com.chj.tencent.util.Cheeses;
import com.chj.tencent.util.Utils;
import com.nineoldandroids.view.ViewHelper;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.CycleInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @包名:com.chj.tencent
 * @类名:DragActivity
 * @作者:陈火炬
 * @时间:2015-8-20 上午10:19:12
 * 
 * @描述:侧滑面板
 * 
 * @SVN版本号:$Rev$
 * @更新人:$Author$
 * @更新描述:TODO
 * 
 */
public class DragActivity extends Activity {
	protected static final String TAG = "DragActivity";
	private DragLayout mDragLayout;// 拖拽布局
	private ListView mLeftList;// 左侧listView
	private ListView mMainList;// 主页listView
	private ImageView mHeaderIcon;// 主页面的图标
	private MyListenerLayout mListenerLayout;// 自定义LinearLayout布局

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drag);

		mDragLayout = (DragLayout) findViewById(R.id.drag_layout);
		mLeftList = (ListView) findViewById(R.id.lv_left);
		mMainList = (ListView) findViewById(R.id.lv_main);
		mHeaderIcon = (ImageView) findViewById(R.id.iv_header_icon);
		mListenerLayout = (MyListenerLayout) findViewById(R.id.mll_main_layout);

		// 查找DragLayout，设置监听(回调)
		mDragLayout.setDragStatusListener(new OnDragStatusChangeListener() {

			@Override
			public void onOpen() {
				Utils.showToast(DragActivity.this, "onOpen");
				// 左面板ListView随机设置一个条目
				Random random = new Random();

				int nextInt = random.nextInt(50);
				mLeftList.smoothScrollToPosition(nextInt);
			}

			@Override
			public void onDrag(float percent) {
				// Log.d(TAG, "onDraging: " + percent);// 0 -> 1
				// 更新图标的透明度:1.0 -> 0.0
				ViewHelper.setAlpha(mHeaderIcon, 1 - percent);
			}

			@Override
			public void onClose() {
				Utils.showToast(DragActivity.this, "onClose");
				// 让图标晃动
				// mHeaderIcon.setTranslationX(translationX);
				ObjectAnimator mAnimator = ObjectAnimator.ofFloat(mHeaderIcon,
						"translationX", 15.0f);// 往右平移15px
				mAnimator.setInterpolator(new CycleInterpolator(4));
				mAnimator.setDuration(1000);// 设置动画时间
				mAnimator.start();// 开启动画
			}
		});

		// 设置引用
		mListenerLayout.setDraglayout(mDragLayout);

		// 加载数据adapter-->list
		mLeftList.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, Cheeses.sCheeseStrings) {
			// 重写getView方法
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView mText = ((TextView) view);
				mText.setTextColor(Color.WHITE);
				return view;
			}
		});

		mMainList.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, Cheeses.NAMES));
	}
}
