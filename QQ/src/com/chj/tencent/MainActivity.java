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

	private static String[] objects = new String[]{"�໬���", "��������", "����ɾ��",
			"ʱ������", "ճ�Կؼ�"};
	private static Class<?>[] clazzs = new Class[]{DragActivity.class};
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
				R.layout.items, objects);

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