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
 * @����:com.chj.tencent.drag
 * @����:DragLayout
 * @����:�»��
 * @ʱ��:2015-8-19 ����10:51:46
 * 
 * @����:�໬���
 * 
 * @SVN�汾��:$Rev$
 * @������:$Author$
 * @��������:TODO
 * 
 */
public class DragLayout extends FrameLayout {
	protected static final String TAG = "DragLayout";

	private ViewDragHelper mDragHelper;
	private ViewGroup mLeftContent;
	private ViewGroup mMainContent;

	private int mWidth; // ��Ļ�Ŀ��
	private int mHeight; // ��Ļ�ĸ߶�
	private int mRange; // �ƶ��ķ�Χ

	private OnDragStatusChangeListener mListener;// ��������
	private Status mStatus = Status.Close;// Ĭ��״̬

	/**
	 * ״̬ö��
	 */
	public static enum Status {
		Close, Open, Drag
	}

	/**
	 * �Զ�����ק״̬�仯�����ӿ�
	 */
	public interface OnDragStatusChangeListener {

		/**
		 * �ر�
		 */
		void onClose();

		/**
		 * ����
		 */
		void onOpen();

		/**
		 * ��ק
		 * 
		 * @param percent�ٷֱ�
		 */
		void onDrag(float percent);
	}

	/**
	 * ��ȡ״̬
	 * 
	 * @return
	 */
	public Status getStatus() {
		return mStatus;
	}

	/**
	 * ����״̬
	 * 
	 * @param mStatus
	 */
	public void setStatus(Status mStatus) {
		this.mStatus = mStatus;
	}

	/**
	 * ������ק״̬����
	 */
	public void setDragStatusListener(OnDragStatusChangeListener mListener) {
		this.mListener = mListener;
	}

	public DragLayout(Context context) {
		this(context, null);// ����д��

		// init();
	}

