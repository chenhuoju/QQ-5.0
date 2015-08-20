package com.chj.tencent.drag;

import com.chj.tencent.drag.DragLayout.Status;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * @����:com.chj.tencent.drag
 * @����:MyListenerLayout
 * @����:�»��
 * @ʱ��:2015-8-20 ����9:20:57
 * 
 * @����:�Զ���LinearLayout����,�����Ż�-->��дViewGroup�����onInterceptTouchEvent��onTouchEvent
 * 
 * @SVN�汾��:$Rev$
 * @������:$Author$
 * @��������:TODO
 * 
 */
public class MyListenerLayout extends LinearLayout {

	private DragLayout mDragLayout;

	public MyListenerLayout(Context context) {
		super(context);
	}

	public MyListenerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * ������ק����
	 * 
	 * @param mDragLayout
	 */
	public void setDraglayout(DragLayout mDragLayout) {
		this.mDragLayout = mDragLayout;
	}

	/**
	 * ���������¼�
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// �����ǰ�ǹر�״̬, ��֮ǰ�����ж�
		if (mDragLayout.getStatus() == Status.Close) {
			return super.onInterceptTouchEvent(ev);
		} else {
			return true;
		}
	}

	/**
	 * �������¼�
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// �����ǰ�ǹر�״̬, ��֮ǰ��������
		if (mDragLayout.getStatus() == Status.Close) {
			return super.onTouchEvent(event);
		} else {
			// ��ָ̧��, ִ�йرղ���
			if (event.getAction() == MotionEvent.ACTION_UP) {
				mDragLayout.close();
			}
			return true;
		}
	}

}
