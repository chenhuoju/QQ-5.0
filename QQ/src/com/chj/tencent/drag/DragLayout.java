package com.chj.tencent.drag;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.nineoldandroids.view.ViewHelper;

/**
 * @包名:com.chj.tencent.drag
 * @类名:DragLayout
 * @作者:陈火炬
 * @时间:2015-8-19 上午10:51:46
 * 
 * @描述:侧滑面板
 * 
 * @SVN版本号:$Rev$
 * @更新人:$Author$
 * @更新描述:TODO
 * 
 */
public class DragLayout extends FrameLayout {
	protected static final String TAG = "DragLayout";

	private ViewDragHelper mDragHelper;
	private ViewGroup mLeftContent;
	private ViewGroup mMainContent;

	private int mWidth; // 屏幕的宽度
	private int mHeight; // 屏幕的高度
	private int mRange; // 移动的范围

	private OnDragStatusChangeListener mListener;// 监听对象
	private Status mStatus = Status.Close;// 默认状态

	/**
	 * 状态枚举
	 */
	public static enum Status {
		Close, Open, Drag
	}

	/**
	 * 自定义拖拽状态变化监听接口
	 */
	public interface OnDragStatusChangeListener {

		/**
		 * 关闭
		 */
		void onClose();

		/**
		 * 开启
		 */
		void onOpen();

		/**
		 * 拖拽
		 * 
		 * @param percent百分比
		 */
		void onDrag(float percent);
	}

	/**
	 * 获取状态
	 * 
	 * @return
	 */
	public Status getStatus() {
		return mStatus;
	}

	/**
	 * 设置状态
	 * 
	 * @param mStatus
	 */
	public void setStatus(Status mStatus) {
		this.mStatus = mStatus;
	}

	/**
	 * 设置拖拽状态监听
	 */
	public void setDragStatusListener(OnDragStatusChangeListener mListener) {
		this.mListener = mListener;
	}

	public DragLayout(Context context) {
		this(context, null);// 其他写法

		// init();
	}

