package com.chj.tencent.util;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
/**
 * 
 * @包名:com.chj.tencent.util
 * @类名:Utils
 * @作者:陈火炬
 * @时间:2015-8-19 下午9:18:26
 * 
 * @描述:Utils工具类
 * 
 * @SVN版本号:$Rev$
 * @更新人:$Author$
 * @更新描述:TODO
 * 
 */
public class Utils {

	public static Toast mToast;

	public static void showToast(Context mContext, String msg) {
		if (mToast == null) {
			mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
		}
		mToast.setText(msg);
		mToast.show();
	}

	/**
	 * dip 转换成 px
	 * 
	 * @param dip
	 * @param context
	 * @return
	 */
	public static float dip2Dimension(float dip, Context context) {
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,
				displayMetrics);
	}
	/**
	 * @param dip
	 * @param context
	 * @param complexUnit
	 *            {@link TypedValue#COMPLEX_UNIT_DIP}
	 *            {@link TypedValue#COMPLEX_UNIT_SP}
	 * @return
	 */
	public static float toDimension(float dip, Context context, int complexUnit) {
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		return TypedValue.applyDimension(complexUnit, dip, displayMetrics);
	}

	/**
	 * 获取状态栏高度
	 * 
	 * @param v
	 * @return
	 */
	public static int getStatusBarHeight(View v) {
		if (v == null) {
			return 0;
		}
		Rect frame = new Rect();
		v.getWindowVisibleDisplayFrame(frame);
		return frame.top;
	}

	public static String getActionName(MotionEvent event) {
		String action = "unknow";
		switch (MotionEventCompat.getActionMasked(event)) {
			case MotionEvent.ACTION_DOWN :
				action = "ACTION_DOWN";
				break;
			case MotionEvent.ACTION_MOVE :
				action = "ACTION_MOVE";
				break;
			case MotionEvent.ACTION_UP :
				action = "ACTION_UP";
				break;
			case MotionEvent.ACTION_CANCEL :
				action = "ACTION_CANCEL";
				break;
			case MotionEvent.ACTION_OUTSIDE :
				action = "ACTION_SCROLL";
				break;
			default :
				break;
		}
		return action;
	}
}
