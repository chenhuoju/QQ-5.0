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
 * @包名:com.chj.tencent
 * @类名:IndexActivity
 * @作者:陈火炬
 * @时间:2015-8-20 上午10:52:27
 * 
 * @描述:快速索引Activity
 * 
 * @SVN版本号:$Rev$
 * @更新人:$Author$
 * @更新描述:TODO
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
	 * 初始化视图
	 */
	private void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去除title
		setContentView(R.layout.activity_index);

		persons = new ArrayList<Person>();
		bar = (QuickIndexBar) findViewById(R.id.bar);
		mMainList = (ListView) findViewById(R.id.lv_main);
		tv_center = (TextView) findViewById(R.id.tv_center);

	}

	/**
	 * 加载数据
	 */
	private void initData() {
		// 获取填充数据 ，然后对数据排序
		fillAndSortData(persons);
		// 加载数据
		mMainList.setAdapter(new IndexAdapter(IndexActivity.this, persons));
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		// 设置监听
		bar.setListener(new OnLetterUpdateListener() {
			@Override
			public void onLetterUpdate(String letter) {
				// Utils.showToast(getApplicationContext(), letter);

				showLetter(letter);
				// 根据字母定位ListView, 找到集合中第一个以letter为拼音首字母的对象,得到索引
				for (int i = 0; i < persons.size(); i++) {
					Person person = persons.get(i);
					String l = person.getPinyin().charAt(0) + "";
					if (TextUtils.equals(letter, l)) {
						// 匹配成功
						mMainList.setSelection(i);
						break;
					}
				}
			}
		});
	}

	/**
	 * 显示字母
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
	 * 填充和排序数据
	 * 
	 * @param persons
	 */
	private void fillAndSortData(ArrayList<Person> persons) {
		// 填充数据
		for (int i = 0; i < Cheeses.NAMES.length; i++) {
			String name = Cheeses.NAMES[i];
			persons.add(new Person(name));
		}

		// 进行排序
		Collections.sort(persons);
	}
}