	public DragLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);// 其他写法

		// init();
	}

	public DragLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init();

	}

	/**
	 * 初始化
	 */
	private void init() {
		// a.初始化操作(通过静态方法)
		mDragHelper = ViewDragHelper.create(this, mCallBack);
	}

	/**
	 * c. 重写回调事件
	 */

	ViewDragHelper.Callback mCallBack = new ViewDragHelper.Callback() {

		/**
		 * 1.根据返回结果决定当前child是否可以拖拽
		 * 
		 * @child：当前被拖拽的view
		 * @pointerId：区分多点触摸的id
		 */
		@Override
		public boolean tryCaptureView(View child, int pointerId) {
			// Log.d(TAG, "tryCaptureView:"
			// + child);
			return true;
		}

		/**
		 * 当capturedChild被捕获时,调用.
		 */
		@Override
		public void onViewCaptured(View capturedChild, int activePointerId) {
			// Log.d(TAG, "onViewCaptured:"
			// + capturedChild);

			super.onViewCaptured(capturedChild, activePointerId);
		}

		/**
		 * 返回拖拽的范围,不对拖拽进行真正的限制. -->仅仅决定了动画执行速度
		 */
		@Override
		public int getViewHorizontalDragRange(View child) {
			return mRange;
		}

		/**
		 * 2. 根据建议值 修正将要移动到的(横向)位置 (重要). -->此时没有发生真正的移动
		 * 
		 * @child 当前拖拽的View
		 * @left 新的位置的建议值
		 * @dx 位置变化量
		 * @left = oldLeft + dx;
		 */
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			// Log.d(TAG,
			// "clampViewPositionHorizontal:"
			// + "oldLeft:"
			// + child.getLeft() + " dx:" +
			// dx + " left:" + left);

			if (child == mMainContent) {
				left = fixLeft(left);
			}
			return left;
		}

		/**
		 * 3. 当View位置改变的时候,处理要做的事情 (更新状态,伴随动画 ,重绘界面).-->此时,View已经发生了位置的改变
		 * 
		 * @changedView 改变位置的View
		 * @left 新的左边值
		 * @dx 水平方向变化量
		 */
		@Override
		public void onViewPositionChanged(View changedView, int left, int top,
				int dx, int dy) {
			super.onViewPositionChanged(changedView, left, top, dx, dy);
			// Log.d(TAG,"onViewPositionChanged:"+
			// "left:" + left + "dx:" + dx);

			int newLeft = left;
			if (changedView == mLeftContent) {
				// 把当前变化量传递给mMainContent
				newLeft = mMainContent.getLeft() + dx;
			}

			// 进行修正
			newLeft = fixLeft(newLeft);

			if (changedView == mLeftContent) {
				// 当左面板移动之后,再强制放回去.
				mLeftContent.layout(0, 0, 0 + mWidth, 0 + mHeight);
				mMainContent.layout(newLeft, 0, newLeft + mWidth, 0 + mHeight);
			}

			// TODO:更新状态,执行动画
			dispatchDragEvent(newLeft);

			// 为了兼容低版本,每次修改值之后,进行重绘
			invalidate();
		}

		/**
		 * 4. 当View被释放的时候, 处理的事情(执行动画)
		 * 
		 * @releasedChild 被释放的子View
		 * @xvel 水平方向的速度, 向右为+
		 * @yvel 竖直方向的速度, 向下为+
		 */

		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			// Log.d(TAG, "onViewReleased:"
			// + "xvel:" + xvel + " yvel:" +
			// yvel);
			super.onViewReleased(releasedChild, xvel, yvel);

			// 判断执行 关闭/开启
			// 先考虑所有开启的情况,剩下的就都是关闭的情况
			if (xvel == 0 && mMainContent.getLeft() > mRange / 2.0f) {
				open();
			} else if (xvel > 0) {
				open();
			} else {
				close();
			}
		}

		@Override
		public void onViewDragStateChanged(int state) {
			super.onViewDragStateChanged(state);
		}

	};

	/**
	 * 根据范围修正左边值
	 * 
	 * @param left
	 * @return
	 */
	private int fixLeft(int left) {
		if (left < 0) {
			return 0;
		} else if (left > mRange) {
			return mRange;
		}
		return left;
	}

	/**
	 * 更新状态,执行动画
	 * 
	 * @param newLeft
	 */
	protected void dispatchDragEvent(int newLeft) {
		float percent = newLeft * 1.0f / mRange;
		// 0.0f -> 1.0f
		// Log.d(TAG, "percent: " + percent);

		if (mListener != null) {
			mListener.onDrag(percent);
		}

		// TODO:更新状态, 执行回调
		Status preStatus = mStatus;// 上一次的状态
		mStatus = updateStatus(percent);// 当前的状态
		if (mStatus != preStatus) {
			// 状态发生变化
			if (mStatus == Status.Close) {
				// 当前变为关闭状态
				if (mListener != null) {
					mListener.onClose();
				}
			} else if (mStatus == Status.Open) {
				if (mListener != null) {
					mListener.onOpen();
				}
			}
		}

		// TODO:伴随动画
		animViews(percent);
	}

	/**
	 * 状态更新
	 * 
	 * @param percent
	 * @return
	 */
	private Status updateStatus(float percent) {
		if (percent == 0f) {
			return Status.Close;
		} else if (percent == 1.0f) {
			return Status.Open;
		}
		return Status.Drag;

	}

	/**
	 * 伴随动画
	 * 
	 * @param percent
	 */
	private void animViews(float percent) {
		// 1.左面板: 缩放动画, 平移动画, 透明度动画
		// 缩放动画 :0.0 -> 1.0 >>> 0.5f -> 1.0f >>> 0.5f * percent + 0.5f
		// mLeftContent.setScaleX(0.5f + 0.5f * percent);
		// mLeftContent.setScaleY(0.5f + 0.5f * percent);
		// 上面是自己写的，下面是利用别人的jar包写的,功能一样
		ViewHelper.setScaleX(mLeftContent, evaluate(percent, 0.5f, 1.0f));
		ViewHelper.setScaleY(mLeftContent, 0.5f + 0.5f * percent);
		// 平移动画:-mWidth / 2.0f -> 0.0f
		ViewHelper.setTranslationX(mLeftContent,
				evaluate(percent, -mWidth / 2.0f, 0));
		// 透明度动画:0.5f -> 1.0f
		ViewHelper.setAlpha(mLeftContent, evaluate(percent, 0.5f, 1.0f));

		// 2.主面板: 缩放动画
		// 缩放动画:1.0f -> 0.8f
		ViewHelper.setScaleX(mMainContent, evaluate(percent, 1.0f, 0.8f));
		ViewHelper.setScaleY(mMainContent, evaluate(percent, 1.0f, 0.8f));

		// 3.背景动画: 亮度变化 (颜色变化)
		getBackground()
				.setColorFilter(
						(Integer) evaluateColor(percent, Color.BLACK,
								Color.TRANSPARENT), Mode.SRC_OVER);
	}

	/**
	 * 估值器
	 * 
	 * @param fraction百分比
	 * @param startValue初始值
	 * @param endValue结束值
	 * @return
	 */
	public Float evaluate(float fraction, Number startValue, Number endValue) {
		float startFloat = startValue.floatValue();
		return startFloat + fraction * (endValue.floatValue() - startFloat);
	}

	/**
	 * 颜色变化过度
	 * 
	 * @param fraction百分比
	 * @param startValue初始值
	 * @param endValue结束值
	 * @return
	 */
	public Object evaluateColor(float fraction, Object startValue,
			Object endValue) {
		int startInt = (Integer) startValue;
		int startA = (startInt >> 24) & 0xff;
		int startR = (startInt >> 16) & 0xff;
		int startG = (startInt >> 8) & 0xff;
		int startB = startInt & 0xff;

		int endInt = (Integer) endValue;
		int endA = (endInt >> 24) & 0xff;
		int endR = (endInt >> 16) & 0xff;
		int endG = (endInt >> 8) & 0xff;
		int endB = endInt & 0xff;

		return (int) ((startA + (int) (fraction * (endA - startA))) << 24)
				| (int) ((startR + (int) (fraction * (endR - startR))) << 16)
				| (int) ((startG + (int) (fraction * (endG - startG))) << 8)
				| (int) ((startB + (int) (fraction * (endB - startB))));
	}

	/**
	 * Ⅱ.持续平滑动画 (高频率调用)
	 */
	@Override
	public void computeScroll() {
		super.computeScroll();

		if (mDragHelper.continueSettling(true)) {
			// 如果返回true, 动画还需要继续执行
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	/**
	 * 重写close方法
	 */
	public void close() {
		close(true);
	}

	/**
	 * 关闭
	 */
	public void close(boolean isSmooth) {
		int finalLeft = 0;
		if (isSmooth) {
			// Ⅰ. 触发一个平滑动画
			if (mDragHelper.smoothSlideViewTo(mMainContent, finalLeft, 0)) {
				// 返回true代表还没有移动到指定位置, 需要刷新界面.
				// 参数传this(child所在的ViewGroup)
				ViewCompat.postInvalidateOnAnimation(this);
			}
		} else {
			mMainContent.layout(finalLeft, 0, finalLeft + mWidth, 0 + mHeight);
		}
	}

	public void open() {
		open(true);
	}

	/**
	 * 开启
	 */
	public void open(boolean isSmooth) {
		int finalLeft = mRange;
		if (isSmooth) {
			// Ⅰ. 触发一个平滑动画
			if (mDragHelper.smoothSlideViewTo(mMainContent, finalLeft, 0)) {
				// 返回true代表还没有移动到指定位置, 需要刷新界面.
				// 参数传this(child所在的ViewGroup)
				ViewCompat.postInvalidateOnAnimation(this);
			}
		} else {
			mMainContent.layout(finalLeft, 0, finalLeft + mWidth, 0 + mHeight);
		}
	}

	// b.传递触摸事件
	/**
	 * 处理拦截事件
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// 传递给mDragHelper
		return mDragHelper.shouldInterceptTouchEvent(ev);
	}

	/**
	 * 处理事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		try {
			mDragHelper.processTouchEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// true返回,持续接收事件
		return true;
	}

	/**
	 * 容错性检查（至少有两子view，子view必须是ViewGroup的子类）
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		if (getChildCount() < 2) {
			throw new IllegalStateException(
					"布局至少有两孩子.Your ViewGroup at least two children");
		}
		if (!(getChildAt(0) instanceof ViewGroup && getChildAt(1) instanceof ViewGroup)) {
			throw new IllegalArgumentException("子view必须是ViewGroup的子类");
		}

		mLeftContent = (ViewGroup) getChildAt(0);
		mMainContent = (ViewGroup) getChildAt(1);
	}

	/**
	 * 当尺寸有变化的时候调用
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mWidth = getMeasuredWidth();
		mHeight = getMeasuredHeight();

		// 移动的范围
		mRange = (int) (mWidth * 0.6f);

		super.onSizeChanged(w, h, oldw, oldh);
	}

}
