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

import com.chj.tencent.drag.DragActivity;
import com.chj.tencent.index.IndexActivity;
import com.chj.tencent.parallax.ParallaxActivity;
import com.chj.tencent.sliding.SlidingDeleteActivity;

public class MainActivity extends Activity implements OnItemClickListener {

	private static String[] objects = new String[]{"�໬���", "��������", "�Ӳ�����",
			"����ɾ��", "ճ�Կؼ�"};

	private static Class<?>[] clazzs = new Class[]{DragActivity.class,
			IndexActivity.class, ParallaxActivity.class,
			SlidingDeleteActivity.class};

	private ListView list;
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��title
		super.onCreate(savedInstanceState);

		initView();
	}

	/**
	 * ��ʼ����ͼ
	 */
	private void initView() {
		setContentView(R.layout.activity_main);

		list = (ListView) findViewById(R.id.list);
		adapter = new ArrayAdapter<String>(getApplicationContext(),
				R.layout.main_items, objects);

		list.setVerticalScrollBarEnabled(false);// ���ع�����
		list.setDividerHeight(0);// ȥ���ָ���
		list.setAdapter(adapter);// ����������ʾ
		list.setOnItemClickListener(this);// ���ü���

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(getApplicationContext(), clazzs[position]);
		startActivity(intent);
	}
}
