package com.chj.tencent.parallax;

import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;

/**
 * @包名:com.chj.tencent.parallax
 * @类名:ResetAnimation
 * @作者:陈火炬
 * @时间:2015-8-20 下午8:33:23
 * 
 * @描述:自定义Animation
 * 
 * @SVN版本号:$Rev$
 * @更新人:$Author$
 * @更新描述:TODO
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

		// 设置动画的加速度
		setInterpolator(new OvershootInterpolator());
		// 设置动画执行时长
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
	 * 类型估值器
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
