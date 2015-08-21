package com.chj.tencent.sliding;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

import com.chj.tencent.R;

/**
 * @包名:com.chj.tencent.sliding
 * @类名:SlidingDeleteActivity
 * @作者:陈火炬
 * @时间:2015-8-20 下午8:58:24
 * 
 * @描述:侧拉删除
 * 
 * @SVN版本号:$Rev$
 * @更新人:$Author$
 * @更新描述:TODO
 * 
 */
public class SlidingDeleteActivity extends Activity
{

	protected static final String	TAG	= "SlidingDeleteActivity";
	private ListView				mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		initView();
		initData();

	}

	/**
	 * 加载数据
	 */
	private void initData()
	{
		// 往listView添加数据，list-->adapter
		mListView.setAdapter(new SlidingAdapter(SlidingDeleteActivity.this));
	}

	/**
	 * 初始化视图
	 */
	private void initView()
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去除标题
		setContentView(R.layout.activity_sliding);

		mListView = (ListView) findViewById(R.id.lv);

	}
}
