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
 * @����:com.chj.tencent.goo
 * @����:GooView
 * @����:�»��
 * @ʱ��:2015-8-21 ����3:58:06
 * 
 * @����:ճ�Կؼ�
 * 
 * @SVN�汾��:$Rev$
 * @������:$Author$
 * @��������:TODO
 * 
 */
public class GooView extends View {

	private static final String TAG = "GooView";
	private Paint mPaint; // ����

	public GooView(Context context) {
		this(context, null);
	}

	public GooView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GooView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// ����ʼ������
		initView();
	}

	/**
	 * ��ʼ����ͼ
	 */
	private void initView() {

		mPaint = new Paint();
		mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);// ȥ�����
		mPaint.setColor(Color.RED);// ���û�����ɫ
	}

	// �̶�Բ������
	PointF[] mStickPoints = new PointF[]{new PointF(250f, 250f), new PointF(250f, 350f)};
	// ��קԲ������
	PointF[] mDragPoints = new PointF[]{new PointF(50f, 250f), new PointF(50f, 350f)};
	// ���Ƶ������
	PointF mControlPoint = new PointF(150f, 300f);

	PointF mDragCenter = new PointF(80f, 80f); // ��קԲ��Բ��
	float mDragRadius = 14f; // ��קԲ�İ뾶

	PointF mStickCenter = new PointF(150f, 150f); // �̶�Բ��Բ��
	float mStickRadius = 12f; // �̶�Բ�İ뾶

	private int statusBarHeight;// ״̬���߶�
	float farestDistance = 80f;// ��Զ����
	private boolean isOutofRange;// �Ƿ񳬳���Χ
	private boolean isDisappear;// �Ƿ���ʧ

	/**
	 * ���Ʒ���
	 */
	@Override
	protected void onDraw(Canvas canvas) {

		// �������ӵ�ֵ, ���Ƶ�, �̶�Բ�뾶
			// 1. ��ȡ�̶�Բ�뾶(������ԲԲ�ľ���)
			float tempStickRadius = getTempStickRadius();// ��ʱ�뾶
	
			// 2. ��ȡֱ����Բ�Ľ���
			float yOffset = mStickCenter.y - mDragCenter.y;
			float xOffset = mStickCenter.x - mDragCenter.x;
			Double lineK = null;
			if (xOffset != 0) {
				lineK = (double) (yOffset / xOffset);
			}
			// ͨ������ͼ�ι��߻�ȡ��������
			mDragPoints = GeometryUtil.getIntersectionPoints(mDragCenter, mDragRadius, lineK);
			mStickPoints = GeometryUtil.getIntersectionPoints(mStickCenter,	tempStickRadius, lineK);
	
			// 3. ��ȡ���Ƶ�����
			mControlPoint = GeometryUtil.getMiddlePoint(mDragCenter, mStickCenter);

		// ���滭��״̬
		canvas.save();
		canvas.translate(0, -statusBarHeight);
		
			// �������Χ(�ο���)
			mPaint.setStyle(Style.STROKE);//���û�����ʽ
			canvas.drawCircle(mStickCenter.x, mStickCenter.y, farestDistance, mPaint);
			mPaint.setStyle(Style.FILL);
			
		if(!isDisappear){
			if(!isOutofRange){
				// ��.�����Ӳ���
				Path path = new Path();
				// ��������1
				path.moveTo(mStickPoints[0].x, mStickPoints[0].y);
				// �ڻ�����1 -> 2(ǰ���������ǿ��Ƶ����꣬�����������ǽ���������)
				path.quadTo(mControlPoint.x, mControlPoint.y, mDragPoints[0].x,	mDragPoints[0].y);
				// �ۻ�ֱ��2 -> 3
				path.lineTo(mDragPoints[1].x, mDragPoints[1].y);
				// �ܻ�����3 -> 4
				path.quadTo(mControlPoint.x, mControlPoint.y, mStickPoints[1].x, mStickPoints[1].y);
				path.close();
				canvas.drawPath(path, mPaint);

					// �����ŵ�(�ο���)
					mPaint.setColor(Color.BLUE);
					canvas.drawCircle(mDragPoints[0].x, mDragPoints[0].y, 3f, mPaint);
					canvas.drawCircle(mDragPoints[1].x, mDragPoints[1].y, 3f, mPaint);
					canvas.drawCircle(mStickPoints[0].x, mStickPoints[0].y, 3f, mPaint);
					canvas.drawCircle(mStickPoints[1].x, mStickPoints[1].y, 3f, mPaint);
					mPaint.setColor(Color.RED);

				// ��.���̶�Բ
				canvas.drawCircle(mStickCenter.x, mStickCenter.y, tempStickRadius, mPaint);
			}
			
			// ��.����קԲ
			canvas.drawCircle(mDragCenter.x, mDragCenter.y, mDragRadius, mPaint);
		}
		
		// �ָ��ϴεı���״̬
		canvas.restore();
	}

	/**
	 * ��ȡ�̶�Բ�뾶(������ԲԲ�ľ���)
	 * 
	 * @return
	 */
	private float getTempStickRadius() {
		// ����֮��ľ���
		float distance = GeometryUtil.getDistanceBetween2Points(mDragCenter, mStickCenter);

		// if(distance>farestDistance){
		// distance=farestDistance;
		// }
		// �Ż�֮��(����Сֵ)
		distance = Math.min(distance, farestDistance);

		// 0.0f -> 1.0f >>> 1.0 -> 0.0
		float percent = distance / farestDistance;
		Log.d(TAG, "percent:" + percent);

		// percent , 100% -> 20% 
		return evaluate(percent, mStickRadius, mStickRadius * 0.2f);
	}

	/**
	 * ��ֵ��
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
	 * �������¼�
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x;
		float y;

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN :// ��ָ����
				isOutofRange = false;
				isDisappear=false;
				x = event.getRawX();
				y = event.getRawY();
				updateDragCenter(x, y);
				break;
			case MotionEvent.ACTION_MOVE :// ��ָ��������
				x = event.getRawX();
				y = event.getRawY();
				updateDragCenter(x, y);
				// ����Ͽ��¼�
				float distance = GeometryUtil.getDistanceBetween2Points(mDragCenter, mStickCenter);
				if(distance > farestDistance){
					// �Ͽ�
					isOutofRange = true;
					invalidate();
				}
				
				break;
			case MotionEvent.ACTION_UP :// ��ָ�ɿ�
				if(isOutofRange){
					float newDistance=GeometryUtil.getDistanceBetween2Points(mDragCenter, mStickCenter);
					if(newDistance>farestDistance){
						// a. ��ק������Χ,�Ͽ�, ����, ��ʧ
						isDisappear=true;
						invalidate();
					}else {
						// b. ��ק������Χ,�Ͽ�,�Ż�ȥ��,�ָ�
						updateDragCenter(mStickCenter.x, mStickCenter.y);
					}
				}else {
					// c. ��קû������Χ, ����,����ȥ	
					final PointF tempDragCenter=new PointF(mDragCenter.x, mDragCenter.y);// ��ʱ����
					
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
					mValueAnimator.setInterpolator(new OvershootInterpolator(4));// ��ֵ��
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
	 * ������קԲԲ������,���ػ����
	 * 
	 * @param x
	 * @param y
	 */
	private void updateDragCenter(float x, float y) {
		mDragCenter.set(x, y);// ��������
		invalidate();// �ػ�
	}

	/**
	 * ���ߴ緢���仯ʱ���˷�������
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		// ��ȡ״̬���߶�
		statusBarHeight = Utils.getStatusBarHeight(this);
	}
}
