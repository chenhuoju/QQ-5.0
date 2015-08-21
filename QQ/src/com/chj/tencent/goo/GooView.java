package com.chj.tencent.goo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.chj.tencent.util.GeometryUtil;
import com.chj.tencent.util.Utils;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

/**
 * @包名:com.chj.tencent.goo
 * @类名:GooView
 * @作者:陈火炬
 * @时间:2015-8-21 下午3:58:06
 * 
 * @描述:粘性控件
 * 
 * @SVN版本号:$Rev$
 * @更新人:$Author$
 * @更新描述:TODO
 * 
 */
public class GooView extends View {

	private static final String TAG = "GooView";
	private Paint mPaint; // 画笔

	public GooView(Context context) {
		this(context, null);
	}

	public GooView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GooView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// 做初始化操作
		initView();
	}

	/**
	 * 初始化视图
	 */
	private void initView() {

		mPaint = new Paint();
		mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);// 去除锯齿
		mPaint.setColor(Color.RED);// 设置画笔颜色
	}

	// 固定圆的坐标
	PointF[] mStickPoints = new PointF[]{new PointF(250f, 250f), new PointF(250f, 350f)};
	// 拖拽圆的坐标
	PointF[] mDragPoints = new PointF[]{new PointF(50f, 250f), new PointF(50f, 350f)};
	// 控制点的坐标
	PointF mControlPoint = new PointF(150f, 300f);

	PointF mDragCenter = new PointF(80f, 80f); // 拖拽圆的圆心
	float mDragRadius = 14f; // 拖拽圆的半径

	PointF mStickCenter = new PointF(150f, 150f); // 固定圆的圆心
	float mStickRadius = 12f; // 固定圆的半径

	private int statusBarHeight;// 状态栏高度
	float farestDistance = 80f;// 最远距离
	private boolean isOutofRange;// 是否超出范围
	private boolean isDisappear;// 是否消失

	/**
	 * 绘制方法
	 */
	@Override
	protected void onDraw(Canvas canvas) {

		// 计算连接点值, 控制点, 固定圆半径
			// 1. 获取固定圆半径(根据两圆圆心距离)
			float tempStickRadius = getTempStickRadius();// 临时半径
	
			// 2. 获取直线与圆的交点
			float yOffset = mStickCenter.y - mDragCenter.y;
			float xOffset = mStickCenter.x - mDragCenter.x;
			Double lineK = null;
			if (xOffset != 0) {
				lineK = (double) (yOffset / xOffset);
			}
			// 通过几何图形工具获取交点坐标
			mDragPoints = GeometryUtil.getIntersectionPoints(mDragCenter, mDragRadius, lineK);
			mStickPoints = GeometryUtil.getIntersectionPoints(mStickCenter,	tempStickRadius, lineK);
	
			// 3. 获取控制点坐标
			mControlPoint = GeometryUtil.getMiddlePoint(mDragCenter, mStickCenter);

		// 保存画布状态
		canvas.save();
		canvas.translate(0, -statusBarHeight);
		
			// 画出最大范围(参考用)
			mPaint.setStyle(Style.STROKE);//设置画笔样式
			canvas.drawCircle(mStickCenter.x, mStickCenter.y, farestDistance, mPaint);
			mPaint.setStyle(Style.FILL);
			
		if(!isDisappear){
			if(!isOutofRange){
				// Ⅲ.画连接部分
				Path path = new Path();
				// ①跳到点1
				path.moveTo(mStickPoints[0].x, mStickPoints[0].y);
				// ②画曲线1 -> 2(前两个参数是控制点坐标，后两个参数是结束点坐标)
				path.quadTo(mControlPoint.x, mControlPoint.y, mDragPoints[0].x,	mDragPoints[0].y);
				// ③画直线2 -> 3
				path.lineTo(mDragPoints[1].x, mDragPoints[1].y);
				// ④画曲线3 -> 4
				path.quadTo(mControlPoint.x, mControlPoint.y, mStickPoints[1].x, mStickPoints[1].y);
				path.close();
				canvas.drawPath(path, mPaint);

					// 画附着点(参考用)
					mPaint.setColor(Color.BLUE);
					canvas.drawCircle(mDragPoints[0].x, mDragPoints[0].y, 3f, mPaint);
					canvas.drawCircle(mDragPoints[1].x, mDragPoints[1].y, 3f, mPaint);
					canvas.drawCircle(mStickPoints[0].x, mStickPoints[0].y, 3f, mPaint);
					canvas.drawCircle(mStickPoints[1].x, mStickPoints[1].y, 3f, mPaint);
					mPaint.setColor(Color.RED);

				// Ⅱ.画固定圆
				canvas.drawCircle(mStickCenter.x, mStickCenter.y, tempStickRadius, mPaint);
			}
			
			// Ⅰ.画拖拽圆
			canvas.drawCircle(mDragCenter.x, mDragCenter.y, mDragRadius, mPaint);
		}
		
		// 恢复上次的保存状态
		canvas.restore();
	}

	/**
	 * 获取固定圆半径(根据两圆圆心距离)
	 * 
	 * @return
	 */
	private float getTempStickRadius() {
		// 两点之间的距离
		float distance = GeometryUtil.getDistanceBetween2Points(mDragCenter, mStickCenter);

		// if(distance>farestDistance){
		// distance=farestDistance;
		// }
		// 优化之后(求最小值)
		distance = Math.min(distance, farestDistance);

		// 0.0f -> 1.0f >>> 1.0 -> 0.0
		float percent = distance / farestDistance;
		Log.d(TAG, "percent:" + percent);

		// percent , 100% -> 20% 
		return evaluate(percent, mStickRadius, mStickRadius * 0.2f);
	}

	/**
	 * 估值器
	 * 
	 * @param fraction
	 * @param startValue
	 * @param endValue
	 * @return
	 */
	public Float evaluate(float fraction, Number startValue, Number endValue) {
		float startFloat = startValue.floatValue();
		return startFloat + fraction * (endValue.floatValue() - startFloat);
	}

	/**
	 * 处理触摸事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x;
		float y;

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN :// 手指按下
				isOutofRange = false;
				isDisappear=false;
				x = event.getRawX();
				y = event.getRawY();
				updateDragCenter(x, y);
				break;
			case MotionEvent.ACTION_MOVE :// 手指触摸滑动
				x = event.getRawX();
				y = event.getRawY();
				updateDragCenter(x, y);
				// 处理断开事件
				float distance = GeometryUtil.getDistanceBetween2Points(mDragCenter, mStickCenter);
				if(distance > farestDistance){
					// 断开
					isOutofRange = true;
					invalidate();
				}
				
				break;
			case MotionEvent.ACTION_UP :// 手指松开
				if(isOutofRange){
					float newDistance=GeometryUtil.getDistanceBetween2Points(mDragCenter, mStickCenter);
					if(newDistance>farestDistance){
						// a. 拖拽超出范围,断开, 松手, 消失
						isDisappear=true;
						invalidate();
					}else {
						// b. 拖拽超出范围,断开,放回去了,恢复
						updateDragCenter(mStickCenter.x, mStickCenter.y);
					}
				}else {
					// c. 拖拽没超出范围, 松手,弹回去	
					final PointF tempDragCenter=new PointF(mDragCenter.x, mDragCenter.y);// 临时坐标
					
				    ValueAnimator mValueAnimator = ValueAnimator.ofFloat(1.0f);
					mValueAnimator.addUpdateListener(new AnimatorUpdateListener() {
						
						@Override
						public void onAnimationUpdate(ValueAnimator valueAnimator) {
							// 0.0 -> 1.0f
							float percent=valueAnimator.getAnimatedFraction();
							PointF pointF = GeometryUtil.getPointByPercent(tempDragCenter, mStickCenter, percent);
							updateDragCenter(pointF.x, pointF.y);
						}
					});
					mValueAnimator.setInterpolator(new OvershootInterpolator(4));// 差值器
					mValueAnimator.setDuration(500);
					mValueAnimator.start();
					
				}
				break;
			default :
				break;
		}
		return true;
	}

	/**
	 * 更新拖拽圆圆心坐标,并重绘界面
	 * 
	 * @param x
	 * @param y
	 */
	private void updateDragCenter(float x, float y) {
		mDragCenter.set(x, y);// 设置坐标
		invalidate();// 重绘
	}

	/**
	 * 当尺寸发生变化时，此方法调用
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		// 获取状态栏高度
		statusBarHeight = Utils.getStatusBarHeight(this);
	}
}
