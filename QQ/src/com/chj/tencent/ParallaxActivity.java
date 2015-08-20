package com.chj.tencent;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.chj.tencent.parallax.ParallaxListView;
import com.chj.tencent.util.Cheeses;

/**
 * @����:com.chj.tencent
 * @����:ParallaxActivity
 * @����:�»��
 * @ʱ��:2015-8-20 ����5:09:48
 * 
 * @����:�Ӳ���Ч
 * 
 * @SVN�汾��:$Rev$
 * @������:$Author$
 * @��������:TODO
 * 
 */
public class ParallaxActivity extends Activity {
	private ParallaxListView mListView;// listVew
	private View mHeaderView;// ͷ����
	private ImageView mImage;// ͷ�����е�imageView

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();
		initData();
	}

	/**
	 * ��ʼ����ͼ
	 */
	private void initView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��title
		setContentView(R.layout.activity_parallax);

		mListView = (ParallaxListView) findViewById(R.id.lv_parallax);
		mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);//����OverScrollMode

	}

	/**
	 * ��������
	 */
	private void initData() {
		// ���Header
		mHeaderView = View.inflate(ParallaxActivity.this,
				R.layout.parallax_view_header, null);
		mImage = (ImageView) mHeaderView.findViewById(R.id.parallax_iv);
		mListView.addHeaderView(mHeaderView);

		/**
		 * ��headerView���ûص�����
		 */
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					/**
					 * ������������֮��, �˷����ᱻ����
					 */
					@Override
					public void onGlobalLayout() {
						// ����ImageViewͼƬ
						mListView.setParallaxImage(mImage);

						mHeaderView.getViewTreeObserver()
								.removeOnGlobalLayoutListener(this);
					}
				});

		// �������
		mListView.setAdapter(new ArrayAdapter<String>(ParallaxActivity.this,
				android.R.layout.simple_list_item_1, Cheeses.NAMES));
	}

}
