package com.chj.tencent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity implements OnItemClickListener {

	private static String[] objects = new String[]{"侧滑面板", "快速索引", "侧拉删除",
			"时差特性", "粘性控件"};
	private static Class<?>[] clazzs = new Class[]{DragActivity.class,
			IndexActivity.class};
	private ListView list;
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉title
		super.onCreate(savedInstanceState);

		initView();
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		setContentView(R.layout.activity_main);

		list = (ListView) findViewById(R.id.list);
		adapter = new ArrayAdapter<String>(getApplicationContext(),
				R.layout.main_items, objects);

		list.setVerticalScrollBarEnabled(false);// 隐藏滚动条
		list.setDividerHeight(0);// 去掉分割线
		list.setAdapter(adapter);// 设置数据显示
		list.setOnItemClickListener(this);// 设置监听

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(getApplicationContext(), clazzs[position]);
		startActivity(intent);
	}
}
