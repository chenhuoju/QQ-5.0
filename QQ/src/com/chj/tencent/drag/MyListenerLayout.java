package com.chj.tencent.drag;

import com.chj.tencent.drag.DragLayout.Status;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * @包名:com.chj.tencent.drag
 * @类名:MyListenerLayout
 * @作者:陈火炬
 * @时间:2015-8-20 上午9:20:57
 * 
 * @描述:自定义LinearLayout布局,触摸优化-->重写ViewGroup里面的onInterceptTouchEvent和onTouchEvent
 * 
 * @SVN版本号:$Rev$
 * @更新人:$Author$
 * @更新描述:TODO
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
	 * 设置拖拽布局
	 * 
	 * @param mDragLayout
	 */
	public void setDraglayout(DragLayout mDragLayout) {
		this.mDragLayout = mDragLayout;
	}

	/**
	 * 处理拦截事件
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// 如果当前是关闭状态, 按之前方法判断
		if (mDragLayout.getStatus() == Status.Close) {
			return super.onInterceptTouchEvent(ev);
		} else {
			return true;
		}
	}

	/**
	 * 处理触摸事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 如果当前是关闭状态, 按之前方法处理
		if (mDragLayout.getStatus() == Status.Close) {
			return super.onTouchEvent(event);
		} else {
			// 手指抬起, 执行关闭操作
			if (event.getAction() == MotionEvent.ACTION_UP) {
				mDragLayout.close();
			}
			return true;
		}
	}

}
