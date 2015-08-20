package com.chj.tencent;

import java.util.Random;

import com.chj.tencent.drag.DragLayout;
import com.chj.tencent.drag.MyListenerLayout;
import com.chj.tencent.drag.DragLayout.OnDragStatusChangeListener;
import com.chj.tencent.util.Cheeses;
import com.chj.tencent.util.Utils;
import com.nineoldandroids.view.ViewHelper;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.CycleInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @����:com.chj.tencent
 * @����:DragActivity
 * @����:�»��
 * @ʱ��:2015-8-20 ����10:19:12
 * 
 * @����:�໬���Activity
 * 
 * @SVN�汾��:$Rev$
 * @������:$Author$
 * @��������:TODO
 * 
 */
public class DragActivity extends Activity {
	protected static final String TAG = "DragActivity";
	private DragLayout mDragLayout;// ��ק����
	private ListView mLeftList;// ���listView
	private ListView mMainList;// ��ҳlistView
	private ImageView mHeaderIcon;// ��ҳ���ͼ��
	private MyListenerLayout mListenerLayout;// �Զ���LinearLayout����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ������
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drag);

		mDragLayout = (DragLayout) findViewById(R.id.drag_layout);
		mLeftList = (ListView) findViewById(R.id.lv_left);
		mMainList = (ListView) findViewById(R.id.lv_main);
		mHeaderIcon = (ImageView) findViewById(R.id.iv_header_icon);
		mListenerLayout = (MyListenerLayout) findViewById(R.id.mll_main_layout);

		// ����DragLayout�����ü���(�ص�)
		mDragLayout.setDragStatusListener(new OnDragStatusChangeListener() {

			@Override
			public void onOpen() {
				Utils.showToast(DragActivity.this, "onOpen");
				// �����ListView�������һ����Ŀ
				Random random = new Random();

				int nextInt = random.nextInt(50);
				mLeftList.smoothScrollToPosition(nextInt);
			}

			@Override
			public void onDrag(float percent) {
				// Log.d(TAG, "onDraging: " + percent);// 0 -> 1
				// ����ͼ���͸����:1.0 -> 0.0
				ViewHelper.setAlpha(mHeaderIcon, 1 - percent);
			}

			@Override
			public void onClose() {
				Utils.showToast(DragActivity.this, "onClose");
				// ��ͼ��ζ�
				// mHeaderIcon.setTranslationX(translationX);
				ObjectAnimator mAnimator = ObjectAnimator.ofFloat(mHeaderIcon,
						"translationX", 15.0f);// ����ƽ��15px
				mAnimator.setInterpolator(new CycleInterpolator(4));
				mAnimator.setDuration(1000);// ���ö���ʱ��
				mAnimator.start();// ��������
			}
		});

		// ��������
		mListenerLayout.setDraglayout(mDragLayout);

		// ��������adapter-->list
		mLeftList.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, Cheeses.sCheeseStrings) {
			// ��дgetView����
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView mText = ((TextView) view);
				mText.setTextColor(Color.WHITE);
				return view;
			}
		});

		mMainList.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, Cheeses.NAMES));
	}
}
