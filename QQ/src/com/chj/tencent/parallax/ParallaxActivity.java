package com.chj.tencent.parallax;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.chj.tencent.R;
import com.chj.tencent.util.Cheeses;

/**
 * @包名:com.chj.tencent.parallax
 * @类名:ParallaxActivity
 * @作者:陈火炬
 * @时间:2015-8-20 下午5:09:48
 * 
 * @描述:视差特效
 * 
 * @SVN版本号:$Rev$
 * @更新人:$Author$
 * @更新描述:TODO
 * 
 */
public class ParallaxActivity extends Activity {
	private ParallaxListView mListView; // listVew
	private View mHeaderView; // 头布局
	private ImageView mImage; // 头布局中的imageView

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();
		initData();
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去除title
		setContentView(R.layout.activity_parallax);

		mListView = (ParallaxListView) findViewById(R.id.lv_parallax);
		mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);// 设置OverScrollMode

	}

	/**
	 * 加载数据
	 */
	private void initData() {
		// 添加Header
		mHeaderView = View.inflate(ParallaxActivity.this,
				R.layout.parallax_view_header, null);
		mImage = (ImageView) mHeaderView.findViewById(R.id.parallax_iv);
		mListView.addHeaderView(mHeaderView);

		/**
		 * 对headerView设置回调监听
		 */
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					/**
					 * 当布局填充结束之后 , 此方法会被调用
					 */
					@Override
					public void onGlobalLayout() {
						// 设置ImageView图片
						mListView.setParallaxImage(mImage);

						mHeaderView.getViewTreeObserver()
								.removeOnGlobalLayoutListener(this);
					}
				});

		// 填充数据
		mListView.setAdapter(new ArrayAdapter<String>(ParallaxActivity.this,
				android.R.layout.simple_list_item_1, Cheeses.NAMES));
	}

}
