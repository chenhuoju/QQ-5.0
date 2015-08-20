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
 * @包名:com.chj.tencent.parallax
 * @类名:ParallaxListView
 * @作者:陈火炬
 * @时间:2015-8-20 下午5:03:31
 * 
 * @描述:视差特效ListView,重写overScrollBy
 * 
 * @SVN版本号:$Rev$
 * @更新人:$Author$
 * @更新描述:TODO
 * 
 */
public class ParallaxListView extends ListView {

	private static final String TAG = "ParallaxListView";
	private ImageView mImage;
	private int mOriginalHeight;// 原始高度
	private int mDrawableHeight;// 图片高度,返回底层可移动对象的固有的高度
	private int newHeight;// 新的高度

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
	 * 设置ImageView图片, 拿到引用
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
	 * 重写overScrollBy方法
	 * 
	 * @deltaY : 竖直方向的瞬时偏移量 / 变化量 dx 顶部到头下拉为-, 底部到头上拉为+
	 * @scrollY : 竖直方向的偏移量 / 变化量
	 * @scrollRangeY : 竖直方向滑动的范围
	 * @maxOverScrollY : 竖直方向最大滑动范围
	 * @isTouchEvent : 是否是手指触摸滑动, true为手指, false为惯性
	 */
	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		// Log.d(TAG, "deltaY: " + deltaY + " scrollY: " + scrollY
		// + " scrollRangeY: " + scrollRangeY + " maxOverScrollY: "
		// + maxOverScrollY + " isTouchEvent: " + isTouchEvent);

		// 手指拉动 并且 是下拉
		if (isTouchEvent && deltaY < 0) {
			// 把拉动的瞬时变化量的绝对值交给Header, 就可以实现放大效果
			if (mImage.getHeight() <= mDrawableHeight) {
				// 手指滑动3像素，图片只移动1像素
				newHeight = (int) (mImage.getHeight() + Math.abs(deltaY / 3.0f));

				// 高度不超出图片最大高度时,才让其生效
				mImage.getLayoutParams().height = newHeight;
				mImage.requestLayout();
			}
		}

		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
				scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY,
				isTouchEvent);
	}

	/**
	 * 处理触摸事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_UP :// 手指释放触摸
				final int startHeight = mImage.getHeight();// 起始高度
				final int endHeight = mOriginalHeight;// 结束高度

				// 执行回弹动画, 方式一: 属性动画/值动画
				// 从当前高度mImage.getHeight(), 执行动画到原始高度mOriginalHeight
				// valueAnimator(startHeight, endHeight);

				// 执行回弹动画, 方式二: 自定义Animation
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
	 * 属性动画/值动画
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
				// percent:百分比 0.0 -> 1.0
				Log.d(TAG, "percent:" + percent);
				Integer newHeight = evaluate(percent, startHeight, endHeight);

				mImage.getLayoutParams().height = newHeight;
				mImage.requestLayout();
			}
		});
		// 设置动画的加速度
		mValueAnimator.setInterpolator(new OvershootInterpolator());
		// 设置动画执行时长
		mValueAnimator.setDuration(500);
		mValueAnimator.start();
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
