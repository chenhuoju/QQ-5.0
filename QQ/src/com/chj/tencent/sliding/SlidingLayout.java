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
 * @����:com.chj.tencent.sliding
 * @����:SlidingLayout
 * @����:�»��
 * @ʱ��:2015-8-20 ����9:06:15
 * 
 * @����:����ɾ���ؼ�
 * 
 * @SVN�汾��:$Rev$
 * @������:$Author$
 * @��������:TODO
 * 
 */
public class SlidingLayout extends FrameLayout {

	private ViewDragHelper mDragHelper;

	private View mFrontView;// ����ǰview
	private View mBackView;// ������view

	private int mHeight;// ����ǰ�ĸ� ��
	private int mWidth;// ����ǰ�Ŀ��
	private int mRange;// ������Χ�Ŀ��

	private Status mStatus = Status.Close;// Ĭ��״̬
	private onSlidingLayoutListener mSlidingListener;// �����ؼ���������

	/**
	 * ״̬ö��
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

		// a.��ʼ��ViewDragHelper
		// sensitivity��������
		mDragHelper = ViewDragHelper.create(this, 1.0f, mCallback);
	}
	/**
	 * �ص�����
	 */
	ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {

		// c.��д����

		/**
		 * �Ƿ񲶻�view
		 * 
		 * @true������view
		 * @false��������view
		 */
		@Override
		public boolean tryCaptureView(View child, int pointerId) {
			return true;
		}

		/**
		 * �޶��ƶ���Χ,viewλ��ˮƽ������ƶ���Χ
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
		 * ��viewλ�øı�ʱ���˷�������
		 */
		public void onViewPositionChanged(View changedView, int left, int top,
				int dx, int dy) {
			// �����¼�
			if (changedView == mFrontView) {
				mBackView.offsetLeftAndRight(dx);
			} else if (changedView == mBackView) {
				mFrontView.offsetLeftAndRight(dx);
			}

			// �ַ��¼�
			dispatchSlidingEvent();

			// �����ϰ汾
			invalidate();

		};

		/**
		 * ��view�ͷ�ʱ���˷�������
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
	 * ��������ַ��¼�����
	 */
	protected void dispatchSlidingEvent() {

		if (mSlidingListener != null) {
			mSlidingListener.onDraging(this);
		}

		// ��¼��һ�ε�״̬
		Status preStatus = mStatus;
		// ���µ�ǰ״̬
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
	 * ״̬����
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
	 * �رշ���
	 */
	public void close() {
		Utils.showToast(getContext(), "Close");
		close(true);
	}

	/**
	 * ��д�رշ���,ʵ��ƽ��Ч��
	 */
	public void close(boolean isSmooth) {
		int finalLeft = 0;
		if (isSmooth) {
			// ��ʼ����
			if (mDragHelper.smoothSlideViewTo(mFrontView, finalLeft, 0)) {
				ViewCompat.postInvalidateOnAnimation(this);// �ػ涯��
			}
		} else {
			layoutContent(false);
		}
	}

	/**
	 * �رշ���
	 */
	public void open() {
		Utils.showToast(getContext(), "Open");
		open(true);
	}

	/**
	 * ��д�رշ���,ʵ��ƽ��Ч��
	 */
	public void open(boolean isSmooth) {
		int finalLeft = -mRange;
		if (isSmooth) {
			// ��ʼ����
			if (mDragHelper.smoothSlideViewTo(mFrontView, finalLeft, 0)) {
				ViewCompat.postInvalidateOnAnimation(this);// �ػ涯��
			}
		} else {
			layoutContent(true);
		}
	}

	/**
	 * �������ֵ
	 */
	@Override
	public void computeScroll() {
		super.computeScroll();

		if (mDragHelper.continueSettling(true)) {
			ViewCompat.postInvalidateOnAnimation(this);// �ػ涯��
		}
	}

	// b. ���ݴ����¼�
	/**
	 * ���������¼�
	 */
	@Override
	public boolean onInterceptTouchEvent(android.view.MotionEvent ev) {
		// ���ݸ�mDragHelper
		return mDragHelper.shouldInterceptTouchEvent(ev);
	}

	/**
	 * �������¼�
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
	 * ��д���ַ���
	 */
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		// �ڷ�λ��
		layoutContent(false);
	};

	/**
	 * ��������,layout�ڷ�λ��
	 * 
	 * @param isOpen
	 */
	private void layoutContent(boolean isOpen) {
		// �ڷ�ǰView
		Rect frontRect = computeFrontViewRect(isOpen);
		mFrontView.layout(frontRect.left, frontRect.top, frontRect.right,
				frontRect.bottom);

		// �ڷź�View
		Rect backRect = computeBackViewRect(frontRect);
		mBackView.layout(backRect.left, backRect.top, backRect.right,
				backRect.bottom);

		// ����˳��, ��mFrontViewǰ��
		bringChildToFront(mFrontView);
	}

	/**
	 * ���������view�ľ���
	 * 
	 * @param frontrect
	 * @return
	 */
	private Rect computeBackViewRect(Rect frontrect) {
		int left = frontrect.right;
		return new Rect(left, 0, left + mRange, 0 + mHeight);
	}

	/**
	 * �������ǰview�ľ���
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
	 * ��xml��������ʱ����
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		mBackView = getChildAt(0);
		mFrontView = getChildAt(1);
	}

	/**
	 * ���ߴ緢���仯ʱ���˷�������
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		mHeight = mFrontView.getMeasuredHeight();
		mWidth = mFrontView.getMeasuredWidth();

		mRange = mBackView.getMeasuredWidth();
	}

	/**
	 * �����ؼ������ӿ�
	 */
	public static interface onSlidingLayoutListener {

		/**
		 * �رշ���
		 */
		void onClose(SlidingLayout slidingLayout);

		/**
		 * ��������
		 */
		void onOpen(SlidingLayout slidingLayout);

		/**
		 * ��ק����
		 */
		void onDraging(SlidingLayout slidingLayout);

		/**
		 * Ҫȥ�ر�
		 */
		void onStartClose(SlidingLayout slidingLayout);

		/**
		 * Ҫȥ����
		 */
		void onStartOpen(SlidingLayout slidingLayout);
	}

	/* ������mStatus��mSlidingListener��set��get���� */
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
