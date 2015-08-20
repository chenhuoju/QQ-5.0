package com.chj.tencent;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.chj.tencent.adapter.IndexAdapter;
import com.chj.tencent.bean.Person;
import com.chj.tencent.index.QuickIndexBar;
import com.chj.tencent.index.QuickIndexBar.OnLetterUpdateListener;
import com.chj.tencent.util.Cheeses;

/**
 * 
 * @����:com.chj.tencent
 * @����:IndexActivity
 * @����:�»��
 * @ʱ��:2015-8-20 ����10:52:27
 * 
 * @����:��������
 * 
 * @SVN�汾��:$Rev$
 * @������:$Author$
 * @��������:TODO
 * 
 */
public class IndexActivity extends Activity {

	private ListView mMainList;
	private ArrayList<Person> persons;
	private TextView tv_center;

	private Handler mHandler = new Handler();
	private QuickIndexBar bar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();
		initListener();
		initData();

	}

	/**
	 * ��ʼ����ͼ
	 */
	private void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��title
		setContentView(R.layout.activity_index);

		persons = new ArrayList<Person>();
		bar = (QuickIndexBar) findViewById(R.id.bar);
		mMainList = (ListView) findViewById(R.id.lv_main);
		tv_center = (TextView) findViewById(R.id.tv_center);

	}

	/**
	 * ��������
	 */
	private void initData() {
		// ��ȡ������� ��Ȼ�����������
		fillAndSortData(persons);
		// ��������
		mMainList.setAdapter(new IndexAdapter(IndexActivity.this, persons));
	}

	/**
	 * ��ʼ������
	 */
	private void initListener() {
		// ���ü���
		bar.setListener(new OnLetterUpdateListener() {
			@Override
			public void onLetterUpdate(String letter) {
				// Utils.showToast(getApplicationContext(), letter);

				showLetter(letter);
				// ������ĸ��λListView, �ҵ������е�һ����letterΪƴ������ĸ�Ķ���,�õ�����
				for (int i = 0; i < persons.size(); i++) {
					Person person = persons.get(i);
					String l = person.getPinyin().charAt(0) + "";
					if (TextUtils.equals(letter, l)) {
						// ƥ��ɹ�
						mMainList.setSelection(i);
						break;
					}
				}
			}
		});
	}

	/**
	 * ��ʾ��ĸ
	 * 
	 * @param letter
	 */
	protected void showLetter(String letter) {
		tv_center.setVisibility(View.VISIBLE);
		tv_center.setText(letter);

		mHandler.removeCallbacksAndMessages(null);
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				tv_center.setVisibility(View.GONE);
			}
		}, 2000);

	}

	/**
	 * ������������
	 * 
	 * @param persons
	 */
	private void fillAndSortData(ArrayList<Person> persons) {
		// �������
		for (int i = 0; i < Cheeses.NAMES.length; i++) {
			String name = Cheeses.NAMES[i];
			persons.add(new Person(name));
		}

		// ��������
		Collections.sort(persons);
	}
}
