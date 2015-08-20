package com.chj.tencent.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chj.tencent.R;
import com.chj.tencent.bean.Person;

/**
 * 
 * @包名:com.chj.tencent.adapter
 * @类名:IndexAdapter
 * @作者:陈火炬
 * @时间:2015-8-20 上午11:22:56
 * 
 * @描述:自定义快速索引adapter
 * 
 * @SVN版本号:$Rev$
 * @更新人:$Author$
 * @更新描述:TODO
 * 
 */
public class IndexAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Person> persons;

	public IndexAdapter(Context mContext, ArrayList<Person> persons) {
		this.mContext = mContext;
		this.persons = persons;
	}

	@Override
	public int getCount() {
		return persons.size();
	}

	@Override
	public Object getItem(int position) {
		return persons.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.index_items, null);

			holder = new ViewHolder();
			holder.mIndex = (TextView) convertView.findViewById(R.id.tv_index);
			holder.mName = (TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Person person = persons.get(position);

		String str = null;
		String currentLetter = person.getPinyin().charAt(0) + "";
		// 根据上一个首字母,决定当前是否显示字母
		if (position == 0) {
			str = currentLetter;
		} else {
			// 上一个人的拼音的首字母
			String preLetter = persons.get(position - 1).getPinyin().charAt(0)
					+ "";
			if (!TextUtils.equals(preLetter, currentLetter)) {
				str = currentLetter;
			}
		}

		// 根据str是否为空,决定是否显示索引栏
		holder.mIndex.setVisibility(str == null ? View.GONE : View.VISIBLE);
		holder.mIndex.setText(currentLetter);
		holder.mName.setText(person.getName());

		return convertView;
	}

	/**
	 * view持有者
	 */
	static class ViewHolder {
		TextView mIndex;// 下标
		TextView mName;// 名字
	}

}
