package com.chj.tencent.sliding;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

import com.chj.tencent.R;

/**
 * @����:com.chj.tencent.sliding
 * @����:SlidingDeleteActivity
 * @����:�»��
 * @ʱ��:2015-8-20 ����8:58:24
 * 
 * @����:����ɾ��
 * 
 * @SVN�汾��:$Rev$
 * @������:$Author$
 * @��������:TODO
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
	 * ��������
	 */
	private void initData()
	{
		// ��listView������ݣ�list-->adapter
		mListView.setAdapter(new SlidingAdapter(SlidingDeleteActivity.this));
	}

	/**
	 * ��ʼ����ͼ
	 */
	private void initView()
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ������
		setContentView(R.layout.activity_sliding);

		mListView = (ListView) findViewById(R.id.lv);

	}
}
