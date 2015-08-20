package com.chj.tencent.parallax;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * @����:com.chj.tencent.parallax
 * @����:ParallaxListView
 * @����:�»��
 * @ʱ��:2015-8-20 ����5:03:31
 * 
 * @����:�Ӳ���ЧListView,��дoverScrollBy
 * 
 * @SVN�汾��:$Rev$
 * @������:$Author$
 * @��������:TODO
 * 
 */
public class ParallaxListView extends ListView {

	private static final String TAG = "ParallaxListView";
	private ImageView mImage;
	private int mOriginalHeight;// ԭʼ�߶�
	private int mDrawableHeight;// ͼƬ�߶�,���صײ���ƶ�����Ĺ��еĸ߶�
	private int newHeight;// �µĸ߶�

	public ParallaxListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ParallaxListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ParallaxListView(Context context) {
		super(context);
	}

	/**
	 * ����ImageViewͼƬ, �õ�����
	 * 
	 * @param mImage
	 */
	public void setParallaxImage(ImageView mImage) {
		this.mImage = mImage;

		mOriginalHeight = mImage.getHeight();// 160
		mDrawableHeight = mImage.getDrawable().getIntrinsicHeight();// 592

		Log.d(TAG, "height: " + mOriginalHeight + " drawableHeight: "
				+ mDrawableHeight);
	}

	/**
	 * ��дoverScrollBy����
	 * 
	 * @deltaY : ��ֱ�����˲ʱƫ���� / �仯�� dx ������ͷ����Ϊ-, �ײ���ͷ����Ϊ+
	 * @scrollY : ��ֱ�����ƫ���� / �仯��
	 * @scrollRangeY : ��ֱ���򻬶��ķ�Χ
	 * @maxOverScrollY : ��ֱ������󻬶���Χ
	 * @isTouchEvent : �Ƿ�����ָ��������, trueΪ��ָ, falseΪ����
	 */
	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		// Log.d(TAG, "deltaY: " + deltaY + " scrollY: " + scrollY
		// + " scrollRangeY: " + scrollRangeY + " maxOverScrollY: "
		// + maxOverScrollY + " isTouchEvent: " + isTouchEvent);

		// ��ָ���� ���� ������
		if (isTouchEvent && deltaY < 0) {
			// ��������˲ʱ�仯���ľ���ֵ����Header, �Ϳ���ʵ�ַŴ�Ч��
			if (mImage.getHeight() <= mDrawableHeight) {
				// ��ָ����3���أ�ͼƬֻ�ƶ�1����
				newHeight = (int) (mImage.getHeight() + Math.abs(deltaY / 3.0f));

				// �߶Ȳ�����ͼƬ���߶�ʱ,��������Ч
				mImage.getLayoutParams().height = newHeight;
				mImage.requestLayout();
			}
		}

		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
				scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY,
				isTouchEvent);
	}

	/**
	 * �������¼�
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_UP :// ��ָ�ͷŴ���
				final int startHeight = mImage.getHeight();// ��ʼ�߶�
				final int endHeight = mOriginalHeight;// �����߶�

				// ִ�лص�����, ��ʽһ: ���Զ���/ֵ����
				// �ӵ�ǰ�߶�mImage.getHeight(), ִ�ж�����ԭʼ�߶�mOriginalHeight
				// valueAnimator(startHeight, endHeight);

				// ִ�лص�����, ��ʽ��: �Զ���Animation
				ResetAnimation resetAnimation = new ResetAnimation(mImage,
						startHeight, endHeight);
				startAnimation(resetAnimation);

				break;
			default :
				break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * ���Զ���/ֵ����
	 * 
	 * @param startHeight
	 * @param endHeight
	 */
	private void valueAnimator(final int startHeight, final int endHeight) {
		ValueAnimator mValueAnimator = ValueAnimator.ofInt(1);
		mValueAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float percent = animation.getAnimatedFraction();
				// percent:�ٷֱ� 0.0 -> 1.0
				Log.d(TAG, "percent:" + percent);
				Integer newHeight = evaluate(percent, startHeight, endHeight);

				mImage.getLayoutParams().height = newHeight;
				mImage.requestLayout();
			}
		});
		// ���ö����ļ��ٶ�
		mValueAnimator.setInterpolator(new OvershootInterpolator());
		// ���ö���ִ��ʱ��
		mValueAnimator.setDuration(500);
		mValueAnimator.start();
	}

	/**
	 * ���͹�ֵ��
	 * 
	 * @param fraction
	 * @param startValue
	 * @param endValue
	 * @return
	 */
	public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
		int startInt = startValue;
		return (int) (startInt + fraction * (endValue - startInt));
	}
}