	public DragLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);// ����д��

		// init();
	}

	public DragLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init();

	}

	/**
	 * ��ʼ��
	 */
	private void init() {
		// a.��ʼ������(ͨ����̬����)
		mDragHelper = ViewDragHelper.create(this, mCallBack);
	}

	/**
	 * c. ��д�ص��¼�
	 */

	ViewDragHelper.Callback mCallBack = new ViewDragHelper.Callback() {

		/**
		 * 1.���ݷ��ؽ��������ǰchild�Ƿ������ק
		 * 
		 * @child����ǰ����ק��view
		 * @pointerId�����ֶ�㴥����id
		 */
		@Override
		public boolean tryCaptureView(View child, int pointerId) {
			// Log.d(TAG, "tryCaptureView:"
			// + child);
			return true;
		}

		/**
		 * ��capturedChild������ʱ,����.
		 */
		@Override
		public void onViewCaptured(View capturedChild, int activePointerId) {
			// Log.d(TAG, "onViewCaptured:"
			// + capturedChild);

			super.onViewCaptured(capturedChild, activePointerId);
		}

		/**
		 * ������ק�ķ�Χ,������ק��������������. -->���������˶���ִ���ٶ�
		 */
		@Override
		public int getViewHorizontalDragRange(View child) {
			return mRange;
		}

		/**
		 * 2. ���ݽ���ֵ ������Ҫ�ƶ�����(����)λ�� (��Ҫ). -->��ʱû�з����������ƶ�
		 * 
		 * @child ��ǰ��ק��View
		 * @left �µ�λ�õĽ���ֵ
		 * @dx λ�ñ仯��
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
		 * 3. ��Viewλ�øı��ʱ��,����Ҫ�������� (����״̬,���涯�� ,�ػ����).-->��ʱ,View�Ѿ�������λ�õĸı�
		 * 
		 * @changedView �ı�λ�õ�View
		 * @left �µ����ֵ
		 * @dx ˮƽ����仯��
		 */
		@Override
		public void onViewPositionChanged(View changedView, int left, int top,
				int dx, int dy) {
			super.onViewPositionChanged(changedView, left, top, dx, dy);
			// Log.d(TAG,"onViewPositionChanged:"+
			// "left:" + left + "dx:" + dx);

			int newLeft = left;
			if (changedView == mLeftContent) {
				// �ѵ�ǰ�仯�����ݸ�mMainContent
				newLeft = mMainContent.getLeft() + dx;
			}

			// ��������
			newLeft = fixLeft(newLeft);

			if (changedView == mLeftContent) {
				// ��������ƶ�֮��,��ǿ�ƷŻ�ȥ.
				mLeftContent.layout(0, 0, 0 + mWidth, 0 + mHeight);
				mMainContent.layout(newLeft, 0, newLeft + mWidth, 0 + mHeight);
			}

			// TODO:����״̬,ִ�ж���
			dispatchDragEvent(newLeft);

			// Ϊ�˼��ݵͰ汾,ÿ���޸�ֵ֮��,�����ػ�
			invalidate();
		}

		/**
		 * 4. ��View���ͷŵ�ʱ��, ���������(ִ�ж���)
		 * 
		 * @releasedChild ���ͷŵ���View
		 * @xvel ˮƽ������ٶ�, ����Ϊ+
		 * @yvel ��ֱ������ٶ�, ����Ϊ+
		 */

		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			// Log.d(TAG, "onViewReleased:"
			// + "xvel:" + xvel + " yvel:" +
			// yvel);
			super.onViewReleased(releasedChild, xvel, yvel);

			// �ж�ִ�� �ر�/����
			// �ȿ������п��������,ʣ�µľͶ��ǹرյ����
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
	 * ���ݷ�Χ�������ֵ
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
	 * ����״̬,ִ�ж���
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

		// TODO:����״̬, ִ�лص�
		Status preStatus = mStatus;// ��һ�ε�״̬
		mStatus = updateStatus(percent);// ��ǰ��״̬
		if (mStatus != preStatus) {
			// ״̬�����仯
			if (mStatus == Status.Close) {
				// ��ǰ��Ϊ�ر�״̬
				if (mListener != null) {
					mListener.onClose();
				}
			} else if (mStatus == Status.Open) {
				if (mListener != null) {
					mListener.onOpen();
				}
			}
		}

		// TODO:���涯��
		animViews(percent);
	}

	/**
	 * ״̬����
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
	 * ���涯��
	 * 
	 * @param percent
	 */
	private void animViews(float percent) {
		// 1.�����: ���Ŷ���, ƽ�ƶ���, ͸���ȶ���
		// ���Ŷ��� :0.0 -> 1.0 >>> 0.5f -> 1.0f >>> 0.5f * percent + 0.5f
		// mLeftContent.setScaleX(0.5f + 0.5f * percent);
		// mLeftContent.setScaleY(0.5f + 0.5f * percent);
		// �������Լ�д�ģ����������ñ��˵�jar��д��,����һ��
		ViewHelper.setScaleX(mLeftContent, evaluate(percent, 0.5f, 1.0f));
		ViewHelper.setScaleY(mLeftContent, 0.5f + 0.5f * percent);
		// ƽ�ƶ���:-mWidth / 2.0f -> 0.0f
		ViewHelper.setTranslationX(mLeftContent,
				evaluate(percent, -mWidth / 2.0f, 0));
		// ͸���ȶ���:0.5f -> 1.0f
		ViewHelper.setAlpha(mLeftContent, evaluate(percent, 0.5f, 1.0f));

		// 2.�����: ���Ŷ���
		// ���Ŷ���:1.0f -> 0.8f
		ViewHelper.setScaleX(mMainContent, evaluate(percent, 1.0f, 0.8f));
		ViewHelper.setScaleY(mMainContent, evaluate(percent, 1.0f, 0.8f));

		// 3.��������: ���ȱ仯 (��ɫ�仯)
		getBackground()
				.setColorFilter(
						(Integer) evaluateColor(percent, Color.BLACK,
								Color.TRANSPARENT), Mode.SRC_OVER);
	}

	/**
	 * ��ֵ��
	 * 
	 * @param fraction�ٷֱ�
	 * @param startValue��ʼֵ
	 * @param endValue����ֵ
	 * @return
	 */
	public Float evaluate(float fraction, Number startValue, Number endValue) {
		float startFloat = startValue.floatValue();
		return startFloat + fraction * (endValue.floatValue() - startFloat);
	}

	/**
	 * ��ɫ�仯����
	 * 
	 * @param fraction�ٷֱ�
	 * @param startValue��ʼֵ
	 * @param endValue����ֵ
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
	 * ��.����ƽ������ (��Ƶ�ʵ���)
	 */
	@Override
	public void computeScroll() {
		super.computeScroll();

		if (mDragHelper.continueSettling(true)) {
			// �������true, ��������Ҫ����ִ��
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	/**
	 * ��дclose����
	 */
	public void close() {
		close(true);
	}

	/**
	 * �ر�
	 */
	public void close(boolean isSmooth) {
		int finalLeft = 0;
		if (isSmooth) {
			// ��. ����һ��ƽ������
			if (mDragHelper.smoothSlideViewTo(mMainContent, finalLeft, 0)) {
				// ����true����û���ƶ���ָ��λ��, ��Ҫˢ�½���.
				// ������this(child���ڵ�ViewGroup)
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
	 * ����
	 */
	public void open(boolean isSmooth) {
		int finalLeft = mRange;
		if (isSmooth) {
			// ��. ����һ��ƽ������
			if (mDragHelper.smoothSlideViewTo(mMainContent, finalLeft, 0)) {
				// ����true����û���ƶ���ָ��λ��, ��Ҫˢ�½���.
				// ������this(child���ڵ�ViewGroup)
				ViewCompat.postInvalidateOnAnimation(this);
			}
		} else {
			mMainContent.layout(finalLeft, 0, finalLeft + mWidth, 0 + mHeight);
		}
	}

	// b.���ݴ����¼�
	/**
	 * ���������¼�
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// ���ݸ�mDragHelper
		return mDragHelper.shouldInterceptTouchEvent(ev);
	}

	/**
	 * �����¼�
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		try {
			mDragHelper.processTouchEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// true����,���������¼�
		return true;
	}

	/**
	 * �ݴ��Լ�飨����������view����view������ViewGroup�����ࣩ
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		if (getChildCount() < 2) {
			throw new IllegalStateException(
					"����������������.Your ViewGroup at least two children");
		}
		if (!(getChildAt(0) instanceof ViewGroup && getChildAt(1) instanceof ViewGroup)) {
			throw new IllegalArgumentException("��view������ViewGroup������");
		}

		mLeftContent = (ViewGroup) getChildAt(0);
		mMainContent = (ViewGroup) getChildAt(1);
	}

	/**
	 * ���ߴ��б仯��ʱ�����
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mWidth = getMeasuredWidth();
		mHeight = getMeasuredHeight();

		// �ƶ��ķ�Χ
		mRange = (int) (mWidth * 0.6f);

		super.onSizeChanged(w, h, oldw, oldh);
	}

}
