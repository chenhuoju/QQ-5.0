package com.chj.tencent.sliding;

//��̬����
import static com.chj.tencent.util.Cheeses.NAMES;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chj.tencent.R;
import com.chj.tencent.sliding.SlidingLayout;
import com.chj.tencent.sliding.SlidingLayout.onSlidingLayoutListener;

/**
 * @����:com.chj.tencent.sliding
 * @����:SlidingAdapter
 * @����:�»��
 * @ʱ��:2015-8-21 ����11:09:41
 * 
 * @����:�Զ������ɾ��adapter
 * 
 * @SVN�汾��:$Rev$
 * @������:$Author$
 * @��������:TODO
 * 
 */
public class SlidingAdapter extends BaseAdapter
{

	protected static final String		TAG		= "SlidingAdapter";
	private Context						mContext;
	private ArrayList<SlidingLayout>	openItems;													// �򿪵���Ŀ����

	// ͼ����Դ
	public static final int[]			RESIDS	= new int[] {
												R.drawable.mm1, R.drawable.mm2, R.drawable.mm3,
												R.drawable.mm4, R.drawable.mm5, R.drawable.mm6 };

	public SlidingAdapter(Context context) {
		this.mContext = context;

		openItems = new ArrayList<SlidingLayout>();
	}

	@Override
	public int getCount()
	{
		if (NAMES != null) { return NAMES.length; }
		return 0;
	}

	@Override
	public Object getItem(int position)
	{
		if (NAMES != null) { return NAMES[position]; }
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;

		if (convertView == null)
		{
			convertView = View.inflate(mContext, R.layout.sliding_items, null);

			holder = new ViewHolder();
			holder.tv_call = (TextView) convertView.findViewById(R.id.tv_call);
			holder.tv_del = (TextView) convertView.findViewById(R.id.tv_del);
			holder.iv_image = (ImageView) convertView
														.findViewById(R.id.iv_image);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		// �������ֺ�ͼ��
		holder.tv_name.setText(NAMES[position]);
		Random rdm = new Random();
		int Ids = rdm.nextInt(RESIDS.length);
		holder.iv_image.setImageResource(RESIDS[Ids]);

		SlidingLayout sl = (SlidingLayout) convertView;
		sl.setSlidingListener(new onSlidingLayoutListener() {

			@Override
			public void onStartOpen(SlidingLayout slidingLayout)
			{
				Log.d(TAG, "onStartOpen");

				// Ҫȥ����ʱ,�ȱ��������Ѵ���Ŀ,����ر�
				for (SlidingLayout item : openItems)
				{
					item.close();
				}
				// ��ռ���
				openItems.clear();
			}

			@Override
			public void onStartClose(SlidingLayout slidingLayout)
			{
				Log.d(TAG, "onStartClose");
			}

			@Override
			public void onOpen(SlidingLayout slidingLayout)
			{
				Log.d(TAG, "onOpen");

				// ����Ŀ��ӽ�����
				openItems.add(slidingLayout);
			}

			@Override
			public void onDraging(SlidingLayout slidingLayout)
			{

			}

			@Override
			public void onClose(SlidingLayout slidingLayout)
			{
				Log.d(TAG, "onClose");

				// ����Ŀ�����д��Ƴ�
				openItems.remove(slidingLayout);
			}
		});

		return convertView;
	}

	static class ViewHolder
	{
		TextView	tv_call;
		TextView	tv_del;
		ImageView	iv_image;
		TextView	tv_name;
	}
}
