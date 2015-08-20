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
 * @����:com.chj.tencent.adapter
 * @����:IndexAdapter
 * @����:�»��
 * @ʱ��:2015-8-20 ����11:22:56
 * 
 * @����:�Զ����������adapter
 * 
 * @SVN�汾��:$Rev$
 * @������:$Author$
 * @��������:TODO
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
		// ������һ������ĸ,������ǰ�Ƿ���ʾ��ĸ
		if (position == 0) {
			str = currentLetter;
		} else {
			// ��һ���˵�ƴ��������ĸ
			String preLetter = persons.get(position - 1).getPinyin().charAt(0)
					+ "";
			if (!TextUtils.equals(preLetter, currentLetter)) {
				str = currentLetter;
			}
		}

		// ����str�Ƿ�Ϊ��,�����Ƿ���ʾ������
		holder.mIndex.setVisibility(str == null ? View.GONE : View.VISIBLE);
		holder.mIndex.setText(currentLetter);
		holder.mName.setText(person.getName());

		return convertView;
	}

	/**
	 * view������
	 */
	static class ViewHolder {
		TextView mIndex;// �±�
		TextView mName;// ����
	}

}
