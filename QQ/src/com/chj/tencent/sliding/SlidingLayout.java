package com.chj.tencent.sliding;

import com.chj.tencent.util.Utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @包名:com.chj.tencent.sliding
 * @类名:SlidingLayout
 * @作者:陈火炬
 * @时间:2015-8-20 下午9:06:15
 * 
 * @描述:侧拉删除控件
 * 
 * @SVN版本号:$Rev$
 * @更新人:$Author$
 * @更新描述:TODO
 * 
 */
public class SlidingLayout extends FrameLayout {

	private ViewDragHelper mDragHelper;

	private View mFrontView;// 侧拉前view
	private View mBackView;// 侧拉后view

	private int mHeight;// 侧拉前的高 度
	private int mWidth;// 侧拉前的宽度
	private int mRange;// 侧拉范围的宽度

	private Status mStatus = Status.Close;// 默认状态
	private onSlidingLayoutListener mSlidingListener;// 侧拉控件监听对象

	/**
	 * 状态枚举
	 */
	public static enum Status {
		Close, Open, Draging
	}

	public SlidingLayout(Context context) {
		this(context, null);
	}

	public SlidingLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlidingLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// a.初始化ViewDragHelper
		// sensitivity：灵敏度
		mDragHelper = ViewDragHelper.create(this, 1.0f, mCallback);
	}
	/**
	 * 回调方法
	 */
	ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {

		// c.重写监听

		/**
		 * 是否捕获view
		 * 
		 * @true：捕获view
		 * @false：不捕获view
		 */
		@Override
		public boolean tryCaptureView(View child, int pointerId) {
			return true;
		}

		/**
		 * 限定移动范围,view位置水平方向的移动范围
		 */
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			// left
			if (child == mFrontView) {
				if (left > 0) {
					return 0;
				} else if (left < -mRange) {
					return -mRange;
				}
			} else if (child == mBackView) {
				if (left > mWidth) {
					return mWidth;
				} else if (left < mWidth - mRange) {
					return mWidth - mRange;
				}
			}
			return left;
		};

		/**
		 * 当view位置改变时，此方法调用
		 */
		public void onViewPositionChanged(View changedView, int left, int top,
				int dx, int dy) {
			// 传递事件
			if (changedView == mFrontView) {
				mBackView.offsetLeftAndRight(dx);
			} else if (changedView == mBackView) {
				mFrontView.offsetLeftAndRight(dx);
			}

			// 分发事件
			dispatchSlidingEvent();

			// 兼容老版本
			invalidate();

		};

		/**
		 * 当view释放时，此方法调用
		 */
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			if (xvel == 0 && mFrontView.getLeft() < -mRange / 2.0f) {
				open();
			} else if (xvel < 0) {
				open();
			} else {
				close();
			}
		};

	};

	/**
	 * 处理侧拉分发事件方法
	 */
	protected void dispatchSlidingEvent() {

		if (mSlidingListener != null) {
			mSlidingListener.onDraging(this);
		}

		// 记录上一次的状态
		Status preStatus = mStatus;
		// 更新当前状态
		mStatus = updateStatus();
		if (preStatus != mStatus && mSlidingListener != null) {
			if (mStatus == Status.Close) {
				mSlidingListener.onClose(this);
			} else if (mStatus == Status.Open) {
				mSlidingListener.onOpen(this);
			} else if (mStatus == Status.Draging) {
				if (preStatus == Status.Close) {
					mSlidingListener.onStartOpen(this);
				} else if (preStatus == Status.Open) {
					mSlidingListener.onStartClose(this);
				}
			}
		}

	}

	/**
	 * 状态更新
	 * 
	 * @return
	 */
	private Status updateStatus() {

		int left = mFrontView.getLeft();
		if (left == 0) {
			return Status.Close;
		} else if (left == -mRange) {
			return Status.Open;
		}
		return Status.Draging;
	}

	/**
	 * 关闭方法
	 */
	public void close() {
		Utils.showToast(getContext(), "Close");
		close(true);
	}

	/**
	 * 重写关闭方法,实现平滑效果
	 */
	public void close(boolean isSmooth) {
		int finalLeft = 0;
		if (isSmooth) {
			// 开始动画
			if (mDragHelper.smoothSlideViewTo(mFrontView, finalLeft, 0)) {
				ViewCompat.postInvalidateOnAnimation(this);// 重绘动画
			}
		} else {
			layoutContent(false);
		}
	}

	/**
	 * 关闭方法
	 */
	public void open() {
		Utils.showToast(getContext(), "Open");
		open(true);
	}

	/**
	 * 重写关闭方法,实现平滑效果
	 */
	public void open(boolean isSmooth) {
		int finalLeft = -mRange;
		if (isSmooth) {
			// 开始动画
			if (mDragHelper.smoothSlideViewTo(mFrontView, finalLeft, 0)) {
				ViewCompat.postInvalidateOnAnimation(this);// 重绘动画
			}
		} else {
			layoutContent(true);
		}
	}

	/**
	 * 计算滚动值
	 */
	@Override
	public void computeScroll() {
		super.computeScroll();

		if (mDragHelper.continueSettling(true)) {
			ViewCompat.postInvalidateOnAnimation(this);// 重绘动画
		}
	}

	// b. 传递触摸事件
	/**
	 * 处理拦截事件
	 */
	@Override
	public boolean onInterceptTouchEvent(android.view.MotionEvent ev) {
		// 传递给mDragHelper
		return mDragHelper.shouldInterceptTouchEvent(ev);
	}

	/**
	 * 处理触摸事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		try {
			mDragHelper.processTouchEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * 重写布局方法
	 */
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		// 摆放位置
		layoutContent(false);
	};

	/**
	 * 布局内容,layout摆放位置
	 * 
	 * @param isOpen
	 */
	private void layoutContent(boolean isOpen) {
		// 摆放前View
		Rect frontRect = computeFrontViewRect(isOpen);
		mFrontView.layout(frontRect.left, frontRect.top, frontRect.right,
				frontRect.bottom);

		// 摆放后View
		Rect backRect = computeBackViewRect(frontRect);
		mBackView.layout(backRect.left, backRect.top, backRect.right,
				backRect.bottom);

		// 调整顺序, 把mFrontView前置
		bringChildToFront(mFrontView);
	}

	/**
	 * 计算侧拉后view的矩形
	 * 
	 * @param frontrect
	 * @return
	 */
	private Rect computeBackViewRect(Rect frontrect) {
		int left = frontrect.right;
		return new Rect(left, 0, left + mRange, 0 + mHeight);
	}

	/**
	 * 计算侧拉前view的矩形
	 * 
	 * @param isOpen
	 * @return
	 */
	private Rect computeFrontViewRect(boolean isOpen) {
		int left = 0;
		if (isOpen) {
			left = -mRange;
		}
		return new Rect(left, 0, left + mWidth, 0 + mHeight);
	}

	/**
	 * 当xml被填充完毕时调用
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		mBackView = getChildAt(0);
		mFrontView = getChildAt(1);
	}

	/**
	 * 当尺寸发生变化时，此方法调用
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		mHeight = mFrontView.getMeasuredHeight();
		mWidth = mFrontView.getMeasuredWidth();

		mRange = mBackView.getMeasuredWidth();
	}

	/**
	 * 侧拉控件监听接口
	 */
	public static interface onSlidingLayoutListener {

		/**
		 * 关闭方法
		 */
		void onClose(SlidingLayout slidingLayout);

		/**
		 * 开启方法
		 */
		void onOpen(SlidingLayout slidingLayout);

		/**
		 * 拖拽方法
		 */
		void onDraging(SlidingLayout slidingLayout);

		/**
		 * 要去关闭
		 */
		void onStartClose(SlidingLayout slidingLayout);

		/**
		 * 要去开启
		 */
		void onStartOpen(SlidingLayout slidingLayout);
	}

	/* 下面是mStatus和mSlidingListener的set和get方法 */
	public Status getStatus() {
		return mStatus;
	}

	public void setStatus(Status mStatus) {
		this.mStatus = mStatus;
	}

	public onSlidingLayoutListener getSlidingListener() {
		return mSlidingListener;
	}

	public void setSlidingListener(onSlidingLayoutListener mSlidingListener) {
		this.mSlidingListener = mSlidingListener;
	}
}
