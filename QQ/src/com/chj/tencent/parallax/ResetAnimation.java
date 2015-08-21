package com.chj.tencent.parallax;

import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;

/**
 * @����:com.chj.tencent.parallax
 * @����:ResetAnimation
 * @����:�»��
 * @ʱ��:2015-8-20 ����8:33:23
 * 
 * @����:�Զ���Animation
 * 
 * @SVN�汾��:$Rev$
 * @������:$Author$
 * @��������:TODO
 * 
 */
public class ResetAnimation extends Animation {

	private ImageView mImage;
	private int startHeight;
	private int endHeight;

	public ResetAnimation(ImageView mImage, int startHeight, int endHeight) {
		this.mImage = mImage;
		this.startHeight = startHeight;
		this.endHeight = endHeight;

		// ���ö����ļ��ٶ�
		setInterpolator(new OvershootInterpolator(2));
		// ���ö���ִ��ʱ��
		setDuration(500);
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		// interpolatedTime 0.0f -> 1.0f

		Integer newHeight = evaluate(interpolatedTime, startHeight, endHeight);

		mImage.getLayoutParams().height = newHeight;
		mImage.requestLayout();

		super.applyTransformation(interpolatedTime, t);
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
