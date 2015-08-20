package com.chj.tencent.index;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * ��������
 * 
 * ���ڸ�����ĸ���ٶ�λ��ϵ��
 * 
 * @author poplar
 */
public class QuickIndexBar extends View {

	private static final String TAG = "QuickIndexBar";

	private static final String[] LETTERS = new String[]{"A", "B", "C", "D",
			"E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
			"R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

	private Paint mPaint;

	private int cellWidth;// ��Ԫ��Ŀ�

	private float cellHeight;// ��Ԫ��ĸ�

	int touchIndex = -1;// ����һ�����

	public QuickIndexBar(Context context) {
		this(context, null);
	}

	public QuickIndexBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public QuickIndexBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// ��������
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		// ���û�����ʾ
		mPaint.setColor(Color.WHITE);
		// ���û�����ʽ
		mPaint.setTypeface(Typeface.DEFAULT_BOLD);
	}

	/**
	 * ����
	 */
	@Override
	protected void onDraw(Canvas canvas) {

		for (int i = 0; i < LETTERS.length; i++) {
			String text = LETTERS[i];
			// ��������
			int x = (int) (cellWidth / 2.0f - mPaint.measureText(text) / 2.0f);
			// ��ȡ�ı��ĸ߶�
			Rect bounds = new Rect();// ����
			mPaint.getTextBounds(text, 0, text.length(), bounds);
			int textHeight = bounds.height();
			int y = (int) (cellHeight / 2.0f + textHeight / 2.0f + i
					* cellHeight);

			// ���ݰ��µ���ĸ, ���û�����ɫ
			mPaint.setColor(touchIndex == i ? Color.GRAY : Color.WHITE);

			// �����ı�A-Z
			canvas.drawText(text, x, y, mPaint);
		}
	}

	/**
	 * �������¼�
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int index = -1;
		switch (MotionEventCompat.getActionMasked(event)) {
			case MotionEvent.ACTION_DOWN :// ����
				// ��ȡ��ǰ����������ĸ����
				index = (int) (event.getY() / cellHeight);
				if (index >= 0 && index < LETTERS.length) {
					// �ж��Ƿ����һ�δ�������һ��
					if (index != touchIndex) {
						if (listener != null) {
							listener.onLetterUpdate(LETTERS[index]);
						}
						Log.d(TAG, "onTouchEvent: " + LETTERS[index]);
						touchIndex = index;
					}
				}
				break;
			case MotionEvent.ACTION_MOVE :// �ƶ�
				index = (int) (event.getY() / cellHeight);
				if (index >= 0 && index < LETTERS.length) {
					// �ж��Ƿ����һ�δ�������һ��
					if (index != touchIndex) {
						if (listener != null) {
							listener.onLetterUpdate(LETTERS[index]);
						}
						Log.d(TAG, "onTouchEvent: " + LETTERS[index]);
						touchIndex = index;
					}
				}
				break;
			case MotionEvent.ACTION_UP :// �ɿ�
				touchIndex = -1;
				break;
			default :
				break;
		}
		invalidate();// �������µ���draw()

		return true;
	}

	/**
	 * ���ߴ緢���仯ʱ���η�������
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		// ��ȡ��Ԫ��Ŀ�͸�
		cellWidth = getMeasuredWidth();
		int mHeight = getMeasuredHeight();
		cellHeight = mHeight * 1.0f / LETTERS.length;
	}

	/**
	 * ��¶һ����ĸ�ļ���
	 */
	public interface OnLetterUpdateListener {
		void onLetterUpdate(String letter);
	}

	// ʵ����һ����������
	private OnLetterUpdateListener listener;

	/**
	 * ��ȡ��������
	 * 
	 * @return
	 */
	public OnLetterUpdateListener getListener() {
		return listener;
	}
	/**
	 * ������ĸ���¼���
	 * 
	 * @param listener
	 */
	public void setListener(OnLetterUpdateListener listener) {
		this.listener = listener;
	}
}
