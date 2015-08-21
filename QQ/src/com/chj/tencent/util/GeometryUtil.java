package com.chj.tencent.util;

import android.graphics.PointF;

/**
 * 
 * @����:com.chj.tencent.util
 * @����:GeometryUtil
 * @����:�»��
 * @ʱ��:2015-8-21 ����5:50:15
 * 
 * @����:����ͼ�ι���
 * 
 * @SVN�汾��:$Rev$
 * @������:$Author$
 * @��������:TODO
 * 
 */
public class GeometryUtil {

	/**
	 * �������֮��ľ��롣
	 * 
	 * As meaning of method name.
	 * 
	 * @param p0
	 * @param p1
	 * @return
	 */
	public static float getDistanceBetween2Points(PointF p0, PointF p1) {
		float distance = (float) Math.sqrt(Math.pow(p0.y - p1.y, 2)
				+ Math.pow(p0.x - p1.x, 2));
		return distance;
	}

	/**
	 * ����������ߵ��е㡣
	 * 
	 * Get middle point between p1 and p2.
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static PointF getMiddlePoint(PointF p1, PointF p2) {
		return new PointF((p1.x + p2.x) / 2.0f, (p1.y + p2.y) / 2.0f);
	}

	/**
	 * ���ݰٷֱȻ�ȡ����֮���ĳ�������ꡣ
	 * 
	 * Get point between p1 and p2 by percent.
	 * 
	 * @param p1
	 * @param p2
	 * @param percent
	 * @return
	 */
	public static PointF getPointByPercent(PointF p1, PointF p2, float percent) {
		return new PointF(evaluateValue(percent, p1.x, p2.x), evaluateValue(
				percent, p1.y, p2.y));
	}

	/**
	 * ���ݷֶ�ֵ�������start��end�У�fractionλ�õ�ֵ��
	 * 
	 * fraction��ΧΪ0 -> 1
	 * 
	 * @param fraction
	 * @param start
	 * @param end
	 * @return
	 */
	public static float evaluateValue(float fraction, Number start, Number end) {
		return start.floatValue() + (end.floatValue() - start.floatValue())
				* fraction;
	}

	/**
	 * ��ȡͨ��ָ��Բ�ģ�б��ΪlineK��ֱ����Բ�Ľ��㡣
	 * 
	 * Get the point of intersection between circle and line.
	 * 
	 * @param pMiddle
	 *            :Բ�����ĵ� (The circle center point.)
	 * @param radius
	 *            :Բ�뾶 (The circle radius.)
	 * @param lineK
	 *            :�ߵ�б�� (The slope of line which cross the pMiddle.)
	 * @return
	 */
	public static PointF[] getIntersectionPoints(PointF pMiddle, float radius,
			Double lineK) {
		PointF[] points = new PointF[2];

		float radian, xOffset = 0, yOffset = 0;
		if (lineK != null) {
			radian = (float) Math.atan(lineK);
			xOffset = (float) (Math.sin(radian) * radius);
			yOffset = (float) (Math.cos(radian) * radius);
		} else {
			xOffset = radius;
			yOffset = 0;
		}
		points[0] = new PointF(pMiddle.x + xOffset, pMiddle.y - yOffset);
		points[1] = new PointF(pMiddle.x - xOffset, pMiddle.y + yOffset);

		return points;
	}
}